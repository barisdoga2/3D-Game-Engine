package dev.gameEditor.ui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dev.engine.renderEngine.DisplayManager;
import dev.engine.waters.WaterType;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.structuresEditor.WaterTypeEditor;

public class NewWaterTypeWindow extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panel = new JPanel();
	private JLabel selectLabel = new JLabel("Please select a directory that contains following files; 'dudv.png', 'normal.png'.");
	private JFileChooser selecter = new JFileChooser();
	
	private WaterTypeEditor waterTypeEditor;
	
	public NewWaterTypeWindow(WaterTypeEditor waterTypeEditor) {
		this.waterTypeEditor = waterTypeEditor;
		
		selecter.setMultiSelectionEnabled(false);
		selecter.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		selecter.addActionListener(this);
		
		panel.add(selectLabel);
		panel.add(selecter);
		
		this.add(panel);
		this.setSize(700, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getActionCommand().equals("ApproveSelection")) {
			File selectedDir = selecter.getSelectedFile();
			File dudvFile = new File(selectedDir.getPath() + "/dudv.png");
			File normalFile = new File(selectedDir.getPath() + "/normal.png");
			if(dudvFile.exists() && normalFile.exists()) {
				for(WaterType s : GameEditor.instance.getResourceLoader().getAllWaterTypes()) {
					if(s.getFolderPath().equals("waters/" + selectedDir.getName())) {
						JOptionPane.showMessageDialog(null, "The foldername already exists in resources!");
						return;
					}
				}

				new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/waters/" + selectedDir.getName()).mkdir();
				
				try {
					ImageIO.write(ImageIO.read(dudvFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/waters/" + selectedDir.getName() + "/" + dudvFile.getName()));
					ImageIO.write(ImageIO.read(normalFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/waters/" + selectedDir.getName() + "/" + normalFile.getName()));
					GameEditor.instance.getResourceLoader().addWaterType(new WaterType(selectedDir.getName(), "waters/" + selectedDir.getName()));
					JOptionPane.showMessageDialog(null, "Successfully Created!");
					dispose();
					waterTypeEditor.refreshList();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}else {
				JOptionPane.showMessageDialog(null, "The directory doesn't contain all files!");
			}
		}else if(e.getActionCommand().equals("CancelSelection")) {
			dispose();
		}
	}

}
