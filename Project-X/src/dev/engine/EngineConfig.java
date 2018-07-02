package dev.engine;

import org.lwjgl.util.vector.Vector3f;

public class EngineConfig {
	
	public static final int SCREEN_WIDTH = 1920;
	public static final int SCREEN_HEIGHT = 1080;
	
	public static final String DISPLAY_TITLE = "Project-X";
	public static final int TARGET_FPS = 120;
	
	public static final Vector3f SKY_COLOR = new Vector3f(0.0f, 0.65f, 0.85f);
	
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	
	public static final float PLAYER_RUN_SPEED = 100;
	public static final float PLAYER_TURN_SPEED = 160;
	
	public static final int ANTIALIASING_SAMPLE_COUNT = 8;
	public static final float ANISOTROPIC_FILTERING_LEVEL = 4f;
	public static final float MIPMAPPING_LEVEL = -0.4f;
	
}
