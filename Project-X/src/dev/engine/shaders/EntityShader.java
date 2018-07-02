package dev.engine.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.entities.Light;
import dev.engine.renderEngine.ShaderProgram;
import dev.engine.utils.Maths;

public class EntityShader extends ShaderProgram{
	
	private static final String vertexShaderFile = "src/dev/engine/shaders/entityVertexShader.vs";
	private static final String fragmentShaderFile = "src/dev/engine/shaders/entityFragmentShader.fs";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColor;
	private int location_atlasNumberOfRows;
	private int location_atlasOffsets;

	public EntityShader() {
		super(vertexShaderFile, fragmentShaderFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");
		this.location_lightPosition = super.getUniformLocation("lightPosition");
		this.location_lightColor = super.getUniformLocation("lightColor");
		this.location_shineDamper = super.getUniformLocation("shineDamper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		this.location_skyColor = super.getUniformLocation("skyColor");
		this.location_atlasNumberOfRows = super.getUniformLocation("atlasNumberOfRows");
		this.location_atlasOffsets = super.getUniformLocation("atlasOffsets");
		
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix4f(this.location_transformationMatrix, transformationMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix4f(this.location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix4f(this.location_viewMatrix, viewMatrix);
	}
	
	public void loadLight(Light light) {
		super.loadVector3f(location_lightPosition, light.getPosition());
		super.loadVector3f(location_lightColor, light.getColor());
	}
	
	public void loadShineValues(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting) {
		super.loadBoolean(location_useFakeLighting, useFakeLighting);
	}
	
	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3f(location_skyColor, skyColor);
	}
	
	public void loadAtlasNumberOfRows(int atlasNumberOfRows) {
		super.loadFloat(location_atlasNumberOfRows, atlasNumberOfRows);
	}
	
	public void loadAtlasOffsets(float atlasXOffset, float atlasYOffset) {
		super.loadVector2f(location_atlasOffsets, new Vector2f(atlasXOffset, atlasYOffset));
	}
	
}
