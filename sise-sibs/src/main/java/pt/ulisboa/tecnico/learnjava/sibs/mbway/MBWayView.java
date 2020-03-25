package pt.ulisboa.tecnico.learnjava.sibs.mbway;

public class MBWayView {

	public MBWayView() {

	}

	public void printCode(int code) {
		System.out.println("Code: " + code + " (don't share it with anyone).");
	}

	public void printConfirmation(Boolean value) {
		if (value) {
			System.out.println("MBWay association completed successfully!");
		} else {
			System.out.println("Wrong confirmation code. Thry association again");
		}
	}

	public void printTransferOutput(String output) {
		System.out.println(output);
	}

	public void printSplitBillOutput(String output) {
		System.out.println(output);
	}

}
