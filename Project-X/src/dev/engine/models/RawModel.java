package dev.engine.models;

public class RawModel {

	private String name;
	private String path;
	private int vaoID;
	private int vertexCount;

	public RawModel(String name, String path, int vaoID, int vertexCount) {
		this.name = name;
		this.path = path;
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public String getName() {
		return name;
	}

	public int getVAOID() {
		return this.vaoID;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	public String getPath() {
		return path;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
