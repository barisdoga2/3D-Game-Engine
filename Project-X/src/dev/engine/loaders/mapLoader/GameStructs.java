package dev.engine.loaders.mapLoader;

import java.util.List;

import dev.engine.models.TexturedModel;
import dev.engine.terrains.TerrainTexture;
import dev.engine.textures.ModelTexture;
import dev.engine.textures.NormalTexture;
import dev.engine.textures.SpecularTexture;

public class GameStructs {

	protected static List<ModelTexture> allModelTextures;
	protected static List<SpecularTexture> allSpecularTextures;
	protected static List<NormalTexture> allNormalTextures;
	protected static List<TexturedModel> allTexturedModels;
	protected static List<TerrainTexture> allTerrainTextures;

	public static void init() {
		allModelTextures = ModelTextureLoader.loadAll();
		allSpecularTextures = SpecularTextureLoader.loadAll();
		allNormalTextures = NormalTextureLoader.loadAll();
		allTexturedModels = TexturedModelLoader.loadAll();
		allTerrainTextures = TerrainTextureLoader.loadAll();
	}

	public static ModelTexture getModelTexture(String name) {
		for (ModelTexture m : allModelTextures)
			if (m.getName().equals(name))
				return m;

		return null;
	}
	
	public static SpecularTexture getSpecularTexture(String name) {
		for (SpecularTexture m : allSpecularTextures)
			if (m.getName().equals(name))
				return m;

		return null;
	}
	
	public static NormalTexture getNormalTexture(String name) {
		for (NormalTexture m : allNormalTextures)
			if (m.getName().equals(name))
				return m;

		return null;
	}

	public static TerrainTexture getTerrainTexture(String name) {

		for (TerrainTexture m : allTerrainTextures) {
			if (m.getName().equals(name))
				return m;
		}

		return null;
	}

	public static TexturedModel getTexturedModel(String name) {
		for (TexturedModel m : allTexturedModels)
			if (m.getName().equals(name))
				return m;

		return null;
	}

	public static Map loadMap(String mapName) {
		return MapLoader.loadAll(mapName);
	}

}
