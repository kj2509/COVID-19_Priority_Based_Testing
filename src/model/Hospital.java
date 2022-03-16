package model;

import java.util.ArrayList;

import database.MySqlController;
import util.FileWriterReader;

public class Hospital extends Person {

	private ArrayList<Hospital> patientsList = null;
	private int id;
	private String patientName;
	private String doctorName;
	private String diagnosis_date;
	private String department;
	private String diagnosis_description;
	private String treatment_description;
	private String patient_type;

	private int totalBeds = 5;
	private int freeBeds = 0;

	public Hospital() {

		super();
		patientsList = new ArrayList<Hospital>();
		patientsList.addAll(getPatients());

	}

	public Hospital(String _nic, String _patientName, int _age, String _doctorName, String _diagnosis_date,
			String _department, String _diagnosis_description, String _treatment_description, String _patient_type) {
		setNic(_nic);
		this.patientName = _patientName;
		setAge(_age);
		this.doctorName = _doctorName;
		this.diagnosis_date = _diagnosis_date;
		this.department = _department;
		this.diagnosis_description = _diagnosis_description;
		this.treatment_description = _treatment_description;
		this.patient_type = _patient_type;
	}
	public Hospital(int id,String _nic, String _patientName, int _age, String _doctorName, String _diagnosis_date,
			String _department, String _diagnosis_description, String _treatment_description, String _patient_type) {
		this.id=id;
		setNic(_nic);
		this.patientName = _patientName;
		setAge(_age);
		this.doctorName = _doctorName;
		this.diagnosis_date = _diagnosis_date;
		this.department = _department;
		this.diagnosis_description = _diagnosis_description;
		this.treatment_description = _treatment_description;
		this.patient_type = _patient_type;
	}

	public String getPatientName() {
		return patientName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDiagnosis_date() {
		return diagnosis_date;
	}

	public void setDiagnosis_date(String diagnosis_date) {
		this.diagnosis_date = diagnosis_date;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDiagnosis_description() {
		return diagnosis_description;
	}

	public void setDiagnosis_description(String diagnosis_description) {
		this.diagnosis_description = diagnosis_description;
	}

	public String getTreatment_description() {
		return treatment_description;
	}

	public void setTreatment_description(String treatment_description) {
		this.treatment_description = treatment_description;
	}

	public String getPatient_type() {
		return patient_type;
	}

	public void setPatient_type(String patient_type) {
		this.patient_type = patient_type;
	}

	public int getTotalBeds() {
		return totalBeds;
	}

	public void setTotalBeds(int totalBeds) {
		this.totalBeds = totalBeds;
	}

	public int getFreeBeds() {
		return freeBeds;
	}

	public void setFreeBeds(int freeBeds) {
		this.freeBeds = freeBeds;
	}

	public ArrayList<Department> getDepartments() {
		ArrayList<Department> record = new ArrayList<>();
		record.add(new Department(1, "Emergency"));
		record.add(new Department(2, "Internal Medicine"));
		record.add(new Department(3, "Physiotherapy"));
		record.add(new Department(4, "Neurology"));
		record.add(new Department(5, "Cardiology"));
		record.add(new Department(6, "ENT"));
		record.add(new Department(7, "Orthopedic"));
		record.add(new Department(8, "Dental"));
		record.add(new Department(9, "Dermatology"));
		record.add(new Department(10, "Ophthalmology"));
		return record;

	}

	public ArrayList<Hospital> getPatientsList() {
		return patientsList;
	}

	public void setPatientsList(ArrayList<Hospital> patientsList) {
		this.patientsList = patientsList;
	}

	// will insert data into database
	public void insertPatient(Hospital hospital) {
		this.patientsList.add(hospital);
		MySqlController sqlController = new MySqlController();
		if (sqlController.insertPatient(hospital)) {

		} else {
			System.out.println("============>dataBase error");
			// write to local file if can't insert successfully to MySql database
			FileWriterReader obj = new FileWriterReader();
			obj.saveHospital(hospital);
		}

	}

	// will retrieve data from database
	public ArrayList<Hospital> getPatients() {

		ArrayList<Hospital> list = new ArrayList<>();
		MySqlController sqlController = new MySqlController();
		FileWriterReader obj = new FileWriterReader();
		list = sqlController.getPatientsRecord();
		list.addAll(obj.readPatientsFromFile());
		return list;
	}

	public void deletePatient(Hospital hospital) {
		this.patientsList.remove(hospital);
		MySqlController sqlController = new MySqlController();
		if(sqlController.deletePatient(hospital)) {
			System.out.println("===================>Patient Record Deleted Successfully");
		}
		
	}
}
