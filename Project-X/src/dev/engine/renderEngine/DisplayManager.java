package dev.engine.renderEngine;

import java.awt.Canvas;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.SharedDrawable;

import dev.engine.EngineConfig;

public class DisplayManager {

	private static SharedDrawable sharedDrawable;
	private static int fpsCap;
	private static long lasftFrameTime;
	private static float deltaTimeSeconds;
	
	public static void CreateDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true).withContextResetIsolation(true);

		EngineConfig config = EngineConfig.getInstance();
		
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = graphicsDevice.getDisplayMode().getWidth();
		int height = graphicsDevice.getDisplayMode().getHeight();

		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat().withSamples(config.getInt("aa_samples")), attribs);
			Display.setTitle(config.getString("title"));
			sharedDrawable = new SharedDrawable(Display.getDrawable());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		fpsCap = config.getInt("fps_cap");

		GL11.glViewport(0, 0, width, height);
		lasftFrameTime = getCurrentTime();
	}

	public static void UpdateDisplay() {
		Display.sync(fpsCap);
		Display.update();

		long currentFrameTime = getCurrentTime();
		deltaTimeSeconds = (currentFrameTime - lasftFrameTime) / 1000f;
		lasftFrameTime = currentFrameTime;
	}
	
	public static void SetParentCanvas(Canvas awtCanvas) {
		try {
			Display.setParent(awtCanvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void MakeCurrentDrawableThread() {
		try {
			sharedDrawable.makeCurrent();
		} catch (LWJGLException e) {
			e.printStackTrace();
		};
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
