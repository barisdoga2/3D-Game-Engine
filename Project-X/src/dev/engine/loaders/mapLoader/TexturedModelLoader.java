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

import dev.engine.models.ModelTexture;
import dev.engine.models.OBJLoader;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;

public class TexturedModelLoader {

private static final String XML_PATH = "res/xml/texturedModels.xml";
	
	protected static List<TexturedModel> loadAll(){
		List<TexturedModel> all = new ArrayList<TexturedModel>();
		try{
			File fXmlFile = new File(XML_PATH);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();			
			NodeList nList = doc.getElementsByTagName("texturedModel");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					String name = eElement.getAttribute("name");
					
					ModelTexture modelTexture = GameStructs.getModelTexture(eElement.getElementsByTagName("modelTexture").item(0).getTextContent());
					//ModelTexture diffuseTexture = GameStructs.getModelTexture(eElement.getElementsByTagName("diffuseTexture").item(0).getTextContent());
					//ModelTexture specularTexture = GameStructs.getModelTexture(eElement.getElementsByTagName("specularTexture").item(0).getTextContent());
					//ModelTexture normalTexture = GameStructs.getModelTexture(eElement.getElementsByTagName("normalTexture").item(0).getTextContent());
					
					RawModel rawModel =  OBJLoader.LoadObjModel(eElement.getElementsByTagName("rawModel").item(0).getTextContent());
					
					all.add(new TexturedModel(name, rawModel, modelTexture));
					
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return all;
	}
	
}
