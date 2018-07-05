package dev.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import dev.engine.renderEngine.DisplayManager;

public class MapEditor implements Runnable{
	
	private int screenWidth;
	private int screenHeight;
	
	private MainBar menuBar;
	public LeftPanel leftPanel;
	public RightPanel rightPanel;
	
	private JFrame frame;
	
	public MapEditor() {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = ((int) screenSize.getWidth());
		screenHeight = ((int) screenSize.getHeight());
		
		frame = new JFrame("Project-H Editor!");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenWidth, screenHeight);
		frame.setResizable(false);
		
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		menuBar = new MainBar(this);
		leftPanel = new LeftPanel(this);
		rightPanel = new RightPanel(this);
		
		frame.setJMenuBar(menuBar);
		frame.add(leftPanel, BorderLayout.WEST);
		frame.add(rightPanel, BorderLayout.CENTER);
		
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
		new Thread(this).run();
	}
	
	@Override
	public void run() {
		DisplayManager.CreateDisplay();
		try {
			Display.setParent(leftPanel.getCanvas());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		while(!DisplayManager.IsDestroyRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL11.glClearColor(0, 0, 1, 1);
			
			
			DisplayManager.UpdateDisplay();
		}
		
		DisplayManager.DestroyDisplay();
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
}
