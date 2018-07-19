package dev.gameEditor.ui.editors.structuresEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.Utils;
import dev.gameEditor.ui.windows.NewModelTextureWindow;

public class ModelTextureEditor extends Editor implements ListSelectionListener, ActionListener, ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel selectModelTexture = new JLabel("Select a Model Texture:");
	private JList<ModelTexture> selectModelTextureS = new JList<ModelTexture>();
	
	private JButton deleteSelectedS = new JButton("Delete Selected");
	private JButton addNewS = new JButton("Add New");
	
	private JLabel selectBaseTexture = new JLabel("Select Base Texture:");
	private JList<BaseTexture> selectBaseTextureS = new JList<BaseTexture>();
	
	private JScrollPane selectModelTextureSScroller;
	private JScrollPane selectBaseTextureSScroller;
	
	private JLabel hasTransparency = new JLabel("Has Transparency:");
	private JCheckBox hasTransparencyS = new JCheckBox();
	
	private JLabel hasFakeLighting = new JLabel("Has Fake Lighting:");
	private JCheckBox hasFakeLightingS = new JCheckBox();
	
	private JLabel atlasRowSize = new JLabel("Atlas Row Size:");
	private JSpinner atlasRowSizeS = new JSpinner();
	
	private JLabel reflectivity = new JLabel("Reflectivity:");
	private JSpinner reflectivityS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.1f));
	
	private JLabel shineDamper = new JLabel("Shine Damper:");
	private JSpinner shineDamperS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.1f));
	
	public ModelTextureEditor() {
		super("Model Textures");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		selectModelTextureS.setListData(GameEditor.instance.getResourceLoader().getAllModelTextures().toArray(new ModelTexture[GameEditor.instance.getResourceLoader().getAllModelTextures().size()]));
		selectModelTextureS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectModelTextureS.setSize(new Dimension(200, 100));
		selectModelTextureS.addListSelectionListener(this);
		selectModelTextureSScroller = new JScrollPane(selectModelTextureS);
		selectModelTextureSScroller.setViewportView(selectModelTextureS);
		selectModelTextureS.addListSelectionListener(this);
		
		deleteSelectedS.addActionListener(this);
		addNewS.addActionListener(this);
		
		selectBaseTextureS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectBaseTextureS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectBaseTextureS.setSize(new Dimension(200, 100));
		selectBaseTextureS.addListSelectionListener(this);
		selectBaseTextureSScroller = new JScrollPane(selectBaseTextureS);
		selectBaseTextureSScroller.setViewportView(selectBaseTextureS);
		selectBaseTextureS.addListSelectionListener(this);
		
		hasTransparencyS.addActionListener(this);
		hasFakeLightingS.addActionListener(this);
		atlasRowSizeS.addChangeListener(this);
		reflectivityS.addChangeListener(this);
		shineDamperS.addChangeListener(this);
		
		labels.add(selectModelTexture);
		tools.add(selectModelTextureSScroller);
		labels.add(deleteSelectedS);
		tools.add(addNewS);
		labels.add(selectBaseTexture);
		tools.add(selectBaseTextureSScroller);
		labels.add(hasTransparency);
		tools.add(hasTransparencyS);
		labels.add(hasFakeLighting);
		tools.add(hasFakeLightingS);
		labels.add(atlasRowSize);
		tools.add(atlasRowSizeS);
		labels.add(reflectivity);
		tools.add(reflectivityS);
		labels.add(shineDamper);
		tools.add(shineDamperS);
		
		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
		
		selectBaseTexture.setVisible(false);
		selectBaseTextureSScroller.setVisible(false);
		hasTransparency.setVisible(false);
		hasTransparencyS.setVisible(false);
		hasFakeLighting.setVisible(false);
		hasFakeLightingS.setVisible(false);
		atlasRowSize.setVisible(false);
		atlasRowSizeS.setVisible(false);
		reflectivity.setVisible(false);
		reflectivityS.setVisible(false);
		shineDamper.setVisible(false);
		shineDamperS.setVisible(false);
	}
	
	private void selectModelTexture(ModelTexture modelTexture) {
		selectBaseTexture.setVisible(true);
		selectBaseTextureSScroller.setVisible(true);
		hasTransparency.setVisible(true);
		hasTransparencyS.setVisible(true);
		hasFakeLighting.setVisible(true);
		hasFakeLightingS.setVisible(true);
		atlasRowSize.setVisible(true);
		atlasRowSizeS.setVisible(true);
		reflectivity.setVisible(true);
		reflectivityS.setVisible(true);
		shineDamper.setVisible(true);
		shineDamperS.setVisible(true);
		
		try {
			selectBaseTextureS.setSelectedValue(modelTexture.getBaseTexture(), false);
			hasTransparencyS.setSelected(modelTexture.isHasTransparency());
			hasFakeLightingS.setSelected(modelTexture.isHasFakeLighting());
			atlasRowSizeS.setValue(modelTexture.getAtlasNumberOfRows());
			reflectivityS.setValue(modelTexture.getReflectivity());
			shineDamperS.setValue(modelTexture.getShineDamper());
		}catch (Exception e) {
			selectBaseTexture.setVisible(false);
			selectBaseTextureSScroller.setVisible(false);
			hasTransparency.setVisible(false);
			hasTransparencyS.setVisible(false);
			hasFakeLighting.setVisible(false);
			hasFakeLightingS.setVisible(false);
			atlasRowSize.setVisible(false);
			atlasRowSizeS.setVisible(false);
			reflectivity.setVisible(false);
			reflectivityS.setVisible(false);
			shineDamper.setVisible(false);
			shineDamperS.setVisible(false);
		}
	}
	
	public void refreshList() {
		try {
			selectModelTextureS.setListData(GameEditor.instance.getResourceLoader().getAllModelTextures().toArray(new ModelTexture[GameEditor.instance.getResourceLoader().getAllModelTextures().size()]));
		}catch (Exception e) {}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(hasFakeLightingS)) {
			selectModelTextureS.getSelectedValue().setHasFakeLighting(hasFakeLightingS.isSelected());
		}else if(e.getSource().equals(hasTransparencyS)) {
			selectModelTextureS.getSelectedValue().setHasTransparency(hasTransparencyS.isSelected());
		}else if(e.getSource().equals(addNewS)) {
			new NewModelTextureWindow(this);
		}else if(e.getSource().equals(deleteSelectedS) && selectModelTextureS.getSelectedValue() != null) {
			if(GameEditor.instance.getResourceLoader().removeModelTexture(selectModelTextureS.getSelectedValue())) {
				refreshList();
				JOptionPane.showMessageDialog(null, "Successfully Removed");
			}else {
				JOptionPane.showMessageDialog(null, "The Textured Model is in use. You can't remove!");
			}
		}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(e.getSource().equals(selectModelTextureS)) {
			selectModelTexture(selectModelTextureS.getSelectedValue());
		}else if(e.getSource().equals(selectBaseTextureS)) {
			selectModelTextureS.getSelectedValue().setBaseTexture(selectBaseTextureS.getSelectedValue());
		}
		
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(e.getSource().equals(atlasRowSizeS)) {
			selectModelTextureS.getSelectedValue().setAtlasNumberOfRows((int)atlasRowSizeS.getValue());
		}else if(e.getSource().equals(reflectivityS)) {
			selectModelTextureS.getSelectedValue().setReflectivity((float)reflectivityS.getValue());
		}else if(e.getSource().equals(shineDamperS)) {
			selectModelTextureS.getSelectedValue().setShineDamper((float)shineDamperS.getValue());
		}
		
		this.revalidate();
		this.repaint();
	}
	
}
