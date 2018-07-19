package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.renderEngine.MasterRenderer;
import dev.engine.terrains.Terrain;
import dev.engine.utils.MousePicker;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.JImage;
import dev.gameEditor.ui.utils.Utils;

public class TerrainHeightEditor extends Editor implements ActionListener, ChangeListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int UP_MODE = 1;
	public static final int SMOOTH_MODE = 0;
	public static final int DOWN_MODE = -1;
	
	private int currentMode = SMOOTH_MODE;
	private Vector3f backBrushPosition = new Vector3f(0,0,0);
	
	private JImage upImage;
	private JImage downImage;
	private JImage smoothImage;
	
	private JLabel brushWidth = new JLabel("Brush Width: "); 
	private JSpinner brushWidthS = new JSpinner(new SpinnerNumberModel(25f, 0f, 100f, 5f));
	
	private JLabel targetHeightDepth = new JLabel("Target Height/Depth: ");
	private JSpinner targetHeightDepthS = new JSpinner(new SpinnerNumberModel(0f, 0f, 1f, 0.05f));


	public TerrainHeightEditor() {
		super("Height Editor");
		
		upImage = new JImage(new Dimension(200, 200));
		downImage = new JImage(new Dimension(200, 200));
		smoothImage = new JImage(new Dimension(200, 200));
		try {
			upImage.setImage(ImageIO.read(new File("res/editor/up.png")));
			downImage.setImage(ImageIO.read(new File("res/editor/down.png")));
			smoothImage.setImage(ImageIO.read(new File("res/editor/smooth.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		upImage.addActionListener(this); 
		downImage.addActionListener(this);
		smoothImage.addActionListener(this);
		
		brushWidthS.addChangeListener(this);
		brushWidthS.setValue(25);
		
		List<JComponent> leftComponents = new ArrayList<JComponent>();
		List<JComponent> rightComponents = new ArrayList<JComponent>();		
		
		leftComponents.add(brushWidth);
		rightComponents.add(brushWidthS);
		leftComponents.add(targetHeightDepth);
		rightComponents.add(targetHeightDepthS);
		leftComponents.add(upImage);
		rightComponents.add(downImage);
		leftComponents.add(smoothImage);
		rightComponents.add(new JLabel("                                                                    "));
		
		Utils.createForm(this, leftComponents.toArray(new Component[leftComponents.size()]), rightComponents.toArray(new Component[leftComponents.size()]), 50, 0, 50, 5);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(brushWidthS)) {
			SpinnerNumberModel model = (SpinnerNumberModel)brushWidthS.getModel();
			float value = model.getNumber().floatValue();
			MasterRenderer.setBrushWidth(value);
		}
	}
	
	@Override
	public void onLoop() {
		MousePicker mp = GameEditor.instance.getMousePicker();
		Terrain newTerrain = mp.getMouseOnTerrain();
		
		if(Mouse.isButtonDown(0) && newTerrain != null) {
			Vector3f mousePos = mp.getCurrentTerrainPoint();
			if(mousePos != null && (backBrushPosition.x != mousePos.x || backBrushPosition.z != mousePos.z)) {
				SpinnerNumberModel model = (SpinnerNumberModel)brushWidthS.getModel();
				float value = model.getNumber().floatValue();
				model = (SpinnerNumberModel)targetHeightDepthS.getModel();
				float value2 = model.getNumber().floatValue();
				
				newTerrain.paintHeightMap(currentMode, mousePos, value, value2);
				
				backBrushPosition = new Vector3f(mousePos.x, 0, mousePos.z);
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(upImage)) {
			currentMode = UP_MODE;
		}else if(e.getSource().equals(downImage)) {
			currentMode = DOWN_MODE;
		}else if(e.getSource().equals(smoothImage)) {
			currentMode = SMOOTH_MODE;
		}
	}
	
}
