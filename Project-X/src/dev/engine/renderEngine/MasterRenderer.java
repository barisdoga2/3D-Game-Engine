package dev.engine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import dev.engine.entities.Camera;
import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.entities.EntityRenderer;
import dev.engine.renderEngine.entities.EntityShader;
import dev.engine.renderEngine.entities.NMEntityRenderer;
import dev.engine.renderEngine.entities.NMEntityShader;
import dev.engine.renderEngine.skybox.SkyboxRenderer;
import dev.engine.renderEngine.skybox.SkyboxShader;
import dev.engine.renderEngine.terrains.TerrainRenderer;
import dev.engine.renderEngine.terrains.TerrainShader;
import dev.engine.renderEngine.waters.WaterRenderer;
import dev.engine.renderEngine.waters.WaterShader;
import dev.engine.terrains.Terrain;
import dev.engine.utils.Maths;
import dev.engine.waters.WaterFrameBuffer;
import dev.engine.waters.WaterTile;

public class MasterRenderer {

	private static boolean renderSkybox = true;
	
	private Matrix4f projectionMatrix;
	private Vector3f skyColor;

	private EntityShader entityShader;
	private EntityRenderer entityRenderer;
	private Map<TexturedModel, List<Entity>> allEntities = new HashMap<TexturedModel, List<Entity>>();
	
	private NMEntityShader nmEntityShader;
	private NMEntityRenderer nmEntityRenderer;
	private Map<TexturedModel, List<Entity>> allNMEntities = new HashMap<TexturedModel, List<Entity>>();

	private Vector4f brushColor = new Vector4f(1.0f, 1.0f, 1.0f, 0.5f);
	private static Vector3f brushPosition = new Vector3f(0.0f, 0.0f, 0.0f);
	private static float brushWidth = 0f;
	private TerrainShader terrainShader;
	private TerrainRenderer terrainRenderer;
	private List<Terrain> allTerrains = new ArrayList<Terrain>();
  
	private SkyboxShader skyboxShader;
	private SkyboxRenderer skyboxRenderer;
	
	private WaterFrameBuffer waterFrameBuffer = new WaterFrameBuffer();
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;

	private List<WaterTile> allWaters = new ArrayList<WaterTile>();
  
	public MasterRenderer(dev.engine.loaders.mapLoader.Map map) {
		MasterRenderer.EnableCulling();
		this.projectionMatrix = Maths.createProjectionMatrix();
		this.skyColor = map.getMapSettings().getSkyColor();

		this.entityShader = new EntityShader();
		this.entityRenderer = new EntityRenderer(entityShader, projectionMatrix, map);

		this.terrainShader = new TerrainShader();
		this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix, map);
		
		this.skyboxShader = new SkyboxShader();
		this.skyboxRenderer = new SkyboxRenderer(skyboxShader, projectionMatrix, map);
		
		this.nmEntityShader = new NMEntityShader();
		this.nmEntityRenderer = new NMEntityRenderer(nmEntityShader, projectionMatrix, map);
		
		this.waterShader = new WaterShader();
		this.waterRenderer = new WaterRenderer(waterShader, projectionMatrix, waterFrameBuffer, map);
	}
	
	public void reLoadSettings() {
		entityRenderer.reLoadSettings();
		terrainRenderer.reLoadSettings();
		skyboxRenderer.reLoadSettings();
		nmEntityRenderer.reLoadSettings();
		waterRenderer.reLoadSettings();
	}
	
	public void renderScene(Camera camera, List<Light> lights) {
		this.render(camera, lights, new Vector4f(0, -1, 0, 100000));
	}
	
	public void renderWaters(Camera testCamera, List<Light> lights) {
		waterShader.start();
		waterShader.loadLights(lights);
		waterShader.loadViewMatrix(testCamera);
		waterRenderer.render(allWaters);
		waterShader.stop();
	}
	
	public void renderWaterEffects(Camera camera, List<Light> lights) {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		waterFrameBuffer.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - WaterTile.WATER_HEIGHT);
		camera.getPosition().y -= distance;
		camera.invertPitch();
		this.renderScene(camera, lights, new Vector4f(0, 1, 0, -WaterTile.WATER_HEIGHT + 1f));
		camera.getPosition().y += distance;
		camera.invertPitch();
		
		waterFrameBuffer.bindRefractionFrameBuffer();
		this.renderScene(camera, lights, new Vector4f(0, -1, 0, WaterTile.WATER_HEIGHT + 1f));
		
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		waterFrameBuffer.unbindCurrentFrameBuffer();
	}
	
	private void renderScene(Camera camera, List<Light> lights, Vector4f clipPlane) {
		this.render(camera, lights, clipPlane);
	}

	private void render(Camera camera, List<Light> lights, Vector4f clipPlane) {
		prepare();		
		
		// Rendering Skybox
		if(renderSkybox)
			skyboxRenderer.render(camera);
		
		// Rendering Terrains
		terrainShader.start();
		terrainShader.loadBrushInfo(brushWidth, brushPosition, brushColor);
		terrainShader.loadClipPane(clipPlane);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(allTerrains);
		terrainShader.stop();
		
		// Rendering Entities
		entityShader.start();
		entityShader.loadClipPane(clipPlane);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(allEntities);
		entityShader.stop();
		
		// Rendering NMEntities
		nmEntityShader.start();
		nmEntityShader.loadClipPane(clipPlane);
		nmEntityShader.loadLights(lights, camera);
		nmEntityShader.loadViewMatrix(camera);
		nmEntityRenderer.render(allNMEntities);
		nmEntityShader.stop();
		
	}
	
	public void cleanRenderObjects() {
		allEntities.clear();
		allNMEntities.clear();
		allTerrains.clear();
		allWaters.clear();
	}

	public void processEntities(List<Entity> entities) {
		for (Entity entity : entities)
			processEntity(entity);
	}

	public void processEntity(Entity entity) {
		
		Map<TexturedModel, List<Entity>> listToAdd = null;
		if(entity.getTexturedModel().getNormalMappingTexture() == null)
			listToAdd = allEntities;
		else
			listToAdd = allNMEntities;
		
		TexturedModel texturedModel = entity.getTexturedModel();
		List<Entity> batch = listToAdd.get(texturedModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			listToAdd.put(texturedModel, newBatch);
		}
		
	}

	public void processTerrains(List<Terrain> terrains) {
		for (Terrain terrain : terrains)
			processTerrain(terrain);
	}

	public void processTerrain(Terrain terrain) {
		this.allTerrains.add(terrain);
	}
	
	public void processWaters(List<WaterTile> allWaters) {
		this.allWaters.addAll(allWaters);
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
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
	}

	public void cleanUp() {
		waterShader.cleanUp();
		skyboxShader.cleanUp();
		terrainShader.cleanUp();
		entityShader.cleanUp();
		nmEntityShader.cleanUp();
	}

	public void setRenderSkybox(boolean value) {
		renderSkybox = value;
	}

	public static boolean isRenderSkybox() {
		return renderSkybox;
	}

	public static void EnableBrush(float brushWidth) {
		MasterRenderer.brushWidth = brushWidth;
	}

	public static void DisableBrush() {
		MasterRenderer.brushWidth = 0;
	}

	public static void setBrushPosition(Vector3f vec) {
		MasterRenderer.brushPosition = vec;
	}

	public static void setBrushWidth(float value) {
		MasterRenderer.brushWidth = value;
	}

}
