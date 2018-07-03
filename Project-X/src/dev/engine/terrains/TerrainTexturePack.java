package dev.engine.terrains;

public class TerrainTexturePack {
	
	public TerrainTexture backgroundTerrainTexture;
	public TerrainTexture rTerrainTexture;
	public TerrainTexture gTerrainTexture;
	public TerrainTexture bTerrainTexture;
	public TerrainTexture aTerrainTexture;
	
	public TerrainTexturePack(TerrainTexture backgroundTerrainTexture, TerrainTexture rTerrainTexture, TerrainTexture gTerrainTexture, TerrainTexture bTerrainTexture, TerrainTexture aTerrainTexture) {
		this.backgroundTerrainTexture = backgroundTerrainTexture;
		this.rTerrainTexture = rTerrainTexture;
		this.gTerrainTexture = gTerrainTexture;
		this.bTerrainTexture = bTerrainTexture;
		this.aTerrainTexture = aTerrainTexture;
	}

	public TerrainTexture getBackgroundTerrainTexture() {
		return backgroundTerrainTexture;
	}

	public TerrainTexture getRTerrainTexture() {
		return rTerrainTexture;
	}

	public TerrainTexture getGTerrainTexture() {
		return gTerrainTexture;
	}

	public TerrainTexture getBTerrainTexture() {
		return bTerrainTexture;
	}

	public TerrainTexture getATerrainTexture() {
		return aTerrainTexture;
	}

}
