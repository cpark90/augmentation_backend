package com.nexr.de.grnd.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * application.properties에 정의된 값을 취득할 때 사용하는 유틸
 * @author ian.kim
 *
 */
@Component
public class PropertiesUtil {

	@Value("${engine.uri.getDataSetList}")
	private String engineUriGetDataSetList;
	
	@Value("${engine.uri.getDataSetItemList}")
	private String engineUriGetDataSetItemList;
	
	@Value("${engine.uri.imgDownload}")
	private String engineUriImgDownload;
	
	@Value("${kubernetes.configPath}")
	private String kubernetesConfigPath;
	
	@Value("${engine.camera.defaultPath}")
	private String cameraDefaultPath;

	@Value("${engine.lidar.defaultPath}")
	private String lidarDefaultPath;

	@Value("${engine.radar.defaultPath}")
	private String radarDefaultPath;
	
	@Value("${engine.format.defaultPath}")
	private String formatDefaultPath;
	
	@Value("${engine.camera.profileName}")
	private String cameraProfileName;
	
	@Value("${engine.lidar.profileName}")
	private String lidarProfileName;
	
	@Value("${engine.radar.profileName}")
	private String radarProfileName;
	
	@Value("${engine.format.profileName}")
	private String formatProfileName;
	
	@Value("${engine.pvcName}")
	private String persistentVolumeClaimName;
	
	@Value("${engine.config.path}")
	private String configPath;
	
	@Value("${engine.volumeMountPath}")
	private String volumeMountPath;
	
	@Value("${engine.waitMilliTime}")
	private long waitMilliTime;
	
	
	public long getWaitMilliTime() {
		return waitMilliTime;
	}

	public String getVolumeMountPath() {
		return volumeMountPath;
	}

	public String getPersistentVolumeClaimName() {
		return persistentVolumeClaimName;
	}

	public String getFormatProfileName() {
		return formatProfileName;
	}

	public String getCameraProfileName() {
		return cameraProfileName;
	}

	public String getLidarProfileName() {
		return lidarProfileName;
	}

	public String getRadarProfileName() {
		return radarProfileName;
	}

	public String getConfigPath() {
		return configPath;
	}

	public String getCameraDefaultPath() {
		return cameraDefaultPath;
	}

	public String getLidarDefaultPath() {
		return lidarDefaultPath;
	}

	public String getRadarDefaultPath() {
		return radarDefaultPath;
	}

	public String getFormatDefaultPath() {
		return formatDefaultPath;
	}

	public String getKubernetesConfigPath() {
		return kubernetesConfigPath;
	}
	
	public String getEngineUriImgDownload() {
		return engineUriImgDownload;
	}

	public String getEngineUriGetDataSetItemList() {
		return engineUriGetDataSetItemList;
	}

	public String getEngineUriGetDataSetList() {
		return engineUriGetDataSetList;
	}
	
	
}
