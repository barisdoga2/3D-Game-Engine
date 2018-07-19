package dev.gameEditor.ui.windows;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import dev.engine.loaders.objLoader.OBJLoader;
import dev.engine.models.RawModel;
import dev.engine.models.TexturedModel;
import dev.engine.renderEngine.DisplayManager;
import dev.engine.textures.ModelTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.structuresEditor.TexturedModelEditor;

public class NewTexturedModelWindow extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private JPanel panel = new JPanel();
	
	private JLabel nameLabel = new JLabel("            Please specify name of the TextureModel            ");
	private JTextField nameArea = new JTextField(20);
	private JLabel modelTextureLabel = new JLabel("                                                Please select a ModelTexture                                                ");
	private JList<ModelTexture> modelTextureSelecter = new JList<ModelTexture>();
	private JLabel rawModelLabel = new JLabel("                          Please select a RawModel                          ");
	private JList<RawModel> rawModelSelecter = new JList<RawModel>();
	
	private JFrame rawModel2;
	private JFileChooser rawModelChooser = new JFileChooser();
	private JButton rawModelLabel2 = new JButton("                          or add new                          ");

	private JButton createButton = new JButton("Create");
	
	private TexturedModelEditor texturedModelEditor;
	
	public NewTexturedModelWindow(TexturedModelEditor texturedModelEditor) {
		this.texturedModelEditor = texturedModelEditor;
		
		modelTextureSelecter.setListData(GameEditor.instance.getResourceLoader().getAllModelTextures().toArray(new ModelTexture[GameEditor.instance.getResourceLoader().getAllModelTextures().size()]));
		modelTextureSelecter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane modelTextureScroller = new JScrollPane(modelTextureSelecter);
		modelTextureScroller.setViewportView(modelTextureSelecter);
				
		rawModelSelecter.setListData(GameEditor.instance.getResourceLoader().getAllRawModels().toArray(new RawModel[GameEditor.instance.getResourceLoader().getAllRawModels().size()]));
		rawModelSelecter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rawModelScroller = new JScrollPane(rawModelSelecter);
		rawModelScroller.setViewportView(rawModelSelecter);
		
		createButton.setPreferredSize(new Dimension(300, 20));
		createButton.addActionListener(this);
		
		rawModelChooser.addActionListener(this);
		rawModelLabel2.addActionListener(this);
		
		panel.add(nameLabel);
		panel.add(nameArea);
		panel.add(rawModelLabel);
		panel.add(rawModelScroller);
		panel.add(rawModelLabel2);
		panel.add(modelTextureLabel);
		panel.add(modelTextureScroller);
		panel.add(createButton);
		
		this.add(panel);
		this.setSize(400, 550);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DisplayManager.MakeCurrentDrawableThread();
		if(e.getSource().equals(createButton)) {
			if(modelTextureSelecter.isSelectionEmpty() || nameArea.getText().length() < 3 || (rawModelSelecter.isSelectionEmpty() && rawModelChooser.getSelectedFile() == null)){
				JOptionPane.showMessageDialog(null, "The minimum length of name is 3, and you have to select a Model Texture and Raw Model.");
				return;
			}
			
			if(null != GameEditor.instance.getResourceLoader().getTexturedModelByName(nameArea.getText())) {
				JOptionPane.showMessageDialog(null, "The specified name already exists!");
				return;
			}
			
			if(!rawModelSelecter.isSelectionEmpty() && rawModelChooser.getSelectedFile() != null) {
				JOptionPane.showMessageDialog(null, "You have to select one Raw Model (You can select a file or select a existing from the list), Please close add Textured Model window and reopen!!");
				return;
			}else if(!rawModelSelecter.isSelectionEmpty()) {
				GameEditor.instance.getResourceLoader().addTexturedModel(new TexturedModel(nameArea.getText(), rawModelSelecter.getSelectedValue(), modelTextureSelecter.getSelectedValue(), null, null));
				JOptionPane.showMessageDialog(null, "Successfully Created!");
				dispose();
				texturedModelEditor.refreshList();
			}else if(rawModelChooser.getSelectedFile() != null) {
				GameEditor.instance.getResourceLoader().addTexturedModel(new TexturedModel(nameArea.getText(), OBJLoader.LoadObjModel("models/objs/" + rawModelChooser.getSelectedFile().getName().replaceAll(".obj", "")), modelTextureSelecter.getSelectedValue(), null, null));
				JOptionPane.showMessageDialog(null, "Successfully Created!");
				dispose();
				texturedModelEditor.refreshList();
			}
		}else if(e.getSource().equals(rawModelLabel2)) {
			rawModel2 = new JFrame();
			rawModel2.add(rawModelChooser);
			rawModel2.setSize(400, 550);
			rawModel2.setResizable(false);
			rawModel2.setLocationRelativeTo(null);
			rawModel2.setVisible(true);
		}else if(e.getActionCommand().equals("ApproveSelection")) {
			if(!rawModelChooser.getSelectedFile().getName().contains(".obj")) {
				JOptionPane.showMessageDialog(null, "File format must be 'obj'!");
			}else {
				boolean found = false;
				for(RawModel r : GameEditor.instance.getResourceLoader().getAllRawModels()) {
					if(r.getName() != null && r.getName().contains(rawModelChooser.getSelectedFile().getName().replaceAll(".obj", "")))
						found = true;
				}
				File fileToWrite = null;
				if(found) {
					int respond = JOptionPane.showConfirmDialog(rawModel2, "This file already exists, do you want to replace ?");
					if(respond == 1) // No
						return;
				}
				
				fileToWrite = new File(GameEditor.instance.getResourceLoader().getResourceFolder().getAbsolutePath() + "/models/objs/" + rawModelChooser.getSelectedFile().getName());
				try {
					fileToWrite.createNewFile();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Coundn't create new file!");
					e1.printStackTrace();
					return;
				}
				
				try {
					BufferedReader br = new BufferedReader(new FileReader(rawModelChooser.getSelectedFile()));
					BufferedWriter bw = new BufferedWriter(new FileWriter(fileToWrite));
					String line;
					while((line = br.readLine()) != null) 
						bw.write(line + "\n");
					bw.close();
					br.close();
				}catch (Exception ee) {
					JOptionPane.showMessageDialog(null, "Coundn't write to file!");
					return;
				}
				JOptionPane.showMessageDialog(null, "Successfully added. Please continue without selecting any!");
				rawModel2.dispose();
			}
			
		}else if(e.getActionCommand().equals("CancelSelection")) {
			rawModelChooser.setSelectedFile(null);
			rawModel2.dispose();
		}
		
	}

}
