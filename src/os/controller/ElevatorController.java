package os.controller;

import os.view.ElevatorGUI;

public class ElevatorController {
	
	// Member Variable
	private ElevatorGUI view;

	
	// Getter and Setter
	public ElevatorGUI getView() {
		if (this.view == null) {
			view = new ElevatorGUI();
		}
		return view;
	}
}
