package dev.engine.terrains;

public class TerrainTexture {

	private String name;
	private int textureID;

	public TerrainTexture(String name, int textureID) {
		this.name = name;
		this.textureID = textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public String getName() {
		return name;
	}

}
