package os.controller;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import os.model.Elevator;
import os.model.ElevatorConst;
import os.view.ElevatorGUI;

public class ElevatorController extends Thread {
	// Member Variable
	private ElevatorGUI view;
	private Elevator[] elevators;
	private JButton[] upButtons;
	private JButton[] downButtons;
	private boolean[] upStatus;
	private boolean[] downStatus;
	
	// Constructor
	public ElevatorController() {
		super();
		
		// init Member Variables
		this.elevators = new Elevator[ElevatorConst.TOTAL_ELEVATOR];
		this.upButtons = new JButton[ElevatorConst.TOTAL_FLOOR];
		this.downButtons = new JButton[ElevatorConst.TOTAL_FLOOR];
		this.upStatus = new boolean[ElevatorConst.TOTAL_FLOOR];
		this.downStatus = new boolean[ElevatorConst.TOTAL_FLOOR];
		
		for (int i = 0; i < ElevatorConst.TOTAL_FLOOR; i++) {
			this.upStatus[i] = false;
			this.downStatus[i] = false;
		}
		
		this.view = new ElevatorGUI(this);// With delegate
	}

	// Getter and Setter
	public void showView() {
		this.view.showFrame();
	}
	
	// Delegate Methods
//	public void addTitle() {
//		this.view.add(new JLabel());
//		for (int i = 0; i < ElevatorConst.TOTAL_ELEVATOR; i++) {
//			this.view.add(new JLabel(String.valueOf(i) + "号电梯", SwingConstants.CENTER));
//		}
//	}
	
	public void addFloorButtons() {
		// Components
		JPanel floorButtonsPanel = new JPanel();
		
		// Set FloorButtonsPanel Style
		floorButtonsPanel.setLayout(new GridLayout(ElevatorConst.TOTAL_FLOOR + 1, 3));
//		floorButtonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
		
		floorButtonsPanel.add(new JLabel("楼层", SwingConstants.CENTER));
		floorButtonsPanel.add(new JLabel("上", SwingConstants.CENTER));
		floorButtonsPanel.add(new JLabel("下", SwingConstants.CENTER));

		// Set Button Listener
		MouseListener upButtonListener = new UpButtonAction();
		MouseListener downButtonListener = new DownButtonAction();
		for (int i = 0; i < ElevatorConst.TOTAL_FLOOR; i++) {
			floorButtonsPanel.add(new JLabel(String.valueOf(20 - i), SwingConstants.CENTER));
			
			if (1 == (20 - i)) {
				this.upButtons[19 - i] = new JButton("上");
				this.upButtons[19 - i].addMouseListener(upButtonListener);
				this.downButtons[19 - i] = new JButton();
				this.downButtons[19 - i].setEnabled(false);
				this.downButtons[19 - i].setBorder(null);
			} else if (ElevatorConst.TOTAL_FLOOR == (20 - i)) {
				this.upButtons[19 - i] = new JButton();
				this.upButtons[19 - i].setEnabled(false);
				this.upButtons[19 - i].setBorder(null);
				this.downButtons[19 - i] = new JButton("下");
				this.downButtons[19 - i].addMouseListener(downButtonListener);
			} else {
				this.upButtons[19 - i] = new JButton("上");
				this.upButtons[19 - i].addMouseListener(upButtonListener);
				this.downButtons[19 - i] = new JButton("下");
				this.downButtons[19 - i].addMouseListener(downButtonListener);
			}
			floorButtonsPanel.add(this.upButtons[19 - i]);
			floorButtonsPanel.add(this.downButtons[19 - i]);
		}
		
		// Add to JFrame
		this.view.add(floorButtonsPanel);
	}
	
	public void addElevator() {
		for (int i = 0; i < ElevatorConst.TOTAL_ELEVATOR; i++) {
			this.elevators[i] = new Elevator();
			this.view.add(this.elevators[i]);
			this.elevators[i].getThread().start();
		}
	}

	// Thread
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Up Buttons
			for (int i = 0; i < this.upStatus.length - 1; i++) {
				if (this.upStatus[i]) {
					// 找一个合适的电梯
					this.findProperUpElevator(i);
				}
			}
			
			// Down Buttons
			for (int i = 1; i < this.downStatus.length; i++) {
				if (this.downStatus[i]) {
					// 找一个合适的电梯
					this.findProperDownElevator(i);
				}
			}
		}
	}
	
//	private int findProperElevator(int s, int floorNum) {
//		int result = -1;
//		int time = 100;
//		for (int i = 0; i < this.elevators.length; i++) {
//			int t = this.elevators[i].computeTimeToFloor(floorNum);
//			if (time >= t) {
//				time = t;
//				result = i;
//			}
//		}
//		return result;
//	}
	
	private void findProperUpElevator(int floorNum) {
		int distance = 40;
		int index = -1;
		for (int i = 0; i < this.elevators.length; i++) {
			int currentFloor = this.elevators[i].getCurrentFloor();
			if (this.elevators[i].getDirection() == ElevatorConst.STATUS_UP && currentFloor <= floorNum) {
				if (distance > (floorNum - currentFloor)) {
					distance = floorNum - currentFloor;
					index = i;
				}
			} else if (this.elevators[i].getDirection() == ElevatorConst.STATUS_IDEL) {
				if (distance > Math.abs(floorNum - currentFloor)) {
					distance = Math.abs(floorNum - currentFloor);
					index = i;
				}
			}
		}
		if (index != -1) {
			this.elevators[index].addTargetFloor(floorNum);
			this.upStatus[floorNum] = false;
			this.upButtons[floorNum].setEnabled(true);
		}
	}
	
	private void findProperDownElevator(int floorNum) {
		int distance = 40;
		int index = -1;
		for (int i = 0; i < this.elevators.length; i++) {
			int currentFloor = this.elevators[i].getCurrentFloor();
			if (this.elevators[i].getDirection() == ElevatorConst.STATUS_DOWN && currentFloor >= floorNum) {
				if (distance > (currentFloor - floorNum)) {
					distance = floorNum - currentFloor;
					index = i;
				}
			} else if (this.elevators[i].getDirection() == ElevatorConst.STATUS_IDEL) {
				if (distance > Math.abs(floorNum - currentFloor)) {
					distance = Math.abs(floorNum - currentFloor);
					index = i;
				}
			}
		}
		if (index != -1) {
			this.elevators[index].addTargetFloor(floorNum);
			this.downStatus[floorNum] = false;
			this.downButtons[floorNum].setEnabled(true);
		}
	}
	
	// Action Listener
	class UpButtonAction extends MouseAdapter implements MouseListener {
		
		@Override
		public void mousePressed(MouseEvent e) {
			for (int i = 0; i < upButtons.length; i++) {
				if (e.getSource() == upButtons[i]) {
					upStatus[i] = true;
				}
			}
		}
		
	}
	
	// Action Listener
	class DownButtonAction extends MouseAdapter implements MouseListener {
		
		@Override
		public void mousePressed(MouseEvent e) {
			for (int i = 0; i < downButtons.length; i++) {
				if (e.getSource() == downButtons[i]) {
					downStatus[i] = true;
					downButtons[i].setEnabled(false);
				}
			}
		}
		
	}
}
