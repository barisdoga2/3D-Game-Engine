package dev.engine.loaders.mapLoader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.entities.Entity;
import dev.engine.entities.Light;
import dev.engine.loaders.ImageLoader;
import dev.engine.models.TexturedModel;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainTexturePack;
import dev.engine.textures.BaseTexture;
import dev.engine.utils.Maths;
import dev.engine.waters.WaterTile;
import dev.gameEditor.GameEditor;

public class Map extends XMLManager{
	
	private String name;
	private List<Entity> allEntities = new ArrayList<Entity>();
	private List<Terrain> allTerrains = new ArrayList<Terrain>();
	private List<Light> allLights = new ArrayList<Light>();
	private List<WaterTile> allWaters = new ArrayList<WaterTile>();
	
	public MapSettings mapSettings;
	
	public Map(File f, ResourceLoader resourceLoader) throws Exception{
		super(f, resourceLoader);
		
		this.name = f.getName().replace(".xml", "");
		this.mapSettings = MapSettings.getMapSettings(this);
		
		loadAllEntities();
		loadAllTerrains();
		loadAllLights();
		loadAllWaters();
	}
	
	public Map(File f, String mapName, ResourceLoader resourceLoader) throws Exception {
		super(f, resourceLoader);
		
		this.name = mapName;
		this.mapSettings = MapSettings.getMapSettings(this);
		
		loadAllEntities();
		loadAllTerrains();
		loadAllLights();
		loadAllWaters();
	}
	
	@Override
	public void loadAll() throws Exception {
		loadAllEntities();
		loadAllTerrains();
		loadAllLights();
		loadAllWaters();
	}
	
	public void addEntity(Entity e) {
		allEntities.add(e);
		
		Element newElement = doc.createElement("entity");
		newElement.setAttribute("name", e.getName());
		
		addChildElement(newElement, "texturedModelName", e.getTexturedModel().getName());
		addChildElement(newElement, "textureAtlasIndex", "" + e.getTextureAtlasIndex());
		addChildElement(newElement, "position", e.getPosition().x + ", " + e.getPosition().y + ", " + e.getPosition().z);
		addChildElement(newElement, "rotation", e.getRotation().x + ", " + e.getRotation().y + ", " + e.getRotation().z);
		addChildElement(newElement, "scale", e.getScale().x + ", " + e.getScale().y + ", " + e.getScale().z);

		doc.getDocumentElement().insertBefore(newElement, null);
	}
	
	public void removeEntity(Entity selectedValue) {
		NodeList nList = doc.getElementsByTagName("entity");
		
		boolean found = false;
		int i = 0;
		for (i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				
				if(name.equals(selectedValue.getName())) {
					eElement.getParentNode().removeChild(eElement);
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			GameEditor.instance.getCurrentMap().allEntities.remove(selectedValue);
		}
	}
	
	public void addTerrain(Terrain t) {
		allTerrains.add(t);
		
		Element newElement = doc.createElement("terrain");
		newElement.setAttribute("name", t.getName());
		
		addChildElement(newElement, "Xgrid", t.getGridX() + "");
		addChildElement(newElement, "Zgrid", t.getGridZ() + "");
		
		TerrainTexturePack ttp = t.getTerrainTexturePack();
		addChildElement(newElement, "backgroundBaseTextureName", ttp.getBackgroundTerrainTexture().getName());
		addChildElement(newElement, "rTextureBaseName", ttp.getRTerrainTexture().getName());
		addChildElement(newElement, "gTextureBaseName", ttp.getGTerrainTexture().getName());
		addChildElement(newElement, "bTextureBaseName", ttp.getBTerrainTexture().getName());
		addChildElement(newElement, "aTextureBaseName", ttp.getATerrainTexture().getName());
		addChildElement(newElement, "blendMapTextureBaseName", t.getBlendMapTexture().getName());
		addChildElement(newElement, "heightMapTextureBaseName", t.getHeightMapTexture().getName());

		doc.getDocumentElement().insertBefore(newElement, null);
	}
	
	public void addLight(Light l) {
		allLights.add(l);
		
		Element newElement = doc.createElement("light");
		newElement.setAttribute("name", name);
		
		addChildElement(newElement, "position", l.getPosition().x + ", " + l.getPosition().y + ", " + l.getPosition().z);
		addChildElement(newElement, "color", l.getColor().x + ", " + l.getColor().y + ", " + l.getColor().z);
		addChildElement(newElement, "attenuation", l.getAttenuation().x + ", " + l.getAttenuation().y + ", " + l.getAttenuation().z);

		doc.getDocumentElement().insertBefore(newElement, null);
	}
	
	public void addWater(WaterTile w) {
		allWaters.add(w);
		
		Element newElement = doc.createElement("water");
		newElement.setAttribute("name", name);
		
		addChildElement(newElement, "midX", "" + w.getMidX());
		addChildElement(newElement, "midZ", "" + w.getMidZ());

		doc.getDocumentElement().insertBefore(newElement, null);
	}
	
	@Override
	public void saveChanges() {
		NodeList nList = doc.getElementsByTagName("entity");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");
				for(Entity e : allEntities) {
					if(e.getName().equals(name)) {
						eElement.getElementsByTagName("texturedModelName").item(0).setTextContent(e.getTexturedModel().getName());
						eElement.getElementsByTagName("textureAtlasIndex").item(0).setTextContent("" + e.getTextureAtlasIndex());
						eElement.getElementsByTagName("position").item(0).setTextContent(e.getPosition().x + ", " + e.getPosition().y + ", " + e.getPosition().z);
						eElement.getElementsByTagName("rotation").item(0).setTextContent(e.getRotation().x + ", " + e.getRotation().y + ", " + e.getRotation().z);
						eElement.getElementsByTagName("scale").item(0).setTextContent(e.getScale().x + ", " + e.getScale().y + ", " + e.getScale().z);
						
						break;
					}
				}
			}
		}
		
		nList = doc.getElementsByTagName("light");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");
				for(Light l : allLights) {
					if(l.getName().equals(name)) {
						eElement.getElementsByTagName("position").item(0).setTextContent(l.getPosition().x + ", " + l.getPosition().y + ", " + l.getPosition().z);
						eElement.getElementsByTagName("color").item(0).setTextContent(l.getColor().x + ", " + l.getColor().y + ", " + l.getColor().z);
						eElement.getElementsByTagName("attenuation").item(0).setTextContent(l.getAttenuation().x + ", " + l.getAttenuation().y + ", " + l.getAttenuation().z);
						
						break;
					}
				}
			}
		}
		
		nList = doc.getElementsByTagName("water");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");
				for(WaterTile w : allWaters) {
					if(w.getName().equals(name)) {
						eElement.getElementsByTagName("midX").item(0).setTextContent("" + w.getMidX());
						eElement.getElementsByTagName("midZ").item(0).setTextContent("" + w.getMidZ());
						
						break;
					}
				}
			}
		}
		
		nList = doc.getElementsByTagName("terrain");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");
				for(Terrain t : allTerrains) {
					if(t.getName().equals(name)) {
						eElement.getElementsByTagName("Xgrid").item(0).setTextContent("" + t.getGridX());
						eElement.getElementsByTagName("Zgrid").item(0).setTextContent("" + t.getGridZ());
						
						TerrainTexturePack ttp = t.getTerrainTexturePack();
						eElement.getElementsByTagName("backgroundBaseTextureName").item(0).setTextContent(ttp.backgroundTerrainTexture.getName());
						eElement.getElementsByTagName("rTextureBaseName").item(0).setTextContent(ttp.getRTerrainTexture().getName());
						eElement.getElementsByTagName("gTextureBaseName").item(0).setTextContent(ttp.getGTerrainTexture().getName());
						eElement.getElementsByTagName("bTextureBaseName").item(0).setTextContent(ttp.getBTerrainTexture().getName());
						eElement.getElementsByTagName("aTextureBaseName").item(0).setTextContent(ttp.getATerrainTexture().getName());
						eElement.getElementsByTagName("blendMapTextureBaseName").item(0).setTextContent(t.getBlendMapTexture().getName());
						eElement.getElementsByTagName("heightMapTextureBaseName").item(0).setTextContent(t.getHeightMapTexture().getName());
						
						BufferedImage bi = ImageLoader.glTextureToBufferedImage(t.getBlendMapTexture().getTextureID());
						BufferedImage bi2 = ImageLoader.glTextureToBufferedImage(t.getHeightMapTexture().getTextureID());
						try {
							ImageIO.write(bi, "png", new File(ResourceLoader.instance.getResourceFolder().getPath() + "/" + t.getBlendMapTexture().getPath() + ".png"));
							ImageIO.write(bi2, "png", new File(ResourceLoader.instance.getResourceFolder().getPath() + "/" + t.getHeightMapTexture().getPath() + ".png"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						break;
					}
				}
			}
		}
		
		try {
			if(xmlFile.getName().toLowerCase().contains("temp")) {
				new File(resourceLoader.getResourceFolder().getPath() + "/xml/" + name).mkdir();
				File newF = new File(resourceLoader.getResourceFolder().getPath() + "/xml/" + name + "/" + name + ".xml");
				newF.createNewFile();
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(newF));
				BufferedReader br = new BufferedReader(new FileReader(newF));
				String line;
				while((line = br.readLine()) != null)
					bw.write(line);
				br.close();
				bw.close();
				xmlFile = newF;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		mapSettings.saveAllSettings(this);
		
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			Result output = new StreamResult(new OutputStreamWriter(new FileOutputStream(xmlFile)));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadAllEntities() {
		NodeList nList = doc.getElementsByTagName("entity");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");
				TexturedModel texturedModel = resourceLoader.getTexturedModelByName(eElement.getElementsByTagName("texturedModelName").item(0).getTextContent());
				int textureAtlasIndex = Integer.parseInt(eElement.getElementsByTagName("textureAtlasIndex").item(0).getTextContent());

				List<Vector3f> positions = new ArrayList<Vector3f>();
				List<Vector3f> rotations = new ArrayList<Vector3f>();
				List<Vector3f> scales = new ArrayList<Vector3f>();
				String[] tokens;

				String position = eElement.getElementsByTagName("position").item(0).getTextContent();
				tokens = position.split(",");
				Maths.createAndStoreVector3f(positions, tokens);
				
				String rotation = eElement.getElementsByTagName("rotation").item(0).getTextContent();
				tokens = rotation.split(",");
				Maths.createAndStoreVector3f(rotations, tokens);
				
				String scale = eElement.getElementsByTagName("scale").item(0).getTextContent();
				tokens = scale.split(",");
				Maths.createAndStoreVector3f(scales, tokens);
				
				for (int i = 0; i < positions.size(); i++)
					allEntities.add(new Entity(name, texturedModel, positions.get(i), rotations.get(i), scales.get(i), textureAtlasIndex));
			}
		}
	}
	
	private void loadAllTerrains() {
		NodeList nList = doc.getElementsByTagName("terrain");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");
				int Xgrid = Integer.parseInt(eElement.getElementsByTagName("Xgrid").item(0).getTextContent());
				int Zgrid = Integer.parseInt(eElement.getElementsByTagName("Zgrid").item(0).getTextContent());

				BaseTexture backgroundTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("backgroundBaseTextureName").item(0).getTextContent());
				BaseTexture rTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("rTextureBaseName").item(0).getTextContent());
				BaseTexture gTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("gTextureBaseName").item(0).getTextContent());
				BaseTexture bTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("bTextureBaseName").item(0).getTextContent());
				BaseTexture aTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("aTextureBaseName").item(0).getTextContent());
				BaseTexture blendMapTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("blendMapTextureBaseName").item(0).getTextContent());
				BaseTexture heightMapTexture = resourceLoader.getBaseTextureByName(eElement.getElementsByTagName("heightMapTextureBaseName").item(0).getTextContent());

				allTerrains.add(new Terrain(name, Xgrid, Zgrid, new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture, aTexture), blendMapTexture, heightMapTexture));
			}
		}
	}
	
	private void loadAllLights() {
		NodeList nList = doc.getElementsByTagName("light");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String[] tokens;

				String name = eElement.getAttribute("name");

				tokens = eElement.getElementsByTagName("position").item(0).getTextContent().split(",");
				Vector3f position = new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));

				tokens = eElement.getElementsByTagName("color").item(0).getTextContent().split(",");
				Vector3f color = new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));

				tokens = eElement.getElementsByTagName("attenuation").item(0).getTextContent().split(",");
				Vector3f attenuation = new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));

				allLights.add(new Light(name, position, color, attenuation));
			}
		}
	}
	
	private void loadAllWaters() {
		NodeList nList = doc.getElementsByTagName("water");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				String name = eElement.getAttribute("name");

				Float midX = Float.parseFloat(eElement.getElementsByTagName("midX").item(0).getTextContent());
				Float midZ = Float.parseFloat(eElement.getElementsByTagName("midZ").item(0).getTextContent());
				
				allWaters.add(new WaterTile(name, midX, midZ));
			}
		}
	}

	public String getName() {
		return name;
	}

	public List<Entity> getAllEntities() {
		return allEntities;
	}

	public List<Terrain> getAllTerrains() {
		return allTerrains;
	}

	public List<Light> getAllLights() {
		return allLights;
	}

	public List<WaterTile> getAllWaters() {
		return allWaters;
	}

	public MapSettings getMapSettings() {
		return mapSettings;
	}
	
	protected void addChildElement(Node node, String elementName, String elementTextContex) {
		Element childElement = doc.createElement(elementName);
		childElement.setTextContent(elementTextContex);
		node.insertBefore(childElement, null);
	}
	
}
