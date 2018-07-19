package dev.gameEditor.ui.editors.mapEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import dev.engine.entities.Entity;
import dev.engine.models.TexturedModel;
import dev.engine.terrains.Terrain;
import dev.engine.utils.MousePicker;
import dev.gameEditor.GameEditor;
import dev.gameEditor.ui.editors.Editor;
import dev.gameEditor.ui.utils.Utils;

public class ObjectEditor extends Editor implements ListSelectionListener, ActionListener, ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Entity selectedEntity;
	
	private JLabel selectTexturedModel = new JLabel("Select a Textured Model to Add:");
	private JList<TexturedModel> selectTexturedModelS = new JList<TexturedModel>();
	private JScrollPane selectTexturedModelSScroller;
	
	private JLabel selectEntity = new JLabel("Select a Entity to Edit:");
	private JList<Entity> selectEntityS = new JList<Entity>();
	private JScrollPane selectEntitySScroller;
	
	public JButton deselect = new JButton("                  Deselect                  ");
	public JButton remove = new JButton("Remove Selected");
	
	private JLabel rotationX = new JLabel("Rotation X: ");
	private JLabel rotationY = new JLabel("Rotation X: ");
	private JLabel rotationZ = new JLabel("Rotation X: ");
	private JSpinner rotationXS = new JSpinner(new SpinnerNumberModel(0.0f, 0f, 360f, 5f));
	private JSpinner rotationYS = new JSpinner(new SpinnerNumberModel(0.0f, 0f, 360f, 5f));
	private JSpinner rotationZS = new JSpinner(new SpinnerNumberModel(0.0f, 0f, 360f, 5f));
	
	private JLabel scaleX = new JLabel("Scale X: ");
	private JLabel scaleY = new JLabel("Scale Y: ");
	private JLabel scaleZ = new JLabel("Scale Z: ");
	private JSpinner scaleXS = new JSpinner(new SpinnerNumberModel(0.0f, 0f, 100f, 1f));
	private JSpinner scaleYS = new JSpinner(new SpinnerNumberModel(0.0f, 0f, 100f, 1f));
	private JSpinner scaleZS = new JSpinner(new SpinnerNumberModel(0.0f, 0f, 100f, 1f));
	
	private JLabel positionX = new JLabel("Position X: ");
	private JLabel positionY = new JLabel("Position Y: ");
	private JLabel positionZ = new JLabel("Position Z: ");
	private JSpinner positionXS = new JSpinner(new SpinnerNumberModel(0.0f, null, null, 1f));
	private JSpinner positionYS = new JSpinner(new SpinnerNumberModel(0.0f, null, null, 1f)); 
	private JSpinner positionZS = new JSpinner(new SpinnerNumberModel(0.0f, null, null, 1f));
	
	public ObjectEditor() {
		super("Object Editor");
		
		ArrayList<JComponent> labels = new ArrayList<JComponent>();
		ArrayList<JComponent> tools = new ArrayList<JComponent>();
		
		selectTexturedModelS.setListData(GameEditor.instance.getResourceLoader().getAllTexturedModels().toArray(new TexturedModel[GameEditor.instance.getResourceLoader().getAllTexturedModels().size()]));
		selectTexturedModelS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectTexturedModelS.setSize(new Dimension(200, 100));
		selectTexturedModelS.addListSelectionListener(this);
		selectTexturedModelSScroller = new JScrollPane(selectTexturedModelS);
		selectTexturedModelSScroller.setViewportView(selectTexturedModelS);
		selectTexturedModelS.addListSelectionListener(this);
		
		selectEntityS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectEntityS.setSize(new Dimension(200, 100));
		selectEntityS.addListSelectionListener(this);
		selectEntitySScroller = new JScrollPane(selectEntityS);
		selectEntitySScroller.setViewportView(selectEntityS);
		selectEntityS.addListSelectionListener(this);
		
		deselect.addActionListener(this);
		remove.addActionListener(this);
		
		labels.add(selectTexturedModel);
		tools.add(selectTexturedModelSScroller);
		labels.add(selectEntity);
		tools.add(selectEntitySScroller);
		labels.add(remove);
		tools.add(deselect);
		
		labels.add(positionX);
		tools.add(positionXS);
		labels.add(positionY);
		tools.add(positionYS);
		labels.add(positionZ);
		tools.add(positionZS);
		labels.add(scaleX);
		tools.add(scaleXS);
		labels.add(scaleY);
		tools.add(scaleYS);
		labels.add(scaleZ);
		tools.add(scaleZS);
		labels.add(rotationX);
		tools.add(rotationXS);
		labels.add(rotationY);
		tools.add(rotationYS);
		labels.add(rotationZ);
		tools.add(rotationZS);
		
		positionX.setVisible(false);
		positionXS.setVisible(false);
		positionXS.addChangeListener(this);
		positionY.setVisible(false);
		positionYS.setVisible(false);
		positionYS.addChangeListener(this);
		positionZ.setVisible(false);
		positionZS.setVisible(false);
		positionZS.addChangeListener(this);
		scaleX.setVisible(false);
		scaleXS.setVisible(false);
		scaleXS.addChangeListener(this);
		scaleY.setVisible(false);
		scaleYS.setVisible(false);
		scaleYS.addChangeListener(this);
		scaleZ.setVisible(false);
		scaleZS.setVisible(false);
		scaleZS.addChangeListener(this);
		rotationX.setVisible(false);
		rotationXS.setVisible(false);
		rotationXS.addChangeListener(this);
		rotationY.setVisible(false);
		rotationYS.setVisible(false);
		rotationYS.addChangeListener(this);
		rotationZ.setVisible(false);
		rotationZS.setVisible(false);
		rotationZS.addChangeListener(this);
		
		Utils.createForm(this, labels.toArray(new JComponent[labels.size()]), tools.toArray(new JComponent[tools.size()]), 50, 0, 50, 10);
		
	}
	
	public void lateInit() {
		selectEntityS.setListData(GameEditor.instance.getCurrentMap().getAllEntities().toArray(new Entity[GameEditor.instance.getCurrentMap().getAllEntities().size()]));
		
		this.revalidate();
		this.repaint();
	}
	
	boolean isAdding = false;
	boolean action = false;
	boolean mouseTrigger = false;
	@Override
	public void onLoop() {
		MousePicker mp = GameEditor.instance.getMousePicker();
		Terrain terrain = mp.getMouseOnTerrain();
		Vector3f mousePosition = mp.getCurrentTerrainPoint();
		
		if(terrain != null && mousePosition != null && selectedEntity != null && !isAdding) {
			selectedEntity.setPosition(mousePosition);
			GameEditor.instance.getMasterRenderer().processEntity(selectedEntity);
		}
		
		if(Mouse.isButtonDown(0) && !mouseTrigger) {
			mouseTrigger = true;
		}else if(!Mouse.isButtonDown(0) && mouseTrigger){
			mouseTrigger = false;
			action = true;
		}
		
		if(action && !isAdding) {
			JFrame f = new JFrame();
			JPanel p = new JPanel();
			JLabel tfl = new JLabel("Please specify a name");
			JTextField tf = new JTextField(10);
			JButton addButton = new JButton("Add");
			p.add(tfl);
			p.add(tf);
			p.add(addButton);
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource().equals(addButton)) {
						for(Entity ee : GameEditor.instance.getCurrentMap().getAllEntities()) {
							if(ee.getName().equals(tf.getText())) {
								JOptionPane.showMessageDialog(null, "The specified name already exists!");
								return;
							}
						}
						selectedEntity.setName(tf.getText());
						GameEditor.instance.getCurrentMap().addEntity(selectedEntity);
						selectedEntity = null;
						isAdding = false;
						f.dispose();
						lateInit();
					}
				}
			});
			f.addWindowListener(new WindowListener() {
				public void windowOpened(WindowEvent e) {}
				public void windowIconified(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {}
				public void windowDeactivated(WindowEvent e) {}
				public void windowClosing(WindowEvent e) {
					isAdding = false;
					lateInit();
				}
				public void windowClosed(WindowEvent e) {}
				public void windowActivated(WindowEvent e) {}
			});
			f.add(p);
			f.setLocationRelativeTo(null);
			f.setSize(200, 100);
			f.setVisible(true);
			action = false;
			isAdding = true;
		}
	}
	
	private void clearSelectEntitySSelection() {
		selectEntityS.clearSelection();
		positionX.setVisible(false);
		positionXS.setVisible(false);
		positionY.setVisible(false);
		positionYS.setVisible(false);
		positionZ.setVisible(false);
		positionZS.setVisible(false);
		scaleX.setVisible(false);
		scaleXS.setVisible(false);
		scaleY.setVisible(false);
		scaleYS.setVisible(false);
		scaleZ.setVisible(false);
		scaleZS.setVisible(false);
		rotationX.setVisible(false);
		rotationXS.setVisible(false);
		rotationY.setVisible(false);
		rotationYS.setVisible(false);
		rotationZ.setVisible(false);
		rotationZS.setVisible(false);
	}
	
	boolean blockChangeListener = false;
	private void activateSelectEntitySSelection() {
		Entity selected = selectEntityS.getSelectedValue();
		blockChangeListener = true;
		positionX.setVisible(true);
		positionXS.setVisible(true);
		positionXS.setValue(selected.getPosition().x);
		positionY.setVisible(true);
		positionYS.setVisible(true);
		positionYS.setValue(selected.getPosition().y);
		positionZ.setVisible(true);
		positionZS.setVisible(true);
		positionZS.setValue(selected.getPosition().z);
		scaleX.setVisible(true);
		scaleXS.setVisible(true);
		scaleXS.setValue(selected.getScale().x);
		scaleY.setVisible(true);
		scaleYS.setVisible(true);
		scaleYS.setValue(selected.getScale().y);
		scaleZ.setVisible(true);
		scaleZS.setVisible(true);
		scaleZS.setValue(selected.getScale().z);
		rotationX.setVisible(true);
		rotationXS.setVisible(true);
		rotationXS.setValue(selected.getRotation().x);
		rotationY.setVisible(true);
		rotationYS.setVisible(true);
		rotationYS.setValue(selected.getRotation().y);
		rotationZ.setVisible(true);
		rotationZS.setVisible(true);
		rotationZS.setValue(selected.getRotation().z);
		blockChangeListener = false;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(e.getSource().equals(selectTexturedModelS)) {
			if(!isAdding) {
				if(selectedEntity == null) {
					selectedEntity = new Entity("..??..XX..??..", selectTexturedModelS.getSelectedValue(), new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1));
				}else {
					selectedEntity.setModel(selectTexturedModelS.getSelectedValue());
				}
			}
		}else if(e.getSource().equals(selectEntityS) && selectEntityS.getSelectedValue() != null) {
			activateSelectEntitySSelection();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(deselect)) {
			clearSelectEntitySSelection();
		}else if(e.getSource().equals(remove) && selectEntityS.getSelectedValue() != null) {
			GameEditor.instance.getCurrentMap().removeEntity(selectEntityS.getSelectedValue());
			lateInit();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(blockChangeListener)
			return;
		
		Entity selected = selectEntityS.getSelectedValue();
		
		selected.setPosition(new Vector3f(getValue(positionXS), getValue(positionYS), getValue(positionZS)));
		selected.setScale(new Vector3f(getValue(scaleXS), getValue(scaleYS), getValue(scaleZS)));
		selected.setRotation(new Vector3f(getValue(rotationXS), getValue(rotationYS), getValue(rotationZS)));
	}
	
	private float getValue(JSpinner spinner) {
		SpinnerNumberModel model = (SpinnerNumberModel)spinner.getModel();
		return model.getNumber().floatValue();
	}
	
}
