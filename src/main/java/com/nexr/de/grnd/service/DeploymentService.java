package com.nexr.de.grnd.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexr.de.grnd.constants.CodeConstants;
import com.nexr.de.grnd.entity.HttpRequestEntity;
import com.nexr.de.grnd.entity.KubernetesAPIEntity;
import com.nexr.de.grnd.entity.engine.CameraEntity;
import com.nexr.de.grnd.entity.engine.EngineEntity;
import com.nexr.de.grnd.entity.engine.FormatEntity;
import com.nexr.de.grnd.entity.engine.LidarEntity;
import com.nexr.de.grnd.entity.engine.RadarEntity;
import com.nexr.de.grnd.exception.EngineException;
import com.nexr.de.grnd.util.FileUtil;
import com.nexr.de.grnd.util.PropertiesUtil;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentBuilder;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1PersistentVolumeClaimVolumeSource;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.openapi.models.V1Volume;
import io.kubernetes.client.openapi.models.V1VolumeMount;

@Service
public class DeploymentService {
	private static Logger LOGGER = LoggerFactory.getLogger(DeploymentService.class);
	
	/**
	 * 현재 배포된 작업을 관리하기 위한 변수(사용할지는 아직 모름)
	 */
//	private static final Map<String, HttpRequestEntity> RUNNING_MAP = new ConcurrentHashMap<>();
	
	@Autowired
	private KubernetesAPIService kuvernetesAPIService;
	
	@Autowired
	private CameraEntity cameraEntity;
	
	@Autowired
	private LidarEntity lidarEntity;
	
	@Autowired
	private RadarEntity radarEntity;
	
	@Autowired
	private FormatEntity formatEntity;
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private KetiService ketiService;
	
	/**
	 * 데이터 변환 시작에 대한 요청을 처리
	 * @param httpRequestEntity
	 * @return 요청에 대한 상태 코드(404 : deploy 실행, 200: 이미 배포된 어플리케이션 있음, 401: 권한 없음)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws EngineException 
	 */
	public KubernetesAPIEntity startEngine(HttpRequestEntity httpRequestEntity) throws FileNotFoundException, IOException, EngineException {
		// *** 카메라, 라이다, 데이다, 포맷변환의 필수항목 체크하는 로직 구현 필요
		
		KubernetesAPIEntity kubernetesAPIEntity = new KubernetesAPIEntity();
		
		LOGGER.info("{}", httpRequestEntity);
		/*
		 * com.nexr.de.grnd.entity.HttpRequestEntity[
		 * engineName=camera
		 * , engineProfile=null
		 * , datasetName=camera0
		 * , configfilePath=D:\RnD\dataset\config\camera_6cd44a6f-0596-4e98-8d6c-5a2a6143a8cb.yaml
		 * , sensorProfilePath=D:\RnD\dataset\camera\camera0
		 * , newFolderName=c2
		 * ]
		 */
		String targetPath = Paths.get(this.fileUtil.getDefaultPathByEngineName(httpRequestEntity), httpRequestEntity.getNewFolderName()).toString();
		LOGGER.info("targetPath:{}", targetPath);
		
		
		EngineEntity engineEntity = this.getEngineEntity(httpRequestEntity);
		
		KubernetesAPIEntity tempKubernetesAPIEntity = this.getDeploymentStatus(engineEntity);
		int deploymentStatusCode = tempKubernetesAPIEntity.getCode();//404 : deploy 실행, 200: 이미 배포된 어플리케이션 있음
		
		LOGGER.info("engineEntity:{}", engineEntity);
		LOGGER.info("deploymentStatusCode:'{}'", deploymentStatusCode);
		
		//현재 작업이 진행중인 것이 있나 확인 필요 : GPU 점유는 한번에 1개 작업만 가능하기 때문에
		if(deploymentStatusCode == CodeConstants.API_NOT_FOUND_CODE) {
			
			//경로가 없으면 생성
			File targetPathFile = new File(targetPath);
			if(!targetPathFile.exists()) {
				targetPathFile.mkdirs();
			}else {
				//경로가 이미 존재하면 변환 결과 파일이 중복?, 덮어쓰기? 등이 될 수 있다고 생각하여 오류로 처리함.
				String errMsg = String.format("'%s' 경로가 존재합니다:(%s)", httpRequestEntity.getNewFolderName(), targetPath);
				LOGGER.error(errMsg);
				throw new EngineException(errMsg, httpRequestEntity.getEngineName());
			}
			
			//디플로이먼트 수행
			kubernetesAPIEntity = this.deployment(engineEntity);
			
			
			
			
			String srcDirectoryPath = httpRequestEntity.getSensorProfilePath();//원본 파일 data가 저장 되어있는 directory path
			String srcGTDirectoryPath = httpRequestEntity.getSensorProfilePath();//원본 파일 data의 GT 버전 data가 저장되어 있는 directory path
			String tgtDirectoryPath = targetPath;//변환 완료된 data를 저장할 directory path. Directory 생성 후 path를 인자로 전달
			String tgtGTDirectoryPath = targetPath;//변환 완료된 GT data를 저장할 directory path. Directory 생성 후 path를 인자로 전달
			String srcSensorProfilePath = httpRequestEntity.getSensorProfilePath();//.yaml 파일(센서 특성 정보) file path
			String tgtsensorProfilePath = targetPath;//변환 완료된 data의 센서 특성 정보를 저장할 path
			String transformConfigPath = httpRequestEntity.getConfigfilePath();//생성된 config file path
			
			String engineName = httpRequestEntity.getEngineName();
			LOGGER.debug("engineName:{}", engineName);
			
			Map<String, Boolean> transform_flag = (Map<String, Boolean>) httpRequestEntity.getEngineProfile().get("transform_flag");
			LOGGER.debug("transform_flag:{}", transform_flag);
			//transform_flag:{FOV=false, resolution_channel=false, distortion=false, position=false, angle=false, color=true}
			
			
			//**************************************************************************
			//1. 변환 엔진 준비 확인 REST API call 로직
			// 준비됨이 확인되면 다음으로 넘어가는 로직으로 구성해야 함
			this.ketiService.wait(httpRequestEntity);
			//**************************************************************************
			
			//**************************************************************************
			//2. 변환 작업 시작 REST API call (KETI에서 정의한 것을 호출)
			//camera : camera_transform() : 화각 변환 (FOV), 해상도 변환(resolution_channel), 왜곡 변환(distortion)
			//            viewSynthesis() : 센서 위치 변환(position), 센서 각도 변환(angle)
			//          color_transform() : 생상 변환(color)
			//lidar  :  lidar_transform() : 화각 변환 (FOV), 해상도 변환(resolution_channel)
			//          lidar_transform() : 센서 위치 변환(position), 센서 각도 변환(angle)
			//radar  :  radar_transform() : 화각 변환 (FOV)
			//          radar_transform() : 센서 위치 변환(position), 센서 각도 변환(angle)
			//**************************************************************************
			this.ketiService.startTransform(srcDirectoryPath, srcGTDirectoryPath, tgtDirectoryPath, tgtGTDirectoryPath, srcSensorProfilePath, tgtsensorProfilePath, transformConfigPath, transform_flag);
			
		}else if(deploymentStatusCode == CodeConstants.API_OK_CODE) {
			//이미 배포된 어플리케이션 있음
			kubernetesAPIEntity = tempKubernetesAPIEntity;
		}else if(deploymentStatusCode == CodeConstants.API_UNAUTHORIZED_CODE) {
			//권한 없음
			kubernetesAPIEntity = tempKubernetesAPIEntity;
			
		}else if(deploymentStatusCode == CodeConstants.API_NAME_MUST_BE_PROVIDED) {
			//이미지의 이름을 전송하지 않은 경우
			kubernetesAPIEntity = tempKubernetesAPIEntity;
			
		}else {
			//정의하지 않은 오류(관리자에게 문의하세요)
			
		}
		
		
		return kubernetesAPIEntity;
	}
	
	
	
	
	public KubernetesAPIEntity finishJob(HttpRequestEntity httpRequestEntity) throws FileNotFoundException, IOException, EngineException {
		// *** 카메라, 라이다, 데이다, 포맷변환의 필수항목 체크하는 로직 구현 필요
		
		KubernetesAPIEntity kubernetesAPIEntity = new KubernetesAPIEntity();
		EngineEntity engineEntity = this.getEngineEntity(httpRequestEntity);
		
		int deploymentStatusCode = this.getDeploymentStatus(engineEntity).getCode();//404 : deploy 실행, 200: 이미 배포된 어플리케이션 있음
		
		LOGGER.info("engineEntity:{}", engineEntity);
		LOGGER.info("deploymentStatusCode:'{}'", deploymentStatusCode);
		
		if(deploymentStatusCode == CodeConstants.API_NOT_FOUND_CODE) {
			//삭제할 deployment가 없는 상태
			
		}else if(deploymentStatusCode == CodeConstants.API_OK_CODE) {
			//배포된 deployment 있음
			//삭제 호출
			kubernetesAPIEntity = this.deploymentDelete(engineEntity);
		}else if(deploymentStatusCode == CodeConstants.API_UNAUTHORIZED_CODE) {
			//권한 없음
			
		}else if(deploymentStatusCode == CodeConstants.API_NAME_MUST_BE_PROVIDED) {
			//이미지의 이름을 전송하지 않은 경우
			
		}else {
			//정의하지 않은 오류(관리자에게 문의하세요)
			
		}
		
		return kubernetesAPIEntity;
	}
	
	
	private EngineEntity getEngineEntity(HttpRequestEntity httpRequestEntity) throws EngineException {
		EngineEntity engineEntity = null;
		
		switch (httpRequestEntity.getEngineName()) {
			case CodeConstants.ENGINE_NAME_CAMERA:
				engineEntity = this.cameraEntity;
				break;
			case CodeConstants.ENGINE_NAME_LIDAR:
				engineEntity = this.lidarEntity;
				break;
			case CodeConstants.ENGINE_NAME_RADAR:
				engineEntity = this.radarEntity;
				break;
			case CodeConstants.ENGINE_NAME_FORMAT:
				engineEntity = this.formatEntity;
				break;
			default:
				break;
		}
		
		if(engineEntity == null) {
			throw new EngineException("'" + httpRequestEntity.getEngineName()+ "'는(은) 지원하지 않는 엔진입니다.", httpRequestEntity.getEngineName());
		}
		
		return engineEntity;
	}
	
	
	private KubernetesAPIEntity deployment(EngineEntity engineEntity) throws FileNotFoundException, IOException {
		KubernetesAPIEntity kubernetesAPIEntity = new KubernetesAPIEntity();
		AppsV1Api appsV1Api = kuvernetesAPIService.getAppsV1Api();
		
		// Create an example deployment
		V1DeploymentBuilder deploymentBuilder = new V1DeploymentBuilder();
		deploymentBuilder.withApiVersion("apps/v1");
		deploymentBuilder.withKind("Deployment");
		
		//metadata
		V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
		v1ObjectMeta.name(engineEntity.getNameByMetadata());
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", engineEntity.getNameByMetadata());
		v1ObjectMeta.labels(labels);
		deploymentBuilder.withMetadata(v1ObjectMeta);
		
		//spec
		V1DeploymentSpec v1DeploymentSpec = new V1DeploymentSpec();
		v1DeploymentSpec.setReplicas(1);
		v1DeploymentSpec.setSelector(new V1LabelSelector().putMatchLabelsItem("app", engineEntity.getNameByMetadata()));
		
		V1PodTemplateSpec v1PodTemplateSpec = new V1PodTemplateSpec();
		v1PodTemplateSpec.setMetadata(new V1ObjectMeta().putLabelsItem("app", engineEntity.getNameByMetadata()));
		
		V1PodSpec v1PodSpec = new V1PodSpec();
		
		V1Container v1Container = new V1Container().name("gdemoengine-run").image(engineEntity.getImageName());
		V1VolumeMount v1VolumeMount = new V1VolumeMount();
		String volumeMountName = "shared-volume";
		v1VolumeMount.setName(volumeMountName);
		v1VolumeMount.setMountPath(this.propertiesUtil.getVolumeMountPath());//해당 경로가 pod 내부 경로로 생성된다 ( /shared 로 고정하자 )
		//pod 접속 방법 : kubectl exec -it [pod name] -- bash
		//접속 후 : ls -al 로 확인 가능
		// "/shared"는 nfs-storage-claim 과 연동되어 NFS설정에 따라 NFS 서버의 /nfs 경로와 싱크를 맞춘다
		v1Container.setVolumeMounts(Collections.singletonList(v1VolumeMount));
		v1PodSpec.setContainers(Collections.singletonList(v1Container));
		
		
		V1Volume v1Volume = new V1Volume();
		v1Volume.setName(volumeMountName);
		V1PersistentVolumeClaimVolumeSource v1PersistentVolumeClaimVolumeSource = new V1PersistentVolumeClaimVolumeSource();
		v1PersistentVolumeClaimVolumeSource.setClaimName(this.propertiesUtil.getPersistentVolumeClaimName());//nfs-pvc.yaml의 metadata.name에 기입한 이름
		v1Volume.setPersistentVolumeClaim(v1PersistentVolumeClaimVolumeSource);
		v1PodSpec.setVolumes(Collections.singletonList(v1Volume));
		v1PodTemplateSpec.setSpec(v1PodSpec);
		v1DeploymentSpec.setTemplate(v1PodTemplateSpec);
		deploymentBuilder.withSpec(v1DeploymentSpec);
		
		V1Deployment v1Deployment = deploymentBuilder.build();
		LOGGER.info(v1Deployment.toString());
		
		try {
			V1Deployment resultV1Deployment = appsV1Api.createNamespacedDeployment(engineEntity.getNamespace(), v1Deployment, null, null, null, null);
			kubernetesAPIEntity.setCode(CodeConstants.API_NOT_FOUND_CODE);//200이 아니면 모두 오류
			LOGGER.debug(resultV1Deployment.getSpec().getReplicas().toString());
		} catch(ApiException apie) {
			/*
			200 OK
			401 Unauthorized
			404 NotFound
			*/
			kubernetesAPIEntity.setCode(apie.getCode());
			kubernetesAPIEntity.setResponseBody(apie.getResponseBody());
			kubernetesAPIEntity.setResponseHeaders(apie.getResponseHeaders());
			kubernetesAPIEntity.setLocalizedMessage(apie.getLocalizedMessage());
			kubernetesAPIEntity.setMessage(apie.getMessage());
			
			LOGGER.error(apie.getResponseBody());
			LOGGER.error("{}", apie.getResponseHeaders());
			LOGGER.error(apie.getLocalizedMessage());
			LOGGER.error(apie.getMessage());
		}
		
		return kubernetesAPIEntity;
	}
	
	
	
	private KubernetesAPIEntity deploymentDelete(EngineEntity engineEntity) throws FileNotFoundException, IOException {
		KubernetesAPIEntity kubernetesAPIEntity = new KubernetesAPIEntity();
		
		AppsV1Api appsV1Api = kuvernetesAPIService.getAppsV1Api();
		
		try {
			V1Status v1Status = appsV1Api.deleteNamespacedDeployment(engineEntity.getNameByMetadata(), engineEntity.getNamespace(), null, null, null, null, null, null);
			kubernetesAPIEntity.setCode(CodeConstants.API_OK_CODE);//200이 아니면 모두 오류
			
			LOGGER.debug(v1Status.toString());
		}catch(ApiException apie) {
			kubernetesAPIEntity.setCode(apie.getCode());
			kubernetesAPIEntity.setResponseBody(apie.getResponseBody());
			kubernetesAPIEntity.setResponseHeaders(apie.getResponseHeaders());
			kubernetesAPIEntity.setLocalizedMessage(apie.getLocalizedMessage());
			kubernetesAPIEntity.setMessage(apie.getMessage());
			
			LOGGER.error(apie.getResponseBody());
			LOGGER.error("{}", apie.getResponseHeaders());
			LOGGER.error(apie.getLocalizedMessage());
			LOGGER.error(apie.getMessage());
		}
		
		return kubernetesAPIEntity;
	}
	
	/**
	 * 
	 * @param engineEntity
	 * @return kubernetesAPIEntity.code가 200이 아니면 모두 오류
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private KubernetesAPIEntity getDeploymentStatus(EngineEntity engineEntity) throws FileNotFoundException, IOException {
		KubernetesAPIEntity kubernetesAPIEntity = new KubernetesAPIEntity();
		
		AppsV1Api appsV1Api = kuvernetesAPIService.getAppsV1Api();
		
		try {
			V1Deployment v1Deployment = appsV1Api.readNamespacedDeploymentStatus(engineEntity.getNameByMetadata(), engineEntity.getNamespace(), null);
			
			kubernetesAPIEntity.setCode(CodeConstants.API_OK_CODE);//200이면 해당 deployment 존재
			LOGGER.debug(v1Deployment.getSpec().getReplicas().toString());
			
		}catch(ApiException apie) {
			/*
			200 OK "deployment 존재"
			401 Unauthorized "권한 오류 : 관리자에게 문의하세요"
			404 NotFound "deployment 없음"
			*/
			kubernetesAPIEntity.setCode(apie.getCode());
			kubernetesAPIEntity.setResponseBody(apie.getResponseBody());
			kubernetesAPIEntity.setResponseHeaders(apie.getResponseHeaders());
			kubernetesAPIEntity.setLocalizedMessage(apie.getLocalizedMessage());
			kubernetesAPIEntity.setMessage(apie.getMessage());
			
			LOGGER.info("Code:'{}'", apie.getCode());
			LOGGER.info("ResponseBody:'{}'", apie.getResponseBody());
			LOGGER.info("ResponseHeaders:'{}'", apie.getResponseHeaders());
			LOGGER.info("LocalizedMessage:'{}'", apie.getLocalizedMessage());
			LOGGER.info("Message:'{}'", apie.getMessage());
		}
		
		return kubernetesAPIEntity;
	}
}
