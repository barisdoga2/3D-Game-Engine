package dev.engine;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.entities.Player;
import dev.engine.loaders.ImageLoader;
import dev.engine.loaders.Loader;
import dev.engine.loaders.mapLoader.GameStructs;
import dev.engine.loaders.mapLoader.Map;
import dev.engine.models.ModelTexture;
import dev.engine.models.OBJLoader;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.terrains.Terrain;

public class EngineTester {

	private static Map testMap;

	public static void main(String[] args) {

		DisplayManager.CreateDisplay();

		// Map Loader Test //

		GameStructs.init();
		testMap = GameStructs.loadMap("testMap");
		Terrain testTerrain = testMap.getAllTerrains().get(0);

		Player player = new Player("Player",
				new TexturedModel(OBJLoader.LoadObjModel("/models/objs/player"),
						new ModelTexture(ImageLoader.loadTexture("/models/textures/playerTexture"))),
				new Vector3f(0, testTerrain.getHeight(0, 0), 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		player.setCurrentTerrain(testTerrain);

		Camera testCamera = new Camera(player);

		// --------------- //

		MasterRenderer masterRenderer = new MasterRenderer(testMap);

		while (!Display.isCloseRequested()) {
			player.update();
			testCamera.update();

			masterRenderer.processEntity(player);
			masterRenderer.processEntities(testMap.getAllEntities());
			masterRenderer.processTerrains(testMap.getAllTerrains());

			masterRenderer.render(testMap.getAllLights(), testCamera);

			DisplayManager.UpdateDisplay();
		}

		masterRenderer.cleanUp();
		ImageLoader.cleanUp();
		Loader.cleanUp();
		DisplayManager.DestroyDisplay();

	}

}
