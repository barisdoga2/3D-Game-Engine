package dev.engine.loaders.mapLoader;

import java.util.List;

import dev.engine.models.ModelTexture;
import dev.engine.models.TexturedModel;
import dev.engine.terrains.TerrainTexture;

public class GameStructs {
	
	protected static List<ModelTexture> allModelTextures;
	protected static List<TexturedModel> allTexturedModels;
	protected static List<TerrainTexture> allTerrainTextures;
		
	public static void init(){
		allModelTextures = ModelTextureLoader.loadAll();
		allTexturedModels = TexturedModelLoader.loadAll();
		allTerrainTextures = TerrainTextureLoader.loadAll();
	}
	
	public static ModelTexture getModelTexture(String name){
		for(ModelTexture m : allModelTextures)
			if(m.getName().equals(name)) return m;
		
		return null;
	}
	
	public static TerrainTexture getTerrainTexture(String name){
		
		for(TerrainTexture m : allTerrainTextures) {
			if(m.getName().equals(name)) return m;
		}
		
		return null;
	}
	
	public static TexturedModel getTexturedModel(String name){
		for(TexturedModel m : allTexturedModels)
			if(m.getName().equals(name)) return m;
		
		return null;
	}

	public static Map loadMap(String mapName){
		return MapLoader.loadAll(mapName);
	}
	
}
