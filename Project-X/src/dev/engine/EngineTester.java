package dev.engine;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.entities.Player;
import dev.engine.models.Loader;
import dev.engine.models.ModelTexture;
import dev.engine.models.OBJLoader;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainTexture;
import dev.engine.terrains.TerrainTexturePack;

public class EngineTester {

	public static void main(String[] args) {

		DisplayManager.CreateDisplay();
		MasterRenderer masterRenderer = new MasterRenderer();
		
		Light testLight = new Light(new Vector3f(1000, 1000, 1000), new Vector3f(1, 1, 1));
		
		TerrainTexture backgroundTerrainTexture = new TerrainTexture(Loader.loadTexture("grassy"));
		TerrainTexture rTerrainTexture = new TerrainTexture(Loader.loadTexture("path"));
		TerrainTexture gTerrainTexture = new TerrainTexture(Loader.loadTexture("dirt"));
		TerrainTexture bTerrainTexture = new TerrainTexture(Loader.loadTexture("desert"));
		TerrainTexture blendMapTexture = new TerrainTexture(Loader.loadTexture("blendMap"));
		TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTerrainTexture, rTerrainTexture, gTerrainTexture, bTerrainTexture);
		Terrain testTerrain = new Terrain(-1, -1, terrainTexturePack, blendMapTexture, "heightMap");
		
		RawModel playerModel = OBJLoader.LoadObjModel("player");
		ModelTexture playerTexture = new ModelTexture(Loader.loadTexture("player"));
		TexturedModel playerTexturedModel = new TexturedModel(playerModel, playerTexture);
		Player player = new Player(playerTexturedModel, new Vector3f(0, testTerrain.getHeight(0, 0), 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		RawModel dragonModel = OBJLoader.LoadObjModel("dragon");
		ModelTexture dragonTexture = new ModelTexture(Loader.loadTexture("dragon"));
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);
		TexturedModel dragonTexturedModel = new TexturedModel(dragonModel, dragonTexture);
		Entity dragonEntity = new Entity(dragonTexturedModel, new Vector3f(-124, testTerrain.getHeight(-124, -63), -63), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		
		RawModel fernModel = OBJLoader.LoadObjModel("fern");
		ModelTexture fernTexture = new ModelTexture(Loader.loadTexture("fernTextureAtlas"));
		fernTexture.setAtlasNumberOfRows(2);
		fernTexture.setHasTransparency(true);
		TexturedModel fernTexturedModel = new TexturedModel(fernModel, fernTexture);
		Entity fernEntity = new Entity(fernTexturedModel, new Vector3f(-87, testTerrain.getHeight(-87, -145), -145), new Vector3f(), new Vector3f(5, 5, 5), 2);
		// Change the '2' to change the texture with any in the atlas.
		
		RawModel grassModel = OBJLoader.LoadObjModel("grass");
		ModelTexture grassTexture = new ModelTexture(Loader.loadTexture("grass"));
		grassTexture.setHasTransparency(true);
		grassTexture.setHasFakeLighting(true);
		TexturedModel grassTexturedModel = new TexturedModel(grassModel, grassTexture);
		Entity grassEntity = new Entity(grassTexturedModel, new Vector3f(-44, testTerrain.getHeight(-44, -35), -35), new Vector3f(), new Vector3f(5, 5, 5));
				
		Camera testCamera = new Camera(player);
		
		while(!Display.isCloseRequested()){
			dragonEntity.increaseRotation(0, 1, 0);
			fernEntity.increaseRotation(0, 1, 0);
			grassEntity.increaseRotation(0, 1, 0);
			player.move(testTerrain);
			testCamera.move();
			
			masterRenderer.processTerrain(testTerrain);
			
			masterRenderer.processEntity(player);
			masterRenderer.processEntity(dragonEntity);
			masterRenderer.processEntity(fernEntity);
			masterRenderer.processEntity(grassEntity);
			masterRenderer.render(testLight, testCamera);
			
			DisplayManager.UpdateDisplay();
		}

		masterRenderer.cleanUp();
		Loader.cleanUp();
		DisplayManager.DestroyDisplay();

	}

}
