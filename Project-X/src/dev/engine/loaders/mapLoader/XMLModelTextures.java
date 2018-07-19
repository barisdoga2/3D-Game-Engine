package dev.engine.loaders.mapLoader;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;

public class XMLModelTextures extends XMLManager{
	
	public XMLModelTextures(File f, ResourceLoader resourceLoader) throws Exception {
		super(f, resourceLoader);
	}
	
	public void addModelTexture(ModelTexture modelTexture) {
		Element newElement = doc.createElement("modelTexture");
		newElement.setAttribute("name", modelTexture.getName());
		newElement.setAttribute("baseTextureName", modelTexture.getBaseTexture().getName());
		newElement.setAttribute("transparency", "" + modelTexture.isHasTransparency());
		newElement.setAttribute("fakeLighting", "" + modelTexture.isHasFakeLighting());
		newElement.setAttribute("shineDamper", "" + modelTexture.getShineDamper());
		newElement.setAttribute("reflectivity", "" + modelTexture.getReflectivity());
		newElement.setAttribute("atlasRowSize", "" + modelTexture.getAtlasNumberOfRows());
		
		doc.getDocumentElement().insertBefore(newElement, null);
		
		resourceLoader.getAllModelTextures().add(modelTexture);
	}
	
	public void removeModelTexture(ModelTexture modelTexture) {
		NodeList nList = doc.getElementsByTagName("modelTexture");
		
		boolean found = false;
		int i = 0;
		for (i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				
				if(name.equals(modelTexture.getName())) {
					eElement.getParentNode().removeChild(eElement);
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			resourceLoader.getAllModelTextures().remove(modelTexture);
		}
	}
	
	@Override
	public void saveChanges() {
		NodeList nList = doc.getElementsByTagName("modelTexture");
		
		for(ModelTexture mt : resourceLoader.getAllModelTextures()) {
			for (int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nList.item(i);
					String name = eElement.getAttribute("name");
					if(name.equals(mt.getName())) {
						eElement.setAttribute("baseTextureName", mt.getBaseTexture().getName());
						eElement.setAttribute("transparency", mt.isHasTransparency() ? "true" : "false");
						eElement.setAttribute("fakeLighting", mt.isHasFakeLighting() ? "true" : "false");
						eElement.setAttribute("shineDamper", "" + mt.getShineDamper());
						eElement.setAttribute("reflectivity", "" + mt.getReflectivity());
						eElement.setAttribute("atlasRowSize", "" + mt.getAtlasNumberOfRows());
						break;
					}
				}
			}
		}
		
		super.saveChanges();
	}

	@Override
	public void loadAll() throws Exception {
		NodeList nList = doc.getElementsByTagName("modelTexture");
		
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				BaseTexture baseTexture = resourceLoader.getBaseTextureByName(eElement.getAttribute("baseTextureName"));
				Boolean transparency = Boolean.parseBoolean(eElement.getAttribute("transparency"));
				Boolean fakeLighting = Boolean.parseBoolean(eElement.getAttribute("fakeLighting"));
				Float shineDamper = Float.parseFloat(eElement.getAttribute("shineDamper"));
				Float reflectivity = Float.parseFloat(eElement.getAttribute("reflectivity"));
				Integer atlasRowSize = Integer.parseInt(eElement.getAttribute("atlasRowSize"));
				resourceLoader.getAllModelTextures().add(new ModelTexture(name, baseTexture, transparency, fakeLighting, shineDamper, reflectivity, atlasRowSize));
			}
		}
	}
	
}
