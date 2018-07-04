package dev.engine.renderEngine.entities;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import dev.engine.entities.Entity;
import dev.engine.models.ModelTexture;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.utils.Maths;

public class NMEntityRenderer {

	private NMEntityShader nmEntityShader;

	public NMEntityRenderer(NMEntityShader nmEntityShader, Matrix4f projectionMatrix, dev.engine.loaders.mapLoader.Map map) {
		this.nmEntityShader = new NMEntityShader();
		
		nmEntityShader.start();
		nmEntityShader.loadFogVariables(map.getFogDensity(), map.getFogGradient());
		nmEntityShader.loadMinLightingVariables(map.getMinDiffuseLighting(), map.getMinSpecularLighting());
		nmEntityShader.loadSkyColor(map.getSkyColor());
		
		nmEntityShader.loadProjectionMatrix(projectionMatrix);
		nmEntityShader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVAOID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		ModelTexture texture = model.getModelTexture();
		nmEntityShader.loadAtlasNumberOfRows(texture.getAtlasNumberOfRows());
		if (texture.isHasTransparency())
			MasterRenderer.DisableCulling();
		nmEntityShader.loadShineValues(texture.getShineDamper(), texture.getReflectivity());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getNormalMappingTexture().getTextureID());
	}

	private void unbindTexturedModel() {
		MasterRenderer.EnableCulling();
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		nmEntityShader.loadTransformationMatrix(transformationMatrix);
		nmEntityShader.loadAtlasOffsets(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

}
