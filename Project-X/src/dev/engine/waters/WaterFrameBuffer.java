package dev.engine.waters;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class WaterFrameBuffer {

	protected static final int REFLECTION_WIDTH = 320;
	private static final int REFLECTION_HEIGHT = 180;
	
	protected static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;

	private int reflectionFrameBufferID;
	private int reflectionColorTextureID;
	private int reflectionDepthBufferID;
	
	private int refractionFrameBufferID;
	private int refractionColorTextureID;
	private int refractionDepthTextureID;

	public WaterFrameBuffer() {
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}
	
	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBufferID = createFrameBuffer();
		reflectionColorTextureID = createColorTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
		reflectionDepthBufferID = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT); // Depth Buffer!!!!!
		unbindCurrentFrameBuffer();
	}
	
	private void initialiseRefractionFrameBuffer() {
		refractionFrameBufferID = createFrameBuffer();
		refractionColorTextureID = createColorTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
		refractionDepthTextureID = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT); // Depth Texture!!!!!
		unbindCurrentFrameBuffer();
	}

	public void bindReflectionFrameBuffer() {
		bindFrameBuffer(reflectionFrameBufferID,REFLECTION_WIDTH,REFLECTION_HEIGHT);
	}
	
	public void bindRefractionFrameBuffer() {
		bindFrameBuffer(refractionFrameBufferID,REFRACTION_WIDTH,REFRACTION_HEIGHT);
	}
	
	private void bindFrameBuffer(int frameBuffer, int width, int height){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}
	
	public void unbindCurrentFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void cleanUp() {
		GL30.glDeleteFramebuffers(reflectionFrameBufferID);
		GL11.glDeleteTextures(reflectionColorTextureID);
		GL30.glDeleteRenderbuffers(reflectionDepthBufferID);
		GL30.glDeleteFramebuffers(refractionFrameBufferID);
		GL11.glDeleteTextures(refractionColorTextureID);
		GL11.glDeleteTextures(refractionDepthTextureID);
	}
	
	private int createFrameBuffer() {
		int frameBufferID = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBufferID);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return frameBufferID;
	}

	private int createColorTextureAttachment( int width, int height) {
		int colorTextureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, colorTextureID, 0); // Add To Currently Bound FBO
		return colorTextureID;
	}
	
	private int createDepthTextureAttachment(int width, int height) {
		int depthTextureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTextureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depthTextureID, 0); // Add To Currently Bound FBO
		return depthTextureID;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBufferID = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBufferID);
		return depthBufferID;
	}
	
	public int getReflectionColorTextureID() {//get the resulting texture
		return reflectionColorTextureID;
	}
	
	public int getRefractionColorTextureID() {//get the resulting texture
		return refractionColorTextureID;
	}
	
	public int getRefractionDepthTextureID(){//get the resulting depth texture
		return refractionDepthTextureID;
	}

}
