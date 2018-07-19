package dev.engine.loaders.mapLoader;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import dev.engine.skybox.Skybox;
import dev.engine.waters.WaterType;

public class MapSettings {
	
	private float tilingFactor;
	private float fogDensity;
	private float fogGradient;
	private Vector3f skyColor;
	private float minDiffuseLighting;
	private float minSpecularLighting;
	
	private Skybox skybox;
	private WaterType waterType;
	
	private float waterWaveMoveSpeed;
	private float waterWaveStrength;
	private float waterTilingFactor;
	private float waterReflectivityFactor;
	private float waterShineDamper;
	private float waterReflectivity;
	
	private MapSettings() { }
	
	public void saveAllSettings(Map map) {
		Node nNode = map.doc.getElementsByTagName("settings").item(0);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			
			eElement.getElementsByTagName("tilingFactor").item(0).setTextContent("" + tilingFactor);
			eElement.getElementsByTagName("fogDensity").item(0).setTextContent("" + fogDensity);
			eElement.getElementsByTagName("fogGradient").item(0).setTextContent("" + fogGradient);
			eElement.getElementsByTagName("skyColor").item(0).setTextContent(skyColor.x + ", " + skyColor.y + ", " + skyColor.z);
			eElement.getElementsByTagName("minDiffuseLighting").item(0).setTextContent("" + minDiffuseLighting);
			eElement.getElementsByTagName("minSpecularLighting").item(0).setTextContent("" + minSpecularLighting);
			
			eElement.getElementsByTagName("skyboxName").item(0).setTextContent(skybox.getName());
			eElement.getElementsByTagName("waterTypeName").item(0).setTextContent(waterType.getName());
			
			eElement.getElementsByTagName("waterWaveMoveSpeed").item(0).setTextContent("" + waterWaveMoveSpeed);
			eElement.getElementsByTagName("waterWaveStrength").item(0).setTextContent("" + waterWaveStrength);
			eElement.getElementsByTagName("waterTilingFactor").item(0).setTextContent("" + waterTilingFactor);
			eElement.getElementsByTagName("waterReflectivityFactor").item(0).setTextContent("" + waterReflectivityFactor);
			eElement.getElementsByTagName("waterShineDamper").item(0).setTextContent("" + waterShineDamper);
			eElement.getElementsByTagName("waterReflectivity").item(0).setTextContent("" + waterReflectivity);	
		}
	}
	
	public float getTilingFactor() {
		return tilingFactor;
	}

	public void setTilingFactor(float tilingFactor) {
		this.tilingFactor = tilingFactor;
	}

	public float getFogDensity() {
		return fogDensity;
	}

	public void setFogDensity(float fogDensity) {
		this.fogDensity = fogDensity;
	}

	public float getFogGradient() {
		return fogGradient;
	}

	public void setFogGradient(float fogGradient) {
		this.fogGradient = fogGradient;
	}

	public Vector3f getSkyColor() {
		return skyColor;
	}

	public void setSkyColor(Vector3f skyColor) {
		this.skyColor = skyColor;
	}

	public float getMinDiffuseLighting() {
		return minDiffuseLighting;
	}

	public void setMinDiffuseLighting(float minDiffuseLighting) {
		this.minDiffuseLighting = minDiffuseLighting;
	}

	public float getMinSpecularLighting() {
		return minSpecularLighting;
	}

	public void setMinSpecularLighting(float minSpecularLighting) {
		this.minSpecularLighting = minSpecularLighting;
	}
	
	public float getWaterWaveMoveSpeed() {
		return waterWaveMoveSpeed;
	}

	public void setWaterWaveMoveSpeed(float waterWaveMoveSpeed) {
		this.waterWaveMoveSpeed = waterWaveMoveSpeed;
	}

	public float getWaterWaveStrength() {
		return waterWaveStrength;
	}

	public void setWaterWaveStrength(float waterWaveStrength) {
		this.waterWaveStrength = waterWaveStrength;
	}

	public float getWaterTilingFactor() {
		return waterTilingFactor;
	}

	public void setWaterTilingFactor(float waterTilingFactor) {
		this.waterTilingFactor = waterTilingFactor;
	}

	public float getWaterReflectivityFactor() {
		return waterReflectivityFactor;
	}

	public void setWaterReflectivityFactor(float waterReflectivityFactor) {
		this.waterReflectivityFactor = waterReflectivityFactor;
	}

	public float getWaterShineDamper() {
		return waterShineDamper;
	}

	public void setWaterShineDamper(float waterShineDamper) {
		this.waterShineDamper = waterShineDamper;
	}

	public float getWaterReflectivity() {
		return waterReflectivity;
	}

	public void setWaterReflectivity(float waterReflectivity) {
		this.waterReflectivity = waterReflectivity;
	}

	public Skybox getSkybox() {
		return skybox;
	}

	public void setSkybox(Skybox skybox) {
		this.skybox = skybox;
	}

	public WaterType getWaterType() {
		return waterType;
	}

	public void setWaterType(WaterType waterType) {
		this.waterType = waterType;
	}

	public static MapSettings getMapSettings(Map map) {
		MapSettings mapSettings = new MapSettings();
		
		Node nNode = map.doc.getElementsByTagName("settings").item(0);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;

			String tilingFactor = eElement.getElementsByTagName("tilingFactor").item(0).getTextContent();
			String fogDensity = eElement.getElementsByTagName("fogDensity").item(0).getTextContent();
			String fogGradient = eElement.getElementsByTagName("fogGradient").item(0).getTextContent();
			String[] skyColorTokens = eElement.getElementsByTagName("skyColor").item(0).getTextContent().split(",");
			String minDiffuseLighting = eElement.getElementsByTagName("minDiffuseLighting").item(0).getTextContent();
			String minSpecularLighting = eElement.getElementsByTagName("minSpecularLighting").item(0).getTextContent();
			
			Skybox skybox = map.resourceLoader.getSkyboxByName(eElement.getElementsByTagName("skyboxName").item(0).getTextContent());
			WaterType waterType = map.resourceLoader.getWaterTypeByName(eElement.getElementsByTagName("waterTypeName").item(0).getTextContent());
			
			float waterWaveMoveSpeed = Float.parseFloat(eElement.getElementsByTagName("waterWaveMoveSpeed").item(0).getTextContent());
			float waterWaveStrength = Float.parseFloat(eElement.getElementsByTagName("waterWaveStrength").item(0).getTextContent());
			float waterTilingFactor = Float.parseFloat(eElement.getElementsByTagName("waterTilingFactor").item(0).getTextContent());
			float waterReflectivityFactor = Float.parseFloat(eElement.getElementsByTagName("waterReflectivityFactor").item(0).getTextContent());
			float waterShineDamper = Float.parseFloat(eElement.getElementsByTagName("waterShineDamper").item(0).getTextContent());
			float waterReflectivity = Float.parseFloat(eElement.getElementsByTagName("waterReflectivity").item(0).getTextContent());

			mapSettings.setTilingFactor(Float.parseFloat(tilingFactor));
			mapSettings.setFogDensity(Float.parseFloat(fogDensity));
			mapSettings.setFogGradient(Float.parseFloat(fogGradient));
			mapSettings.setSkyColor(new Vector3f(Float.parseFloat(skyColorTokens[0]), Float.parseFloat(skyColorTokens[1]),Float.parseFloat(skyColorTokens[2])));
			mapSettings.setMinDiffuseLighting(Float.parseFloat(minDiffuseLighting));
			mapSettings.setMinSpecularLighting(Float.parseFloat(minSpecularLighting));
			mapSettings.setWaterWaveMoveSpeed(waterWaveMoveSpeed);
			mapSettings.setWaterWaveStrength(waterWaveStrength);
			mapSettings.setWaterTilingFactor(waterTilingFactor);
			mapSettings.setWaterReflectivityFactor(waterReflectivityFactor);
			mapSettings.setWaterShineDamper(waterShineDamper);
			mapSettings.setWaterReflectivity(waterReflectivity);
			mapSettings.setSkybox(skybox);
			mapSettings.setWaterType(waterType);
			
		}
		
		return mapSettings;
	}
	
}
