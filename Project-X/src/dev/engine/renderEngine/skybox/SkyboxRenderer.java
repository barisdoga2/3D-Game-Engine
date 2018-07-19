package dev.engine.renderEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import dev.engine.entities.Camera;
import dev.engine.loaders.mapLoader.Map;
import dev.engine.skybox.Skybox;

public class SkyboxRenderer {

	private Map map;
	private SkyboxShader skyboxShader;
	private Skybox skyBox;
	
	public SkyboxRenderer(SkyboxShader skyboxShader, Matrix4f projectionMatrix, dev.engine.loaders.mapLoader.Map map) {
		this.skyboxShader = skyboxShader;
		this.map = map;
		this.skyBox = map.getMapSettings().getSkybox();
		
		skyboxShader.start();
		skyboxShader.loadProjectionMatrix(projectionMatrix);
		skyboxShader.stop();
	}
	
	public void reLoadSettings() {
		this.skyBox = map.getMapSettings().getSkybox();
	}
	
	public void render(Camera camera) {
		skyboxShader.start();
		
		skyboxShader.loadViewMatrix(camera);
		
		GL30.glBindVertexArray(skyBox.getCube().getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skyBox.getTextureID());
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skyBox.getCube().getVertexCount());
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		skyboxShader.stop();
	}
	
}
