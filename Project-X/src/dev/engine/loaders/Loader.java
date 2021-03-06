package dev.engine.loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import dev.engine.models.RawModel;

public class Loader {

	private static List<Integer> allVAOs = new ArrayList<Integer>();
	private static List<Integer> allVBOs = new ArrayList<Integer>();
	
	public static RawModel loadToVAO(String name, String path, float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);

		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 3, tangents);

		unbindVAO();
		return new RawModel(name, path, vaoID, indices.length);
	}

	public static RawModel loadToVAO(String name, String path, float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);

		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);

		unbindVAO();
		return new RawModel(name, path, vaoID, indices.length);
	}
	
	public static RawModel loadToVAO(String name, String path, float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(name, path, vaoID, positions.length / dimensions);
	}
	
	private static int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		allVAOs.add(vaoID);

		GL30.glBindVertexArray(vaoID);

		return vaoID;
	}

	private static void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		int vboID = GL15.glGenBuffers();
		allVBOs.add(vboID);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private static void bindIndicesBuffer(int[] indices) {
		IntBuffer buffer = storeDataInIntBuffer(indices);
		int vboID = GL15.glGenBuffers();
		allVBOs.add(vboID);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static void cleanUp() {
		for (int id : allVAOs) {
			GL30.glDeleteVertexArrays(id);
		}

		for (int id : allVBOs) {
			GL15.glDeleteBuffers(id);
		}
	}

	public static void changeAttributeData(int vaoID, int attributeNumber, int coordinateSize, float[] newData){
		GL30.glBindVertexArray(vaoID);
		storeDataInAttributeList(attributeNumber, coordinateSize, newData);
		unbindVAO();
	}

}
