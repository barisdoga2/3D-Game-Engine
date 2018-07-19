package dev.engine.waters;

import dev.engine.loaders.ImageLoader;

public class WaterType {
	
	private String name;
	private String folderPath;
	private int dudvTextureID;
	private int normalTextureID;
	
	public WaterType(String name, String folderPath) {
		this.name = name;
		this.folderPath = folderPath;
		
		dudvTextureID = ImageLoader.loadTexture(folderPath + "/dudv");
		normalTextureID = ImageLoader.loadTexture(folderPath + "/normal");
	}

	public String getName() {
		return name;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public int getDudvTextureID() {
		return dudvTextureID;
	}

	public int getNormalTextureID() {
		return normalTextureID;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
