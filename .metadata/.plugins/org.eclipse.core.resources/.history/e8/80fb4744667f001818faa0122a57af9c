package dev.engine.skybox;

import org.lwjgl.util.vector.Matrix4f;

import dev.engine.entities.Camera;
import dev.engine.renderEngine.ShaderProgram;
import dev.engine.utils.Maths;

public class SkyboxShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/dev/engine/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "src/dev/engine/skybox/skyboxFragmentShader.glsl";

	private int location_projectionMatrix;
	private int location_viewMatrix;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix4f(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);

		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		
		super.loadMatrix4f(location_viewMatrix, matrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	
}
