package dev.engine.models;

public class TexturedModel {
	
	private String name;
	private RawModel rawModel;
	private ModelTexture modelTexture;
	private ModelTexture normalMappingTexture;
	
	public TexturedModel(String name, RawModel rawModel, ModelTexture modelTexture, ModelTexture normalMappingTexture) {
		this.name = name;
		this.rawModel = rawModel;
		this.modelTexture = modelTexture;
		this.normalMappingTexture = normalMappingTexture;
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

	public ModelTexture getNormalMappingTexture() {
		return normalMappingTexture;
	}
	
}
