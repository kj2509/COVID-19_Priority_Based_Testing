package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Hospital;
import model.Person;

public class MySqlController {

	/**
	  *Creating Person Table 
	  * */
	private final String PERSON_TABLE_NAME = "tbl_person";
	private final String ID = "id";
	private final String NIC = "nic";
	private final String FIRST_NAME = "first_name";
	private final String LAST_NAME = "last_name";
	private final String DOB = "dob";
	private final String GENDER = "gender";
	private final String ADDRESS = "address";

	private final String CREATE_TABLE_PERSON = "CREATE TABLE IF NOT EXISTS " + PERSON_TABLE_NAME + " (" + ID
			+ " INTEGER PRIMARY KEY AUTO_INCREMENT, " + NIC + " VARCHAR(30), " + FIRST_NAME + " VARCHAR(50), "
			+ LAST_NAME + " VARCHAR(50), " + DOB + " VARCHAR(20), " + GENDER + " VARCHAR(8), " + ADDRESS
			+ " VARCHAR(350)" + ") ";

	/**
	  *Creating Hospital Table 
	  * */
	private final String HOSPITAL_TABLE_NAME = "tbl_hospital";
	private final String hos_ID = "id";
	private final String hos_NIC = "patient_nic";
	private final String PATIENT_NAME = "patient_name";
	private final String AGE = "age";
	private final String DOCTOR_NAME = "doctor_name";
	private final String DIAGNOSIS_DATE = "date";
	private final String DEPARTMENT = "department";
	private final String DIAGNOSIS_DESCRIPTION = "diagnosis_desc";
	private final String TREATMENT_DESCRIPTION = "treatment_desc";
	private final String PATIENT_TYPE = "patient_type";

	public final String CREATE_TABLE_HOSPITAL = "CREATE TABLE IF NOT EXISTS " + HOSPITAL_TABLE_NAME + " (" + hos_ID
			+ " INTEGER PRIMARY KEY AUTO_INCREMENT, " + hos_NIC + " VARCHAR(30), " + PATIENT_NAME + " VARCHAR(50), "
			+ AGE + " INTEGER(6), " + DOCTOR_NAME + " VARCHAR(30), " + DIAGNOSIS_DATE + " VARCHAR(30), " + DEPARTMENT
			+ " VARCHAR(30), " + DIAGNOSIS_DESCRIPTION + " VARCHAR(300), " + TREATMENT_DESCRIPTION + " VARCHAR(300), "
			+ PATIENT_TYPE + " VARCHAR(10)" + ") ";

	public MySqlController() {
		super();

	}

	/**
	  *Retrieve data from database from Person table
	  * */ 
	public ArrayList<Person> getPopulationRecord() {

		ArrayList<Person> record = new ArrayList<>();
		String sql = "SELECT * FROM " + PERSON_TABLE_NAME;
		PreparedStatement pstm = null;
		ResultSet res = null;
		Connection connection = MySqlConnector.getConnection();
		if (connection != null) {
			if (createTable(connection, CREATE_TABLE_PERSON)) {
				try {
					pstm = connection.prepareStatement(sql);
					res = pstm.executeQuery();
					while (res.next()) {
						Person object = new Person(res.getString(NIC), res.getString(FIRST_NAME),
								res.getString(LAST_NAME), res.getString(DOB), res.getString(GENDER),
								res.getString(ADDRESS));
						record.add(object);
					}
				} catch (SQLException e) {
					System.out.println("=====================>getPopulationRecord():" + e.getMessage());

				} finally {
					try {
						if (res != null) {
							res.close();
						}
						if (pstm != null) {
							pstm.close();
						}
						if (connection != null) {
							connection.close();
						}

					} catch (SQLException e) {
						System.out.println("=====================>getPopulationRecord():SQL Exception in finally");
					}
				}

			}
		}
		
		return record;
	}

	/**
	  *Retrieve data from database from Hospital Table
	  * */ 
	public ArrayList<Hospital> getPatientsRecord() {

		ArrayList<Hospital> record = new ArrayList<>();
		String sql = "SELECT * FROM " + HOSPITAL_TABLE_NAME;
		PreparedStatement pstm = null;
		ResultSet res = null;
		Connection connection = MySqlConnector.getConnection();
		if (connection != null) {
			if (createTable(connection, CREATE_TABLE_HOSPITAL)) {
				try {
					pstm = connection.prepareStatement(sql);
					res = pstm.executeQuery();
					while (res.next()) {
						Hospital patientRecord = new Hospital(res.getInt(hos_ID), res.getString(hos_NIC),
								res.getString(PATIENT_NAME), res.getInt(AGE), res.getString(DOCTOR_NAME),
								res.getString(DIAGNOSIS_DATE), res.getString(DEPARTMENT),
								res.getString(DIAGNOSIS_DESCRIPTION), res.getString(TREATMENT_DESCRIPTION),
								res.getString("PATIENT_TYPE"));
						record.add(patientRecord);
					}
				} catch (SQLException e) {
					System.out.println("=====================>getPatientsRecord():" + e.getMessage());
				} finally {
					try {
						if (res != null) {
							res.close();
						}
						if (pstm != null) {
							pstm.close();
						}
						if (connection != null) {
							connection.close();
						}

					} catch (SQLException e) {
						System.out.println("=====================>getPatientsRecord():SQL Exception in finally");
					}
				}
			}
		}


		return record;
	}

	/**
	 * to insert data in tbl_person in database
	 */
	public boolean insertPerson(Person person) {
		boolean returnValue = false;
		String sql = "INSERT INTO " + PERSON_TABLE_NAME + " (" + NIC + ", " + FIRST_NAME + ", " + LAST_NAME + ", " + DOB
				+ ", " + GENDER + ", " + ADDRESS + " ) VALUES(?,?,?,?,?,?)";

		Connection connection = MySqlConnector.getConnection();
		PreparedStatement pstm = null;

		if (connection != null) {
			if (createTable(connection, CREATE_TABLE_PERSON)) {
				try {
					pstm = connection.prepareStatement(sql);
					pstm.setString(1, person.getNic());
					pstm.setString(2, person.getFirstName());
					pstm.setString(3, person.getLastName());
					pstm.setString(4, person.getDateOfBirth());
					pstm.setString(5, person.getGender());
					pstm.setString(6, person.getAddress());
					pstm.executeUpdate();
					returnValue = true;
				} catch (SQLException e) {
					System.out.println("=====================>insertPerson():" + e.getMessage());
					returnValue = true;
				} finally {

					try {
						if (pstm != null) {
							pstm.close();
						}
						if (connection != null) {
							connection.close();
						}

					} catch (SQLException e) {
						System.out.println("=====================>insertPerson()->finallyBlock:" + e.getMessage());
					}
				}

			}
		}
		return returnValue;
	}

	/**
	 * to insert data in tbl_hospital in database
	 */
	public boolean insertPatient(Hospital hospital) {
		boolean returnValue = false;
		String sql = "INSERT INTO " + HOSPITAL_TABLE_NAME + " (" + hos_NIC + ", " + PATIENT_NAME + ", " + AGE + ", "
				+ DOCTOR_NAME + ", " + DIAGNOSIS_DATE + ", " + DEPARTMENT + ", " + DIAGNOSIS_DESCRIPTION + ", "
				+ TREATMENT_DESCRIPTION + "," + PATIENT_TYPE + " ) VALUES(?,?,?,?,?,?,?,?,?)";
		Connection connection = MySqlConnector.getConnection();
		PreparedStatement pstm = null;
		if (connection != null) {
			if (createTable(connection, CREATE_TABLE_HOSPITAL)) {
				try {
					pstm = connection.prepareStatement(sql);
					pstm.setString(1, hospital.getNic());
					pstm.setString(2, hospital.getPatientName());
					pstm.setInt(3, hospital.getAge());
					pstm.setString(4, hospital.getDoctorName());
					pstm.setString(5, hospital.getDiagnosis_date());
					pstm.setString(6, hospital.getDepartment());
					pstm.setString(7, hospital.getDiagnosis_description());
					pstm.setString(8, hospital.getTreatment_description());
					pstm.setString(9, hospital.getPatient_type());
					pstm.executeUpdate();
					returnValue = true;
				} catch (SQLException e) {
					returnValue = false;
					System.out.println("=====================>insertPatient():" + e.getMessage());
				} finally {
					try {
						if (pstm != null) {
							pstm.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException e) {
						System.out.println("=====================>insertPatient()->finallyBlock:" + e.getMessage());
					}
				}
			}
		}
		return returnValue;
	}

	/**
	 * to create Table with given database connection and query
	 */
	public boolean createTable(Connection con, String query) {
		boolean returnValue = false;
		PreparedStatement pstm = null;
		try {
			pstm = con.prepareStatement(query);
			pstm.executeUpdate();
			returnValue = true;
		} catch (SQLException e) {
			returnValue = false;
			System.out.println("=====================>createTable()->:" + e.getMessage());
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					System.out.println("=====================>createTable()->finallyBlock:" + e.getMessage());
				}
			}
		}
		return returnValue;
	}
	
	/**
	  *Delete patient from patient Table
	  * */ 
	public boolean deletePatient(Hospital hospital) {
		boolean returnValue = false;
		String sql = "DELETE  FROM " + HOSPITAL_TABLE_NAME + " WHERE " + hos_ID + " = " + hospital.getId() + " AND "
				+ hos_NIC + "=" + "'" + hospital.getNic() + "'";

		Connection connection = MySqlConnector.getConnection();
		PreparedStatement pstm = null;
		if (connection != null) {
			if (createTable(connection, CREATE_TABLE_HOSPITAL)) {

				try {
					pstm = connection.prepareStatement(sql);
				 pstm.execute();
					returnValue =true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return returnValue;
	}


}
