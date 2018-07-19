package dev.gameEditor.ui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.gameEditor.GameEditor;

public class NewMapWindow extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel panel = new JPanel();
	private JLabel enterNameLabel = new JLabel("Please enter name of new map.");
	private JTextField nameTextArea = new JTextField(15);
	private JButton createButton = new JButton("Create!");
	
	public NewMapWindow() {
		createButton.addActionListener(this);
		
		panel.add(enterNameLabel);
		panel.add(nameTextArea);
		panel.add(createButton);
		
		this.add(panel);
		this.setSize(200, 110);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource().equals(createButton)) {
			if(nameTextArea.getText().endsWith("Map")) {
				GameEditor.instance.addMap(nameTextArea.getText());
				JOptionPane.showMessageDialog(null, "New Map Successfully Added!");
				dispose();
			}else {
				JOptionPane.showMessageDialog(null, "Mapname must be end with 'Map'!");
			}
		}
	}
	
}
