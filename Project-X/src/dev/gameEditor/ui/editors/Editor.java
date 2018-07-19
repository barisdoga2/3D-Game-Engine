package dev.gameEditor.ui.editors;

import java.awt.Color;

import javax.swing.JPanel;

public class Editor extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tabName;
	
	public Editor(String tabName) {
		this.tabName = tabName;
		this.setBackground(new Color(81, 81, 81));
	}
	
	public String getTabName() {
		return tabName;
	}
	
	public void onLoop() {
		
	}
	
}
