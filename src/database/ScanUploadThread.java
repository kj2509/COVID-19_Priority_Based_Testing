package database;
import java.util.ArrayList;

import model.Hospital;
import model.Person;
import util.FileWriterReader;

public class ScanUploadThread implements Runnable{

	private Thread thread = null;
	private String name = "";
	private ArrayList<Hospital> patientList;
	private ArrayList<Person> personList;

	MySqlController sqlController = new MySqlController();
	FileWriterReader fileWriterReader = new FileWriterReader();
	
	public ScanUploadThread(String _name) {
		this.name=_name;
	}

	@Override
	public void run() {
		while(true) {
			if(name.equals("personThread")) {
				personThread();
			}else if(name.equals("patientThread")) {
				patientThread();
			}
			//stop thread for thirty seconds to check if there is data in file or not
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void personThread() {
		personList = fileWriterReader.readPersonsFromFile();
		if (personList != null && personList.size() > 0) {
			for (Person person : personList) {
				if (sqlController.insertPerson(person)) {
					fileWriterReader.removePersonObjectFromFile(person);
					//stop thread for three seconds after each record inserted from file to database
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			personList.clear();
		} else {
			System.out.println("==========>No data Available in personsFile to Upload");
		}	
	}
	
	private void patientThread() {
		patientList = fileWriterReader.readPatientsFromFile();
		if (patientList != null && patientList.size() > 0) {
			for (Hospital patient : patientList) {
				if (sqlController.insertPatient(patient)) {
					fileWriterReader.removeHospitalObjectFromFile(patient);
					//stop thread for three seconds after each record inserted from file to database
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			patientList.clear();

		} else {
			System.out.println("==========>No data Available in patientsFile to Upload");
		}	
	}

	public void start() {
		 System.out.println("Starting " + name);
		if (thread == null) {
			thread = new Thread(this, name);
			thread.start();
		}
	}

}
