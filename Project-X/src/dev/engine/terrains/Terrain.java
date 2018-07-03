package dev.engine.terrains;

import java.awt.image.BufferedImage;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.loaders.Loader;
import dev.engine.models.RawModel;
import dev.engine.utils.Maths;

public class Terrain {

	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

	private String name;
	private float tilingFactor;
	private float x;
	private float z;
	private RawModel rawModel;
	private TerrainTexturePack terrainTexturePack;
	private TerrainTexture blendMapTexture;

	private float[][] heights;

	public Terrain(String name, int gridX, int gridZ, float tilingFactor, TerrainTexturePack terrainTexturePack,
			TerrainTexture blendMapTexture, BufferedImage heightMap) {
		this.name = name;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.tilingFactor = tilingFactor;
		this.terrainTexturePack = terrainTexturePack;
		this.blendMapTexture = blendMapTexture;

		this.rawModel = generateTerrain(heightMap);
	}

	private RawModel generateTerrain(BufferedImage heightMap) {
		int vertexCount = heightMap.getHeight();
		heights = new float[vertexCount][vertexCount];

		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < vertexCount; i++) {
			for (int j = 0; j < vertexCount; j++) {
				float height = getHeight(j, i, heightMap);
				Vector3f normal = calculateNormal(j, i, heightMap);
				heights[j][i] = height;
				vertices[vertexPointer * 3] = (float) j / ((float) vertexCount - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < vertexCount - 1; gz++) {
			for (int gx = 0; gx < vertexCount - 1; gx++) {
				int topLeft = (gz * vertexCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return Loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float getHeight(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / (float) (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
			return 0;
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float retVal;
		if (xCoord < (1 - zCoord)) {
			retVal = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			retVal = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return retVal;
	}

	private Vector3f calculateNormal(int x, int z, BufferedImage heightMap) {
		float heightL = getHeight(x - 1, z, heightMap);
		float heightR = getHeight(x + 1, z, heightMap);
		float heightD = getHeight(x, z - 1, heightMap);
		float heightU = getHeight(x, z + 1, heightMap);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	public float getHeight(int x, int z, BufferedImage heightMap) {
		if (x < 0 || x >= heightMap.getHeight() || z < 0 || z >= heightMap.getHeight())
			return 0;

		float height = heightMap.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public TerrainTexture getBlendMapTexture() {
		return blendMapTexture;
	}

	public TerrainTexturePack getTerrainTexturePack() {
		return terrainTexturePack;
	}

	public float getTilingFactor() {
		return tilingFactor;
	}

	public String getName() {
		return name;
	}

}
