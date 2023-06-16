package com.nexr.de.grnd.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexr.de.grnd.constants.CodeConstants;
import com.nexr.de.grnd.entity.HttpRequestEntity;
import com.nexr.de.grnd.entity.HttpResponseEntity;
import com.nexr.de.grnd.util.FileUtil;
import com.nexr.de.grnd.util.PropertiesUtil;

@RestController
@RequestMapping("/demo")
public class DemoController {
	private static Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
	
	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	private Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	
	/**
	 * 클라이언트에서 작업을 요청할 때 호출하는 메서드
	 * @param edgeJobRequest
	 * @return
	 */
	@RequestMapping(value="/delayCheck", method=RequestMethod.POST)
//	public ResponseEntity<HttpResponse> sendEdgeJob(@RequestBody EdgeJobRequest edgeJobRequest){
//	public ResponseEntity<HttpResponse> sendEdgeJob(HttpServletResponse response){
	public ResponseEntity<Map<String, Object>> sendEdgeJob(@RequestBody Map requestBody){
		long sTime = System.currentTimeMillis();
		LOGGER.info(requestBody.toString());
		
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("startTime", requestBody.get("startTime"));
		result.put("requestId", requestBody.get("requestId"));
		result.put("duration", (System.currentTimeMillis()-sTime));
		
		return ResponseEntity.ok(result);
	}
	
	
	@RequestMapping(value="/getYaml", method=RequestMethod.POST)
	public ResponseEntity<HttpResponseEntity> getYaml(@RequestBody HttpRequestEntity httpRequestEntity) {
		HttpResponseEntity result = new HttpResponseEntity();
		
		try {
			String defaultPath = this.fileUtil.getDefaultPathByEngineName(httpRequestEntity);
			LOGGER.info("getDefaultPathByEngineName('{}')->{}", httpRequestEntity.getEngineName(), defaultPath);
			//getDefaultPathByEngineName('camera')->D:/RnD/dataset/camera
			
			String profilePath = Paths.get(defaultPath, httpRequestEntity.getDatasetName(), this.fileUtil.getProfileName(httpRequestEntity)).toString();
			LOGGER.info("profilePath->{}", profilePath);
			
			String profile = this.fileUtil.readTextFile(profilePath);
			
			Map<String, Object> searchResult = new HashMap<String, Object>();
			searchResult.put("profile", profile);
			
			result.setResponseData(searchResult);
			
			result.setError(false);
			result.setStatusCode(CodeConstants.HTTP_SUCCESS_CODE);
			result.setErrorMessage("");
			result.setResponseMessage("getDatasetList request is success.");
			
		} catch (Exception e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("getDatasetList request is fail.");
			LOGGER.error("getDatasetList - result ---->{}", result);
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
	}
	
//	@RequestMapping(value="/getDatasetList", method=RequestMethod.GET)
	@RequestMapping(value="/getDatasetList", method=RequestMethod.POST)
	public ResponseEntity<HttpResponseEntity> getDatasetList(@RequestBody HttpRequestEntity httpRequestEntity) {
		
		HttpResponseEntity result = new HttpResponseEntity();
		
		try {
			String defaultPath = this.fileUtil.getDefaultPathByEngineName(httpRequestEntity);
			LOGGER.info("getDefaultPathByEngineName('{}')->{}", httpRequestEntity.getEngineName(), defaultPath);
			
			
			List<Map<String, Object>> searchResultList = new ArrayList<Map<String,Object>>();
			Map<String, Object> resultItem = null;
			File[] datasetArr = new File(defaultPath).listFiles();
			
			if(datasetArr != null) {
				for(int idx = 0; idx < datasetArr.length; idx++) {
					File itemFile = datasetArr[idx];
					if(itemFile.isDirectory()) {
						File tempFile = new File(Paths.get(itemFile.toPath().toString(), "image").toString());
						if(tempFile.exists() && tempFile.list().length > 0) {
							resultItem = null;
							resultItem = new HashMap<String, Object>();
							resultItem.put("label", itemFile.getName());
							resultItem.put("value", itemFile.getName());
							searchResultList.add(resultItem);
						}else {
							LOGGER.info("'{}'는 exists:{}, child List:{}", tempFile.toPath().toString(), tempFile.exists(), tempFile.list());
						}
					}else {
						LOGGER.info("'{}'는 파일입니다.", itemFile.toPath().toString());
					}
				}
			}
			
			
			Map<String, Object> searchResult = new HashMap<String, Object>();
			searchResult.put("searchResult", searchResultList);
			
			result.setResponseData(searchResult);
			
			result.setError(false);
			result.setStatusCode(CodeConstants.HTTP_SUCCESS_CODE);
			result.setErrorMessage("");
			result.setResponseMessage("getDatasetList request is success.");
			
		} catch (Exception e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("getDatasetList request is fail.");
			LOGGER.error("getDatasetList - result ---->{}", result);
			e.printStackTrace();
		}
		
		
		
		return ResponseEntity.ok(result);
	}
	
	
	@RequestMapping(value="/getDatasetItemList", method=RequestMethod.POST)
	public ResponseEntity<HttpResponseEntity> getDatasetItemList(@RequestBody HttpRequestEntity httpRequestEntity) {
		
		HttpResponseEntity result = new HttpResponseEntity();
		
		try {

			Map<String, Object> searchResult = new HashMap<String, Object>();
			
			String datasetName = (String) httpRequestEntity.getDatasetName();
			
			//app 구동 상대 경로를 기준으로 움직임. 단, 파라메터의 맨 앞에 드라이브 경로가 상태 경로로 시작하면
			FileSystemResource resource = new FileSystemResource(this.fileUtil.getDefaultPathByEngineName(httpRequestEntity) + "org" + File.separator + datasetName + File.separator);
			LOGGER.info("############# resource.getPath :{}", resource.getPath());
			
			File file = new File(resource.getPath());
			
			File[] datasetArr = file.listFiles();
			
			List<Map<String, Object>> searchResultList = new ArrayList<Map<String,Object>>();
			
			//임시코드 시작
			if("camera0".equals(datasetName)) {
				Map<String, Object> resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/rgb_0.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/rgb_0_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/rgb_2.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/rgb_2_transformed.png");
				searchResultList.add(resultItem);
			}else if("camera1".equals(datasetName)) {
				Map<String, Object> resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000001.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000001_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000020.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000020_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000040.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000040_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000058.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000058_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000077.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000077_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000098.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000098_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000118.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000118_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000139.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000139_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000157.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000157_transformed.png");
				searchResultList.add(resultItem);
				resultItem = new HashMap<String, Object>();
				resultItem.put("orgPath", "/dataset/org/"+datasetName+"/image/20220915_153053_camera1_camera_image_raw_compressed_000175.png");
				resultItem.put("transformedPath", "/dataset/transformed/"+datasetName+"_transformed0/image/20220915_153053_camera1_camera_image_raw_compressed_000175_transformed.png");
				searchResultList.add(resultItem);
				
				
			}
			searchResult.put("yamlPath", "/dataset/org/" + datasetName + "/camera_info.yaml");
			//임시코드 끝
			
			searchResult.put("imgList", searchResultList);
			
			result.setResponseData(searchResult);
			
			result.setError(false);
			result.setStatusCode(CodeConstants.HTTP_SUCCESS_CODE);
			result.setErrorMessage("");
			result.setResponseMessage("getDatasetItemList request is success.");
			
			
		} catch (Exception e) {
			result.setError(true);
			result.setStatusCode(CodeConstants.HTTP_ERROR_CODE);
			result.setErrorMessage(e.getMessage());
			result.setResponseMessage("getDatasetItemList request is fail.");
			LOGGER.error("getDatasetItemList - result ---->{}", result);
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
	}
	
	
	
	/**
	 * 변환 결과 다운로드
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/imgDownload", method=RequestMethod.POST)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		String fileListStr = request.getParameter("fileList");
		String dataSetName = request.getParameter("datasetName");

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+dataSetName+"_download.zip");
		response.setStatus(HttpServletResponse.SC_OK);

		String[] fileNames = fileListStr.split(",");

		LOGGER.info("############# fileListStr :{}", fileListStr);

		try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
			for (String file : fileNames) {
				ClassLoader classLoader = getClass().getClassLoader();
				
				LOGGER.info("############# classLoader.getResource :{}", classLoader.getResource("static"+ file).getFile());
				LOGGER.info("############# FILE NAME :{}", file.substring(file.lastIndexOf("/") + 1));
				
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static"+ file);
				
				ZipEntry e = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
				e.setSize(inputStream.available());
				e.setTime(System.currentTimeMillis());
				// etc.
				zippedOut.putNextEntry(e);
				// And the content of the resource:
				StreamUtils.copy(inputStream, zippedOut);
				zippedOut.closeEntry();
			}
			zippedOut.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
