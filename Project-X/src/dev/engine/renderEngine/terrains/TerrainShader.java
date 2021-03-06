package dev.engine.renderEngine.terrains;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import dev.engine.entities.Camera;
import dev.engine.entities.Light;
import dev.engine.renderEngine.ShaderProgram;
import dev.engine.utils.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String vertexShaderFile = "src/dev/engine/renderEngine/terrains/terrainVertexShader.glsl";
	private static final String fragmentShaderFile = "src/dev/engine/renderEngine/terrains/terrainFragmentShader.glsl";

	private static final int MAX_LIGHTS = 4;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_lightAttenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_aTexture;
	private int location_blendMapTexture;
	private int location_densityOfFog;
	private int location_gradientOfFog;
	private int location_tilingFactor;
	private int location_minDiffuseLighting;
	private int location_minSpecularLighting;
	private int location_clipPlane;
	private int location_brushPosition;
	private int location_brushWidth;
	private int location_brushColor;

	public TerrainShader() {
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
		this.location_shineDamper = super.getUniformLocation("shineDamper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_skyColor = super.getUniformLocation("skyColor");
		this.location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		this.location_rTexture = super.getUniformLocation("rTexture");
		this.location_gTexture = super.getUniformLocation("gTexture");
		this.location_bTexture = super.getUniformLocation("bTexture");
		this.location_aTexture = super.getUniformLocation("aTexture");
		this.location_blendMapTexture = super.getUniformLocation("blendMapTexture");
		this.location_densityOfFog = super.getUniformLocation("densityOfFog");
		this.location_gradientOfFog = super.getUniformLocation("gradientOfFog");
		this.location_tilingFactor = super.getUniformLocation("tilingFactor");
		this.location_minDiffuseLighting = super.getUniformLocation("minDiffuseLighting");
		this.location_minSpecularLighting = super.getUniformLocation("minSpecularLighting");
		this.location_clipPlane = super.getUniformLocation("clipPlane");
		this.location_brushPosition = super.getUniformLocation("brushPosition");
		this.location_brushWidth = super.getUniformLocation("brushWidth");
		this.location_brushColor = super.getUniformLocation("brushColor");

		this.location_lightPosition = new int[MAX_LIGHTS];
		this.location_lightColor = new int[MAX_LIGHTS];
		this.location_lightAttenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			this.location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			this.location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			this.location_lightAttenuation[i] = super.getUniformLocation("lightAttenuation[" + i + "]");
		}
	}
	
	public void loadBrushInfo(float brushWidth, Vector3f brushPosition, Vector4f brushColor) {
		super.loadFloat(location_brushWidth, brushWidth);
		super.loadVector3f(location_brushPosition, brushPosition);
		super.loadVector4f(location_brushColor, brushColor);
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

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
				super.loadVector3f(location_lightAttenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector3f(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector3f(location_lightAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadShineValues(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadSkyColor(Vector3f skyColor) {
		super.loadVector3f(location_skyColor, skyColor);
	}

	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_aTexture, 4);
		super.loadInt(location_blendMapTexture, 5);
	}

	public void loadFogVariables(float densityOfFog, float gradientOfFog) {
		super.loadFloat(location_densityOfFog, densityOfFog);
		super.loadFloat(location_gradientOfFog, gradientOfFog);
	}

	public void loadTilingFactor(float tilingFactor) {
		super.loadFloat(location_tilingFactor, tilingFactor);
	}

	public void loadMinLightingVariables(float minDiffuseLighting, float minSpecularLighting) {
		super.loadFloat(location_minDiffuseLighting, minDiffuseLighting);
		super.loadFloat(location_minSpecularLighting, minSpecularLighting);
	}
	
	public void loadClipPane(Vector4f clipPlane) {
		super.loadVector4f(location_clipPlane, clipPlane);
	}

}
