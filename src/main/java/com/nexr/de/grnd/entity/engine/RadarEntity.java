package com.nexr.de.grnd.entity.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nexr.de.grnd.constants.CodeConstants;

@Component
public class RadarEntity implements EngineEntity {
	
	@Value("${engine.radar.nameByMetadata}")
	private String nameByMetadata;
	
	@Value("${engine.radar.imageName}")
	private String imageName;
	
	@Value("${engine.radar.namespace}")
	private String namespace;
	
	
	@Override
	public String getEngineName() {
		return CodeConstants.ENGINE_NAME_RADAR;
	}
	
	@Override
	public String getNameByMetadata() {
		return this.nameByMetadata;
	}

	@Override
	public String getImageName() {
		return this.imageName;
	}

	@Override
	public String getNamespace() {
		return this.namespace;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "[imageName=" + this.imageName + ", namespace=" + this.namespace + 
				", nameByMetadata=" + this.nameByMetadata + 
				"]";
	}
}
