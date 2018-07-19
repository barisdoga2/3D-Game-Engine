package dev.gameEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Camera;
import dev.engine.loaders.mapLoader.Map;
import dev.engine.loaders.mapLoader.ResourceLoader;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.renderEngine.MasterRenderer;
import dev.engine.utils.Maths;
import dev.engine.utils.MousePicker;
import dev.gameEditor.ui.LeftPanel;
import dev.gameEditor.ui.MainBar;
import dev.gameEditor.ui.editors.RightPanel;

public class GameEditor extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static boolean showMessages = false;
	public static GameEditor instance;
	
	private Map mapToRender;
		
	private int screenWidth;
	private int screenHeight;
	
	private MainBar menuBar;
	private LeftPanel leftPanel;
	private RightPanel rightPanel;
	
	private ResourceLoader resourceLoader;
	private Map changeMapToRender;
	private MasterRenderer masterRenderer;
	
	private MousePicker mousePicker;
			
	public GameEditor() throws Exception{
		super("Project-X MapEditor");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = ((int) screenSize.getWidth());
		screenHeight = ((int) screenSize.getHeight());
		GameEditor.instance = this;
		
		menuBar = new MainBar();
		leftPanel = new LeftPanel();
		rightPanel = new RightPanel();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(screenWidth, screenHeight);
		this.setResizable(false);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		this.setJMenuBar(menuBar);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				
		this.setVisible(true);
		
		DisplayManager.CreateDisplay();
		DisplayManager.SetParentCanvas(leftPanel.getCanvas());
		resourceLoader = new ResourceLoader(new File("../Project-X/res"));
		resourceLoader.loadAllResources();
		menuBar.lateInit();
		rightPanel.lateInit(resourceLoader.getAllMaps().get(1));
		editorGameLoop();
		DisplayManager.DestroyDisplay();
	}
	
	
	private void editorGameLoop() {
	
		mapToRender = resourceLoader.getMapByName("testMap");
		masterRenderer = new MasterRenderer(mapToRender);
		
		Camera testCamera = new Camera();
		
		mousePicker = new MousePicker(testCamera, Maths.createProjectionMatrix(), mapToRender.getAllTerrains());
		
		rightPanel.getObjectEditor().lateInit();
		
		while(!DisplayManager.IsDestroyRequested()) {
			
			rightPanel.onLoop();
			
			if(changeMapToRender != null) {
				mapToRender = changeMapToRender;
				masterRenderer = new MasterRenderer(mapToRender);
				changeMapToRender = null;
			}
			
			testCamera.update();
			mousePicker.update();
			
			Vector3f vec = mousePicker.getCurrentTerrainPoint();
			if(vec != null)
				MasterRenderer.setBrushPosition(vec);
			
			masterRenderer.processEntities(mapToRender.getAllEntities());
			masterRenderer.processTerrains(mapToRender.getAllTerrains());
			masterRenderer.processWaters(mapToRender.getAllWaters());
			
			masterRenderer.renderWaterEffects(testCamera, mapToRender.getAllLights());
			masterRenderer.renderScene(testCamera, mapToRender.getAllLights());
			masterRenderer.renderWaters(testCamera, mapToRender.getAllLights());
	
			masterRenderer.cleanRenderObjects();
			DisplayManager.UpdateDisplay();
		}
	}
	
	public void addMap(String mapName) {
		resourceLoader.addMap(mapName);
		menuBar.addMap(mapName);
	}
	
	public void saveChanges() {
		resourceLoader.saveChanges();
	}
	
	public void exitWithoutSave() {
		System.exit(1);
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
	
	public MousePicker getMousePicker() {
		return mousePicker;
	}

	public void changeMap(String actionCommand) {
		changeMapToRender = resourceLoader.getMapByName(actionCommand);
		rightPanel.lateInit(changeMapToRender);
	}

	public MasterRenderer getMasterRenderer() {
		return masterRenderer;
	}
	
	public Map getCurrentMap() {
		return mapToRender;
	}
	
}
