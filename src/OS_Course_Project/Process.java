package OS_Course_Project;

import java.util.ArrayList;
public class Process {

	String processName;
	String addresses; 
	double startTime = -1;
	double finishTime = -1;
	double arrivalTime;
	double burstTime; // Elapses time
	int size;
	double remainingTime;
	int numberOfFaults = 0;
	int pageLocation; // process location 
	double waitTime;
	double turnaround;

	ArrayList<Page> pages = new ArrayList<Page>();

	public Process(String processName, double arrivalTime, double burstTime, int size,String addresses) {
		this.processName = processName;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.size = size;
		this.addresses = addresses; 
		this.remainingTime = burstTime;
	}

	public Process() {

	}

}