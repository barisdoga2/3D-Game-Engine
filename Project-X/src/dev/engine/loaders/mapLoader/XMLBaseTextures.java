package dev.engine.loaders.mapLoader;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dev.engine.loaders.ImageLoader;
import dev.engine.textures.BaseTexture;

public class XMLBaseTextures extends XMLManager{
	
	public XMLBaseTextures(File f, ResourceLoader resourceLoader) throws Exception {
		super(f, resourceLoader);
	}
	
	public void addBaseTexture(BaseTexture baseTexture) {
		Element newElement = doc.createElement("baseTexture");
		newElement.setAttribute("name", baseTexture.getName());
		newElement.setAttribute("path", baseTexture.getPath());
		
		doc.getDocumentElement().insertBefore(newElement, null);
		
		resourceLoader.getAllBaseTextures().add(baseTexture);
	}

	@Override
	public void loadAll() throws Exception {
		NodeList nList = doc.getElementsByTagName("baseTexture");
		
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);

				String name = eElement.getAttribute("name");
				String texturePath = eElement.getAttribute("path");

				resourceLoader.getAllBaseTextures().add(new BaseTexture(name, texturePath, ImageLoader.loadTexture(texturePath)));
			}
		}
	}

	public void removeBaseTexture(BaseTexture baseTexture) {
		NodeList nList = doc.getElementsByTagName("baseTexture");
		
		boolean found = false;
		int i = 0;
		for (i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nList.item(i);
				String name = eElement.getAttribute("name");
				
				if(name.equals(baseTexture.getName())) {
					eElement.getParentNode().removeChild(eElement);
					found = true;
					break;
				}
			}
		}
		
		if(found) {
			resourceLoader.getAllBaseTextures().remove(baseTexture);
		}
		
	}

}
