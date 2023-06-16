package com.nexr.de.grnd.entity;

import java.util.Map;

public class HttpRequestEntity {
	private String engineName;//엔진의 종류
	private String datasetName;//UI에서 선택한 입력데이터
	private Map<String, Object> engineProfile;//config 파일 내용
	private String configfilePath;//transform_config_path : 파일명까지 저장함(20230511), startEngine 호출시 사용
	private String sensorProfilePath;//src_sensor_profile_path, startEngine 호출시 사용
	private String newFolderName;//사용자가 입력한 데이터명 : 입력데이터 경로 만들기 용도로 사용함
	
	/**
	 * 사용자가 입력한 데이터명 : 입력데이터 경로 만들기 용도로 사용함
	 * @return
	 */
	public String getNewFolderName() {
		return newFolderName;
	}

	public void setNewFolderName(String newFolderName) {
		this.newFolderName = newFolderName;
	}
	
	/**
	 * 
	 * @return UI에서 선택한 입력데이터
	 */
	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	/**
	 * transform_config_path : 파일명까지 저장함(20230511), startEngine 호출시 사용
	 * @return
	 */
	public String getConfigfilePath() {
		return configfilePath;
	}

	public void setConfigfilePath(String configfilePath) {
		this.configfilePath = configfilePath;
	}

	/**
	 * src_sensor_profile_path, startEngine 호출시 사용
	 * @return
	 */
	public String getSensorProfilePath() {
		return sensorProfilePath;
	}

	public void setSensorProfilePath(String sensorProfilePath) {
		this.sensorProfilePath = sensorProfilePath;
	}

	/**
	 * 
	 * @return config 파일 내용
	 */
	public Map<String, Object> getEngineProfile() {
		return engineProfile;
	}

	public void setEngineProfile(Map<String, Object> engineProfile) {
		this.engineProfile = engineProfile;
	}
	
	/**
	 * 
	 * @return 엔진의 종류
	 */
	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[engineName=" + this.engineName + ", engineProfile=" + this.engineProfile + 
				", datasetName=" + this.datasetName + ", configfilePath=" + this.configfilePath + 
				", sensorProfilePath=" + this.sensorProfilePath + ", newFolderName=" + this.newFolderName + 
				"]";
	}
}
