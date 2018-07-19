package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

import dev.engine.loaders.mapLoader.Map;
import dev.engine.renderEngine.MasterRenderer;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.editors.EditorSettingsEditor;

public class MapEditor extends JPanel implements ChangeListener{

	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane jTabbedPane;
	
	private ObjectEditor objectEditor = new ObjectEditor();
	private TerrainEditor terrainEditor = new TerrainEditor();
	private WaterEditor waterEditor = new WaterEditor();
	
	private MapSettingsEditor mapSettingsEditor;
	private EditorSettingsEditor ese = new EditorSettingsEditor();
	
	public MapEditor(Map map) {
		super(new GridLayout(1, 1));
		UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.darkShadow", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.background", new Color(81, 81, 81));
		
		this.setBackground(new Color(81, 81, 81));
		this.mapSettingsEditor = new MapSettingsEditor(map);
	}
	
	public void lateInit() {
		jTabbedPane = new JTabbedPane();
		jTabbedPane.addChangeListener(this);
		jTabbedPane.add(ese.getTabName(), ese);
		jTabbedPane.add(mapSettingsEditor.getTabName(), mapSettingsEditor);
		jTabbedPane.add(objectEditor.getTabName(), objectEditor);
		jTabbedPane.add(terrainEditor.getTabName(), terrainEditor);
		jTabbedPane.add(waterEditor.getTabName(), waterEditor);
		jTabbedPane.setSelectedComponent(terrainEditor);

		this.add(jTabbedPane);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(jTabbedPane.getSelectedIndex() == 3) 
			MasterRenderer.EnableBrush(25);
		else 
			MasterRenderer.DisableBrush();
	}

	public void onLoop() {
		((Editor)jTabbedPane.getSelectedComponent()).onLoop();
	}

	public ObjectEditor getObjectEditor() {
		return objectEditor;
	}
	
}
