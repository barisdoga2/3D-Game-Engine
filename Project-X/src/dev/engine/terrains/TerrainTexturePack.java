package dev.engine.terrains;

import dev.engine.textures.BaseTexture;

public class TerrainTexturePack {

	public BaseTexture backgroundTerrainTexture;
	public BaseTexture rTerrainTexture;
	public BaseTexture gTerrainTexture;
	public BaseTexture bTerrainTexture;
	public BaseTexture aTerrainTexture;

	public TerrainTexturePack(BaseTexture backgroundTerrainTexture, BaseTexture rTerrainTexture,
			BaseTexture gTerrainTexture, BaseTexture bTerrainTexture, BaseTexture aTerrainTexture) {
		this.backgroundTerrainTexture = backgroundTerrainTexture;
		this.rTerrainTexture = rTerrainTexture;
		this.gTerrainTexture = gTerrainTexture;
		this.bTerrainTexture = bTerrainTexture;
		this.aTerrainTexture = aTerrainTexture;
	}

	public BaseTexture getBackgroundTerrainTexture() {
		return backgroundTerrainTexture;
	}

	public BaseTexture getRTerrainTexture() {
		return rTerrainTexture;
	}

	public BaseTexture getGTerrainTexture() {
		return gTerrainTexture;
	}

	public BaseTexture getBTerrainTexture() {
		return bTerrainTexture;
	}

	public BaseTexture getATerrainTexture() {
		return aTerrainTexture;
	}

}
