package dev.engine.models;

import dev.engine.textures.ModelTexture;
import dev.engine.textures.NormalTexture;
import dev.engine.textures.SpecularTexture;

public class TexturedModel {
	
	private String name;
	private RawModel rawModel;
	private ModelTexture modelTexture;
	private NormalTexture normalMappingTexture;
	private SpecularTexture specularMappingTexture;
	
	public TexturedModel(String name, RawModel rawModel, ModelTexture modelTexture, NormalTexture normalMappingTexture, SpecularTexture specularMappingTexture) {
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

	public NormalTexture getNormalMappingTexture() {
		return normalMappingTexture;
	}
	
	public SpecularTexture getSpecularMappingTexture() {
		return specularMappingTexture;
	}
	
}
