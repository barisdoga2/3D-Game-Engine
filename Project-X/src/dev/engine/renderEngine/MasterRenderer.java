package dev.engine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import dev.engine.EngineConfig;
import dev.engine.entities.Camera;
import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.models.TexturedModel;
import dev.engine.shaders.EntityRenderer;
import dev.engine.shaders.EntityShader;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainRenderer;
import dev.engine.terrains.TerrainShader;
import dev.engine.utils.Maths;

public class MasterRenderer {
	
	private Matrix4f projectionMatrix;
	
	private EntityShader entityShader;
	private EntityRenderer entityRenderer;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer() {
		MasterRenderer.EnableCulling();
		this.projectionMatrix = Maths.createProjectionMatrix();
		
		this.entityShader = new EntityShader();
		this.entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		
		this.terrainShader = new TerrainShader();
		this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public void render(Light light, Camera camera) {
		prepare();
		
		// Rendering Terrains
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		
		terrainRenderer.render(terrains);
		
		terrainShader.stop();
		terrains.clear();
		
		
		// Rendering Entities
		entityShader.start();
		entityShader.loadLight(light);
		entityShader.loadViewMatrix(camera);
		
		entityRenderer.render(entities);
		
		entityShader.stop();
		entities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel texturedModel = entity.getTexturedModel();
		List<Entity> batch = entities.get(texturedModel);
		if(batch != null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(texturedModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public static void EnableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void DisableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(EngineConfig.SKY_COLOR.x, EngineConfig.SKY_COLOR.y, EngineConfig.SKY_COLOR.z, 1);
	}
	
	public void cleanUp() {
		entityShader.cleanUp();
	}
	
}
