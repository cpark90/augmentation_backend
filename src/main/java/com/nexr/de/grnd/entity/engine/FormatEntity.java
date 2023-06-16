package com.nexr.de.grnd.entity.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nexr.de.grnd.constants.CodeConstants;

@Component
public class FormatEntity implements EngineEntity {
	
	@Value("${engine.format.nameByMetadata}")
	private String nameByMetadata;
	
	@Value("${engine.format.imageName}")
	private String imageName;
	
	@Value("${engine.format.namespace}")
	private String namespace;
	
	
	@Override
	public String getEngineName() {
		return CodeConstants.ENGINE_NAME_FORMAT;
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
