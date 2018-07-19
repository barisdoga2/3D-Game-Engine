package dev.engine.textures;

public class BaseTexture{
	
	private String name;
	private String path;
	private int textureID;
	
	public BaseTexture(String name, String path, int textureID) {
		this.name = name;
		this.path = path;
		this.textureID = textureID;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public int getTextureID() {
		return textureID;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
