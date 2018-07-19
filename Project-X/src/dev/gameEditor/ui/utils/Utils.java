package dev.gameEditor.ui.utils;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class Utils {

	 public static void createForm(JPanel parent, Component[] leftComponents,Component[] rightComponents, int initialX, int initialY, int xPad, int yPad) {
		SpringLayout layout = new SpringLayout();
		int numRows = Math.max(leftComponents.length, rightComponents.length);
		
		Spring xSpring = Spring.constant(initialX);
		Spring ySpring = Spring.constant(initialY);
		Spring xPadSpring = Spring.constant(xPad);
		Spring yPadSpring = Spring.constant(yPad);
		Spring negXPadSpring = Spring.constant(-xPad);
			
		parent.setLayout(layout);
		for (int i = 0; i < numRows; i++) {
			parent.add(leftComponents[i]);
			parent.add(rightComponents[i]);
		}
			
		Spring maxEastSpring = layout.getConstraint("East", leftComponents[0]);
		for (int row = 1; row < numRows; row++) {
			maxEastSpring = Spring.max(maxEastSpring, layout.getConstraint("East",leftComponents[row]));
		}
			
		SpringLayout.Constraints lastConsL = null;
		SpringLayout.Constraints lastConsR = null;
		Spring parentWidth = layout.getConstraint("East", parent);
		Spring rWidth = null;
		Spring maxHeightSpring = null;
		Spring rX = Spring.sum(maxEastSpring, xPadSpring); //right col location
		Spring negRX = Spring.minus(rX); //negative of rX
		
		for (int row = 0; row < numRows; row++) {
			SpringLayout.Constraints consL = layout.getConstraints(leftComponents[row]);
			SpringLayout.Constraints consR = layout.getConstraints(rightComponents[row]);
			
			consL.setX(xSpring);
			consR.setX(rX);
			
			rWidth = consR.getWidth();
			consR.setWidth(Spring.sum(Spring.sum(parentWidth, negRX),negXPadSpring));
			if (row == 0) {
				consL.setY(ySpring);
				consR.setY(ySpring);
				maxHeightSpring = Spring.sum(ySpring, Spring.max(consL.getHeight(), consR.getHeight()));
			} else {
				Spring y = Spring.sum(Spring.max(lastConsL.getConstraint("South"),lastConsR.getConstraint("South")),yPadSpring);
				
				consL.setY(y);
				consR.setY(y);
				maxHeightSpring = Spring.sum(yPadSpring,Spring.sum(maxHeightSpring,Spring.max(consL.getHeight(),consR.getHeight())));
			}
			lastConsL = consL;
			lastConsR = consR;
		} 
		SpringLayout.Constraints consParent = layout.getConstraints(parent);
		consParent.setConstraint("East",Spring.sum(rX, Spring.sum(rWidth, xPadSpring)));
		consParent.setConstraint("South",Spring.sum(maxHeightSpring, yPadSpring));
	}
	
}
