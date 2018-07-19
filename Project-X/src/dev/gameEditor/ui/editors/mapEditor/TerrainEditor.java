package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import dev.gameEditor.ui.editors.Editor;

public class TerrainEditor extends Editor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane jTabbedPane;
	private TerrainHeightEditor heightEditor = new TerrainHeightEditor();
	private TerrainTextureEditor textureEditor = new TerrainTextureEditor();
	private TerrainAdder terrainAdder = new TerrainAdder();
	
	public TerrainEditor() {
		super("Terrain Editor");
		this.setLayout(new GridLayout(1, 1));
		
		UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.darkShadow", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.background", new Color(81, 81, 81));
		
		this.setBackground(new Color(81, 81, 81));
		
		jTabbedPane = new JTabbedPane();
		
		jTabbedPane.add("Height Editor", heightEditor);
		jTabbedPane.add("Texture Editor", textureEditor);
		jTabbedPane.add("Terrain Adder", terrainAdder);
		jTabbedPane.setSelectedComponent(terrainAdder);
		
		this.add(jTabbedPane);
	}
	
	@Override
	public void onLoop() {
		((Editor)jTabbedPane.getSelectedComponent()).onLoop();
	}
	
}
