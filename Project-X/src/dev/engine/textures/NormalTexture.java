package dev.engine.textures;

public class NormalTexture {
	
	private String name;
	private int textureID;
	
	public NormalTexture(String name, int textureID) {
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
