package dev.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.lwjgl.opengl.GL11;

import dev.engine.renderEngine.DisplayManager;

public class EditorRun {
	
	private int screenWidth;
	private int screenHeight;
	
	private MainBar menuBar;
	public LeftPanel leftPanel;
	public RightPanel rightPanel;
	
	private JFrame frame;
	
	public EditorRun() {
		
		DisplayManager.CreateDisplay();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = ((int) screenSize.getWidth());
		screenHeight = ((int) screenSize.getHeight());
		
		frame = new JFrame("Project-H Editor!");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		while(!DisplayManager.IsDestroyRequested()) {
			GL11.glClear(GL11.GL_COLOR);
			DisplayManager.UpdateDisplay();
		}
		
		DisplayManager.DestroyDisplay();
	}
	
	public static void main(String[] args) {
		new EditorRun();
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
}
