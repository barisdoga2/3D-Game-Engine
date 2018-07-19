package dev.gameEditor.ui.editors.structuresEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
import dev.engine.skybox.Skybox;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.JImage;
import dev.gameEditor.ui.utils.Utils;
import dev.gameEditor.ui.windows.NewSkyboxWindow;

public class SkyboxEditor extends Editor implements ListSelectionListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel right = new JLabel("Right:");
	private JImage rightS = new JImage(new Dimension(100, 100));
	private JLabel left = new JLabel("Left:");
	private JImage leftS = new JImage(new Dimension(100, 100));
	private JLabel top = new JLabel("Top:");
	private JImage topS = new JImage(new Dimension(100, 100));
	private JLabel bottom = new JLabel("Bottom:");
	private JImage bottomS = new JImage(new Dimension(100, 100));
	private JLabel back = new JLabel("Back:");
	private JImage backS = new JImage(new Dimension(100, 100));
	private JLabel front = new JLabel("Front:");
	private JImage frontS = new JImage(new Dimension(100, 100));
	
	private JLabel selectSkybox = new JLabel("Select Skybox:");
	private JList<Skybox> selectSkyboxS = new JList<Skybox>();
	
	private JButton addNewS = new JButton("            Add New            ");
	private JButton deleteSelectedS = new JButton("Delete Selected");
	
	private JScrollPane selectSkyboxSScroller;

	public SkyboxEditor() {
		super("Skyboxes");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		selectSkyboxS.setListData(GameEditor.instance.getResourceLoader().getAllSkyboxes().toArray(new Skybox[GameEditor.instance.getResourceLoader().getAllSkyboxes().size()]));
		selectSkyboxS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectSkyboxS.setSize(new Dimension(200, 100));
		selectSkyboxS.addListSelectionListener(this);
		selectSkyboxSScroller = new JScrollPane(selectSkyboxS);
		selectSkyboxSScroller.setViewportView(selectSkyboxS);
		
		addNewS.addActionListener(this);
		deleteSelectedS.addActionListener(this);
				
		labels.add(right);
		tools.add(rightS);
		labels.add(left);
		tools.add(leftS);
		labels.add(top);
		tools.add(topS);
		labels.add(bottom);
		tools.add(bottomS);
		labels.add(back);
		tools.add(backS);
		labels.add(front);
		tools.add(frontS);
		
		labels.add(selectSkybox);
		tools.add(selectSkyboxSScroller);
		labels.add(deleteSelectedS);
		tools.add(addNewS);

		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
		
	}
	
	public void refreshList() {
		try {
			selectSkyboxS.setListData(GameEditor.instance.getResourceLoader().getAllSkyboxes().toArray(new Skybox[GameEditor.instance.getResourceLoader().getAllSkyboxes().size()]));
		}catch(Exception e) {}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addNewS)) {
			new NewSkyboxWindow(this);
		}else if(e.getSource().equals(deleteSelectedS) && selectSkyboxS.getSelectedValue() != null) {
			if(GameEditor.instance.getResourceLoader().removeSkybox(selectSkyboxS.getSelectedValue())) {
				refreshList();
				JOptionPane.showMessageDialog(null, "Successfully Removed");
			}else {
				JOptionPane.showMessageDialog(null, "The Base Texture is in use. You can't remove!");
			}
			
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getSource().equals(selectSkyboxS)) {
			try {
				rightS.setImage(ImageLoader.loadBufferedImage(selectSkyboxS.getSelectedValue().getFolderPath() + "/right"));
				leftS.setImage(ImageLoader.loadBufferedImage(selectSkyboxS.getSelectedValue().getFolderPath() + "/left"));
				topS.setImage(ImageLoader.loadBufferedImage(selectSkyboxS.getSelectedValue().getFolderPath() + "/top"));
				bottomS.setImage(ImageLoader.loadBufferedImage(selectSkyboxS.getSelectedValue().getFolderPath() + "/bottom"));
				backS.setImage(ImageLoader.loadBufferedImage(selectSkyboxS.getSelectedValue().getFolderPath() + "/back"));
				frontS.setImage(ImageLoader.loadBufferedImage(selectSkyboxS.getSelectedValue().getFolderPath() + "/front"));
			}catch (Exception ee) {
				BufferedImage tmp = new BufferedImage((int)rightS.getSize().getWidth(), (int)rightS.getSize().getHeight(), BufferedImage.TYPE_INT_RGB);
				rightS.setImage(tmp);
				leftS.setImage(tmp);
				topS.setImage(tmp);
				bottomS.setImage(tmp);
				backS.setImage(tmp);
				frontS.setImage(tmp);
			}
		}
	}

}
