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
import dev.engine.waters.WaterType;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.JImage;
import dev.gameEditor.ui.utils.Utils;
import dev.gameEditor.ui.windows.NewWaterTypeWindow;

public class WaterTypeEditor extends Editor implements ListSelectionListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel dudv = new JLabel("Right:");
	private JImage dudvS = new JImage(new Dimension(200, 200));
	private JLabel normal = new JLabel("Left:");
	private JImage normalS = new JImage(new Dimension(200, 200));
	
	private JLabel waterType = new JLabel("Select Water Type:");
	private JList<WaterType> waterTypeS = new JList<WaterType>();
	
	private JButton addNewS = new JButton("                   Add New                   ");
	private JButton deleteSelectedS = new JButton("Delete Selected");
	
	private JScrollPane waterTypeSScroller;

	public WaterTypeEditor() {
		super("Water Types");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		waterTypeS.setListData(GameEditor.instance.getResourceLoader().getAllWaterTypes().toArray(new WaterType[GameEditor.instance.getResourceLoader().getAllWaterTypes().size()]));
		waterTypeS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		waterTypeS.setSize(new Dimension(200, 100));
		waterTypeS.addListSelectionListener(this);
		waterTypeSScroller = new JScrollPane(waterTypeS);
		waterTypeSScroller.setViewportView(waterTypeS);
		
		addNewS.addActionListener(this);
		deleteSelectedS.addActionListener(this);
				
		labels.add(dudv);
		tools.add(dudvS);
		labels.add(normal);
		tools.add(normalS);
		
		labels.add(waterType);
		tools.add(waterTypeSScroller);
		labels.add(deleteSelectedS);
		tools.add(addNewS);

		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
		
	}
	
	public void refreshList() {
		try {
			waterTypeS.setListData(GameEditor.instance.getResourceLoader().getAllWaterTypes().toArray(new WaterType[GameEditor.instance.getResourceLoader().getAllWaterTypes().size()]));
		}catch(Exception e) {}
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addNewS)) {
			new NewWaterTypeWindow(this);
		}else if(e.getSource().equals(deleteSelectedS) && waterTypeS.getSelectedValue() != null) {
			if(GameEditor.instance.getResourceLoader().removeWaterType(waterTypeS.getSelectedValue())) {
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
		
		if(e.getSource().equals(waterTypeS)) {
			try {
				dudvS.setImage(ImageLoader.loadBufferedImage(waterTypeS.getSelectedValue().getFolderPath() + "/dudv"));
				normalS.setImage(ImageLoader.loadBufferedImage(waterTypeS.getSelectedValue().getFolderPath() + "/normal"));
			}catch (Exception ee) {
				BufferedImage tmp = new BufferedImage((int)dudvS.getSize().getWidth(), (int)dudvS.getSize().getHeight(), BufferedImage.TYPE_INT_RGB);
				dudvS.setImage(tmp);
				normalS.setImage(tmp);
			}
		}
	}

}
