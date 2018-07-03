package dev.engine.loaders.mapLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.loaders.ImageLoader;
import dev.engine.terrains.TerrainTexture;

public class TerrainTextureLoader {

	private static final String XML_PATH = "res/xml/terrainTextures.xml";

	protected static List<TerrainTexture> loadAll() {
		List<TerrainTexture> all = new ArrayList<TerrainTexture>();
		try {
			File fXmlFile = new File(XML_PATH);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("terrainTexture");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					String name = eElement.getAttribute("name");
					String textureLocation = eElement.getElementsByTagName("textureLocation").item(0).getTextContent();
					int textureID = ImageLoader.loadTexture(textureLocation);

					all.add(new TerrainTexture(name, textureID));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return all;
	}

}
