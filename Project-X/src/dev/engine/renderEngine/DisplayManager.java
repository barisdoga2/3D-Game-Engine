package dev.engine.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import dev.engine.EngineConfig;

public class DisplayManager {
	
	private static long lasftFrameTime;
	private static float deltaTimeSeconds;
	
	public static void CreateDisplay() {
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(EngineConfig.SCREEN_WIDTH, EngineConfig.SCREEN_HEIGHT));
			Display.create(new PixelFormat().withSamples(EngineConfig.ANTIALIASING_SAMPLE_COUNT), attribs);
			Display.setTitle(EngineConfig.DISPLAY_TITLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, EngineConfig.SCREEN_WIDTH, EngineConfig.SCREEN_HEIGHT);
		lasftFrameTime = getCurrentTime();
	}
	
	public static void UpdateDisplay() {
		Display.sync(EngineConfig.TARGET_FPS);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		deltaTimeSeconds = (currentFrameTime - lasftFrameTime) / 1000f;
		lasftFrameTime = currentFrameTime;
	}
	
	public static void DestroyDisplay() {
		Display.destroy();
	}
	
	public static boolean IsDestroyRequested() {
		return Display.isCloseRequested();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static float getDeltaTimeSeconds() {
		return deltaTimeSeconds;
	}
	
}
