package dev.gameEditor.ui.editors.structuresEditor;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import dev.gameEditor.ui.editors.EditorSettingsEditor;

public class StructuresEditor extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TexturedModelEditor texturedModelEditor = new TexturedModelEditor();
	private ModelTextureEditor modelTextureEditor = new ModelTextureEditor();
	private BaseTextureEditor baseTextureEditor = new BaseTextureEditor();
	private WaterTypeEditor waterTypeEditor = new WaterTypeEditor();
	private SkyboxEditor skyboxEditor = new SkyboxEditor();
	private EditorSettingsEditor ese = new EditorSettingsEditor();
	
	public StructuresEditor() {
		super(new GridLayout(1, 1));
		UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.darkShadow", new ColorUIResource( new Color(81, 81, 81) ));
		UIManager.put("TabbedPane.background", new Color(81, 81, 81));
		
		this.setBackground(new Color(81, 81, 81));
	}
	
	public void lateInit() {
		JTabbedPane j = new JTabbedPane();
		
		j.add(baseTextureEditor.getTabName(), baseTextureEditor);
		j.add(modelTextureEditor.getTabName(), modelTextureEditor);
		j.add(ese.getTabName(), ese);
		j.add(texturedModelEditor.getTabName(), texturedModelEditor);
		j.add(waterTypeEditor.getTabName(), waterTypeEditor);
		j.add(skyboxEditor.getTabName(), skyboxEditor);
		
		j.setSelectedComponent(modelTextureEditor);

		this.add(j);
	}
	
	public void onLoop() {
		
	}
	
}
