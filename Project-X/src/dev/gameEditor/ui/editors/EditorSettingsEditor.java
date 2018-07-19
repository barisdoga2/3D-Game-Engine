package dev.gameEditor.ui.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import dev.engine.renderEngine.DisplayManager;
import dev.engine.renderEngine.MasterRenderer;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.utils.Utils;

public class EditorSettingsEditor extends Editor implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel showSkybox = new JLabel("Show Skyboxes:");
	private JCheckBox showSkyboxS = new JCheckBox();
		
	public EditorSettingsEditor() {
		super("Editor Settings");

		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		showSkyboxS.addActionListener(this);
		
		showSkyboxS.setSelected(MasterRenderer.isRenderSkybox());
		
		labels.add(showSkybox);
		tools.add(showSkyboxS);
		
		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getSource().equals(showSkyboxS)) {
			GameEditor.instance.getMasterRenderer().setRenderSkybox(showSkyboxS.isSelected());
		}
	}
	
}
