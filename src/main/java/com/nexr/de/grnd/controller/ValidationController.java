package com.nexr.de.grnd.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nexr.de.grnd.constants.CodeConstants;
import com.nexr.de.grnd.entity.HttpRequestEntity;
import com.nexr.de.grnd.entity.HttpResponseEntity;
import com.nexr.de.grnd.exception.EngineException;
import com.nexr.de.grnd.util.FileUtil;

@RestController
@RequestMapping("/val")
public class ValidationController {
	private static Logger LOGGER = LoggerFactory.getLogger(ValidationController.class);
	
	@Autowired
	private FileUtil fileUtil;
	
//	private Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	
	/**
	 * 카메라, 라이다, 데이다, 포맷변환 작업 요청 수신
	 * @param httpRequestEntity
	 * @return
	 */
	@RequestMapping(value="/config", method=RequestMethod.POST)
	public ResponseEntity<HttpResponseEntity> profileValidate(@RequestBody HttpRequestEntity httpRequestEntity) {
		HttpResponseEntity result = new HttpResponseEntity();
		
		try {
			//config 파일 생성 로직 : 경로 및 파일명 반환 받기 - transform_config_path 로 사용 : 파일명도 같이 보내기(20230511)
			String configfilePath = this.fileUtil.makeYamlConfigFile(httpRequestEntity);//transform_config_path
			String sensorProfilePath = Paths.get(this.fileUtil.getDefaultPathByEngineName(httpRequestEntity), httpRequestEntity.getDatasetName()).toString();//src_sensor_profile_path
			String newFolderName = Paths.get(this.fileUtil.getDefaultPathByEngineName(httpRequestEntity), httpRequestEntity.getNewFolderName()).toString();
			
			File configPathObj = new File(newFolderName);
			if(configPathObj.exists()) {
				throw new EngineException("데이터명 '" + httpRequestEntity.getNewFolderName() + "'는(은) 이미 존재합니다.", httpRequestEntity.getNewFolderName());
			}
			
			LOGGER.info("profilePath:{}, configfilePath:{}", sensorProfilePath, configfilePath);
			//profilePath:D:\RnD\dataset\camera\camera0, configfilePath:D:\RnD\dataset\config\camera_c82f9847-b778-4d04-bee9-1212ffc69ecb.yaml
			
			
			
			
			//**************************************************************************
			//TODO: KETI에서 제공하는 api 호출하기
			Map<String, Object> validationResult = new HashMap<String, Object>();//KETI에서 제공하는 api 호출하기
			validationResult.put("code", "OK");//임시코드
			validationResult.put("message", "code가 'OK'가 아니면 여기에 문제의 원인을 보내주세요. UI에 표시하겠습니다.");//임시코드
			//OK 면 "변환 시작" 버튼 활성화, 이외 비활성화 유지
			//**************************************************************************
			
			
			
			
			Map<String, Object> apiResult = new HashMap<String, Object>();
			apiResult.put("configfilePath", configfilePath);
			apiResult.put("sensorProfilePath", sensorProfilePath);
			apiResult.put("validationResult", validationResult);
			result.setResponseData(apiResult);
			
			result.setError(false);
			result.setStatusCode(CodeConstants.HTTP_SUCCESS_CODE);
			result.setErrorMessage("");
			result.setResponseMessage("validation checked success.");
			
		} catch (Exception e) {
			result.setError(true);
			Map<String, Object> apiResult = new HashMap<String, Object>();
			Map<String, Object> validationResult = new HashMap<String, Object>();
			validationResult.put("code", "ERROR");//임시코드
			validationResult.put("message", e.getMessage());//임시코드
			apiResult.put("validationResult", validationResult);
			result.setResponseData(apiResult);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("validation checked fail.");
		}
		
		return ResponseEntity.ok(result);
	}
	
}
