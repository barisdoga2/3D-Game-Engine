package dev.editor;

import dev.engine.renderEngine.DisplayManager;

public class EditorRun {
	
	public static void main(String[] args) {
		DisplayManager.CreateDisplay();
		
		while(!DisplayManager.IsDestroyRequested()) {
			
			DisplayManager.UpdateDisplay();
		}
		
		DisplayManager.DestroyDisplay();
	}
	
}
