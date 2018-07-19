package dev.gameEditor.ui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dev.engine.loaders.ImageLoader;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.textures.BaseTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.structuresEditor.BaseTextureEditor;

public class NewBaseTextureWindow extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BaseTextureEditor bte;
	private JPanel panel = new JPanel();
	private JLabel selectLabel = new JLabel("Please select a png file.");
	private JFileChooser selecter = new JFileChooser();

	public NewBaseTextureWindow(BaseTextureEditor bte) {
		this.bte = bte;
		
		selecter.addActionListener(this);

		panel.add(selectLabel);
		panel.add(selecter);
		
		this.add(panel);
		this.setSize(500, 500);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		
		if(e.getSource().equals(selecter)) {
			if(e.getActionCommand().equals("ApproveSelection")) {
				if(selecter.getSelectedFile().getName().contains(".png")) {
					
					String fileName = selecter.getSelectedFile().getName().replace(".png", "");
					BaseTexture bt = GameEditor.instance.getResourceLoader().getBaseTextureByName(fileName);
					if(bt == null) {
						try {
							BufferedImage bi = ImageIO.read(selecter.getSelectedFile());
							File saveFile = new File(GameEditor.instance.getResourceLoader().getResourceFolder().getPath() + "/baseTextures/" + fileName + ".png");
							saveFile.createNewFile();
							ImageIO.write(bi, "jpg", saveFile);
							GameEditor.instance.getResourceLoader().addBaseTexture(new BaseTexture(fileName, "baseTextures/" + fileName, ImageLoader.loadTexture("baseTextures/" + fileName)));
							JOptionPane.showMessageDialog(null, "Selected file successfully added as BaseTexture");
							dispose();
							bte.refreshList();
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "An error occured while reading/writing the selected file!");
						}
					}else {
						JOptionPane.showMessageDialog(null, "A file has same name, please change the name!");
					}
				}else {
					JOptionPane.showMessageDialog(null, "You only can select .png files!");
				}
			}else {
				this.dispose();
			}
		}
	}
	
}
