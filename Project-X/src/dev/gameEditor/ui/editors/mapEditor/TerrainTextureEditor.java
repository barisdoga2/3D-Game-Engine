package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import dev.engine.loaders.ImageLoader;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.terrains.Terrain;
import dev.engine.utils.MousePicker;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.JImage;
import dev.gameEditor.ui.utils.Utils;

public class TerrainTextureEditor extends Editor implements ChangeListener, ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Terrain currentTerrain;
	private Vector4f currentColor = new Vector4f(0,0,0,0);
	private Vector3f backBrushPosition = new Vector3f(0,0,0);
	
	private JImage backImage = new JImage(new Dimension(200, 200));
	private JImage rImage = new JImage(new Dimension(200, 200));
	private JImage gImage = new JImage(new Dimension(200, 200));
	private JImage bImage = new JImage(new Dimension(200, 200));
	private JImage aImage = new JImage(new Dimension(200, 200));
	
	private JLabel brushWidth = new JLabel("Brush Width: ");
	private JSpinner brushWidthS = new JSpinner(new SpinnerNumberModel(25f, 0f, 100f, 5f));
	
	private JLabel brushHardness = new JLabel("Brush Hardness: ");
	private JSpinner brushHardnessS = new JSpinner(new SpinnerNumberModel(0.01f, 0f, 1f, 0.025f));

	public TerrainTextureEditor() {
		super("Texture Editor");
		
		backImage.addActionListener(this);
		rImage.addActionListener(this);
		gImage.addActionListener(this);
		bImage.addActionListener(this);
		aImage.addActionListener(this);
		
		brushWidthS.addChangeListener(this);
		brushWidthS.setValue(25);
		
		List<JComponent> leftComponents = new ArrayList<JComponent>();
		List<JComponent> rightComponents = new ArrayList<JComponent>();		
		
		leftComponents.add(brushWidth);
		rightComponents.add(brushWidthS);
		leftComponents.add(brushHardness);
		rightComponents.add(brushHardnessS);
		leftComponents.add(backImage);
		rightComponents.add(rImage);
		leftComponents.add(gImage);
		rightComponents.add(bImage);
		leftComponents.add(aImage);
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
		
		if(newTerrain != null && currentTerrain != null) {
			if(!currentTerrain.equals(newTerrain)) {
				backImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getBackgroundTerrainTexture().getTextureID()));
				rImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getRTerrainTexture().getTextureID()));
				gImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getGTerrainTexture().getTextureID()));
				bImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getBTerrainTexture().getTextureID()));
				aImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getATerrainTexture().getTextureID()));
				currentTerrain = newTerrain;
			}
		}else if (newTerrain != null && currentTerrain == null){
			backImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getBackgroundTerrainTexture().getTextureID()));
			rImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getRTerrainTexture().getTextureID()));
			gImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getGTerrainTexture().getTextureID()));
			bImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getBTerrainTexture().getTextureID()));
			aImage.setImage(ImageLoader.glTextureToBufferedImage(newTerrain.getTerrainTexturePack().getATerrainTexture().getTextureID()));
			currentTerrain = newTerrain;
		}
		
		if(Mouse.isButtonDown(0) && currentTerrain != null) {
			Vector3f mousePos = mp.getCurrentTerrainPoint();
			if(mousePos != null && (backBrushPosition.x != mousePos.x || backBrushPosition.z != mousePos.z)) {
				SpinnerNumberModel model = (SpinnerNumberModel)brushWidthS.getModel();
				float value = model.getNumber().floatValue();
				model = (SpinnerNumberModel)brushHardnessS.getModel();
				float value2 = model.getNumber().floatValue();
				
				currentTerrain.paintBlendMap(currentColor, mousePos, value, value2);
				backBrushPosition = new Vector3f(mousePos.x, 0, mousePos.z);
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(backImage)) {
			currentColor = new Vector4f(-1,-1,-1,1);
		}else if(e.getSource().equals(rImage)) {
			currentColor = new Vector4f(1,0,0,1);
		}else if(e.getSource().equals(gImage)) {
			currentColor = new Vector4f(0,1,0,1);
		}else if(e.getSource().equals(bImage)) {
			currentColor = new Vector4f(0,0,1,1);
		}else if(e.getSource().equals(aImage)) {
			currentColor = new Vector4f(0,0,0,-1);
		}
	}
	
}
