package dev.engine.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import dev.engine.EngineConfig;
import dev.engine.renderEngine.skybox.TextureData;

public class ImageLoader {
	
	private ImageLoader() {}
	
	private static List<Integer> allTextures = new ArrayList<Integer>();

	public static int loadTexture(String pngFileName) {
		Texture texture = null;
		EngineConfig config = EngineConfig.getInstance();

		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + pngFileName + ".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, config.getFloat("mipmaps"));
			if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT,
						amount);
			} else {
				System.err.println("Anisotropic Filering Doesn't Supported!");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + pngFileName + ".png , didn't work");
			System.exit(-1);
		}
		allTextures.add(texture.getTextureID());

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		return texture.getTextureID();
	}

	public static int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		allTextures.add(texID);

		GL13.glActiveTexture(texID);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");

			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(),
					data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		return texID;
	}

	private static TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;

		try {

			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", unsuccessful.");
			System.exit(-1);
		}

		return new TextureData(buffer, width, height);
	}
  
	public static BufferedImage loadBufferedImage(String file) {
		BufferedImage bufferedImage = null;

		try {
			bufferedImage = ImageIO.read(new File("res/" + file + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bufferedImage;
	}
	
	public static BufferedImage glTextureToBufferedImage(int textureID) {
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		int format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);

		for (int x = 0; x < width; ++x) {
		    for (int y = 0; y < height; ++y) {
		        int i = (x + y * width) * 4;

		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        int a = buffer.get(i + 3) & 0xFF;

		        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		return image;
	}

	public static void cleanUp() {
		for (int id : allTextures) {
			GL11.glDeleteTextures(id);
		}
	}

}
