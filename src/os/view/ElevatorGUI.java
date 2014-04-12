package os.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import os.model.*;

public class ElevatorGUI extends JFrame {

	// Member Variable
	private static final long serialVersionUID = 1L;

	// Contractor
	public ElevatorGUI() throws HeadlessException {
		super();

		// UI Methods
		this.setLayout(new GridLayout(1, ElevatorConst.totalElevator));
		
		this.configureMenuBar();
		this.configureFloorButtonsPanel();
		
		this.configureJFrame();
	}

	// Private Methods for UI
	// Configure JFrame
	private void configureJFrame() {
		this.setTitle("Elevator Simulator");// Set title
		this.setSize(800, 494);// Set size of window
		this.setResizable(false);// Can't change the size
		this.setLocationRelativeTo(null);// Set the position of the window -
											// Screen's Center
	}

	// Configure Menu Bar
	private void configureMenuBar() {
		// Components
		JMenuBar menuBar;
		JMenu elevatorMenu;
		JMenu helpMenu;
		JMenuItem elevatorMenuItem;
		JMenuItem helpMenuItem;

		// Create the Menu Bar
		menuBar = new JMenuBar();

		// Build Elevator Menu
		elevatorMenu = new JMenu("Elevator");
		elevatorMenu.setMnemonic(KeyEvent.VK_E);

		// Add Menu Items to Menu "Elevator"
		elevatorMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		elevatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		elevatorMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(EXIT_ON_CLOSE);
			}

		});
		elevatorMenu.add(elevatorMenuItem);

		// Build About Menu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		// Add Menu Items to Menu "About"
		helpMenuItem = new JMenuItem("About", KeyEvent.VK_A);
		helpMenu.add(helpMenuItem);

		helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		helpMenu.add(helpMenuItem);

		// Add Menus "File" and "Help" to Menu Bar
		menuBar.add(elevatorMenu);
		menuBar.add(helpMenu);

		// Add Components
		this.setJMenuBar(menuBar);
	}
	
	// Configure Floor Buttons Panel
	private void configureFloorButtonsPanel() {
		// Components
		JPanel floorButtonsPanel = new JPanel();
		
		// Set FloorButtonsPanel Style
		floorButtonsPanel.setLayout(new GridLayout(ElevatorConst.totalFloor + 1, 3));
//		floorButtonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
//		floorButtonsPanel.setPreferredSize(new Dimension(0, 100));
		floorButtonsPanel.setBackground(Color.green);
		
		
		// Add to JFrame
		this.add(floorButtonsPanel);
	}
	
	// Show View
	public void showFrame() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
