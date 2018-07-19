package dev.gameEditor.ui;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;

import dev.gameEditor.GameEditor;

public class LeftPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	
	public double scale = 1;

	public LeftPanel() {
		this.setBackground(new Color(61, 61, 61));
		
		canvas = new Canvas();
		canvas.setFocusable(false);
		canvas.setSize((int)(GameEditor.instance.getScreenWidth() / 1.4), GameEditor.instance.getScreenHeight());
		add(canvas);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
}
