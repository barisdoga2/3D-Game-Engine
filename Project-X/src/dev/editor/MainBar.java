package dev.editor;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainBar extends JMenuBar{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainBar(EditorRun editor) {
		super();
		
		JMenuItem testItem = new JMenuItem("Test Item");
		this.add(testItem);
	}
	
}
