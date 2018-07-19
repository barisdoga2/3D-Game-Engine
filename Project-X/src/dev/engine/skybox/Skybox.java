package dev.engine.skybox;

import dev.engine.loaders.ImageLoader;
import dev.engine.loaders.Loader;
import dev.engine.models.RawModel;

public class Skybox {
	
	public static final int SKYBOX_SIZE = 1000;
	
	private String name;
	private RawModel cube;
	private int textureID;
	private String folderPath;
	
	public Skybox(String name, String folderPath) {
		this.name = name;
		this.folderPath = folderPath;
		this.textureID = ImageLoader.loadCubeMap(getTexturesAsStringArray());
		this.cube = Loader.loadToVAO(null, null, getVerticesArray(SKYBOX_SIZE), 3);
	}
	
	public RawModel getCube() {
		return cube;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFolderPath() {
		return folderPath;
	}

	private String[] getTexturesAsStringArray() {
		return new String[] {folderPath + "/right", folderPath + "/left", folderPath + "/top", folderPath + "/bottom", folderPath + "/back", folderPath + "/front"};
	}
	
	private static float[] getVerticesArray(float size) {
		return new float[] { 
				-size, size, -size, -size, -size, -size, size, -size, -size, size, -size, -size, size, size, -size, -size,
				size, -size,

				-size, -size, size, -size, -size, -size, -size, size, -size, -size, size, -size, -size, size, size, -size,
				-size, size,

				size, -size, -size, size, -size, size, size, size, size, size, size, size, size, size, -size, size, -size,
				-size,

				-size, -size, size, -size, size, size, size, size, size, size, size, size, size, -size, size, -size, -size,
				size,

				-size, size, -size, size, size, -size, size, size, size, size, size, size, -size, size, size, -size, size,
				-size,

				-size, -size, -size, -size, -size, size, size, -size, -size, size, -size, -size, -size, -size, size, size,
				-size, size
				};
	}
	
	public String toString() {
		return name;
	}
	
}
