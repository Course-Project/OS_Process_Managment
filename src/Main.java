import os.controller.ElevatorController;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ElevatorController controller = new ElevatorController();
		controller.showView();
		controller.start();
	}

}
