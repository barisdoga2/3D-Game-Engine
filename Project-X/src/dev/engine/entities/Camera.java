package dev.engine.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static final int MAX_ZOOM_IN_DISTANCE = 75;
	private static final int MAX_ZOOM_OUT_DISTANCE = 200;
	
	private static final int MAX_PITCH_DEGREES = 80;
	private static final int MIN_PITCH_DEGREES = 10;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 27.5f;
	private float yaw;
	private float roll;
	
	private float distanceFromPlayer = 200;
	private float angleAroundPlayer = 0;
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void update() {
		move();
	}
	
	private void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotation().y + angleAroundPlayer);
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotation().y + angleAroundPlayer;
		float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - xOffset;
		position.z = player.getPosition().z - zOffset;
		position.y = player.getPosition().y + verticalDistance + 5; // TODO - +5 is used to move cameras y to center of player. Fix this with dynamic variable later.
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		
		if(distanceFromPlayer < MAX_ZOOM_IN_DISTANCE)
			distanceFromPlayer = MAX_ZOOM_IN_DISTANCE;
		else if(distanceFromPlayer > MAX_ZOOM_OUT_DISTANCE)
			distanceFromPlayer = MAX_ZOOM_OUT_DISTANCE;
	}
	
	private void calculatePitch() {
		// 0 is Left Button
		// 1 is Right Button
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
		
		if(pitch < MIN_PITCH_DEGREES)
			pitch = MIN_PITCH_DEGREES;
		else if(pitch > MAX_PITCH_DEGREES)
			pitch = MAX_PITCH_DEGREES;
	}
	
	private void calculateAngleAroundPlayer() {
		// 0 is Left Button
		// 1 is Right Button
		if(Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public float getAngleAroundPlayer() {
		return angleAroundPlayer;
	}

	public void setAngleAroundPlayer(float angleAroundPlayer) {
		this.angleAroundPlayer = angleAroundPlayer;
	}

}
