package dev.engine.loaders.mapLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.engine.entities.Entity;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;
import dev.engine.skybox.Skybox;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainTexturePack;
import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;
import dev.engine.waters.WaterType;

public class ResourceLoader {
	
	public static ResourceLoader instance;
	
	private File resourceFolder;
	private List<BaseTexture> allBaseTextures = new ArrayList<BaseTexture>();
	private List<ModelTexture> allModelTextures = new ArrayList<ModelTexture>();
	private List<TexturedModel> allTexturedModels = new ArrayList<TexturedModel>();
	private List<Skybox> allSkyboxes = new ArrayList<Skybox>();
	private List<WaterType> allWaterTypes = new ArrayList<WaterType>();
	
	private XMLBaseTextures baseTexturesXML;
	private XMLModelTextures modelTexturesXML;
	private XMLStructures structuresXML;
	
	private MapLoader mapLoader;
	
	public ResourceLoader(File f) throws Exception{
		instance = this;
		resourceFolder = f;
		baseTexturesXML = new XMLBaseTextures(new File(f.getPath().replace('\\', '/').toString() + "/xml/BaseTextures.xml"), this);
		modelTexturesXML = new XMLModelTextures(new File(f.getPath().replace('\\', '/').toString() + "/xml/ModelTextures.xml"), this);
		structuresXML = new XMLStructures(new File(f.getPath().replace('\\', '/').toString() + "/xml/Structures.xml"), this);
		
		List<File> allMaps = new ArrayList<File>();
		File[] allFiles = new File(f.getPath().replace('\\', '/').toString() + "/xml/").listFiles();
		for(File ff : allFiles)
			if(ff.isDirectory() && ff.getName().endsWith("Map"))
				allMaps.add(new File(ff.getAbsolutePath() + "\\" + ff.getName() + ".xml"));
		mapLoader = new MapLoader(allMaps, this);
	}
	
	public void loadAllResources() throws Exception {
		baseTexturesXML.loadAll();
		modelTexturesXML.loadAll();
		structuresXML.loadAll();
		mapLoader.loadAllMaps();
	}
	
	public void saveChanges() {
		baseTexturesXML.saveChanges();
		modelTexturesXML.saveChanges();
		structuresXML.saveChanges();
		mapLoader.saveAllChanges();
	}
	
	public BaseTexture getBaseTextureByName(String name) {
		for(BaseTexture e : allBaseTextures)
			if(e.getName().equals(name))
				return e;
		return null;
	}

	public ModelTexture getModelTextureByName(String name) {
		for(ModelTexture e : allModelTextures)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	public TexturedModel getTexturedModelByName(String name) {
		for(TexturedModel e : allTexturedModels)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	public Skybox getSkyboxByName(String name) {
		for(Skybox e : allSkyboxes)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	public WaterType getWaterTypeByName(String name) {
		for(WaterType e : allWaterTypes)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	public Map getMapByName(String name) {
		for(Map map : mapLoader.getAllMaps())
			if(map.getName().equals(name))
				return map;
		return null;
	}
	
	public void addBaseTexture(BaseTexture baseTexture) {
		baseTexturesXML.addBaseTexture(baseTexture);
	}
	
	public boolean removeBaseTexture(BaseTexture baseTexture) {
		for(ModelTexture t : allModelTextures)
			if(t.getBaseTexture().equals(baseTexture))
				return false;
		for(Map m : getAllMaps()) 
			for(Terrain t : m.getAllTerrains()) {
				TerrainTexturePack ttp = t.getTerrainTexturePack();
				if(t.getBlendMapTexture().equals(baseTexture) || t.getHeightMapTexture().equals(baseTexture) || ttp.getBackgroundTerrainTexture().equals(baseTexture) || ttp.getATerrainTexture().equals(baseTexture) || ttp.getRTerrainTexture().equals(baseTexture) || ttp.getGTerrainTexture().equals(baseTexture) || ttp.getBTerrainTexture().equals(baseTexture))
					return false;
			}
			
		baseTexturesXML.removeBaseTexture(baseTexture);
		return true;
	}
	
	public void addModelTexture(ModelTexture modelTexture) {
		modelTexturesXML.addModelTexture(modelTexture);
	}
	
	public boolean removeTexturedModel(TexturedModel texturedModel) {
		for(Map m : getAllMaps()) 
			for(Entity t : m.getAllEntities()) {
				if(t.getTexturedModel().equals(texturedModel))
					return false;
			}
		structuresXML.removeTexturedModel(texturedModel);
		return true;
	}
	
	public void addTexturedModel(TexturedModel texturedModel) {
		structuresXML.addTexturedModel(texturedModel);
	}
	
	public boolean removeModelTexture(ModelTexture modelTexture) {
		for(TexturedModel t : getAllTexturedModels()) 
			if(t.getModelTexture().equals(modelTexture))
				return false;
		
		modelTexturesXML.removeModelTexture(modelTexture);
		return true;
	}
	
	public void addSkybox(Skybox skybox) {
		structuresXML.addSkybox(skybox);
	}
	
	public boolean removeSkybox(Skybox skybox) {
		for(Map t : getAllMaps()) 
			if(t.getMapSettings().getSkybox().equals(skybox))
				return false;
		
		structuresXML.removeSkybox(skybox);
		return true;
	}
	
	public void addWaterType(WaterType waterType) {
		structuresXML.addWaterType(waterType);
	}
	
	public boolean removeWaterType(WaterType waterType) {
		for(Map t : getAllMaps()) 
			if(t.getMapSettings().getWaterType().equals(waterType))
				return false;
		
		structuresXML.removeWaterType(waterType);
		return true;
	}
	
	public void addMap(String mapName) {
		mapLoader.addMap(mapName);
	}

	public List<BaseTexture> getAllBaseTextures() {
		return allBaseTextures;
	}

	public List<ModelTexture> getAllModelTextures() {
		return allModelTextures;
	}

	public List<TexturedModel> getAllTexturedModels() {
		return allTexturedModels;
	}

	public List<Skybox> getAllSkyboxes() {
		return allSkyboxes;
	}

	public List<WaterType> getAllWaterTypes() {
		return allWaterTypes;
	}

	public List<Map> getAllMaps() {
		return mapLoader.getAllMaps();
	}

	public File getResourceFolder() {
		return resourceFolder;
	}

	public List<RawModel> getAllRawModels() {
		List<RawModel> retVal = new ArrayList<RawModel>();
		for(TexturedModel tm : allTexturedModels)
			if(tm.getRawModel().getName() != null)
				retVal.add(tm.getRawModel());
		return retVal;
	}
	
}
