package dev.engine.loaders.mapLoader;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.terrains.Terrain;

public class Map {

	private List<Entity> allEntities;
	private List<Terrain> allTerrains;
	private List<Light> allLights;
	
	private float tilingFactor;
	private float fogDensity;
	private float fogGradient;
	private Vector3f skyColor;
	private float minDiffuseLighting;
	private float minSpecularLighting;
	
	public Map() {
		allEntities = new ArrayList<Entity>();
		allTerrains = new ArrayList<Terrain>();
		allLights = new ArrayList<Light>();
	}
	
	public void addEntities(List<Entity> e) {
		allEntities.addAll(e);
	}
	
	public void addEntity(Entity e) {
		allEntities.add(e);
	}
	
	public void addTerrains(List<Terrain> t) {
		allTerrains.addAll(t);
	}
	
	public void addTerrain(Terrain t) {
		allTerrains.add(t);
	}
	
	public void addLights(List<Light> l) {
		allLights.addAll(l);
	}
	
	public void addLight(Light l) {
		allLights.add(l);
	}

	public List<Entity> getAllEntities() {
		return allEntities;
	}

	public List<Terrain> getAllTerrains() {
		return allTerrains;
	}

	public List<Light> getAllLights() {
		return allLights;
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
	
}
