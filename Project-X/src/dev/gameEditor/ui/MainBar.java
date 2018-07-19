package dev.gameEditor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import dev.engine.loaders.mapLoader.Map;
import dev.engine.renderEngine.DisplayManager;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.windows.NewMapWindow;

public class MainBar extends JMenuBar implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem saveChanges = new JMenuItem("Save Changes");
	private JMenuItem exitWithoutSave = new JMenuItem("Exit without Save");
	
	private JMenu mapMenu = new JMenu("Map");
	private JMenuItem newMap = new JMenuItem("New Map");

	public MainBar() {
		super();
	}
	
	public void lateInit() {		
		saveChanges.addActionListener(this);
		exitWithoutSave.addActionListener(this);
		fileMenu.add(saveChanges);
		fileMenu.add(exitWithoutSave);
		
		newMap.addActionListener(this);
		mapMenu.add(newMap);
		mapMenu.addSeparator();
		for(Map map : GameEditor.instance.getResourceLoader().getAllMaps()) {
			JMenuItem mapMenuItem = new JMenuItem(map.getName());
			mapMenuItem.addActionListener(this);
			mapMenuItem.setActionCommand("CHANGEMAP " + map.getName());
			mapMenu.add(mapMenuItem);
		}
		
		this.add(fileMenu);
		this.add(mapMenu);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		DisplayManager.MakeCurrentDrawableThread();
		if(a.getSource().equals(saveChanges)) {
			int retVal = JOptionPane.showConfirmDialog(null, "The changes cannot be undone after saving. Are you sure to save ?");
			if(retVal == JOptionPane.OK_OPTION) {
				if(GameEditor.showMessages)
					JOptionPane.showMessageDialog(null, "Saving the changes can take a while. Please be patient!");
				GameEditor.instance.saveChanges();
				if(GameEditor.showMessages)
					JOptionPane.showMessageDialog(null, "The changes has been successfully saved!");
			}
		}else if(a.getSource().equals(exitWithoutSave)) {
			int retVal = JOptionPane.showConfirmDialog(null, "Are you sure to exit without saving ?");
			if(retVal == JOptionPane.OK_OPTION) {
				GameEditor.instance.exitWithoutSave();
			}
		}else if(a.getSource().equals(newMap)) {
			new NewMapWindow();
		}
		
		if(a.getActionCommand() != null) {
			if(a.getActionCommand().contains("CHANGEMAP"))
				GameEditor.instance.changeMap(a.getActionCommand().split(" ")[1]);
		}
	}

	public void addMap(String mapName) {
		JMenuItem mapMenuItem = new JMenuItem(mapName);
		mapMenuItem.addActionListener(this);
		mapMenuItem.setActionCommand("CHANGEMAP " + mapName);
		mapMenu.add(mapMenuItem);
		
		this.revalidate();
		this.repaint();
	}
	
}
