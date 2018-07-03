package dev.engine.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import dev.engine.EngineConfig;

public class ImageLoader {

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
		} catch (Exception e) {
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

	public static BufferedImage loadBufferedImage(String file) {
		BufferedImage bufferedImage = null;

		try {
			bufferedImage = ImageIO.read(new File("res/" + file + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bufferedImage;
	}

	public static void cleanUp() {
		for (int id : allTextures) {
			GL11.glDeleteTextures(id);
		}
	}

}
