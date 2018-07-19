package dev.gameEditor.ui.editors.structuresEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.Utils;
import dev.gameEditor.ui.windows.NewTexturedModelWindow;

public class TexturedModelEditor extends Editor implements ListSelectionListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel selectTexturedModel = new JLabel("Select a Textured Model:");
	private JList<TexturedModel> selectTexturedModelS = new JList<TexturedModel>();
	
	private JButton addTexturedModelS = new JButton("Add New");
	private JButton deleteTexturedModelS = new JButton("Delete Selected");
	
	private JLabel selectModelTexture = new JLabel("Select Model Texture:");
	private JList<ModelTexture> selectModelTextureS = new JList<ModelTexture>();
	
	private JLabel selectNormalTexture = new JLabel("Select a Normal Base Texture:");
	private JList<BaseTexture> selectNormalTextureS = new JList<BaseTexture>();
	private JLabel deselectNormalTexture = new JLabel("");
	private JButton deselectNormalTextureS = new JButton("Deselect Normal Base Texure");
	
	private JLabel selectSpecularTexture = new JLabel("Select a Specular Base Texture:");
	private JList<BaseTexture> selectSpecularTextureS = new JList<BaseTexture>();
	private JLabel deselectSpecularTexture = new JLabel("");
	private JButton deselectSpecularTextureS = new JButton("Deselect Specular Base Texure");
	
	private JScrollPane selectTexturedModelSScroller;
	private JScrollPane selectModelTextureSScroller;
	private JScrollPane selectNormalTextureSScroller;
	private JScrollPane selectSpecularTextureSScroller;
	
	public TexturedModelEditor() {
		super("Textured Models");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		selectTexturedModelS.setListData(GameEditor.instance.getResourceLoader().getAllTexturedModels().toArray(new TexturedModel[GameEditor.instance.getResourceLoader().getAllTexturedModels().size()]));
		selectTexturedModelS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectTexturedModelS.setSize(new Dimension(200, 100));
		selectTexturedModelS.addListSelectionListener(this);
		selectTexturedModelSScroller = new JScrollPane(selectTexturedModelS);
		selectTexturedModelSScroller.setViewportView(selectTexturedModelS);
		selectTexturedModelS.addListSelectionListener(this);
		
		addTexturedModelS.addActionListener(this);
		deleteTexturedModelS.addActionListener(this);
		
		selectModelTextureS.setListData(GameEditor.instance.getResourceLoader().getAllModelTextures().toArray(new ModelTexture[GameEditor.instance.getResourceLoader().getAllModelTextures().size()]));
		selectModelTextureS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectModelTextureS.setSize(new Dimension(200, 100));
		selectModelTextureS.addListSelectionListener(this);
		selectModelTextureSScroller = new JScrollPane(selectModelTextureS);
		selectModelTextureSScroller.setViewportView(selectModelTextureS);
		selectModelTextureS.addListSelectionListener(this);
		
		selectSpecularTextureS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectSpecularTextureS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectSpecularTextureS.setSize(new Dimension(200, 100));
		selectSpecularTextureS.addListSelectionListener(this);
		selectSpecularTextureSScroller = new JScrollPane(selectSpecularTextureS);
		selectSpecularTextureSScroller.setViewportView(selectSpecularTextureS);
		selectSpecularTextureS.addListSelectionListener(this);
		deselectSpecularTextureS.addActionListener(this);
		
		selectNormalTextureS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectNormalTextureS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectNormalTextureS.setSize(new Dimension(200, 100));
		selectNormalTextureS.addListSelectionListener(this);
		selectNormalTextureSScroller = new JScrollPane(selectNormalTextureS);
		selectNormalTextureSScroller.setViewportView(selectNormalTextureS);
		selectNormalTextureS.addListSelectionListener(this);
		deselectNormalTextureS.addActionListener(this);
		
		labels.add(selectTexturedModel);
		tools.add(selectTexturedModelSScroller);
		labels.add(deleteTexturedModelS);
		tools.add(addTexturedModelS);
		labels.add(selectModelTexture);
		tools.add(selectModelTextureSScroller);
		labels.add(selectSpecularTexture);
		tools.add(selectSpecularTextureSScroller);
		labels.add(deselectSpecularTexture);
		tools.add(deselectSpecularTextureS);
		labels.add(selectNormalTexture);
		tools.add(selectNormalTextureSScroller);
		labels.add(deselectNormalTexture);
		tools.add(deselectNormalTextureS);
		
		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
		
		selectModelTexture.setVisible(false);
		selectModelTextureSScroller.setVisible(false);
		selectNormalTexture.setVisible(false);
		selectNormalTextureSScroller.setVisible(false);
		selectSpecularTexture.setVisible(false);
		selectSpecularTextureSScroller.setVisible(false);
		deselectSpecularTexture.setVisible(false);
		deselectSpecularTextureS.setVisible(false);
		deselectNormalTexture.setVisible(false);
		deselectNormalTextureS.setVisible(false);
	}
	
	private void selectTexturedModel(TexturedModel texturedModel) {
		selectModelTexture.setVisible(true);
		selectModelTextureSScroller.setVisible(true);
		selectNormalTexture.setVisible(true);
		selectNormalTextureSScroller.setVisible(true);
		selectSpecularTexture.setVisible(true);
		selectSpecularTextureSScroller.setVisible(true);
		deselectSpecularTexture.setVisible(true);
		deselectSpecularTextureS.setVisible(true);
		deselectNormalTexture.setVisible(true);
		deselectNormalTextureS.setVisible(true);
		
		try {
			selectModelTextureS.setSelectedValue(texturedModel.getModelTexture(), false);
			selectSpecularTextureS.setSelectedValue(texturedModel.getSpecularMappingTexture(), false);
			selectNormalTextureS.setSelectedValue(texturedModel.getNormalMappingTexture(), false);
		}catch (Exception e) {
			selectModelTexture.setVisible(false);
			selectModelTextureSScroller.setVisible(false);
			selectNormalTexture.setVisible(false);
			selectNormalTextureSScroller.setVisible(false);
			selectSpecularTexture.setVisible(false);
			selectSpecularTextureSScroller.setVisible(false);
			deselectSpecularTexture.setVisible(false);
			deselectSpecularTextureS.setVisible(false);
			deselectNormalTexture.setVisible(false);
			deselectNormalTextureS.setVisible(false);
		}
		
	}
	
	public void refreshList() {
		selectTexturedModelS.setListData(GameEditor.instance.getResourceLoader().getAllTexturedModels().toArray(new TexturedModel[GameEditor.instance.getResourceLoader().getAllTexturedModels().size()]));
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getSource().equals(selectTexturedModelS)) {
			selectTexturedModel(selectTexturedModelS.getSelectedValue());
		}else if(e.getSource().equals(selectModelTextureS)) {
			selectTexturedModelS.getSelectedValue().setModelTexture(selectModelTextureS.getSelectedValue());
		}else if(e.getSource().equals(selectSpecularTextureS)) {
			selectTexturedModelS.getSelectedValue().setSpecularMappingTexture(selectSpecularTextureS.getSelectedValue());
		}else if(e.getSource().equals(selectNormalTextureS)) {
			selectTexturedModelS.getSelectedValue().setNormalMappingTexture(selectNormalTextureS.getSelectedValue());
		}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(deselectNormalTextureS)) {
			selectNormalTextureS.clearSelection();
		}else if(e.getSource().equals(deselectSpecularTextureS)) {
			selectSpecularTextureS.clearSelection();
		}else if(e.getSource().equals(addTexturedModelS)) {
			new NewTexturedModelWindow(this);
		}else if(e.getSource().equals(deleteTexturedModelS) && selectTexturedModelS.getSelectedValue() != null) {
			if(GameEditor.instance.getResourceLoader().removeTexturedModel(selectTexturedModelS.getSelectedValue())) {
				refreshList();
				JOptionPane.showMessageDialog(null, "Successfully Removed");
			}else {
				JOptionPane.showMessageDialog(null, "The Textured Model is in use. You can't remove!");
			}
		}
		
		this.revalidate();
		this.repaint();
	}
	
}
