package dev.gameEditor.ui.editors;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import dev.engine.loaders.mapLoader.Map;
import dev.gameEditor.ui.editors.mapEditor.MapEditor;
import dev.gameEditor.ui.editors.mapEditor.ObjectEditor;
import dev.gameEditor.ui.editors.structuresEditor.StructuresEditor;

public class RightPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane jTabbedPane;
	
	private Map currentMap;
	private StructuresEditor structuresEditor;
	private MapEditor mapEditor;
	
	public RightPanel() {
		super(new GridLayout(1, 1));
		UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.darkShadow", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.background", new Color(81, 81, 81));
		
		this.setBackground(new Color(81, 81, 81));
	}

	public void lateInit(Map map) {
		try {
			this.remove(jTabbedPane);
		}catch (Exception e) {}
		currentMap = map;
		
		mapEditor = new MapEditor(currentMap);
		mapEditor.lateInit();
		structuresEditor = new StructuresEditor();
		structuresEditor.lateInit();
		
		jTabbedPane = new JTabbedPane();
		
		jTabbedPane.add("Structures", structuresEditor);
		jTabbedPane.add("Map", mapEditor);
		jTabbedPane.setSelectedComponent(mapEditor);
		
		this.add(jTabbedPane);
		
		this.revalidate();
		this.repaint();
	}

	public void onLoop() {
		if(jTabbedPane.getSelectedComponent().equals(mapEditor))
			mapEditor.onLoop();
		else if(jTabbedPane.getSelectedComponent().equals(structuresEditor))
			structuresEditor.onLoop();
	}

	public ObjectEditor getObjectEditor() {
		return mapEditor.getObjectEditor();
	}
	
}
