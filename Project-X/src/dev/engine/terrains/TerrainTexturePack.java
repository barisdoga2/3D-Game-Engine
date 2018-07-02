package dev.engine.terrains;

public class TerrainTexturePack {
	
	public TerrainTexture backgroundTerrainTexture;
	public TerrainTexture rTerrainTexture;
	public TerrainTexture gTerrainTexture;
	public TerrainTexture bTerrainTexture;
	
	public TerrainTexturePack(TerrainTexture backgroundTerrainTexture, TerrainTexture rTerrainTexture, TerrainTexture gTerrainTexture, TerrainTexture bTerrainTexture) {
		this.backgroundTerrainTexture = backgroundTerrainTexture;
		this.rTerrainTexture = rTerrainTexture;
		this.gTerrainTexture = gTerrainTexture;
		this.bTerrainTexture = bTerrainTexture;
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

}
