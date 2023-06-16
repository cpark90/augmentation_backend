package com.nexr.de.grnd.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexr.de.grnd.entity.HttpRequestEntity;
import com.nexr.de.grnd.exception.EngineException;
import com.nexr.de.grnd.util.PropertiesUtil;

@Service
public class KetiService {
	private static Logger LOGGER = LoggerFactory.getLogger(KetiService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	public void startTransform(
			String srcDirectoryPath,
			String srcGTDirectoryPath,
			String tgtDirectoryPath,
			String tgtGTDirectoryPath,
			String srcSensorProfilePath,
			String tgtsensorProfilePath,
			String transformConfigPath,
			Map<String, Boolean> transform_flag
			) {
		//**************************************************************************
		//TODO: 2. 변환 작업 시작 REST API call (KETI에서 정의한 것을 호출)
		//camera : camera_transform() : 화각 변환 (FOV), 해상도 변환(resolution_channel), 왜곡 변환(distortion)
		//            viewSynthesis() : 센서 위치 변환(position), 센서 각도 변환(angle)
		//          color_transform() : 생상 변환(color)
		//lidar  :  lidar_transform() : 화각 변환 (FOV), 해상도 변환(resolution_channel)
		//          lidar_transform() : 센서 위치 변환(position), 센서 각도 변환(angle)
		//radar  :  radar_transform() : 화각 변환 (FOV)
		//          radar_transform() : 센서 위치 변환(position), 센서 각도 변환(angle)
		//**************************************************************************
		LOGGER.info("transform_flag:{}", transform_flag);
		//transform_flag:{FOV=false, resolution_channel=false, distortion=false, position=false, angle=false, color=true}
		
		
		//transform_flag 값에 따라 API 호출해주세요
		
		
	}
	
	/**
	 * 엔진이 ready 상태가 될때까지 대기(engine.waitMilliTime 초 이상 대기시 EngineException 발생)
	 * @param httpRequestEntity
	 * @throws EngineException 
	 */
	public void wait(HttpRequestEntity httpRequestEntity) throws EngineException {
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
		
		boolean isWait = true;
		long startTime = System.currentTimeMillis();
		
		while(isWait) {
			
			
			//**************************************************************************
			//TODO: 1. 변환 엔진 준비 확인 REST API call (KETI에서 정의한 것을 호출)
			//**************************************************************************
			String callResult = "OK";//API 호출 결과
			
			if("OK".equals(callResult)) {
				isWait = false;//변경하여 while 구문 종료해주세요
				break;
			}
			
			if((System.currentTimeMillis() - startTime) > this.propertiesUtil.getWaitMilliTime()) {
				String errMsg = String.format("엔진 시작 대기시간이 %fms를 초과 했습니다(EngineName:%s).", this.propertiesUtil.getWaitMilliTime(), httpRequestEntity.getEngineName());
				LOGGER.warn(errMsg);
				throw new EngineException(errMsg, httpRequestEntity.getEngineName());
			}
			
			try {
				Thread.sleep(200L);//0.2초 대기(임의로 정한 값입니다.)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
}
