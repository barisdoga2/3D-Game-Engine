package dev.editor;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;

public class LeftPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	
	public double scale = 1;

	public LeftPanel(MapEditor editor) {
		this.setBackground(new Color(61, 61, 61));
		
		canvas = new Canvas();
		canvas.setFocusable(false);
		canvas.setSize((int)(editor.getScreenWidth() / 1.4), editor.getScreenHeight());
		add(canvas);
		
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
}
