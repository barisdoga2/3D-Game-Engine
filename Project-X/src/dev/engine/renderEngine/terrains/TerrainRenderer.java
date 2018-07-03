package dev.engine.renderEngine.terrains;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.EngineConfig;
import dev.engine.models.RawModel;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainTexturePack;
import dev.engine.utils.Maths;

public class TerrainRenderer {
	
	private TerrainShader terrainShader;
	
	public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix) {
		this.terrainShader = terrainShader;
		
		terrainShader.start();
		terrainShader.loadTilingFactor(40);
		terrainShader.loadFogVariables(0.004f, 11.0f);
		terrainShader.connectTextureUnits();
		terrainShader.loadProjectionMatrix(projectionMatrix);
		terrainShader.loadSkyColor(EngineConfig.SKY_COLOR);
		terrainShader.stop();
	}
	
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			bindTexturedModel(terrain);
			prepareTerrain(terrain);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTexturedModel();
		}
	}
	
	private void bindTexturedModel(Terrain terrain) {
		RawModel rawModel = terrain.getRawModel();
		
		GL30.glBindVertexArray(rawModel.getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		terrainShader.loadShineValues(1, 0);
		bindTextures(terrain);
	}
	
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack terrainTexturePack = terrain.getTerrainTexturePack();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getBackgroundTerrainTexture().getTextureID());
	
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getRTerrainTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getGTerrainTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getBTerrainTexture().getTextureID());
	
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMapTexture().getTextureID());
	
	}
	
	private void prepareTerrain(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		terrainShader.loadTransformationMatrix(transformationMatrix);
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
}
