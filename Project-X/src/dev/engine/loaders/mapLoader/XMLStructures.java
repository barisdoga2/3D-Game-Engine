package dev.engine.loaders.mapLoader;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.loaders.objLoader.OBJLoader;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;
import dev.engine.skybox.Skybox;
import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;
import dev.engine.waters.WaterType;

public class XMLStructures extends XMLManager{

	public XMLStructures(File f, ResourceLoader resourceLoader) throws Exception {
		super(f, resourceLoader);
	}
	
	public void addTexturedModel(TexturedModel texturedModel) {
		Element newElement = doc.createElement("texturedModel");
		newElement.setAttribute("name", texturedModel.getName());
		newElement.setAttribute("rawModelPath", texturedModel.getRawModel().getPath());
		newElement.setAttribute("modelTextureName", texturedModel.getModelTexture().getName());
		newElement.setAttribute("normalBaseTextureName", "");
		newElement.setAttribute("specularBaseTextureName", "");
		
		doc.getDocumentElement().insertBefore(newElement, null);
		
		resourceLoader.getAllTexturedModels().add(texturedModel);
	}
	
	public void removeTexturedModel(TexturedModel texturedModel) {
		NodeList nList = doc.getElementsByTagName("texturedModel");
		
		boolean found = false;
		int i = 0;
		for (i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				
				if(name.equals(texturedModel.getName())) {
					eElement.getParentNode().removeChild(eElement);
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			resourceLoader.getAllTexturedModels().remove(texturedModel);
		}
	}
	
	public void addSkybox(Skybox skybox) {
		Element newElement = doc.createElement("skybox");
		newElement.setAttribute("name", skybox.getName());
		newElement.setAttribute("folderPath", skybox.getFolderPath());
		
		doc.getDocumentElement().insertBefore(newElement, null);
		
		resourceLoader.getAllSkyboxes().add(skybox);
	}
	
	public void removeSkybox(Skybox skybox) {
		NodeList nList = doc.getElementsByTagName("skybox");
		
		boolean found = false;
		int i = 0;
		for (i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				
				if(name.equals(skybox.getName())) {
					eElement.getParentNode().removeChild(eElement);
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			resourceLoader.getAllSkyboxes().remove(skybox);
		}
	}
	
	public void addWaterType(WaterType waterType) {
		Element newElement = doc.createElement("waterType");
		newElement.setAttribute("name", waterType.getName());
		newElement.setAttribute("folderPath", waterType.getFolderPath());
		
		doc.getDocumentElement().insertBefore(newElement, null);
		
		resourceLoader.getAllWaterTypes().add(waterType);
	}
	
	public void removeWaterType(WaterType waterType) {
		NodeList nList = doc.getElementsByTagName("waterType");
		
		boolean found = false;
		int i = 0;
		for (i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				
				if(name.equals(waterType.getName())) {
					eElement.getParentNode().removeChild(eElement);
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			resourceLoader.getAllWaterTypes().remove(waterType);
		}
	}
	
	@Override
	public void saveChanges() {
		// Saving TexturedModels
		NodeList nList = doc.getElementsByTagName("texturedModel");
		for(TexturedModel tm : resourceLoader.getAllTexturedModels()) {
			for (int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nList.item(i);
					String name = eElement.getAttribute("name");
					
					if(tm.getName().equals(name)) {
						eElement.setAttribute("modelTextureName", tm.getModelTexture().getName());
						eElement.setAttribute("specularBaseTextureName", tm.getSpecularMappingTexture() == null ? "" : tm.getSpecularMappingTexture().getName());
						eElement.setAttribute("normalBaseTextureName", tm.getNormalMappingTexture() == null ? "" : tm.getNormalMappingTexture().getName());
						break;
					}
				}
			}
		}
		
		super.saveChanges();
	}

	@Override
	public void loadAll() throws Exception {
		// Loading TexturedModels
		NodeList nList = doc.getElementsByTagName("texturedModel");
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				String rawModelPath = eElement.getAttribute("rawModelPath");
				ModelTexture modelTextureName = resourceLoader.getModelTextureByName(eElement.getAttribute("modelTextureName"));
				BaseTexture specularBaseTextureName = resourceLoader.getBaseTextureByName(eElement.getAttribute("specularBaseTextureName"));
				BaseTexture normalBaseTextureName = resourceLoader.getBaseTextureByName(eElement.getAttribute("normalBaseTextureName"));
				RawModel rawModel = null;
				if(normalBaseTextureName == null)
					rawModel = OBJLoader.LoadObjModel(rawModelPath);
				else
					rawModel = OBJLoader.LoadObjModel(rawModelPath);
				resourceLoader.getAllTexturedModels().add(new TexturedModel(name, rawModel, modelTextureName, normalBaseTextureName, specularBaseTextureName));
			}
		}
		
		// Loading Skyboxes
		nList = doc.getElementsByTagName("skybox");
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				String folderPath = eElement.getAttribute("folderPath");
				resourceLoader.getAllSkyboxes().add(new Skybox(name, folderPath));
			}
		}
		
		// Loading Skyboxes
		nList = doc.getElementsByTagName("waterType");
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				String folderPath = eElement.getAttribute("folderPath");
				resourceLoader.getAllWaterTypes().add(new WaterType(name, folderPath));
			}
		}
	}

}
