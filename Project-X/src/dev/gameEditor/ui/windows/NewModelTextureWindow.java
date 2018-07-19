package dev.gameEditor.ui.windows;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import dev.engine.textures.BaseTexture;
import dev.engine.textures.ModelTexture;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.structuresEditor.ModelTextureEditor;

public class NewModelTextureWindow extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panel = new JPanel();
	
	private JLabel nameLabel = new JLabel("Please specify name of the ModelTexture");
	private JTextField nameArea = new JTextField(20);
	private JLabel baseTextureLabel = new JLabel("Please select a BaseTexture");
	private JList<BaseTexture> baseTextureSelecter = new JList<BaseTexture>();
	private JButton createButton = new JButton("Create");
	
	private ModelTextureEditor modelTextureEditor;
	
	public NewModelTextureWindow(ModelTextureEditor modelTextureEditor) {
		this.modelTextureEditor = modelTextureEditor;
		
		baseTextureSelecter.setListData(GameEditor.instance.getResourceLoader().getAllBaseTextures().toArray(new BaseTexture[GameEditor.instance.getResourceLoader().getAllBaseTextures().size()]));
		baseTextureSelecter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane baseTextureScroller = new JScrollPane(baseTextureSelecter);
		baseTextureScroller.setViewportView(baseTextureSelecter);
		
		createButton.setPreferredSize(new Dimension(200, 20));
		createButton.addActionListener(this);
		
		panel.add(nameLabel);
		panel.add(nameArea);
		panel.add(baseTextureLabel);
		panel.add(baseTextureScroller);
		panel.add(createButton);
		
		this.add(panel);
		this.setSize(300, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(createButton)) {
			if(baseTextureSelecter.isSelectionEmpty() || nameArea.getText().length() < 3){
				JOptionPane.showMessageDialog(null, "The minimum length of name is 3, and you have to select a Base Texture.");
				return;
			}
			
			if(null != GameEditor.instance.getResourceLoader().getBaseTextureByName(nameArea.getText())) {
				JOptionPane.showMessageDialog(null, "The specified name already exists!");
				return;
			}
			
			GameEditor.instance.getResourceLoader().addModelTexture(new ModelTexture(nameArea.getText(), baseTextureSelecter.getSelectedValue()));
			JOptionPane.showMessageDialog(null, "Successfully Created!");
			dispose();
			modelTextureEditor.refreshList();
		}
	}

}
