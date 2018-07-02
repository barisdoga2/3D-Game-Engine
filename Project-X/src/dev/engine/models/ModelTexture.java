package dev.engine.models;

public class ModelTexture {
	
	private int textureID;
	
	private boolean hasTransparency = false;
	private boolean hasFakeLighting = false;
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private int atlasNumberOfRows = 1;
	/*
	 * Atlas Indexing Example
	 * Atlases must be square.
	 * [0 , 1 , 2]
	 * [3 , 4 , 5]
	 * [6 , 7 , 8]
	 */
	
	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getTextureID() {
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isHasFakeLighting() {
		return hasFakeLighting;
	}

	public void setHasFakeLighting(boolean hasFakeLighting) {
		this.hasFakeLighting = hasFakeLighting;
	}

	public int getAtlasNumberOfRows() {
		return atlasNumberOfRows;
	}

	public void setAtlasNumberOfRows(int atlasNumberOfRows) {
		this.atlasNumberOfRows = atlasNumberOfRows;
	}
	
}
