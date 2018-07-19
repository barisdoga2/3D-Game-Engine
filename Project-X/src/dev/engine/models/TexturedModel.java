package dev.engine.models;

import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;

public class TexturedModel {
	
	private String name;
	private RawModel rawModel;
	private ModelTexture modelTexture;
	private BaseTexture normalMappingTexture;
	private BaseTexture specularMappingTexture;
	
	public TexturedModel(String name, RawModel rawModel, ModelTexture modelTexture, BaseTexture normalMappingTexture, BaseTexture specularMappingTexture) {
		this.name = name;
		this.rawModel = rawModel;
		this.modelTexture = modelTexture;
		this.normalMappingTexture = normalMappingTexture;
		this.specularMappingTexture = specularMappingTexture;
	}
	
	public TexturedModel(RawModel rawModel, ModelTexture modelTexture) {
		this.rawModel = rawModel;
		this.modelTexture = modelTexture;
	}
	
	public RawModel getRawModel() {
		return this.rawModel;
	}
	
	public ModelTexture getModelTexture() {
		return this.modelTexture;
	}

	public String getName() {
		return name;
	}

	public BaseTexture getNormalMappingTexture() {
		return normalMappingTexture;
	}
	
	public BaseTexture getSpecularMappingTexture() {
		return specularMappingTexture;
	}

	public void setModelTexture(ModelTexture modelTexture) {
		this.modelTexture = modelTexture;
	}

	public void setSpecularMappingTexture(BaseTexture specularTexture) {
		this.specularMappingTexture = specularTexture;
	}

	public void setNormalMappingTexture(BaseTexture normalTexture) {
		this.normalMappingTexture = normalTexture;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void setRawModel(RawModel rawModel) {
		this.rawModel = rawModel;
	}
	
}
