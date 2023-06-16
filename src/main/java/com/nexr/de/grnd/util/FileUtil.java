package com.nexr.de.grnd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexr.de.grnd.constants.CodeConstants;
import com.nexr.de.grnd.entity.HttpRequestEntity;
import com.nexr.de.grnd.exception.EngineException;


@Component
public class FileUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	public String makeJsonConfigFile(HttpRequestEntity httpRequestEntity) throws IOException, EngineException {
		String result = this.propertiesUtil.getConfigPath();
		
		File configPathObj = new File(result);
		if(!configPathObj.exists()) {
			configPathObj.mkdirs();
		}
		
//		String fileName = httpRequestEntity.getEngineName() + "_info.json";//파일명 uuid로 만들기
		String fileName = httpRequestEntity.getEngineName() + "_" + UUID.randomUUID().toString() + ".json";//파일명 uuid로 만들기
		result = Paths.get(result, fileName).toString();
		
		try (FileWriter fileWriter = new FileWriter( result );) {
			Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
			fileWriter.write( gson.toJson(httpRequestEntity.getEngineProfile()) );
			fileWriter.flush();
		}
		
		return result;
	}
	
	public String makeYamlConfigFile(HttpRequestEntity httpRequestEntity) throws IOException {
		String result = this.propertiesUtil.getConfigPath();
		
		File configPathObj = new File(result);
		if(!configPathObj.exists()) {
			configPathObj.mkdirs();
		}
		
//		String fileName = httpRequestEntity.getEngineName() + "_info.yaml";//파일명 uuid로 만들기
		String fileName = httpRequestEntity.getEngineName() + "_" + UUID.randomUUID().toString() + ".yaml";//파일명 uuid로 만들기
		result = Paths.get(result, fileName).toString();
		
		try (FileWriter fileWriter = new FileWriter( result );) {
			new Yaml().dump(httpRequestEntity.getEngineProfile(), fileWriter);
		}
		
		return result;
	}
	
	public String getDefaultPathByEngineName(HttpRequestEntity httpRequestEntity) throws EngineException {
		String result = "";
		
		switch (httpRequestEntity.getEngineName()) {
			case CodeConstants.ENGINE_NAME_CAMERA:
				result = this.propertiesUtil.getCameraDefaultPath();
				break;
			case CodeConstants.ENGINE_NAME_LIDAR:
				result = this.propertiesUtil.getLidarDefaultPath();
				break;
			case CodeConstants.ENGINE_NAME_RADAR:
				result = this.propertiesUtil.getRadarDefaultPath();
				break;
			case CodeConstants.ENGINE_NAME_FORMAT:
				result = this.propertiesUtil.getFormatDefaultPath();
				break;
			default:
				break;
		}
		
		if("".equals(result)) {
			throw new EngineException("'" + httpRequestEntity.getEngineName()+ "'는(은) 지원하지 않는 엔진입니다.", httpRequestEntity.getEngineName());
		}
		
		return result;
	}
	
	public String getProfileName(HttpRequestEntity httpRequestEntity) throws EngineException {
		String result = "";
		
		switch (httpRequestEntity.getEngineName()) {
			case CodeConstants.ENGINE_NAME_CAMERA:
				result = this.propertiesUtil.getCameraProfileName();
				break;
			case CodeConstants.ENGINE_NAME_LIDAR:
				result = this.propertiesUtil.getLidarProfileName();
				break;
			case CodeConstants.ENGINE_NAME_RADAR:
				result = this.propertiesUtil.getRadarProfileName();
				break;
			case CodeConstants.ENGINE_NAME_FORMAT:
				result = this.propertiesUtil.getFormatProfileName();
				break;
			default:
				break;
		}
		
		if("".equals(result)) {
			throw new EngineException("'" + httpRequestEntity.getEngineName()+ "'는(은) 지원하지 않는 엔진입니다.", httpRequestEntity.getEngineName());
		}
		
		return result;
	}
	
	public String readTextFile(String filePath) throws Exception {
		String result = "";
		
		try(
			InputStream inputStream = new FileInputStream(new File(filePath));
		) {
			
			Yaml y = new Yaml();
			Map<String, Object> mapResult = y.loadAs(inputStream, Map.class);
			
			Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
			result = gson.toJson(mapResult);
			
		}catch(Exception e) {
			LOGGER.info("File read fail. filePath : {}", filePath);
			throw e;
		}

		return result;
	}
	
}
