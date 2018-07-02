package dev.engine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.EngineConfig;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.terrains.Terrain;

public class Player extends Entity{
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		super(model, position, rotation, scale);
	}
	
	public void move(Terrain currentTerrain) {
		checkInputs();
		
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDeltaTimeSeconds(), 0);
		
		float distance = currentSpeed * DisplayManager.getDeltaTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(getRotation().y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(getRotation().y)));
		super.increasePosition(dx, 0, dz);
		
		super.getPosition().y = currentTerrain.getHeight(getPosition().x, getPosition().z);
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = EngineConfig.PLAYER_RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -EngineConfig.PLAYER_RUN_SPEED;
		}else {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = EngineConfig.PLAYER_TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -EngineConfig.PLAYER_TURN_SPEED;
		}else {
			this.currentTurnSpeed = 0;
		}
	}
	
}