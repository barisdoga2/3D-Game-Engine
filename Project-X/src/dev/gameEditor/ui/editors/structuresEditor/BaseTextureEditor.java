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

import dev.engine.loaders.ImageLoader;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.textures.BaseTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.JImage;
import dev.gameEditor.ui.utils.Utils;
import dev.gameEditor.ui.windows.NewBaseTextureWindow;

public class BaseTextureEditor extends Editor implements ListSelectionListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel selectedBaseTexture = new JLabel("Selected Base Texture:");
	private JImage selectedBaseTextureS = new JImage(new Dimension(200, 200));
	
	private JLabel selectBaseTexture = new JLabel("Select Base Texture:");
	private JList<BaseTexture> selectBaseTextureS = new JList<BaseTexture>();
	
	private JButton addNewS = new JButton("                  Add New                  ");
	private JButton deleteSelectedS = new JButton("               Delete Selected             ");
	
	
	public BaseTextureEditor() {
		super("Base Textures");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		selectBaseTextureS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectBaseTextureS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectBaseTextureS.setSize(new Dimension(200, 100));
		selectBaseTextureS.addListSelectionListener(this);
		JScrollPane selectBaseTextureScroller = new JScrollPane(selectBaseTextureS);
		selectBaseTextureScroller.setViewportView(selectBaseTextureS);
		
		addNewS.addActionListener(this);
		deleteSelectedS.addActionListener(this);
		
		labels.add(selectedBaseTexture);
		tools.add(selectedBaseTextureS);
		labels.add(selectBaseTexture);
		tools.add(selectBaseTextureScroller);
		labels.add(deleteSelectedS);
		tools.add(addNewS);
		
		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
	}
	
	public void refreshList() {
		selectBaseTextureS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		if(e.getSource().equals(selectBaseTextureS)) {
			try {
				selectedBaseTextureS.setImage(ImageLoader.glTextureToBufferedImage(selectBaseTextureS.getSelectedValue().getTextureID()));
			}catch (Exception ee) {}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addNewS)) {
			new NewBaseTextureWindow(this);
		}else if(e.getSource().equals(deleteSelectedS) && selectBaseTextureS.getSelectedValue() != null) {
			if(GameEditor.instance.getResourceLoader().removeBaseTexture(selectBaseTextureS.getSelectedValue())) {
				refreshList();
				JOptionPane.showMessageDialog(null, "Successfully Removed");
			}else {
				JOptionPane.showMessageDialog(null, "The Base Texture is in use. You can't remove!");
			}
			
		}
	}

}
