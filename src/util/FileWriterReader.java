package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import model.Hospital;
import model.Person;

public class FileWriterReader {
	String personsFile = "persons.txt";
	String patientsFile = "patients.txt";
	
	/**
	  * Converts the object of Hospital to String and insert into patients.txt File 
	  * */
	public void saveHospital(Hospital patient) {

		String objectString = patient.getNic() + "|" + patient.getPatientName() + "|" + patient.getAge() + "|"
				+ patient.getDoctorName() + "|" + patient.getDiagnosis_date() + "|" + patient.getDepartment() + "|"
				+ patient.getDiagnosis_description() + "|" + patient.getTreatment_description();

		saveToFile(patientsFile, objectString, true);

	}
	
	/**
	  * returns the List of Hospital class Object after reading from patients.txt File
	  * */
	public ArrayList<Hospital> readPatientsFromFile() {

		ArrayList<Hospital> list = new ArrayList<Hospital>();
		File file = new File(patientsFile);
		
	
		try {
			file.createNewFile();//if file already exists  will do nothing
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if(line!="" && !line.isEmpty()) {

					String[] arr = line.split("\\|");
					if(arr.length==9) {
						list.add(
								new Hospital(arr[0], arr[1], Integer.parseInt(arr[2]), arr[3], arr[4], arr[5], arr[6], arr[7],arr[8]));
					}

					
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	  * Converts the object of person to string  and insert into persons.txt File 
	  * */
	public void savePerson(Person person) {

		String objectString = person.getNic() + "|" + person.getFirstName() + "|" + person.getLastName() + "|"
				+ person.getDateOfBirth() + "|" + person.getGender() + "|" + person.getAddress();

		saveToFile(personsFile, objectString, true);
	}
	
	/**
	  * returns the List of Person class Object after reading from persons.txt File
	  * */
	public ArrayList<Person> readPersonsFromFile() {
		ArrayList<Person> list = new ArrayList<Person>();
		File file = new File(personsFile);
		try {
			file.createNewFile();//if file already exists  will do nothing
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if(line!="" && !line.isEmpty()) {
					String[] arr = line.split("\\|");
					list.add(new Person(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]));
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	  * Delete the hospital class object String  from patints.txt File
	  * */
	public void removeHospitalObjectFromFile(Hospital patient) {
		File inputFile = new File(patientsFile);
		File tempFile = new File("temp.txt");
		String _objectString=patient.getNic() + "|" + patient.getPatientName() + "|" + patient.getAge() + "|"
				+ patient.getDoctorName() + "|" + patient.getDiagnosis_date() + "|" + patient.getDepartment() + "|"
				+ patient.getDiagnosis_description() + "|" + patient.getTreatment_description()+"|"+patient.getPatient_type();

		try {
			FileWriter fw = new FileWriter(tempFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			Scanner scanner = new Scanner(inputFile);
			while (scanner.hasNext()) {
				String currentLine = scanner.nextLine().trim();
				if (currentLine.equals(_objectString.trim())) {
					continue;
				} else {
					pw.println(currentLine);
				}
			}
			scanner.close();
			pw.flush();
			pw.close();
			inputFile.delete();
			File dump = new File(patientsFile);
			tempFile.renameTo(dump);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	  * Delete the Person class object String  from persons.txt File
	  * */
	public void removePersonObjectFromFile(Person person) {
		File inputFile = new File(personsFile);
		File tempFile = new File("tmp.txt");
		String _objectString=person.getNic() + "|" + person.getFirstName() + "|" + person.getLastName() + "|"
				+ person.getDateOfBirth() + "|" + person.getGender() + "|" + person.getAddress();

		try {
			FileWriter fw = new FileWriter(tempFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			Scanner scanner = new Scanner(inputFile);
			while (scanner.hasNext()) {
				String currentLine = scanner.nextLine().trim();
				if (currentLine.equals(_objectString)) {
					continue;
				} else {
					pw.println(currentLine);
				}
			}
			scanner.close();
			pw.flush();
			pw.close();
			inputFile.delete();
			File dump = new File(personsFile);
			tempFile.renameTo(dump);

		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	  *save object String in txt File
	  * */
	public void saveToFile(String fileName, String _objectString, boolean append) {
		// 1-create a file
		File file = new File(fileName);

		// 2-Create a FileWriter
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, append);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 3-Create a printWriter
		PrintWriter pw = new PrintWriter(fw);

		pw.println(_objectString);

		pw.close();
	}
}
