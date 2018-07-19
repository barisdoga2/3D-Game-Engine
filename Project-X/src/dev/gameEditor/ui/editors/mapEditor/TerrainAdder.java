package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import dev.engine.loaders.ImageLoader;
import dev.engine.terrains.Terrain;
import dev.engine.terrains.TerrainTexturePack;
import dev.engine.textures.BaseTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.Utils;

public class TerrainAdder extends Editor implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel selectBackground = new JLabel("Select Background Texture: ");
	private JLabel selectR = new JLabel("Select R Texture: ");
	private JLabel selectG = new JLabel("Select G Texture: ");
	private JLabel selectB = new JLabel("Select B Texture: ");
	private JLabel selectA = new JLabel("Select A Texture: ");
	
	private JList<BaseTexture> selectBackgroundS = new JList<BaseTexture>();
	private JScrollPane selectBackgroundSScroller;
	private JList<BaseTexture> selectRS = new JList<BaseTexture>();
	private JScrollPane selectRSScroller;
	private JList<BaseTexture> selectGS = new JList<BaseTexture>();
	private JScrollPane selectGSScroller;
	private JList<BaseTexture> selectBS = new JList<BaseTexture>();
	private JScrollPane selectBSScroller;
	private JList<BaseTexture> selectAS = new JList<BaseTexture>();
	private JScrollPane selectASScroller;
	
	private JLabel name = new JLabel("Please specify a name: ");
	private JTextField nameS = new JTextField(10);
	
	private JLabel gridX = new JLabel("Grid X: ");
	private JLabel gridZ = new JLabel("Grid Z: ");
	private JSpinner gridXS = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
	private JSpinner gridZS = new JSpinner(new SpinnerNumberModel(0, null, null, 1));
	
	private JButton addButton = new JButton("Add");

	public TerrainAdder() {
		super("Terrain Adder");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		selectBackgroundS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectBackgroundS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectBackgroundS.setSize(new Dimension(200, 100));
		selectBackgroundSScroller = new JScrollPane(selectBackgroundS);
		selectBackgroundSScroller.setViewportView(selectBackgroundS);
		
		selectRS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectRS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectRS.setSize(new Dimension(200, 100));
		selectRSScroller = new JScrollPane(selectRS);
		selectRSScroller.setViewportView(selectRS);
		
		selectGS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectGS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectGS.setSize(new Dimension(200, 100));
		selectGSScroller = new JScrollPane(selectGS);
		selectGSScroller.setViewportView(selectGS);
		
		selectBS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectBS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectBS.setSize(new Dimension(200, 100));
		selectBSScroller = new JScrollPane(selectBS);
		selectBSScroller.setViewportView(selectBS);
		
		selectAS.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		selectAS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectAS.setSize(new Dimension(200, 100));
		selectASScroller = new JScrollPane(selectAS);
		selectASScroller.setViewportView(selectAS);
		
		addButton.addActionListener(this);
		
		labels.add(selectBackground);
		tools.add(selectBackgroundSScroller);
		labels.add(selectR);
		tools.add(selectRSScroller);
		labels.add(selectG);
		tools.add(selectGSScroller);
		labels.add(selectB);
		tools.add(selectBSScroller);
		labels.add(selectA);
		tools.add(selectASScroller);
		labels.add(name);
		tools.add(nameS);
		labels.add(gridX);
		tools.add(gridXS);
		labels.add(gridZ);
		tools.add(gridZS);
		labels.add(addButton);
		tools.add(new JLabel("                                               "));
		
		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addButton)) {
			BaseTexture backgroundTexture = selectBackgroundS.getSelectedValue();
			BaseTexture rTexture = selectRS.getSelectedValue();
			BaseTexture gTexture = selectGS.getSelectedValue();
			BaseTexture bTexture = selectBS.getSelectedValue();
			BaseTexture aTexture = selectAS.getSelectedValue();
			
			if(backgroundTexture != null && rTexture != null && gTexture != null && bTexture != null && aTexture != null && !nameS.getText().equals("")) {
				
				for(Terrain t : GameEditor.instance.getCurrentMap().getAllTerrains()) {
					if(t.getName().equals(nameS.getText())) {
						JOptionPane.showMessageDialog(this, "Specified name already exists.");
						return;
					}
					
					if(t.getGridX() == getValue(gridXS) && t.getGridZ() == getValue(gridZS)) {
						JOptionPane.showMessageDialog(this, "Specified grid coordinates already exists.");
						return;
					}
				}
				
				BufferedImage heightMap = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
				for(int y = 0 ; y < 256 ; y++)
					for(int x = 0 ; x < 256 ; x++)
						heightMap.setRGB(x, y, new Color(127, 127, 127).getRGB());
					
				BufferedImage blendMap = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
				for(int y = 0 ; y < 256 ; y++)
					for(int x = 0 ; x < 256 ; x++)
						blendMap.setRGB(x, y, new Color(0, 0, 0).getRGB());
				
				try {
					ImageIO.write(heightMap, "png", new File("res/baseTextures/" + "heightMap" + nameS.getText() + ".png"));
					ImageIO.write(blendMap, "png", new File("res/baseTextures/" + "blendMap" + nameS.getText() + ".png"));
				}catch(Exception ee) {
					ee.printStackTrace();
				}
				
				BaseTexture heightMapTexture = new BaseTexture("heightMap" + nameS.getText(), "baseTextures/" + "heightMap" + nameS.getText(), ImageLoader.loadTexture("baseTextures/" + "heightMap" + nameS.getText()));
				BaseTexture blendMapTexture = new BaseTexture("blendMap" + nameS.getText(), "baseTextures/" + "blendMap" + nameS.getText(), ImageLoader.loadTexture("baseTextures/" + "blendMap" + nameS.getText()));
				
				GameEditor.instance.getResourceLoader().addBaseTexture(heightMapTexture);
				GameEditor.instance.getResourceLoader().addBaseTexture(blendMapTexture);
				
				GameEditor.instance.getCurrentMap().addTerrain(new Terrain(nameS.getText(), getValue(gridXS), getValue(gridZS), new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture, aTexture), blendMapTexture, heightMapTexture));
				
			}else
				JOptionPane.showMessageDialog(this, "Please fill all areas.");
		}
	}
	
	private int getValue(JSpinner spinner) {
		SpinnerNumberModel model = (SpinnerNumberModel)spinner.getModel();
		return model.getNumber().intValue();
	}
	
}
