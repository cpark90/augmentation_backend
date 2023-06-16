package com.nexr.de.grnd.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nexr.de.grnd.constants.CodeConstants;
import com.nexr.de.grnd.entity.HttpRequestEntity;
import com.nexr.de.grnd.entity.HttpResponseEntity;
import com.nexr.de.grnd.entity.KubernetesAPIEntity;
import com.nexr.de.grnd.exception.EngineException;
import com.nexr.de.grnd.service.DeploymentService;

@RestController
@RequestMapping("/kube")
public class KubeAPIController {
//	private static Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	private DeploymentService deploymentService;
	
//	private Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	
	/**
	 * 카메라, 라이다, 데이다, 포맷변환 작업 요청 수신
	 * @param httpRequestEntity
	 * @return
	 */
	@RequestMapping(value="/startEngine", method=RequestMethod.POST)
	public ResponseEntity<HttpResponseEntity> startEngine(@RequestBody HttpRequestEntity httpRequestEntity) {
		HttpResponseEntity result = new HttpResponseEntity();
		
		try {
			KubernetesAPIEntity kubernetesAPIEntity = deploymentService.startEngine(httpRequestEntity);
			
			Map<String, Object> apiResult = new HashMap<String, Object>();
			apiResult.put("kubernetesAPIEntity", kubernetesAPIEntity);
			
			result.setResponseData(apiResult);
			
			result.setError(false);
			result.setStatusCode(CodeConstants.HTTP_SUCCESS_CODE);
			result.setErrorMessage("");
			result.setResponseMessage("startEngine success.");
		} catch (EngineException e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("startEngine request is fail.");
			
		} catch (IOException e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("startEngine request is fail.");
		}
		
		return ResponseEntity.ok(result);
	}
	
	
	/*
	 * 테스트, 셈플... 삭제 예정
	 */
//	@RequestMapping(value="/startEngine", method=RequestMethod.GET)
//	public ResponseEntity<HttpResponseEntity> startEngine() throws FileNotFoundException, IOException{
//		
//		HttpRequestEntity httpRequestEntity = new HttpRequestEntity();
//		httpRequestEntity.setEngineName(CodeConstants.ENGINE_NAME_CAMERA);
//		
//		HttpResponseEntity result = this.startEngine(httpRequestEntity).getBody();
//		
//		return ResponseEntity.ok(result);
//	}
	
	
	/*
	 * 테스트, 셈플... 삭제 예정
	 */
	@RequestMapping(value="/finishJob", method=RequestMethod.GET)
	public ResponseEntity<HttpResponseEntity> finishJob() throws FileNotFoundException, IOException{
		
		HttpRequestEntity httpRequestEntity = new HttpRequestEntity();
		httpRequestEntity.setEngineName(CodeConstants.ENGINE_NAME_CAMERA);
		
		HttpResponseEntity result = this.finishJob(httpRequestEntity).getBody();
		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 작업이 완료되면 본 API를 호출하여 deployment를 제거
	 * @param httpRequestEntity
	 * @return
	 */
	@RequestMapping(value="/finishJob", method=RequestMethod.POST)
	public ResponseEntity<HttpResponseEntity> finishJob(@RequestBody HttpRequestEntity httpRequestEntity) {
		HttpResponseEntity result = new HttpResponseEntity();
		
		try {
			KubernetesAPIEntity kubernetesAPIEntity = deploymentService.finishJob(httpRequestEntity);
			
			Map<String, Object> apiResult = new HashMap<String, Object>();
			apiResult.put("kubernetesAPIEntity", kubernetesAPIEntity);
			
			result.setResponseData(apiResult);
			
			result.setError(false);
			result.setStatusCode(CodeConstants.HTTP_SUCCESS_CODE);
			result.setErrorMessage("");
			result.setResponseMessage("finishJob success.");
			
		} catch (EngineException e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("finishJob request is fail.");
			
		} catch (IOException e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("finishJob request is fail.");
		}
		
		return ResponseEntity.ok(result);
	}
	
	
	
}
