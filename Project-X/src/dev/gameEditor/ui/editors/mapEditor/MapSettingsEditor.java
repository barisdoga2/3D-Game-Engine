package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lwjgl.util.vector.Vector3f;

import dev.engine.loaders.mapLoader.Map;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.skybox.Skybox;
import dev.engine.waters.WaterType;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.Utils;

public class MapSettingsEditor extends Editor implements ChangeListener, KeyListener, ListSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private Map map;
	
	private JLabel tilingFactor = new JLabel("Tiling Factor:");
	private JSpinner tilingFactorS = new JSpinner();
	
	private JLabel fogDensity = new JLabel("Fog Density:");
	private JSpinner fogDensityS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.001f));
	
	private JLabel fogGradient = new JLabel("Fog Gradient:");
	private JSpinner fogGradientS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.001f));
	
	private JLabel rSkyColor = new JLabel("Sky Color(RED):");
	private JLabel gSkyColor = new JLabel("Sky Color(GREEN):");
	private JLabel bSkyColor = new JLabel("Sky Color(BLUE):");
	private JSpinner rSkyColorS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	private JSpinner gSkyColorS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	private JSpinner bSkyColorS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel minDiffuseLighting = new JLabel("Min Diffuse:");
	private JSpinner minDiffuseLightingS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel minSpecularLighting = new JLabel("Min Specular:");
	private JSpinner minSpecularLightingS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));

	private JLabel waterWaveMoveSpeed = new JLabel("Water Move Speed:");
	private JSpinner waterWaveMoveSpeedS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel waterWaveStrength = new JLabel("Water Wave Strength:");
	private JSpinner waterWaveStrengthS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel waterReflectivityFactor = new JLabel("Water Reflectivity Factor:");
	private JSpinner waterReflectivityFactorS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel waterTilingFactor = new JLabel("Water Tiling Factor:");
	private JSpinner waterTilingFactorS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel waterShineDamper = new JLabel("Water Shine Damper:");
	private JSpinner waterShineDamperS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel waterReflectivity = new JLabel("Water Reflectivity:");
	private JSpinner waterReflectivityS = new JSpinner(new SpinnerNumberModel(0.1f, null, null, 0.05f));
	
	private JLabel skyBox = new JLabel("Skybox:");
	private JList<Skybox> skyBoxS = new JList<Skybox>();
	
	private JLabel waterType = new JLabel("Water Type:");
	private JList<WaterType> waterTypeS = new JList<WaterType>();
	
	@SuppressWarnings("rawtypes")
	public MapSettingsEditor(Map map) {
		super("Map Settings");
		this.map = map;
		
		ArrayList<JComponent> spinners = new ArrayList<JComponent>();
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		
		tilingFactorS.setValue(map.getMapSettings().getTilingFactor());
		fogDensityS.setValue(map.getMapSettings().getFogDensity());
		fogGradientS.setValue(map.getMapSettings().getFogGradient());
		rSkyColorS.setValue(map.getMapSettings().getSkyColor().x);
		gSkyColorS.setValue(map.getMapSettings().getSkyColor().y);
		bSkyColorS.setValue(map.getMapSettings().getSkyColor().z);
		minDiffuseLightingS.setValue(map.getMapSettings().getMinDiffuseLighting());
		minSpecularLightingS.setValue(map.getMapSettings().getMinSpecularLighting());
		waterWaveMoveSpeedS.setValue(map.getMapSettings().getWaterWaveMoveSpeed());
		waterWaveStrengthS.setValue(map.getMapSettings().getWaterWaveStrength());
		waterReflectivityFactorS.setValue(map.getMapSettings().getWaterReflectivityFactor());
		waterTilingFactorS.setValue(map.getMapSettings().getWaterTilingFactor());
		waterShineDamperS.setValue(map.getMapSettings().getWaterShineDamper());
		waterReflectivityS.setValue(map.getMapSettings().getWaterReflectivity());
		
		skyBoxS.setListData(GameEditor.instance.getResourceLoader().getAllSkyboxes().toArray(new Skybox[GameEditor.instance.getResourceLoader().getAllSkyboxes().size()]));
		skyBoxS.setSelectedValue(map.getMapSettings().getSkybox(), false);
		skyBoxS.addListSelectionListener(this);
		skyBoxS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		skyBoxS.setSize(new Dimension(200, 100));
		JScrollPane skyboxSScroller = new JScrollPane(skyBoxS);
		skyboxSScroller.setViewportView(skyBoxS);
		
		waterTypeS.setListData(GameEditor.instance.getResourceLoader().getAllWaterTypes().toArray(new WaterType[GameEditor.instance.getResourceLoader().getAllWaterTypes().size()]));
		waterTypeS.setSelectedValue(map.getMapSettings().getWaterType(), false);
		waterTypeS.addListSelectionListener(this);
		waterTypeS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		waterTypeS.setSize(new Dimension(200, 100));
		JScrollPane waterTypeSScroller = new JScrollPane(waterTypeS);
		waterTypeSScroller.setViewportView(waterTypeS);
		
		labels.add(tilingFactor);
		spinners.add(tilingFactorS);
		labels.add(fogDensity);
		spinners.add(fogDensityS);
		labels.add(fogGradient);
		spinners.add(fogGradientS);
		labels.add(rSkyColor);
		spinners.add(rSkyColorS);
		labels.add(gSkyColor);
		spinners.add(gSkyColorS);
		labels.add(bSkyColor);
		spinners.add(bSkyColorS);
		labels.add(minDiffuseLighting);
		spinners.add(minDiffuseLightingS);
		labels.add(minSpecularLighting);
		spinners.add(minSpecularLightingS);
		labels.add(waterWaveMoveSpeed);
		spinners.add(waterWaveMoveSpeedS);
		labels.add(waterWaveStrength);
		spinners.add(waterWaveStrengthS);
		labels.add(waterReflectivityFactor);
		spinners.add(waterReflectivityFactorS);
		labels.add(waterTilingFactor);
		spinners.add(waterTilingFactorS);
		labels.add(waterShineDamper);
		spinners.add(waterShineDamperS);
		labels.add(waterReflectivity);
		spinners.add(waterReflectivityS);
		labels.add(skyBox);
		spinners.add(skyboxSScroller);
		labels.add(waterType);
		spinners.add(waterTypeSScroller);
		
		for(JComponent s : spinners) {
			if(s instanceof JSpinner)
				((JSpinner)s).addChangeListener(this);
			else if(s instanceof JList)
				((JList)s).addListSelectionListener(this);
			s.addKeyListener(this);
		}
		
		Utils.createForm(this, labels.toArray(new Component[labels.size()]), spinners.toArray(new Component[labels.size()]), 50, 0, 50, 5);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) 
			stateChanged(new ChangeEvent(e.getSource()));
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getSource().equals(skyBoxS)) {
			map.getMapSettings().setSkybox(skyBoxS.getSelectedValue());
		}else if(e.getSource().equals(waterTypeS)) {
			map.getMapSettings().setWaterType(waterTypeS.getSelectedValue());
		}
		
		GameEditor.instance.getMasterRenderer().reLoadSettings();
		
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getSource().equals(tilingFactorS)) {
			map.mapSettings.setTilingFactor((int)tilingFactorS.getValue());
		}else if(e.getSource().equals(fogDensityS)) {
			map.mapSettings.setFogDensity((float)fogDensityS.getValue());
		}else if(e.getSource().equals(fogGradientS)) {
			map.mapSettings.setFogGradient((float)fogGradientS.getValue());
		}else if(e.getSource().equals(rSkyColorS) || e.getSource().equals(gSkyColorS) || e.getSource().equals(bSkyColorS)) {
			map.mapSettings.setSkyColor(new Vector3f((float) rSkyColorS.getValue(), (float) gSkyColorS.getValue(), (float) bSkyColorS.getValue()));
		}else if(e.getSource().equals(minDiffuseLightingS)) {
			map.mapSettings.setMinDiffuseLighting((float) minDiffuseLightingS.getValue());
		}else if(e.getSource().equals(minSpecularLightingS)) {
			map.mapSettings.setMinSpecularLighting((float) minSpecularLightingS.getValue());
		}else if(e.getSource().equals(waterWaveMoveSpeedS)) {
			map.mapSettings.setWaterWaveMoveSpeed((float) waterWaveMoveSpeedS.getValue());
		}else if(e.getSource().equals(waterWaveStrengthS)) {
			map.mapSettings.setWaterWaveStrength((float) waterWaveStrengthS.getValue());
		}else if(e.getSource().equals(waterReflectivityFactorS)) {
			map.mapSettings.setWaterReflectivityFactor((float) waterReflectivityFactorS.getValue());
		}else if(e.getSource().equals(waterTilingFactorS)) {
			map.mapSettings.setWaterTilingFactor((float) waterTilingFactorS.getValue());
		}else if(e.getSource().equals(waterShineDamperS)) {
			map.mapSettings.setWaterShineDamper((float) waterShineDamperS.getValue());
		}else if(e.getSource().equals(waterReflectivityS)) {
			map.mapSettings.setWaterReflectivity((float) waterReflectivityS.getValue());
		}
		
		GameEditor.instance.getMasterRenderer().reLoadSettings();

		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
