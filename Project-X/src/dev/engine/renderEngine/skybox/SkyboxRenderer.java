package dev.engine.renderEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import dev.engine.entities.Camera;
import dev.engine.loaders.ImageLoader;
import dev.engine.loaders.Loader;
import dev.engine.models.RawModel;

public class SkyboxRenderer {

	private RawModel cube;
	private int textureID;
	private SkyboxShader skyboxShader;
	
	public SkyboxRenderer(SkyboxShader skyboxShader, Matrix4f projectionMatrix, dev.engine.loaders.mapLoader.Map map) {
		this.skyboxShader = skyboxShader;
		this.textureID = ImageLoader.loadCubeMap(map.getSkyBox());
		this.cube = Loader.loadToVAO(getVerticesArray(map.getSkyboxSize()), 3);
		
		skyboxShader.start();
		skyboxShader.loadProjectionMatrix(projectionMatrix);
		skyboxShader.stop();
	}
	
	public void render(Camera camera) {
		skyboxShader.start();
		
		skyboxShader.loadViewMatrix(camera);
		
		GL30.glBindVertexArray(cube.getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		skyboxShader.stop();
	}
	
	private static float[] getVerticesArray(float size) {
		return new float[] { 
				-size, size, -size, -size, -size, -size, size, -size, -size, size, -size, -size, size, size, -size, -size,
				size, -size,

				-size, -size, size, -size, -size, -size, -size, size, -size, -size, size, -size, -size, size, size, -size,
				-size, size,

				size, -size, -size, size, -size, size, size, size, size, size, size, size, size, size, -size, size, -size,
				-size,

				-size, -size, size, -size, size, size, size, size, size, size, size, size, size, -size, size, -size, -size,
				size,

				-size, size, -size, size, size, -size, size, size, size, size, size, size, -size, size, size, -size, size,
				-size,

				-size, -size, -size, -size, -size, size, size, -size, -size, size, -size, -size, -size, -size, size, size,
				-size, size
				};
	}
	
}