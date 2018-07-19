package dev.engine.loaders.mapLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class XMLManager {
	
	protected ResourceLoader resourceLoader;
	protected File xmlFile;
	protected DocumentBuilderFactory dbFactory;
	protected DocumentBuilder dBuilder;
	protected Document doc;

	public XMLManager(File f, ResourceLoader resourceLoader) throws Exception{
		this.xmlFile = f;
		this.resourceLoader = resourceLoader;
		this.dbFactory = DocumentBuilderFactory.newInstance();
		this.dBuilder = dbFactory.newDocumentBuilder();
		this.doc = dBuilder.parse(xmlFile);
	}
	
	public abstract void loadAll() throws Exception;
	
	public void saveChanges() {
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
	
	protected void addChildElement(Node node, String elementName, String elementTextContex) {
		Element childElement = doc.createElement(elementName);
		childElement.setTextContent(elementTextContex);
		node.insertBefore(childElement, null);
	}
	
}
