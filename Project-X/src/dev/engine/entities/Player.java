package dev.engine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.EngineConfig;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;

public class Player extends Entity {

	private float mSpeed;
	private float mTurnSpeed;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;

	public Player(String name, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		super(name, model, position, rotation, scale);

		EngineConfig config = EngineConfig.getInstance();
		mSpeed = config.getFloat("player_run_speed");
		mTurnSpeed = config.getFloat("player_turn_speed");
	}
  
	@Override
	public void update() {
		move();
	}

	private void move() {
		checkInputs();

		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDeltaTimeSeconds(), 0);

		float distance = currentSpeed * DisplayManager.getDeltaTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(getRotation().y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(getRotation().y)));
		super.increasePosition(dx, 0, dz);

		if (this.getCurrentTerrain() != null)
			super.getPosition().y = this.getCurrentTerrain().getHeight(getPosition().x, getPosition().z);
	}

	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = EngineConfig.PLAYER_RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -EngineConfig.PLAYER_RUN_SPEED;
		}else {
			this.currentSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = mTurnSpeed;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -mTurnSpeed;
		} else {
			this.currentTurnSpeed = 0;
		}
		
	}

}
