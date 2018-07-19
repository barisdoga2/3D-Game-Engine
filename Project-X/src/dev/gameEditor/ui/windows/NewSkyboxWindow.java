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
import dev.engine.skybox.Skybox;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.structuresEditor.SkyboxEditor;

public class NewSkyboxWindow extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panel = new JPanel();
	private JLabel selectLabel = new JLabel("Please select a directory that contains following files; 'bottom.png', 'top.png', 'back.png', 'front.png', 'right.png', 'left.png'.");
	private JFileChooser selecter = new JFileChooser();
	
	private SkyboxEditor skyboxEditor;
	
	public NewSkyboxWindow(SkyboxEditor skyboxEditor) {
		this.skyboxEditor = skyboxEditor;
		
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
			File rightFile = new File(selectedDir.getPath() + "/right.png");
			File leftFile = new File(selectedDir.getPath() + "/left.png");
			File backFile = new File(selectedDir.getPath() + "/back.png");
			File frontFile = new File(selectedDir.getPath() + "/front.png");
			File topFile = new File(selectedDir.getPath() + "/top.png");
			File bottomFile = new File(selectedDir.getPath() + "/bottom.png");
			if(rightFile.exists() && leftFile.exists() && backFile.exists() && frontFile.exists() && topFile.exists() && bottomFile.exists()) {
				for(Skybox s : GameEditor.instance.getResourceLoader().getAllSkyboxes()) {
					if(s.getFolderPath().equals("skyboxes/" + selectedDir.getName())) {
						JOptionPane.showMessageDialog(null, "The foldername already exists in resources!");
						return;
					}
				}

				new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName()).mkdir();
				
				try {
					ImageIO.write(ImageIO.read(rightFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName() + "/" + rightFile.getName()));
					ImageIO.write(ImageIO.read(leftFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName() + "/" + leftFile.getName()));
					ImageIO.write(ImageIO.read(backFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName() + "/" + backFile.getName()));
					ImageIO.write(ImageIO.read(frontFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName() + "/" + frontFile.getName()));
					ImageIO.write(ImageIO.read(topFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName() + "/" + topFile.getName()));
					ImageIO.write(ImageIO.read(bottomFile), "png", new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/skyboxes/" + selectedDir.getName() + "/" + bottomFile.getName()));
					GameEditor.instance.getResourceLoader().addSkybox(new Skybox(selectedDir.getName(), "skyboxes/" + selectedDir.getName()));
					JOptionPane.showMessageDialog(null, "Successfully Created!");
					dispose();
					skyboxEditor.refreshList();
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
