package dev.engine.terrains;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import dev.engine.loaders.ImageLoader;
import dev.engine.loaders.Loader;
import dev.engine.models.RawModel;
import dev.engine.textures.BaseTexture;
import dev.engine.utils.Maths;

public class Terrain {

	public static final float SIZE = 1600;
	public static final float MAX_HEIGHT = 100;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

	private String name;
	private float x;
	private float z;
	private RawModel rawModel;
	private TerrainTexturePack terrainTexturePack;
	private BaseTexture blendMapTexture;
	private BaseTexture heightMapTexture;

	private float[][] heights;

	public Terrain(String name, int gridX, int gridZ, TerrainTexturePack terrainTexturePack,
			BaseTexture blendMapTexture, BaseTexture heightMapTexture) {
		this.name = name;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.terrainTexturePack = terrainTexturePack;
		this.blendMapTexture = blendMapTexture;
		this.heightMapTexture = heightMapTexture;
		
		BufferedImage heightMap = ImageLoader.glTextureToBufferedImage(heightMapTexture.getTextureID());
		
		this.rawModel = generateTerrain(heightMap);
	}
	
	public void paintHeightMap(int mode, Vector3f mousePos, float radius, float targetHeightDepth) {
		radius = radius / 2f;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getHeightMapTexture().getTextureID());
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		int format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
		FloatBuffer bb = BufferUtils.createFloatBuffer(width * height * 4);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, format, GL11.GL_FLOAT, bb);
		int xOffset = (int)((mousePos.x - getGridX() * SIZE) * (width / SIZE) - radius / 2f);
		int zOffset = (int)((mousePos.z - getGridZ() * SIZE) * (height / SIZE) - radius / 2f);
		int xCenter = xOffset + (int)(radius / 2f);
		int zCenter = zOffset + (int)(radius / 2f);
				
		for(int z = zOffset ; z < zOffset + (int)radius ; z++) {
			for(int x = xOffset ; x < xOffset + (int)radius ; x++) {
				if(Math.sqrt(Math.abs(xCenter - x) * Math.abs(xCenter - x) + Math.abs(zCenter - z) * Math.abs(zCenter - z)) > radius / 2f)
					continue;
				if((z * width + x) * 4 + 3 >= width * height * 4 || (z * width + x) * 4 + 3 < 0)
					continue;
				
				int index = (z * width + x) * 4;
				float r = bb.get(index);
				float g = bb.get(index + 1);
				float b = bb.get(index + 2);

				float nextR = r + targetHeightDepth * (1 - smoothStep(x, z, xCenter, zCenter, radius)) * mode;
				float nextG = g + targetHeightDepth * (1 - smoothStep(x, z, xCenter, zCenter, radius)) * mode;
				float nextB = b + targetHeightDepth * (1 - smoothStep(x, z, xCenter, zCenter, radius)) * mode;
				
				bb.put(index, nextR);
				bb.put(index + 1, nextG);
				bb.put(index + 2, nextB);
			}
		}
		
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, format, GL11.GL_FLOAT, bb);
		
		BufferedImage heightMap = ImageLoader.glTextureToBufferedImage(heightMapTexture.getTextureID());
		this.rawModel = generateTerrain(heightMap);
	}
	
	private static float smoothStep(int x, int y, int xRef, int yRef, float padding){
		float smoothStep = 0.0f;
		
		float dist = (float) Math.sqrt(Math.pow( (xRef - x), 2) + Math.pow( (yRef - y), 2));
		float maxDist = padding;
		
		if(dist > padding)
			return 1;
		
		smoothStep = dist / maxDist;
		smoothStep = (float) (3 * Math.pow(smoothStep, 2) - 2 * Math.pow(smoothStep, 3));
		
		return smoothStep;
	}
	
	public void paintBlendMap(Vector4f currentColor, Vector3f mousePos, float radius, float hardness) {
		radius = radius * 1.3f;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getBlendMapTexture().getTextureID());
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		int format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
		FloatBuffer bb = BufferUtils.createFloatBuffer(width * height * 4);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, format, GL11.GL_FLOAT, bb);
		int xOffset = (int)((mousePos.x - getGridX() * SIZE) * (width / SIZE) - radius / 2f);
		int zOffset = (int)((mousePos.z - getGridZ() * SIZE) * (height / SIZE) - radius / 2f);
		int xCenter = xOffset + (int)(radius / 2f);
		int zCenter = zOffset + (int)(radius / 2f);
		
		float multiplier = 0.0f;
		
		for(int z = zOffset ; z < zOffset + (int)radius ; z++) {
			for(int x = xOffset ; x < xOffset + (int)radius ; x++) {
				multiplier = (float) ((radius / 2f) / Math.sqrt(Math.abs(xCenter - x) * Math.abs(xCenter - x) + Math.abs(zCenter - z) * Math.abs(zCenter - z)));
				if(Math.sqrt(Math.abs(xCenter - x) * Math.abs(xCenter - x) + Math.abs(zCenter - z) * Math.abs(zCenter - z)) > radius / 2f)
					continue;
				if((z * width + x) * 4 + 3 >= width * height * 4 || (z * width + x) * 4 + 3 < 0)
					continue;
				int index = (z * width + x) * 4;
				float r = bb.get(index);
				float g = bb.get(index + 1);
				float b = bb.get(index + 2);
				float a = bb.get(index + 3);
				
				float nextR = r + currentColor.x * hardness * multiplier;
				float nextG = g + currentColor.y * hardness * multiplier;
				float nextB = b + currentColor.z * hardness * multiplier;
				float nextA = a + currentColor.w * hardness * multiplier;
				
				bb.put(index, nextR);
				bb.put(index + 1, nextG);
				bb.put(index + 2, nextB);
				bb.put(index + 3, nextA);
			}
		}
		
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, format, GL11.GL_FLOAT, bb);
	}
	
	public void regenerateTerrain(BufferedImage heightMap) { // -TODO- When necessary - Change reCreating algorithm with better algorithm that allows least memory usage and cpu usage. Related with mapEditor systems height map editor
		
		int vertexCount = heightMap.getWidth();
		heights = new float[vertexCount][vertexCount];
		
		int count = vertexCount * vertexCount;
		
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		
		int vertexPointer = 0;
		for(int i = 0 ; i < vertexCount ; i++){
			for(int j = 0 ; j < vertexCount ; j++){
				vertices[vertexPointer * 3 + 0] = (float) j / ((float) vertexCount - 1) * SIZE;
				float height = getHeight(j, i, heightMap);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				
				Vector3f normal = calculateNormal(j, i, heightMap);
				normals[vertexPointer * 3 + 0] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				vertexPointer++;
			}
			
		}
		
		Loader.changeAttributeData(rawModel.getVAOID(), 0, 3, vertices); // Vertices
		Loader.changeAttributeData(rawModel.getVAOID(), 2, 3, normals); // Normals
	}

	private RawModel generateTerrain(BufferedImage heightMapImage){
		
		int vertexCount = heightMapImage.getWidth();
		heights = new float[vertexCount][vertexCount];
		
		int count = vertexCount * vertexCount;
		
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
		
		int vertexPointer = 0;
		for(int i = 0 ; i < vertexCount ; i++){
			
			for(int j = 0 ; j < vertexCount ; j++){
				
				vertices[vertexPointer * 3 + 0] = (float) j / ((float) vertexCount - 1) * SIZE;
				float height = getHeight(j, i, heightMapImage);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexCount - 1) * SIZE;
				
				Vector3f normal = calculateNormal(j, i, heightMapImage);
				normals[vertexPointer * 3 + 0] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				
				textureCoords[vertexPointer * 2 + 0] = (float) j / ((float) vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexCount - 1);
				
				vertexPointer++;
			}
			
		}
		int pointer = 0;
		for(int gz = 0 ; gz < vertexCount - 1 ; gz++){
			
			for(int gx = 0; gx<vertexCount - 1 ; gx++){
				
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
		
		return Loader.loadToVAO(null, null, vertices, textureCoords, normals, indices);
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
	
	public int getGridX() {
		return (int) (x / SIZE);
	}
	
	public int getGridZ() {
		return (int) (z / SIZE);
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

	public BaseTexture getBlendMapTexture() {
		return blendMapTexture;
	}
	
	public BaseTexture getHeightMapTexture() {
		return heightMapTexture;
	}

	public TerrainTexturePack getTerrainTexturePack() {
		return terrainTexturePack;
	}

	public String getName() {
		return name;
	}

}
