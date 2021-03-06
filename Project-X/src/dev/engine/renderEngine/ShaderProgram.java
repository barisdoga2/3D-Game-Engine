package dev.engine.renderEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram {

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	private boolean isRunning;
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	public ShaderProgram(String vertexShaderFile, String fragmentShaderFile) {
		this.vertexShaderID = loadShader(vertexShaderFile, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentShaderFile, GL20.GL_FRAGMENT_SHADER);
		this.programID = GL20.glCreateProgram();

		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(this.programID);
		GL20.glValidateProgram(this.programID);
		getAllUniformLocations();
		
		start();
		connectTextureUnits();
		stop();
	}

	public void start() {
		isRunning = true;
		GL20.glUseProgram(this.programID);
	}

	public void stop() {
		isRunning = false;
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.programID);
	}
	
	protected abstract void connectTextureUnits();
	
	protected abstract void bindAttributes();

	protected void bindAttribute(int attributeIndex, String variableName) {
		GL20.glBindAttribLocation(this.programID, attributeIndex, variableName);
	}

	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String uniformVariableName) {
		return GL20.glGetUniformLocation(this.programID, uniformVariableName);
	}

	protected void loadBoolean(int locationOfUniformVariable, boolean valueToLoad) {
		GL20.glUniform1f(locationOfUniformVariable, valueToLoad ? 1f : 0f);
	}

	protected void loadFloat(int locationOfUniformVariable, float valueToLoad) {
		GL20.glUniform1f(locationOfUniformVariable, valueToLoad);
	}

	protected void loadVector3f(int locationOfUniformVariable, Vector3f vector3fToLoad) {
		GL20.glUniform3f(locationOfUniformVariable, vector3fToLoad.x, vector3fToLoad.y, vector3fToLoad.z);
	}

	protected void loadVector2f(int locationOfUniformVariable, Vector2f vector2fToLoad) {
		GL20.glUniform2f(locationOfUniformVariable, vector2fToLoad.x, vector2fToLoad.y);
	}

	protected void loadMatrix4f(int locationOfUniformVariable, Matrix4f matrix4fToLoad) {
		matrix4fToLoad.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(locationOfUniformVariable, false, matrixBuffer);
	}

	protected void loadInt(int locationOfUniformVariable, int valueToLoad) {
		GL20.glUniform1i(locationOfUniformVariable, valueToLoad);
	}
	
	protected void loadVector4f(int locationOfUniformVariable, Vector4f vector4fToLoad) {
		GL20.glUniform4f(locationOfUniformVariable, vector4fToLoad.x, vector4fToLoad.y, vector4fToLoad.z, vector4fToLoad.w);
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("//\n");
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader " + file + " !");
		}
		return shaderID;
	}
	
}
