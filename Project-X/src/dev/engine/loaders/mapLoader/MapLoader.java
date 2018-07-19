package dev.engine.loaders.mapLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
	
	private ResourceLoader resourceLoader;
	private List<File> allMapFiles = new ArrayList<File>();
	private List<Map> allMaps = new ArrayList<Map>();
	
	public MapLoader(List<File> mapFiles, ResourceLoader resourceLoader) throws Exception {
		this.allMapFiles = mapFiles;
		this.resourceLoader = resourceLoader;
	}
	
	public void loadAllMaps() throws Exception{
		for(File mapFile : allMapFiles)
			allMaps.add(new Map(mapFile, resourceLoader));
	}

	public void saveAllChanges() {
		for(Map map : allMaps)
			map.saveChanges();
	}

	public List<Map> getAllMaps() {
		return allMaps;
	}

	public Map addMap(String mapName){
		try {
			File f = File.createTempFile(mapName + "-temp", ".xml");
			String str = "<" + mapName + ">\n";
			str += "<settings>";
			str += "<tilingFactor>40.0</tilingFactor>\n";
			str += "<fogDensity>0.002</fogDensity>\n";
			str += "<fogGradient>5.0</fogGradient>\n";
			str += "<skyColor>0.0, 0.65, 0.85</skyColor>\n";
			str += "<minDiffuseLighting>0.33</minDiffuseLighting>\n";
			str += "<minSpecularLighting>0.0</minSpecularLighting>\n";
			str += "<skyboxName>skybox</skyboxName>\n";
			str += "<waterTypeName>water</waterTypeName>\n";
			str += "<waterWaveMoveSpeed>0.03</waterWaveMoveSpeed>\n";
			str += "<waterWaveStrength>0.04</waterWaveStrength>\n";
			str += "<waterReflectivityFactor>0.5</waterReflectivityFactor>\n";
			str += "<waterTilingFactor>1.0</waterTilingFactor>\n";
			str += "<waterShineDamper>10.0</waterShineDamper>\n";
			str += "<waterReflectivity>0.5</waterReflectivity>\n";
			str += "</settings>\n";
			str += "<waters>\n";
			str += "</waters>\n";
			str += "<terrains>\n";
			str += "</terrains>\n";
			str += "<lights>\n";
			str += "</lights>\n";
			str += "<entities>\n";
			str += "</entities>\n";
			str += "</" + mapName + ">\n";
		    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		    writer.write(str);
		    writer.close();
		    
			Map addedMap = new Map(f, mapName, resourceLoader);
			allMaps.add(addedMap);
			return addedMap;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
