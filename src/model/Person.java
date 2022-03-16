package model;

import database.MySqlController;
import util.FileWriterReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Calendar;

public class Person {
	private String nic;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String gender;
	private String address;
	private int age;

	private ArrayList<Person> populationList;

	public Person() {
	
	}
	public Person(String nic, String firstName, String lastName, String _dateOfBirth, String gender, String address) {
		this.nic = nic;
		this.firstName = firstName;
		this.lastName = lastName;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		try {
			this.dateOfBirth = sdf.parse(_dateOfBirth);
		} catch (ParseException ex) {
			Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.gender = gender;
		this.address = address;
		this.age = calculatAge(_dateOfBirth);
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(dateOfBirth);

	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {

		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int calculatAge(String _dob) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		Calendar dob = Calendar.getInstance();
		try {
			dob.setTime(sdf.parse(_dob));
		} catch (ParseException ex) {
			Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
		}

		int dobYear = dob.get(Calendar.YEAR);

		Calendar now = Calendar.getInstance();

		int currentYear = now.get(Calendar.YEAR);

		int age = currentYear - dobYear;

		int dobMonth = dob.get(Calendar.MONTH);
		int currentMonth = now.get(Calendar.MONTH);
		if (dobMonth > currentMonth) {
			age--;
		} else if (currentMonth == dobMonth) {
			int dobDay = dob.get(Calendar.DATE);
			int currentDay = dob.get(Calendar.DATE);

			if (dobDay > currentDay) {
				age--;
			}
		}
		return age;

	}

	public ArrayList<Person> getPopulationList() {
		return populationList;
	}

	public void setPopulationList(ArrayList<Person> populationList) {
		this.populationList = populationList;
	}

	public void scanPopulation() {
		populationList = new ArrayList<Person>();
		populationList.addAll(getPopulation());
	}
	public ArrayList<Person> getPopulation() {
		ArrayList<Person> list = new ArrayList<>();
		MySqlController sqlController = new MySqlController();
		list = sqlController.getPopulationRecord();
		FileWriterReader obj=new FileWriterReader();
		list.addAll(obj.readPersonsFromFile());
		return list;
	}

	public void insertPerson(Person person) {
		populationList.add(person);
		MySqlController sqlController = new MySqlController();
		if(sqlController.insertPerson(person)) {
		}
		else {
			System.out.println("============>dataBase error");
			//write to local file if can't insert successfully to MySql database
			FileWriterReader obj=new FileWriterReader();
			obj.savePerson(person);
		}
	}

	public Person searchByNic(String _nic) {
		Person person = new Person();
		return person;
	}

}
