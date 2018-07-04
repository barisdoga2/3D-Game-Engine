package dev.engine.textures;

public class SpecularTexture {
	
	private String name;
	private int textureID;
	
	public SpecularTexture(String name, int textureID) {
		this.name = name;
		this.textureID = textureID;
	}

	public String getName() {
		return name;
	}

	public int getTextureID() {
		return textureID;
	}
	
}
