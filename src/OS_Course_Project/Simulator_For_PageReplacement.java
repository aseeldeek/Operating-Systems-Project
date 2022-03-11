package OS_Course_Project;

/**
 * <<<--- THIS PROJECT DONE BY -->>> :
 *-- Aseel Deek        - 1190587  
 *-- Lojain Abdalrazaq - 1190707 
 *-- Laith Abdalrazaq  - 1190715
 * <<< ------------------------------ >>>  
 * NOTE: to implement this code java version 17 was used to write the code
 *       and the Window builder was used to create the GUI  
 *       
 */
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JTextArea;

public class Simulator_For_PageReplacement extends JFrame implements ActionListener {
	
	/*---->  For the GUI  */
	static Simulator_For_PageReplacement SimulatorMain;    
	static Simulator_For_PageReplacement Simulatorframe;  
	private JPanel contentPane;
	private JTextField TimeQuan;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JTextArea algo;
	private JTable table;
	private JTable table_1;
	private JButton btnNewButton;
	private JTextPane FaultPage;
	private JTextPane totalCycles;
	/* --------------------------------------- */

	int frames = 0; // M
	int number_ofProcesses = 0;
	int MinSize = 0;
	int ref_len = 0;
	Boolean isFull = false; // is full of not
	final static int cycles_for_disk = 300;
	final static int cycles_for_memory_access = 1;
	int pointer = 0;
	int cycles = 0;
	int tQuant = 2;
	int prevProcess = 0;
	int totalNumberOfFaults = 0;
	String files_Path;
	int comboChoice;
	Frame memory[];
	ArrayList<Process> processes = new ArrayList<Process>(); // save processes
	ArrayList<Process> readyQueue = new ArrayList<Process>(); // ready queue to add arrived processes that are ready to
																// run
	ArrayList<Page> stackLRU = new ArrayList<Page>(); // for LRU Algorithm
	ArrayList<Page> stackFIFO = new ArrayList<Page>(); // for FIFO Algorithm
	JComboBox<Object> comboBox = new JComboBox<Object>();


	/**
	 * The Main Function For Our Class
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulatorframe = new Simulator_For_PageReplacement();
					Simulatorframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Simulator_For_PageReplacement() {
		
		setTitle("Paging Simulater");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 521);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(211, 211, 211));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Choose Input File");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel.setBounds(10, 67, 120, 18);
		contentPane.add(lblNewLabel);

		btnNewButton = new JButton("Import a File");
		btnNewButton.setBackground(new Color(245, 245, 245));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // connection will happen here
				browseActionPerformed(e);
			}
		});
		btnNewButton.setForeground(SystemColor.textText);
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnNewButton.setBounds(165, 66, 120, 21);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("Choose Time Quantom");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel_1.setBounds(10, 41, 149, 16);
		contentPane.add(lblNewLabel_1);

		TimeQuan = new JTextField();
		TimeQuan.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		TimeQuan.setBounds(165, 38, 120, 19);
		contentPane.add(TimeQuan);
		TimeQuan.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Select an algorithm");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel_2.setBounds(10, 9, 129, 21);
		contentPane.add(lblNewLabel_2);

		comboBox.setForeground(new Color(0, 0, 0));
		comboBox.setBackground(new Color(245, 245, 245));
		comboBox.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		comboBox.setBounds(165, 9, 120, 21);
		contentPane.add(comboBox);

		comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "LRU", "FIFO" }));
		comboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (comboBox.getSelectedItem() == "LRU") {
					System.out.println(comboBox.getSelectedItem());
					comboChoice = 1;

				} else if (comboBox.getSelectedItem() == "FIFO") {
					System.out.println(comboBox.getSelectedItem());
					comboChoice = 2;
				}

				comboBoxActionPerformed(evt); // go up and down
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(93, 225, 1, 2);
		contentPane.add(separator);

		JLabel lblNewLabel_3 = new JLabel("Total Page Fault");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel_3.setBounds(224, 454, 120, 18);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Total Cycles");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel_4.setBounds(21, 453, 109, 21);
		contentPane.add(lblNewLabel_4);

		FaultPage = new JTextPane(); /* total page fault */
		FaultPage.setBounds(364, 454, 70, 19);
		FaultPage.setEditable(false);
		contentPane.add(FaultPage);

		totalCycles = new JTextPane(); /* total Cycles */
		totalCycles.setBounds(144, 455, 70, 19);
		totalCycles.setEditable(false);
		contentPane.add(totalCycles);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(345, 7, 650, 168);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "PID", "Start", "Duration", "Size", "Memory Traces" }));
		table.setFont(new Font("Times New Roman", Font.BOLD, 12));
		table.setBackground(new Color(255, 255, 255));

		JLabel lblNewLabel_5 = new JLabel("Scheduling Processes");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD, 11));
		lblNewLabel_5.setBounds(355, 189, 129, 13);
		contentPane.add(lblNewLabel_5);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(345, 212, 650, 199);
		contentPane.add(scrollPane_1);

		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Process", "Arrival Time", "CPU Burst",
				"Finish Time", "Waitting Time (W) ", "TurnAround time (TA)", "No. of page Fault" }));

		JButton btnNewButton_1 = new JButton("Import  a file again ");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("EDDASDD");
				Simulatorframe.dispose();
				Simulatorframe = new Simulator_For_PageReplacement();
				Simulatorframe.setVisible(true);
			}
		});
		btnNewButton_1.setBackground(new Color(255, 255, 255));
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnNewButton_1.setBounds(597, 428, 163, 27);
		contentPane.add(btnNewButton_1);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(34, 130, 279, 314);
		contentPane.add(scrollPane_2);

		algo = new JTextArea(); /* save the output of the algorithm */
		algo.setEditable(false);
		scrollPane_2.setViewportView(algo);

		JLabel lblNewLabel_6 = new JLabel("Algorithm Output");
		lblNewLabel_6.setBackground(new Color(255, 255, 255));
		lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel_6.setBounds(119, 107, 109, 13);
		contentPane.add(lblNewLabel_6);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(45);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(67);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(59);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(63);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(98);
		table_1.getColumnModel().getColumn(5).setPreferredWidth(112);
		table_1.getColumnModel().getColumn(6).setPreferredWidth(88);
		
	}
	private void comboBoxActionPerformed(java.awt.event.ActionEvent e) {
		Object c = (Object) e.getSource();

	}// event_jComboBox1ActionPerformed

	private void browseActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser choose = new JFileChooser();
		File selectedFile = new File("");
		choose.setCurrentDirectory(selectedFile);
		int returnValue = choose.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = choose.getSelectedFile();
			files_Path = selectedFile.getParent();
			readFile(selectedFile);
			btnNewButton.setEnabled(false);
		}

	}// browseActionPerformed

	/**
	 * Creating Read file Function .
	 */

	public void readFile(File file) {
		int count = 0;
		Scanner input = null;
		try {
			input = new Scanner(file);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				count = count + 1;

				if (count == 1) {
					number_ofProcesses = Integer.parseInt(line); // change to integer
				} else if (count == 2) {
					frames = Integer.parseInt(line); // change to integer
				} else if (count == 3) {
					MinSize = Integer.parseInt(line); // change to integer
				} else {
					String token[] = line.split(" ");
					processes.add(new Process(token[0], Double.parseDouble(token[1]), Double.parseDouble(token[2]),
							Integer.parseInt(token[3]), token[4]));

					String addresses[] = token[4].split(","); // save the traces
					for (int i = 0; i < addresses.length; i++) {
						int pageNumber = (int) (Integer.parseInt(addresses[i], 16) / Math.pow(2, 12)); // turn the
																										// traces to
																										// page number
						processes.get(processes.size() - 1).pages.add(new Page(processes.size() - 1, pageNumber));

					}
				}
			}

			// add to the Table
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			for (int k = 0; k < number_ofProcesses; k++) {
				model.addRow(new Object[] { processes.get(k).processName, String.valueOf(processes.get(k).arrivalTime),
						String.valueOf(processes.get(k).burstTime), String.valueOf(processes.get(k).size),
						processes.get(k).addresses });

				/* finds the total page fault */
				totalNumberOfFaults += processes.get(k).numberOfFaults;
			}
			RoundRobin_Algorithm(); // Round Robin

		} catch (FileNotFoundException ex) {
			Logger.getLogger(Simulator_For_PageReplacement.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("number of Process:-> " + number_ofProcesses); // prints the process number from the file
		System.out.println("Size:-> " + frames); // prints the physical memory size in frame
		System.out.println("Minimum Size :-> " + MinSize); // print the minimum size per page

	} /* end of file reader function */

	/**
	 * Creating Round Robin Function .
	 */

	public void RoundRobin_Algorithm() {

		memory = new Frame[frames];
		for (int i = 0; i < frames; i++) // create a memory of the entered size
		{
			memory[i] = new Frame();

		}

		int i = 0;
		if (!TimeQuan.getText().equals("")) { // read the time Quantum from the user
			tQuant = Integer.valueOf(TimeQuan.getText());
		}
		int time = 0; // current time
		while (!finished_Status()) {
			prevProcess = i; // to trace the context switch
			arrival_Status(time); // to check if any process has arrived at this moment

			if (readyQueue.size() == 0) {
				time++;
				continue;
			}

			i = i % readyQueue.size(); // these two lines, how the processes are taken from the Queue
			Process p = readyQueue.get(i++);

			if (prevProcess != i) { // when there is a context switch add 5 Cycles
				cycles += 5;
			}

			if (p.startTime == -1) {
				p.startTime = time;
			}

			ref_len = tQuant + p.pageLocation; // this is the reference string length for processes

			/* Choice 1 */ if (comboChoice == 1) {
				for (int j = p.pageLocation; j < tQuant + p.pageLocation; j++) {

					LRU_Algorithm(p.pages.get(j)); /* always send the pages like this p1 p1 p1 p0 p0 p0 p1 */
					arrival_Status(time++); // check if any process has arrived at this moment
					p.remainingTime--;

					if (p.remainingTime == 0) { // when the process finishes
						break;
					}

				}
				p.pageLocation += tQuant;

				if (p.remainingTime == 0) {
					p.finishTime = time;
					readyQueue.remove(p);
					i = i - 1; /* start from the first process arrived */
				}

				p.turnaround = p.finishTime - p.arrivalTime; // find the TA
				p.waitTime = p.turnaround - p.burstTime; // find the W

			}

			/* Choice 2 */ else if (comboChoice == 2) {

				for (int j = p.pageLocation; j < tQuant + p.pageLocation; j++) {

					FIFO_Algo(p.pages.get(j)); /** always send the page p1 p1 p1 p0 p0 p0 p1 */

					arrival_Status(time++); // check if any process has arrived at this moment
					p.remainingTime--;

					if (p.remainingTime == 0) { // when the process finishes
						break;
					}

				}
				p.pageLocation += tQuant;

				if (p.remainingTime == 0) {
					p.finishTime = time;
					readyQueue.remove(p);
					i = i - 1; /* start from the first process arrived */
				}

				p.turnaround = p.finishTime - p.arrivalTime; // find the TA
				p.waitTime = p.turnaround - p.burstTime; // find the W

			}

		} // end of the while ( repeat this function till all the processes finish )

		// finds the total cycles for all processes
		System.out.println("Total Cycles are : " + cycles + "\n");
		totalCycles.setFont(new Font("Times New Roman", 0, 13));
		totalCycles.setText(totalCycles.getText() + cycles);

		// this table is to show the output of the Round Robin algorithm
		DefaultTableModel model = (DefaultTableModel) table_1.getModel();
		for (int k = 0; k < processes.size(); k++) {
			model.addRow(new Object[] { processes.get(k).processName, String.valueOf(processes.get(k).arrivalTime),
					String.valueOf(processes.get(k).burstTime), String.valueOf(processes.get(k).finishTime),
					String.valueOf(processes.get(k).waitTime), String.valueOf(processes.get(k).turnaround),
					String.valueOf(processes.get(k).numberOfFaults) });

			/* <<<<<<<<<<<<<<< finds the total page fault for a file >>>>>>>>>>>>>>>>>>> */
			totalNumberOfFaults = totalNumberOfFaults + processes.get(k).numberOfFaults;
		}

		System.out.println("\nTotal Page Falut is : " + totalNumberOfFaults + "\n"); // prints the total # of faults in
																						// terminal
		FaultPage.setFont(new Font("Times New Roman", 0, 13));
		FaultPage.setText(FaultPage.getText() + totalNumberOfFaults); // prints the total # of faults in GUI

	} // End of Round Robin Algorithm

	public boolean finished_Status() { // function to check if all process has finished or not
		for (Process p : processes) {
			if (p.finishTime == -1) // if any process has finish time = -1, then it hasn't finished yet
			{
				return false;
			}
		}
		return true;
	}

	public void arrival_Status(int time) { // check if any process has arrived at the given time
		for (Process p : processes) {
			if (p.arrivalTime == time && !readyQueue.contains(p)) // if a process arrived at the given time
			{
				readyQueue.add(p); // then add it to the readyQueue

			}
		}
	}

	/**
	 * FIFO_PageReplacment_Algorithm
	 */

	public void FIFO_Algo(Page pg) {
		String result = "";
		if (stackFIFO.contains(pg)) {
			stackFIFO.remove(stackFIFO.indexOf(pg));
		}
		stackFIFO.add(pg);
		int search = -1;
		for (int j = 0; j < frames; j++) {
			if (is_Equal(pg, memory[j].page)) {

				search = j;// inside the memory
				cycles = cycles + cycles_for_memory_access; // counts the cycles When enter the Memory
                 
				 int id = pg.processPid ;
	             
	             String out = ""; 
					for (int w = 0; w < frames; w++) {
						if (memory[w].page == null) {
							out += "* ";
						}
						else
						out += memory[w].page.processPid +" " ;			
					}
					System.out.print(id + ": Memory is: ---> " + out +"\n");
					result += " P" + id + ": Memory is: ---> " + out +"\n";


				System.out.println("Hit: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")");
				result += " Hit: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")\n\n";
				break;
			}
		}
		if (search == -1) {
			processes.get(pg.processPid).numberOfFaults++; // increase the faults
			memory[pointer].page = pg;
			cycles = cycles + cycles_for_disk; // counts the cycles When enter the disk
			pointer = (pointer + 1) % frames;
			 int id = pg.processPid ;
             
             String out = ""; 
				for (int w = 0; w < frames; w++) {
					if (memory[w].page == null) {
						out += "* ";
					}
					else
					out += memory[w].page.processPid +" " ;			
				}
				System.out.print(id + ": Memory is: ---> " + out +"\n");
				result += " P" + id + ": Memory is: ---> " + out +"\n";


			System.out.println(
					"Page Fault: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")");
			result += " Page Fault: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")\n\n";

		}

		algo.setFont(new Font("Times New Roman", 0, 13));
		algo.setText(algo.getText() + result);

	}

	/**
	 * LRU_PageReplacment_Algorithm
	 */

	public void LRU_Algorithm(Page pg) {
		String result = "";
		if (stackLRU.contains(pg)) { // this condition when the Queue has the process
			stackLRU.remove(stackLRU.indexOf(pg));
		}
		stackLRU.add(pg); // add the process

		int search = -1; // check if the element is in the Queue

		for (int j = 0; j < frames; j++) { // start to save the processes inside the memory

			/* case 1 */ if (is_Equal(pg, memory[j].page)) {

				search = j;// inside the memory
				cycles = cycles + cycles_for_memory_access; // counts the cycles When enter the Memory
                 int id = pg.processPid ;
                 
                 String out = ""; 
 				for (int w = 0; w < frames; w++) {
 					if (memory[w].page == null) {
 						out += "* ";
 					}
 					else
 					out += memory[w].page.processPid +" " ;			
 				}
 				System.out.print(id + ": Memory is: ---> " + out +"\n");
 				result += " P" + id + ": Memory is: ---> " + out +"\n";

				System.out.println("Hit: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")");
				result += " Hit: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")\n\n";

				break;
			}
		}

		/* case 2 */ if (search == -1) {
			/** the Algorithm of LRU **/
			if (isFull) {
				int min_loc = ref_len;
				for (int j = 0; j < frames; j++) {
					if (stackLRU.contains(memory[j].page)) {
						int temp = stackLRU.indexOf(memory[j].page);
						if (temp < min_loc) {
							min_loc = temp;
							pointer = j;
						}
					}
				}
			}
			memory[pointer].page = pg;
			processes.get(pg.processPid).numberOfFaults++;
			cycles = cycles + cycles_for_disk; // counts the cycles when enter the disk
			 int id = pg.processPid ;
             
             String out = ""; 
				for (int w = 0; w < frames; w++) {
					if (memory[w].page == null) {
						out += "* ";
					}
					else
					out += memory[w].page.processPid +" " ;			
				}
				System.out.print(id + ": Memory is: ---> " + out +"\n");
				result += " P" + id + ": Memory is: ---> " + out +"\n";

			System.out.println(
					"Page Fault: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")");
			result += " Page Fault: (Number of Page Faults: " + processes.get(pg.processPid).numberOfFaults + ")\n\n";

			pointer++;

			if (pointer == frames) {
				pointer = 0;
				isFull = true;
			}
		}

		/* show the output in the GUI */
		algo.setFont(new Font("Times New Roman", 0, 13));
		algo.setText(algo.getText() + result);
	}

	/**
	 * check if the process is in the MEMORY or not
	 */

	public boolean is_Equal(Page pg1, Page pg2) {
		if (pg1 == null || pg2 == null) {
			return false;
		}
		if ((pg1.processPid == pg2.processPid)) {
			return true;
		}

		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
