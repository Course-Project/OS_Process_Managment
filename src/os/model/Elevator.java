package os.model;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class Elevator extends JPanel implements Runnable {

	// Member Variable
	private static final long serialVersionUID = 2L;
	private Thread thread;
	private int currentFloor;
	private int targetFloor;
	private int direction;
	private boolean[] floorStatus;

	private JLabel statusLabel;
	private JButton[] floorNumButtons;
	private JButton[] floorButtons;

	// Constructor
	public Elevator() {
		super();

		// init Member Variables
		this.direction = ElevatorConst.STATUS_IDEL;
		this.currentFloor = 0;
		this.targetFloor = 0;
		this.thread = new Thread(this);

		this.floorStatus = new boolean[ElevatorConst.TOTAL_FLOOR];
		for (int i = 0; i < this.floorStatus.length; i++) {
			this.floorStatus[i] = false;
		}

		// init Button Array
		this.floorNumButtons = new JButton[ElevatorConst.TOTAL_FLOOR];
		this.floorButtons = new JButton[ElevatorConst.TOTAL_FLOOR];

		// UI Methods
		this.configureElevator();

	}

	// UI Methods
	private void configureElevator() {
		this.setLayout((new GridLayout(ElevatorConst.TOTAL_FLOOR + 1, 2)));
		this.setBorder(new MatteBorder(0, 2, 0, 0, Color.orange));

		this.add(new JLabel("楼层号", SwingConstants.CENTER));

		this.statusLabel = new JLabel(String.valueOf(this.currentFloor + 1),
				SwingConstants.CENTER);
		this.statusLabel.setForeground(Color.RED);
		this.add(this.statusLabel);

		MouseListener numListener = new NumButtonAction();
		for (int i = 0; i < ElevatorConst.TOTAL_FLOOR; i++) {
			this.floorNumButtons[19 - i] = new JButton(String.valueOf(20 - i));
			this.add(this.floorNumButtons[19 - i]);

			this.floorNumButtons[19 - i].addMouseListener(numListener);

			this.floorButtons[19 - i] = new JButton();
			this.floorButtons[19 - i].setEnabled(false);
			this.floorButtons[19 - i].setOpaque(true);
			this.floorButtons[19 - i].setBorderPainted(false);
			this.floorButtons[19 - i].setBackground(Color.yellow);
			this.add(this.floorButtons[19 - i]);
		}

		this.floorButtons[this.currentFloor].setBackground(Color.red);
	}

	class NumButtonAction extends MouseAdapter implements MouseListener {

		public void mousePressed(MouseEvent e) {
			for (int i = 0; i < floorNumButtons.length; i++) {
				if (e.getSource() == floorNumButtons[i]) {
					if (i != currentFloor) {
						floorStatus[i] = true;
					}

					if (direction == ElevatorConst.STATUS_IDEL) {
						targetFloor = i;
					} else if (direction == ElevatorConst.STATUS_UP) {
						targetFloor = getMaxPressedButton();
					} else if (direction == ElevatorConst.STATUS_DOWN) {
						targetFloor = getMinPressedButton();
					}
				}
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (this.direction == ElevatorConst.STATUS_UP
					|| this.direction == ElevatorConst.STATUS_DOWN) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.direction = ElevatorConst.STATUS_IDEL;
			}

			if (this.targetFloor > this.currentFloor) {
				this.direction = ElevatorConst.STATUS_UP;
				this.moveUpFloor();
				this.direction = ElevatorConst.STATUS_IDEL;
				this.statusLabel.setText(String.valueOf(this.targetFloor + 1));
			} else if (this.targetFloor < this.currentFloor) {
				this.direction = ElevatorConst.STATUS_DOWN;
				this.statusLabel.setText(String.valueOf(this.currentFloor + 1)
						+ "下");
				this.moveDownFloor();
				this.direction = ElevatorConst.STATUS_IDEL;
				this.statusLabel.setText(String.valueOf(this.targetFloor + 1));
			} else if (this.targetFloor == this.currentFloor
					&& this.floorStatus[this.targetFloor]) {
				this.direction = ElevatorConst.STATUS_IDEL;
				this.openDoor();
				this.statusLabel.setText(String.valueOf(this.targetFloor + 1));
			}
		}
	}

	private void moveUpFloor() {
		// i -- current floor index, i + 1 -- next floor index, index + 1 =
		// floor number
		for (int i = this.currentFloor; i <= this.targetFloor; i++) {
			this.currentFloor = i;
			try {
				this.statusLabel.setText(String.valueOf(i + 1) + "上");
				Thread.sleep(600);

				if (this.floorStatus[i]) {
					this.floorStatus[i] = false;

					this.floorButtons[i].setBackground(Color.green);
					this.statusLabel.setText(String.valueOf(i + 1) + "开门");
					Thread.sleep(1000);

					this.floorButtons[i].setBackground(Color.red);
					this.statusLabel.setText(String.valueOf(i + 1) + "关门");
					Thread.sleep(1000);
				}

				if (i + 1 <= this.targetFloor) {
					this.floorButtons[i].setBackground(Color.yellow);
					this.statusLabel.setText(String.valueOf(i + 2) + "上");
					this.floorButtons[i + 1].setBackground(Color.red);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.statusLabel.setText(String.valueOf(this.currentFloor + 1));
	}

	private void moveDownFloor() {
		// i -- current floor index, i - 1 -- next floor index, index + 1 =
		// floor number
		for (int i = this.currentFloor; i >= this.targetFloor; i--) {
			this.currentFloor = i;
			try {
				this.statusLabel.setText(String.valueOf(i + 1) + "下");
				Thread.sleep(600);

				if (this.floorStatus[i]) {
					this.floorStatus[i] = false;

					this.floorButtons[i].setBackground(Color.green);
					this.statusLabel.setText(String.valueOf(i + 1) + "开门");
					Thread.sleep(1000);

					this.floorButtons[i].setBackground(Color.red);
					this.statusLabel.setText(String.valueOf(i + 1) + "关门");
					Thread.sleep(1000);
				}

				if (i - 1 >= this.targetFloor) {
					this.floorButtons[i].setBackground(Color.yellow);
					this.statusLabel.setText(String.valueOf(i) + "下");
					this.floorButtons[i - 1].setBackground(Color.red);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.statusLabel.setText(String.valueOf(this.currentFloor + 1));
	}

	private void openDoor() {
		try {
			if (this.floorStatus[this.currentFloor]) {
				this.floorStatus[this.currentFloor] = false;

				this.floorButtons[this.currentFloor].setBackground(Color.green);
				this.statusLabel.setText(String.valueOf(this.currentFloor + 1)
						+ "开门");
				Thread.sleep(1000);

				this.floorButtons[this.currentFloor].setBackground(Color.red);
				this.statusLabel.setText(String.valueOf(this.currentFloor + 1)
						+ "关门");
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.statusLabel.setText(String.valueOf(this.currentFloor + 1));
	}

	private int getMaxPressedButton() {
		int max = 0;
		for (int i = floorStatus.length - 1; i >= 0; i--) {
			if (floorStatus[i]) {
				max = i;
				break;
			}
		}
		return max;
	}

	private int getMinPressedButton() {
		int min = 0;
		for (int i = 0; i < floorStatus.length; i++) {
			if (floorStatus[i]) {
				min = i;
				break;
			}
		}
		return min;
	}

	public void addTargetFloor(int t) {
		if (this.direction == ElevatorConst.STATUS_IDEL) {
			this.targetFloor = t;
			this.floorStatus[t] = true;
		} else if (this.direction == ElevatorConst.STATUS_UP) {
			if (t >= this.targetFloor) {
				this.targetFloor = t;
				this.floorStatus[t] = true;
			} else if (t >= this.currentFloor) {
				this.floorStatus[t] = true;
			} else {

			}
		} else if (this.direction == ElevatorConst.STATUS_DOWN) {
			if (t <= this.targetFloor) {
				this.targetFloor = t;
				this.floorStatus[t] = true;
			} else if (t <= this.currentFloor) {
				this.floorStatus[t] = true;
			} else {

			}
		}
	}

//	public int computeTimeToFloor(int a) {
//		int result = -1;
//		if (this.direction == ElevatorConst.STATUS_IDEL) {
//			result = Math.abs(a - this.currentFloor);
//		} else if (this.direction == ElevatorConst.STATUS_UP) {
//			if (a > this.targetFloor) {
//				result = a - this.currentFloor;
//			} else if (a > this.currentFloor) {
//				result = a - this.currentFloor;
//			} else {
//				result = (this.targetFloor - this.currentFloor)
//						+ (this.targetFloor - a);
//			}
//		} else if (this.direction == ElevatorConst.STATUS_DOWN) {
//			if (a < this.targetFloor) {
//				result = this.currentFloor - a;
//			} else if (a < this.currentFloor) {
//				result = this.currentFloor - a;
//			} else {
//				result = (this.currentFloor - this.targetFloor)
//						+ (a - this.targetFloor);
//			}
//		}
//		return result;
//	}

	// Getter and Setter
	public Thread getThread() {
		return thread;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getTargetFloor() {
		return targetFloor;
	}

	public int getDirection() {
		return direction;
	}
}
