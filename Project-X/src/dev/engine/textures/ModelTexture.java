package dev.engine.textures;

public class ModelTexture{

	private String name;
	private BaseTexture baseTexture;
	
	private boolean hasTransparency = false;
	private boolean hasFakeLighting = false;
	private float shineDamper = 1;
	private float reflectivity = 0;
	private int atlasNumberOfRows = 1;

	public ModelTexture(String name, BaseTexture baseTexture, boolean hasTransparency, boolean hasFakeLighting, float shineDamper, float reflectivity, int atlasNumberOfRows) {
		this.name = name;
		this.baseTexture = baseTexture;
		this.hasTransparency = hasTransparency;
		this.hasFakeLighting = hasFakeLighting;
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
		this.atlasNumberOfRows = atlasNumberOfRows;
	}
	
	public ModelTexture(String name, BaseTexture baseTexture) {
		this.name = name;
		this.baseTexture = baseTexture;
	}
	
	public BaseTexture getBaseTexture() {
		return baseTexture;
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

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public void setBaseTexture(BaseTexture baseTexture) {
		this.baseTexture = baseTexture;
	}

}
