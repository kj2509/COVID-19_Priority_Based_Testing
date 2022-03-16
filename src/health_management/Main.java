package health_management; //Note:- This was first built on sceneBuilder but then due to preference of just using javaFx was changed
                           
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import database.ScanUploadThread;
import model.Person;
import model.Department;
import model.Hospital;
import model.Ministry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * All suppressions are made so that an ease of code modifications are possible
 * without jeopardizing the program functionality
 */
public class Main extends Application {
	BorderPane root;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = new BorderPane();
			Scene scene = new Scene(root, 1000, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Health Management");
			primaryStage.show();
			displayLogin("main");
		    

			/**
			 * These are the two thread which starts when application start Responsible for
			 * upload data to the MySql server from textFiles There are two textFiles
			 * "persons.txt" and "patients.txt" persons.txt file is used to store the data
			 * of Person class Object when Internet is down patients.txt file is used to
			 * store the data of Hospital class Object when Internet is down This thread
			 * scans the data from file line by line and delete object data from file after
			 * successful insert to MySql server
			 */
			ScanUploadThread personThread = new ScanUploadThread("personThread");
			personThread.start();
			ScanUploadThread patientThread = new ScanUploadThread("patientThread");
			patientThread.start();
			
			/**
			 * to stop child threads when main thread stops
			 */
			primaryStage.setOnCloseRequest(e -> {
				Platform.exit();
				System.exit(0);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void displayLogin(String name) {
		root.getChildren().clear();
		root.setCenter(Login(name));
	}

	/**
	 * is called when Application starts to Display Dashboard
	 */
	public void displayDashboard() {

		root.getChildren().clear();
		root.setCenter(dashboard());

	}

	/**
	 * is called when "National Registration button" clicked on Dashboard
	 */
	public void displayNRC() {
		root.getChildren().clear();
		root.setCenter(NRC());
	}

	/**
	 * is called when "Hospital" button clicked on Dashboard
	 */
	public void displayHospital() {

		root.getChildren().clear();
		root.setCenter(Hospital());
	}

	/**
	 * is called when Ministry button clicked on Dashboard
	 */
	public void displayMinistry() {
		root.getChildren().clear();
		root.setCenter(Ministry());
		checkStatus();
	}

	/**
	 * ======================= Login=================================
	 */
	
	public BorderPane Login(String login_desc) {
		BorderPane borderPane = new BorderPane();
		borderPane.setMaxHeight(Double.MAX_VALUE);
		borderPane.setMaxWidth(Double.MAX_VALUE);

		
		/* ============Title=========== */
		
		HBox titleBox = new HBox();
		titleBox.setAlignment(Pos.CENTER);
		Label lblTitle = new Label();

		if (login_desc.equals("main")) {
			lblTitle.setText("Health Management System");
		} else if (login_desc.equals("NRC")) {
			lblTitle.setText("NRC");
		} else if (login_desc.equals("Hospital")) {
			lblTitle.setText("Hospital");
		} else if (login_desc.equals("Ministry")) {
			lblTitle.setText("Ministry");
		}

		lblTitle.getStyleClass().add("lblTitle");
		titleBox.getChildren().add(lblTitle);
		
		/* ============Title=========== */
		
		/* ============UserName=========== */
		
		HBox userNameBox = new HBox();
		Label lblUsername = new Label("Username ");

		TextField txtUsername = new TextField();
		
		userNameBox.setAlignment(Pos.CENTER);
		userNameBox.getChildren().addAll(lblUsername, txtUsername);
		
		/* ============UserName=========== */
		
		/* ============Password=========== */
		
		HBox passwordBox = new HBox();
		passwordBox.setAlignment(Pos.CENTER);
		Label lblPassword = new Label("Password ");
		PasswordField txtPassword = new PasswordField();
		

		passwordBox.setAlignment(Pos.CENTER);
		passwordBox.getChildren().addAll(lblPassword, txtPassword);
		
		/* ============Password=========== */
		
		/* ============btnBox=========== */
		
		HBox btnBox = new HBox();
		btnBox.setAlignment(Pos.CENTER);
		btnBox.setSpacing(10);
		Button btnClear = new Button("Clear");
		Button btnLogin = new Button("Login");
		btnBox.getChildren().addAll(btnClear, btnLogin);

		VBox box = new VBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(titleBox, userNameBox, passwordBox, btnBox);
		box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		box.setSpacing(10);

		borderPane.setCenter(box);

		HBox statusBox = new HBox();
		statusBox.setAlignment(Pos.CENTER);
		statusBox.setPrefHeight(100);
		statusBox.setPrefWidth(200);
		BorderPane.setAlignment(statusBox, Pos.CENTER);

		Label lblStatus = new Label();
		HBox.setHgrow(lblStatus, Priority.ALWAYS);
		lblStatus.setTextFill(Color.RED);
		statusBox.getChildren().add(lblStatus);

		borderPane.setBottom(statusBox);

		btnClear.setOnAction(e -> {
			txtUsername.setText("");
			txtPassword.setText("");
		});

		btnLogin.setOnAction(e -> {
			String userName = txtUsername.getText().trim();
			String password = txtPassword.getText().trim();
			if (userName.isEmpty() || userName == null) {
				lblStatus.setText("Username is required");
				return;
			} else if (password.isEmpty() || password == null) {
				lblStatus.setText("Password is required");
				return;
			}

			
		
			if (lblTitle.getText().equals("Health Management System")) {
				// display Dashboard
				if (verifyMainLogin(userName, password)) {
					displayDashboard();
				} else {
					lblStatus.setText("Invalid Credentials");
				}
			} else if (lblTitle.getText().equals("NRC")) {
				// display Nrc Screen
				if (verifyNRCLogin(userName, password)) {
					displayNRC();
				} else {
					lblStatus.setText("Invalid Credentials");
				}
			} else if (lblTitle.getText().equals("Hospital")) {
				// display Hospital Screen
				if (verifyHospitalLogin(userName, password)) {
					displayHospital();
				} else {
					lblStatus.setText("Invalid Credentials");
				}
			} else if (lblTitle.getText().equals("Ministry")) {
				// display Ministry Screen
				if (verifyMinistryLogin(userName, password)) {
					displayMinistry();
				} else {
					lblStatus.setText("Invalid Credentials");
				}
			}
		});

		return borderPane;
	}

	public boolean verifyMainLogin(String userName, String password) {
		if (userName.equals("kevin") && password.equals("root")) {
			return true;
		}
		return false;
	}

	public boolean verifyNRCLogin(String userName, String password) {
		if (userName.equals("saeed") && password.equals("toor")) {
			return true;
		}
		return false;
	}

	public boolean verifyHospitalLogin(String userName, String password) {
		if (userName.equals("fatima") && password.equals("roottoor")) {
			return true;
		}
		return false;
	}

	public boolean verifyMinistryLogin(String userName, String password) {
		if (userName.equals("kevin_ministry") && password.equals("ministry_root")) {
			return true;
		}
		return false;
	}

	/**
	 * ======================= Login=================================
	 */

	/**
	 * ======================= Start of NRC Screen=================================
	 */
	
	/**
	 * Responsible for populating dashboard 
	 */
	
	public AnchorPane dashboard() {

		AnchorPane anchorPane = new AnchorPane();

		TilePane tilePane = new TilePane();
		tilePane.setAlignment(Pos.CENTER);

		AnchorPane.setTopAnchor(tilePane, 1.0);
		AnchorPane.setLeftAnchor(tilePane, 1.0);
		AnchorPane.setBottomAnchor(tilePane, 1.0);
		AnchorPane.setRightAnchor(tilePane, 1.0);
		anchorPane.getChildren().add(tilePane);

		/* ==============NRC Button=================== */
		
		Button btnNrc = new Button();
		btnNrc.setContentDisplay(ContentDisplay.CENTER);
		btnNrc.getStyleClass().add("btn");
		btnNrc.setOnAction(e -> {
			
			displayLogin("NRC");
		});

		VBox nrcBox = new VBox();
		nrcBox.setMaxWidth(-1);
		nrcBox.setMaxHeight(-1);
		nrcBox.setPrefHeight(200);
		nrcBox.setPrefWidth(200);
		nrcBox.setAlignment(Pos.CENTER);

		Image imageNrc = new Image("images/nrc.jpg");
		ImageView imgNrc = new ImageView(imageNrc);
		imgNrc.setFitHeight(180);
		imgNrc.setFitWidth(200);
		imgNrc.setPickOnBounds(true);
		VBox.setVgrow(imgNrc, Priority.ALWAYS);

		nrcBox.getChildren().add(imgNrc);

		Label lblNrc = new Label("National Registration Center");
		lblNrc.setMaxHeight(-1);
		lblNrc.setPrefHeight(20);
		lblNrc.getStyleClass().add("dashboard_btn");

		nrcBox.getChildren().add(lblNrc);
		btnNrc.setGraphic(nrcBox);

		/* ==============/NRC Button=================== */

		/* ==============Hospital Button=================== */
		
		Button btnHospital = new Button();
		btnHospital.setContentDisplay(ContentDisplay.CENTER);
		btnHospital.getStyleClass().add("btn");
		btnHospital.setOnAction(e -> {			
			displayLogin("Hospital");
		});

		VBox hospitalBox = new VBox();
		hospitalBox.setMaxWidth(-1);
		hospitalBox.setMaxHeight(-1);
		hospitalBox.setPrefHeight(200);
		hospitalBox.setPrefWidth(200);
		hospitalBox.setAlignment(Pos.CENTER);

		Image imageHospital = new Image("images/hospital.jpg");
		ImageView imgHospital = new ImageView(imageHospital);
		imgHospital.setFitHeight(180);
		imgHospital.setFitWidth(200);
		imgHospital.setPickOnBounds(true);
		VBox.setVgrow(imgHospital, Priority.ALWAYS);

		hospitalBox.getChildren().add(imgHospital);

		Label lblHospital = new Label("Hospital");
		lblHospital.setMaxHeight(-1);
		lblHospital.setPrefHeight(20);
		lblHospital.getStyleClass().add("dashboard_btn");

		hospitalBox.getChildren().add(lblHospital);
		btnHospital.setGraphic(hospitalBox);

		/* ==============/Hospital Button=================== */

		/* ==============Ministry Button=================== */
		
		Button btnMinistry = new Button();
		btnMinistry.setContentDisplay(ContentDisplay.CENTER);
		btnMinistry.getStyleClass().add("btn");
		btnMinistry.setOnAction(e -> {
			displayLogin("Ministry");
		});

		VBox ministryBox = new VBox();
		ministryBox.setMaxWidth(-1);
		ministryBox.setMaxHeight(-1);
		ministryBox.setPrefHeight(200);
		ministryBox.setPrefWidth(200);
		ministryBox.setAlignment(Pos.CENTER);

		Image imageMinistry = new Image("images/Ministry.png");
		ImageView imgMinistry = new ImageView(imageMinistry);
		imgMinistry.setFitHeight(180);
		imgMinistry.setFitWidth(200);
		imgMinistry.setPickOnBounds(true);
		VBox.setVgrow(imgMinistry, Priority.ALWAYS);

		ministryBox.getChildren().add(imgMinistry);

		Label lblMinistry = new Label("Ministry of Health");
		lblMinistry.setMaxHeight(-1);
		lblMinistry.setPrefHeight(20);
		lblMinistry.getStyleClass().add("dashboard_btn");

		ministryBox.getChildren().add(lblMinistry);
		btnMinistry.setGraphic(ministryBox);

		/* ==============/Hospital Button=================== */

		tilePane.getChildren().addAll(btnNrc, btnHospital, btnMinistry);
		TilePane.setMargin(btnNrc, new Insets(10, 10, 10, 10));
		TilePane.setMargin(btnHospital, new Insets(10, 10, 10, 10));
		TilePane.setMargin(btnMinistry, new Insets(10, 10, 10, 10));

		return anchorPane;

	}

	/**
	 * Responsible for populating National Registration Screen
	 * ======================= NRC Screen=================================
	 */
	public AnchorPane NRC() {
		Person personObject = new Person();
		personObject.scanPopulation();

		AnchorPane anchorPane = new AnchorPane();
		/* ==============/Title=================== */
		
		HBox titleBox = new HBox();
		titleBox.setPrefHeight(50);
		titleBox.setMaxHeight(-1);
		titleBox.setAlignment(Pos.CENTER);
		AnchorPane.setLeftAnchor(titleBox, 1.0);
		AnchorPane.setTopAnchor(titleBox, 1.0);
		AnchorPane.setRightAnchor(titleBox, 1.0);

		titleBox.getStyleClass().add("nrc_titleBox");

		Label lblTitle = new Label("National Registration Center");
		lblTitle.getStyleClass().add("nrc_title");

		titleBox.getChildren().add(lblTitle);

		/* ============== / Title=================== */

		/* ============================Center=================================== */
		// GridPane with two columns and Four Rows
		GridPane gridPane = getMainGrid();

		/* =======Add New Button======= */
		Button btnAddNew = new Button("Add New");
		btnAddNew.getStyleClass().add("nrc_btnAddNew");
		btnAddNew.setPrefHeight(35.0);
		GridPane.setMargin(btnAddNew, new Insets(10, 0, 0, 0));
		btnAddNew.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(btnAddNew, 0, 0);

		/* =======Search Button======= */
		
		Button btnSearch = new Button("Search");
		btnSearch.getStyleClass().add("nrc_btnAddNew");
		btnSearch.setPrefHeight(35.0);
		btnSearch.setMaxWidth(Double.MAX_VALUE);
		GridPane.setMargin(btnSearch, new Insets(10, 0, 0, 0));

		gridPane.add(btnSearch, 1, 0);

		/* =======/SearchButton======= */

		/* =======Add New Panel======= */
		
		VBox pnlAddNew = new VBox();
		pnlAddNew.setAlignment(Pos.TOP_CENTER);
		pnlAddNew.getStyleClass().add("nrc_pnlAddNew");
		pnlAddNew.setSpacing(5);
		GridPane.setHgrow(pnlAddNew, Priority.ALWAYS);
		GridPane.setVgrow(pnlAddNew, Priority.ALWAYS);

		GridPane pnlGrid_addNew = pnlGrid_addNew();

		pnlGrid_addNew.add(new Label("NIC"), 0, 0);
		pnlGrid_addNew.add(new Label("First Name"), 0, 1);
		pnlGrid_addNew.add(new Label("Last Name"), 0, 2);
		pnlGrid_addNew.add(new Label("DOB"), 0, 3);
		pnlGrid_addNew.add(new Label("Gender"), 0, 4);
		pnlGrid_addNew.add(new Label("Address"), 0, 5);
		
		/* =============TextFileds============= */
		
		TextField txtNic = new TextField();
		addNumericListener(txtNic);
		TextField txtFirstName = new TextField();
		addListener(txtFirstName);
		TextField txtLastName = new TextField();
		addListener(txtLastName);
		DatePicker datePicker = new DatePicker();
		datePicker.setEditable(false);
	

		// disable future dates
		Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						// Must call super
						super.updateItem(item, empty);

						// Show Weekends in blue color
						DayOfWeek day = DayOfWeek.from(item);
						if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
							this.setTextFill(Color.BLUE);
						}

						// Disable all future date cells
						if (item.isAfter(LocalDate.now())) {
							this.setDisable(true);
						}
					}
				};

			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
		datePicker.setOnAction(e -> {
			datePicker.setStyle("-fx-border-width:0px");
		});
		
		HBox radio_box = new HBox();
		radio_box.setAlignment(Pos.CENTER_LEFT);
		radio_box.setSpacing(5.0);
		radio_box.setPrefWidth(200);
		radio_box.setPrefHeight(100);

		ToggleGroup radioGroup = new ToggleGroup();
		RadioButton btnMale = new RadioButton("Male");
		btnMale.setToggleGroup(radioGroup);
		btnMale.setSelected(true);

		RadioButton btnFemale = new RadioButton("Female");
		btnFemale.setToggleGroup(radioGroup);

		radio_box.getChildren().addAll(btnMale, btnFemale);
		TextField txtAddress = new TextField();
		addListener(txtAddress);

		pnlGrid_addNew.add(txtNic, 1, 0);
		pnlGrid_addNew.add(txtFirstName, 1, 1);
		pnlGrid_addNew.add(txtLastName, 1, 2);
		pnlGrid_addNew.add(datePicker, 1, 3);
		pnlGrid_addNew.add(radio_box, 1, 4);
		pnlGrid_addNew.add(txtAddress, 1, 5);

		pnlAddNew.getChildren().add(pnlGrid_addNew);

		Button btnSave = new Button("Save");
		btnSave.setMaxWidth(200);
		btnSave.setPrefHeight(35);
		btnSave.getStyleClass().add("btn");
		pnlAddNew.getChildren().add(btnSave);
		
		/* ======= /Add New Panel ======= */

		/**
		 * ======= Search Panel =======
		 */

		VBox pnlSearch = new VBox();
		pnlSearch.setAlignment(Pos.TOP_CENTER);
		pnlSearch.getStyleClass().add("nrc_pnlAddNew");
		pnlSearch.setSpacing(5.0);
		GridPane.setHgrow(pnlSearch, Priority.ALWAYS);
		GridPane.setVgrow(pnlSearch, Priority.ALWAYS);
		GridPane.setHalignment(pnlSearch, HPos.CENTER);

		HBox searchBox = new HBox();
		searchBox.setAlignment(Pos.CENTER_LEFT);
		searchBox.setPrefHeight(35);
		searchBox.setSpacing(10);

		TextField txtSearch = new TextField();
		addNumericListener(txtSearch);
		HBox.setHgrow(txtSearch, Priority.ALWAYS);

		searchBox.getChildren().add(new Label("NIC"));
		searchBox.getChildren().add(txtSearch);

		VBox pnlSearchResult = new VBox();
		pnlSearchResult.setAlignment(Pos.TOP_LEFT);

		Button btnSearchRecord = new Button("Search Record");
		btnSearchRecord.getStyleClass().add("btn");
		btnSearchRecord.setOnAction(e -> {
			pnlSearchResult.getChildren().clear();
			String nic = txtSearch.getText();
			if (nic.isEmpty() || nic == "") {
				txtSearch.setStyle("-fx-border-color: red; fx-border-width:2px");
				return;
			} else {
				boolean found = false;
				for (Person person : personObject.getPopulationList()) {
					if (person.getNic().equals(nic)) {
						pnlSearchResult.getChildren().add(new Label("NIC : " + person.getNic()));
						pnlSearchResult.getChildren().add(new Label("First Name : " + person.getFirstName()));
						pnlSearchResult.getChildren().add(new Label("Last Name : " + person.getLastName()));
						pnlSearchResult.getChildren().add(new Label("DOB : " + person.getDateOfBirth()));
						pnlSearchResult.getChildren().add(new Label("Age : " + person.getAge()));
						pnlSearchResult.getChildren().add(new Label("Gender : " + person.getGender()));
						pnlSearchResult.getChildren().add(new Label("Address : " + person.getAddress()));
						found = true;
						break;
					}

				}
				if (!found) {
					pnlSearchResult.getChildren().add(new Label("No Record Found"));
				}

			}

		});
		pnlSearch.getChildren().addAll(searchBox, btnSearchRecord, pnlSearchResult);
		/**
		 * ======= / Search Panel =======
		 **/

		/**
		 * ======= Label Population =======
		 */

		Label population = new Label("Total Population");
		GridPane.setHalignment(population, HPos.RIGHT);
		Label lblPopulation = new Label("");
		lblPopulation.setText("" + personObject.getPopulationList().size());

		population.getStyleClass().add("nrc_lblPopulation");
		lblPopulation.getStyleClass().add("nrc_lblPopulation");

		/**
		 * ======= Back Button =======
		 */

		Button btnBack = new Button("Back");
		btnBack.setPrefHeight(35);
		btnBack.setMaxWidth(Double.MAX_VALUE);
		btnBack.getStyleClass().add("nrc_btnAddNew");

		ImageView icon = new ImageView(new Image("icons/back.png"));
		icon.setFitHeight(35);
		icon.setFitWidth(35);
		icon.setPickOnBounds(true);
		icon.setPreserveRatio(true);
		btnBack.setGraphic(icon);

		/* ============================/Center=================================== */

		btnAddNew.setOnAction(e -> {
			pnlAddNew.setDisable(false);
			pnlSearch.setDisable(true);
		});

		btnSearch.setOnAction(e -> {

			pnlAddNew.setDisable(true);
			pnlSearch.setDisable(false);
		});
		btnSave.setOnAction(e -> {
			if (validate(txtNic, txtFirstName, txtLastName, datePicker, radioGroup, txtAddress)) {

				Person person = new Person(txtNic.getText(), txtFirstName.getText(), txtLastName.getText(),
						datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
						((RadioButton) radioGroup.getSelectedToggle()).getText(), txtAddress.getText());
				personObject.insertPerson(person);
				lblPopulation.setText("" + personObject.getPopulationList().size());
				clear(txtNic);
				clear(txtFirstName);
				clear(txtLastName);
				clear(datePicker);
				btnMale.setSelected(true);
				clear(txtAddress);
			}
		});

		btnBack.setOnAction(e -> {
			displayDashboard();

		});

		gridPane.add(pnlAddNew, 0, 1);
		pnlAddNew.setDisable(true);
		gridPane.add(pnlSearch, 1, 1);
		pnlSearch.setDisable(true);
		gridPane.add(population, 0, 2);
		gridPane.add(lblPopulation, 1, 2);
		gridPane.add(btnBack, 0, 3);
		anchorPane.getChildren().addAll(titleBox, gridPane);
		return anchorPane;

	}

	/**
	 * Returns the main grid for NRC Screen
	 */
	public GridPane getMainGrid() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setLayoutY(51);
		AnchorPane.setLeftAnchor(gridPane, 1.0);
		AnchorPane.setTopAnchor(gridPane, 51.0);
		AnchorPane.setRightAnchor(gridPane, 1.0);
		AnchorPane.setBottomAnchor(gridPane, 1.0);

		ColumnConstraints col0 = new ColumnConstraints();
		col0.setHgrow(Priority.ALWAYS);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);

		gridPane.getColumnConstraints().addAll(col0, col1);

		RowConstraints row0 = new RowConstraints();
		row0.setVgrow(Priority.NEVER);
		RowConstraints row1 = new RowConstraints();
		row1.setVgrow(Priority.ALWAYS);
		RowConstraints row2 = new RowConstraints();
		row2.setValignment(VPos.BOTTOM);
		row2.setVgrow(Priority.ALWAYS);
		RowConstraints row3 = new RowConstraints();
		row3.setVgrow(Priority.NEVER);
		row3.setMaxHeight(35);

		gridPane.getRowConstraints().addAll(row0, row1, row2, row3);
		return gridPane;

	}

	/**
	 * Returns the gridpane for addNew Panel in NRC Screen
	 */
	public GridPane pnlGrid_addNew() {
		GridPane pnlGrid_addNew = new GridPane();
		pnlGrid_addNew.setHgap(5.0);
		pnlGrid_addNew.setVgap(5.0);

		ColumnConstraints pnlGrid_addNew_col0 = new ColumnConstraints();
		pnlGrid_addNew_col0.setHalignment(HPos.RIGHT);
		pnlGrid_addNew_col0.setMinWidth(10.0);
		pnlGrid_addNew_col0.setMaxWidth(100.0);

		ColumnConstraints pnlGrid_addNew_col1 = new ColumnConstraints();
		pnlGrid_addNew_col1.setHgrow(Priority.ALWAYS);

		pnlGrid_addNew.getColumnConstraints().addAll(pnlGrid_addNew_col0, pnlGrid_addNew_col1);

		RowConstraints pnlGrid_addNew_row0 = new RowConstraints();
		pnlGrid_addNew_row0.setPrefHeight(30);
		RowConstraints pnlGrid_addNew_row1 = new RowConstraints();
		pnlGrid_addNew_row1.setPrefHeight(30);
		RowConstraints pnlGrid_addNew_row2 = new RowConstraints();
		pnlGrid_addNew_row2.setPrefHeight(30);
		RowConstraints pnlGrid_addNew_row3 = new RowConstraints();
		pnlGrid_addNew_row3.setPrefHeight(30);
		RowConstraints pnlGrid_addNew_row4 = new RowConstraints();
		pnlGrid_addNew_row4.setPrefHeight(30);
		RowConstraints pnlGrid_addNew_row5 = new RowConstraints();
		pnlGrid_addNew_row5.setPrefHeight(30);

		pnlGrid_addNew.getRowConstraints().addAll(pnlGrid_addNew_row0, pnlGrid_addNew_row1, pnlGrid_addNew_row2,
				pnlGrid_addNew_row3, pnlGrid_addNew_row4, pnlGrid_addNew_row5);
		return pnlGrid_addNew;

	}

	/**
	 * Validate the TextFields of NRC Screen if anyone empty the highlight with red
	 * color border
	 */
	public boolean validate(TextField txtNIC, TextField txtFirstName, TextField txtLastName, DatePicker datePicker,
			ToggleGroup group, TextField txtAddress) {
		boolean returnValue = true;
		String nic = txtNIC.getText();
		String firstName = txtFirstName.getText();
		String lastName = txtLastName.getText();
		String dateOfBirth;
		if (datePicker.getValue() == null) {
			dateOfBirth = null;
		} else {
			dateOfBirth = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		String address = txtAddress.getText();

		if (nic.isEmpty() || nic == null || nic == "") {
			txtNIC.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (firstName.isEmpty() || firstName == null || firstName == "") {
			txtFirstName.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (lastName.isEmpty() || lastName == null || lastName == "") {
			txtLastName.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (dateOfBirth == null || dateOfBirth.isEmpty() || dateOfBirth == "") {
			datePicker.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (address.isEmpty() || address == null || address == "") {
			txtAddress.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		}

		return returnValue;
	}

	/**
	 * ======================= End of NRC Screen=================================
	 */

	/**
	 * ======================= Start of Hospital Screen=================================
	 */
	
	/**
	 * Responsible for populating Hospital Screen
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BorderPane Hospital() {
		Hospital hospitalObject = new Hospital();
		hospitalObject.scanPopulation();
		BorderPane borderPane = new BorderPane();
	
		/**
		 * ======================= sideBar=================================
		 */
		
		VBox sideBar = new VBox();
		BorderPane.setAlignment(sideBar, Pos.CENTER);
		BorderPane.setMargin(sideBar, new Insets(10, 10, 10, 10));

		GridPane sideBar_grid = get_sideBarGrid();
		sideBar_grid.add(new Label("NIC"), 0, 0);
		sideBar_grid.add(new Label("Patient Name"), 0, 1);
		sideBar_grid.add(new Label("Age"), 0, 2);
		sideBar_grid.add(new Label("Doctor Name"), 0, 3);
		sideBar_grid.add(new Label("Diagnosis Date"), 0, 4);
		sideBar_grid.add(new Label("Department"), 0, 5);
		sideBar_grid.add(new Label("Patient Diagnosis"), 0, 6);
		sideBar_grid.add(new Label("Treatment"), 0, 7);
		sideBar_grid.add(new Label("Patient Type:"), 0, 8);
		sideBar_grid.add(new Label("Total Beds:"), 0, 9);
		sideBar_grid.add(new Label("Free Beds:"), 0, 10);

		TextField txtNIC = new TextField();
		addNumericListener(txtNIC);

		Button btnSearch = new Button("Search");
		btnSearch.getStyleClass().add("hos_btn");

		TextField txtPatientName = new TextField();
		addListener(txtPatientName);
		GridPane.setColumnSpan(txtPatientName, 2);

		TextField txtAge = new TextField();
		addNumericListener(txtAge);
		GridPane.setColumnSpan(txtAge, 2);

		TextField txtDoctorName = new TextField();
		addListener(txtDoctorName);
		GridPane.setColumnSpan(txtDoctorName, 2);

		DatePicker datePicker = new DatePicker();
		datePicker.setEditable(false);
		datePicker.setOnAction(event -> {
			datePicker.setStyle("-fx-border-width:0px");
		});
		GridPane.setColumnSpan(datePicker, 2);

		ComboBox<Department> cmbDepartment = new ComboBox<>();//change
		GridPane.setColumnSpan(cmbDepartment, 2);
		cmbDepartment.setPromptText("---Select---");
		cmbDepartment.setMaxWidth(Double.MAX_VALUE);
		cmbDepartment.getItems().addAll(hospitalObject.getDepartments());
		cmbDepartment.setConverter(new DepartmentConverter());
		cmbDepartment.getSelectionModel().select(0);
		cmbDepartment.valueProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
			if (newValue != null) {
			
				
				cmbDepartment.setStyle("-fx-border-width:0px");
			}

		});

		TextField txtDiagnosis = new TextField();
		addListener(txtDiagnosis);
		GridPane.setColumnSpan(txtDiagnosis, 2);

		TextField txtTreatment = new TextField();
		addListener(txtTreatment);
		GridPane.setColumnSpan(txtTreatment, 2);

		HBox radio_box = new HBox();
		radio_box.setAlignment(Pos.CENTER_LEFT);
		radio_box.setSpacing(5.0);
		radio_box.setPrefWidth(200);
		radio_box.setPrefHeight(100);

		ToggleGroup radioGroup = new ToggleGroup();
		RadioButton btnWalkIn = new RadioButton("walk in");
		btnWalkIn.setToggleGroup(radioGroup);
		btnWalkIn.setSelected(true);

		RadioButton btnAdmit = new RadioButton("admit");
		btnAdmit.setToggleGroup(radioGroup);

		radio_box.getChildren().addAll(btnWalkIn, btnAdmit);

		Label lblFreeBeds = new Label();
		lblFreeBeds.setText("" + hospitalObject.getFreeBeds());

		Button btnMakeAppointment = new Button("Make an Appointement");
		btnMakeAppointment.getStyleClass().add("hos_btn");

		Button btnClear = new Button("Clear");
		btnClear.getStyleClass().add("hos_btn");
		GridPane.setHgrow(btnClear, Priority.ALWAYS);

		Button btnDelete = new Button("Delete");
		btnDelete.getStyleClass().add("hos_btn");
		GridPane.setHgrow(btnDelete, Priority.ALWAYS);

		Label lblStatus = new Label();
		GridPane.setColumnSpan(lblStatus, 2);
		GridPane.setHgrow(lblStatus, Priority.ALWAYS);

		sideBar_grid.add(txtNIC, 1, 0);
		sideBar_grid.add(btnSearch, 2, 0);
		sideBar_grid.add(txtPatientName, 1, 1);
		sideBar_grid.add(txtAge, 1, 2);
		sideBar_grid.add(txtDoctorName, 1, 3);
		sideBar_grid.add(datePicker, 1, 4);
		sideBar_grid.add(cmbDepartment, 1, 5);
		sideBar_grid.add(txtDiagnosis, 1, 6);
		sideBar_grid.add(txtTreatment, 1, 7);
		sideBar_grid.add(radio_box, 1, 8);
		sideBar_grid.add(new Label("" + hospitalObject.getTotalBeds()), 1, 9);
		sideBar_grid.add(lblFreeBeds, 1, 10);
		sideBar_grid.add(btnMakeAppointment, 1, 11);
		sideBar_grid.add(btnClear, 0, 11);

		sideBar_grid.add(btnDelete, 1, 12);
		sideBar_grid.add(lblStatus, 1, 13);

		/**
		 * ======= Back Button =======
		 */
		
		HBox box = new HBox();
		box.setAlignment(Pos.BOTTOM_CENTER);
		box.setPrefHeight(100);
		box.setPrefWidth(200);

		Button btnBack = new Button("Back");
		btnBack.setPrefHeight(35);
		btnBack.setMaxWidth(Double.MAX_VALUE);
		btnBack.getStyleClass().add("nrc_btnAddNew");
		HBox.setHgrow(btnBack, Priority.ALWAYS);

		ImageView icon = new ImageView(new Image("icons/back.png"));
		icon.setFitHeight(35);
		icon.setFitWidth(35);
		icon.setPickOnBounds(true);
		icon.setPreserveRatio(true);
		btnBack.setGraphic(icon);

		box.getChildren().add(btnBack);

		sideBar.getChildren().addAll(sideBar_grid, box);
		
		/**
		 * ======================= /sideBar=================================
		 */
		
		/**
		 * ======================= Center=================================
		 */
		
		TableView<Hospital> tableView = new TableView();
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		createTable(tableView);
		populateRecord(tableView, hospitalObject, lblFreeBeds);
		
		/*
		 * adding listener to tableView so that when user click on any row than populate
		 * the sidebar textFilds with selected Object
		 **/
		
		tableView.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener) (observable, oldValue, newValue) -> {
					if (tableView.getSelectionModel().getSelectedItem() != null) {
						Hospital patientRecord = tableView.getSelectionModel().getSelectedItem();
						txtNIC.setText("" + patientRecord.getNic());
						txtPatientName.setText(patientRecord.getPatientName());
						txtAge.setText("" + patientRecord.getAge());
						txtDoctorName.setText(patientRecord.getDoctorName());
						DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
						LocalDate localDate = LocalDate.parse(patientRecord.getDiagnosis_date(), df);

						datePicker.setValue(localDate);
						for (Department dep : hospitalObject.getDepartments()) {
							if (dep.getName().equals(patientRecord.getDepartment())) {
								cmbDepartment.getSelectionModel().select(dep);
								break;
							}
						}
						txtDiagnosis.setText(patientRecord.getDiagnosis_description());
						txtTreatment.setText(patientRecord.getTreatment_description());
						if (patientRecord.getPatient_type().equals("walk in")) {
							radioGroup.selectToggle(btnWalkIn);
						} else if (patientRecord.getPatient_type().equals("admit")) {
							radioGroup.selectToggle(btnAdmit);
						}

						lblStatus.setText("");

					}

				});

		/**
		 * ======================= /Center=================================
		 */

		btnSearch.setOnAction(e -> {
			String id = txtNIC.getText();
			if (id.isEmpty() || id == null) {
				lblStatus.setText("National ID of Patient is required");
			} else {

				if (!search(id, hospitalObject, txtPatientName, txtAge)) {
					lblStatus.setText("No Record Found");
				} else {
					lblStatus.setText("");
				}

			}
		});
		btnMakeAppointment.setOnAction(e -> {
			String patient_type = ((RadioButton) radioGroup.getSelectedToggle()).getText();
			if (patient_type.equals("admit")) {
				if (hospitalObject.getFreeBeds() == 0) {
					lblStatus.setText("No Bed Available  ");
					return;
				}
			}

			if (validatePatientRecord(txtNIC, txtPatientName, txtAge, txtDoctorName, datePicker, cmbDepartment,
					txtDiagnosis, txtTreatment)) {
				String nic = txtNIC.getText();
				String patientName = txtPatientName.getText();
				String age = txtAge.getText();
				String doctorName = txtDoctorName.getText();
				String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				String department = cmbDepartment.getSelectionModel().getSelectedItem().getName();
				String diagnosis = txtDiagnosis.getText();
				String treatment = txtTreatment.getText();
				// String patient_type=((RadioButton) radioGroup.getSelectedToggle()).getText();
				Hospital currentPatient = new Hospital(nic, patientName, Integer.parseInt(age), doctorName, date,
						department, diagnosis, treatment, patient_type);
				// saveThe Patient record
				hospitalObject.insertPatient(currentPatient);
				// maintain the record of new patients in another arrayList so that we can
				// upload this record to database using new thread
				
				populateRecord(tableView, hospitalObject, lblFreeBeds);
				tableView.refresh();
				clear(txtNIC);
				clear(txtPatientName);
				clear(txtAge);
				clear(txtDoctorName);
				clear(datePicker);
				clear(cmbDepartment);
				clear(txtDiagnosis);
				clear(txtTreatment);
				radioGroup.selectToggle(btnWalkIn);
				lblStatus.setText("");
			}
		});
		btnClear.setOnAction(e -> {
			clear(txtNIC);
			clear(txtPatientName);
			clear(txtAge);
			clear(txtDoctorName);
			clear(datePicker);
			clear(cmbDepartment);
			clear(txtDiagnosis);
			clear(txtTreatment);
			radioGroup.selectToggle(btnWalkIn);
			lblStatus.setText("");

		});
		btnBack.setOnAction(e -> {
			displayDashboard();
		});

		btnDelete.setOnAction(e -> {
			ObservableList<Hospital> selectedRow;
			selectedRow = tableView.getSelectionModel().getSelectedItems();
			if (selectedRow.size() > 0) {
				hospitalObject.deletePatient(selectedRow.get(0));
			
				populateRecord(tableView, hospitalObject, lblFreeBeds);
				tableView.refresh();
				clear(txtNIC);
				clear(txtPatientName);
				clear(txtAge);
				clear(txtDoctorName);
				clear(datePicker);
				clear(cmbDepartment);
				clear(txtDiagnosis);
				clear(txtTreatment);
				radioGroup.selectToggle(btnWalkIn);
				lblStatus.setText("");
			}

		});

		borderPane.setLeft(sideBar);
		borderPane.setCenter(tableView);
		return borderPane;
	}

	/**
	 * Returns grid for sideBarGrid in hospital Screen
	 */
	public GridPane get_sideBarGrid() {
		GridPane sideBar_grid = new GridPane();
		sideBar_grid.setVgap(10);
		sideBar_grid.setHgap(10);
		sideBar_grid.setAlignment(Pos.TOP_CENTER);

		ColumnConstraints col0 = new ColumnConstraints();
		col0.setHalignment(HPos.RIGHT);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setHgrow(Priority.ALWAYS);

		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHalignment(HPos.CENTER);

		sideBar_grid.getColumnConstraints().addAll(col0, col1, col2);

		RowConstraints row0 = new RowConstraints();
		row0.setPrefHeight(30);

		RowConstraints row1 = new RowConstraints();
		row1.setPrefHeight(30);

		RowConstraints row2 = new RowConstraints();
		row2.setPrefHeight(30);

		RowConstraints row3 = new RowConstraints();
		row3.setPrefHeight(30);

		RowConstraints row4 = new RowConstraints();
		row4.setPrefHeight(30);

		RowConstraints row5 = new RowConstraints();
		row5.setPrefHeight(30);

		RowConstraints row6 = new RowConstraints();
		row6.setPrefHeight(30);

		RowConstraints row7 = new RowConstraints();
		row7.setPrefHeight(30);

		RowConstraints row8 = new RowConstraints();
		row8.setPrefHeight(30);

		RowConstraints row9 = new RowConstraints();
		row9.setPrefHeight(30);

		RowConstraints row10 = new RowConstraints();
		row9.setPrefHeight(30);

		RowConstraints row11 = new RowConstraints();
		row9.setPrefHeight(30);
		RowConstraints row12 = new RowConstraints();
		row9.setPrefHeight(30);

		sideBar_grid.getRowConstraints().addAll(row0, row1, row2, row3, row4, row5, row6, row7, row8, row9, row10,
				row11, row12);
		return sideBar_grid;

	}

	/**
	 * Create Table for displaying data in tableView in hospital screen
	 */
	@SuppressWarnings("unchecked")
	public void createTable(TableView<Hospital> tableView) {
		TableColumn<Hospital, String> nid = new TableColumn<>("NIC");
		nid.setCellValueFactory(new PropertyValueFactory<>("nic"));
		nid.setPrefWidth(100);

		TableColumn<Hospital, String> patientName = new TableColumn<>("Patient Name");
		patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
		patientName.setPrefWidth(200);

		TableColumn<Hospital, Integer> age = new TableColumn<>("Age");
		age.setCellValueFactory(new PropertyValueFactory<>("age"));
		age.setPrefWidth(20);

		TableColumn<Hospital, String> doctorName = new TableColumn<>("Doctor Name");
		doctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
		doctorName.setPrefWidth(200);

		TableColumn<Hospital, String> diagnosis_date = new TableColumn<>("Date");
		diagnosis_date.setCellValueFactory(new PropertyValueFactory<>("diagnosis_date"));
		diagnosis_date.setPrefWidth(100);

		TableColumn<Hospital, String> department = new TableColumn<>("Department");
		department.setCellValueFactory(new PropertyValueFactory<>("department"));
		department.setPrefWidth(100);

		TableColumn<Hospital, String> diagnosis_description = new TableColumn<>("Diagnosis");
		diagnosis_description.setCellValueFactory(new PropertyValueFactory<>("diagnosis_description"));

		TableColumn<Hospital, String> treatment_description = new TableColumn<>("Treatment");
		treatment_description.setCellValueFactory(new PropertyValueFactory<>("treatment_description"));

		TableColumn<Hospital, String> patient_type = new TableColumn<>("Type");
		patient_type.setCellValueFactory(new PropertyValueFactory<>("patient_type"));

		

		tableView.getColumns().setAll(nid, patientName, age, doctorName, diagnosis_date, department,
				diagnosis_description, treatment_description, patient_type);

	}

	/**
	 * populate the data in table
	 */
	public void populateRecord(TableView<Hospital> tableView, Hospital hospitalObject, Label lblFreeBeds) {

		ObservableList<Hospital> observableList = FXCollections.observableArrayList();
		tableView.getItems().clear();
		observableList.clear();
		int occupiedBeds = 0;
		for (Hospital hos : hospitalObject.getPatientsList()) {
			observableList.add(hos);

			if (hos.getPatient_type().equals("admit")) {
				occupiedBeds++;
			}
		}
		hospitalObject.setFreeBeds(hospitalObject.getTotalBeds() - occupiedBeds);
		lblFreeBeds.setText("" + hospitalObject.getFreeBeds());
		tableView.setItems(observableList);

	}

	/**
	 * Validate data of patient if anyone is empty the higlight with red color
	 * border
	 */
	public boolean validatePatientRecord(TextField txtNIC, TextField txtPatientName, TextField txtAge,
			TextField txtDoctorName, DatePicker datePicker, ComboBox<Department> cmbDepartment, TextField txtDiagnosis,
			TextField txtTreatment) {

		boolean returnValue = true;
		String nic = txtNIC.getText();
		String patientName = txtPatientName.getText();
		String age = txtAge.getText();
		String doctorName = txtDoctorName.getText();
		String date;
		if (datePicker.getValue() == null) {
			date = null;
		} else {
			date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		}

		String department = cmbDepartment.getSelectionModel().getSelectedItem().getName();
		String diagnosis = txtDiagnosis.getText();
		String treatment = txtTreatment.getText();

		if (nic.isEmpty() || nic == null || nic == "") {
			txtNIC.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (patientName.isEmpty() || patientName == null || patientName == "") {
			txtPatientName.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (age.isEmpty() || age == null || age == "") {
			txtAge.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (doctorName.isEmpty() || doctorName == null || doctorName == "") {
			txtDoctorName.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (date == null || date.isEmpty() || date == "") {
			datePicker.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (department.isEmpty() || department == null || department == "") {
			cmbDepartment.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (diagnosis.isEmpty() || diagnosis == null || diagnosis == "") {
			txtDiagnosis.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		} else if (treatment.isEmpty() || treatment == null || treatment == "") {
			txtTreatment.setStyle("-fx-border-color: red; fx-border-width:2px");
			returnValue = false;
		}
		return returnValue;
	}

	/**
	 * will call when user clicks on search Button in Hospital Screen validate if
	 * Person data available in Population List if found than populate the textField
	 * PatientName and age
	 */
	public boolean search(String _id, Hospital hospitalObject, TextField txtPatientName, TextField txtAge) {
		boolean returnValue = false;
		// calling the parent classMethod
		for (Person person : hospitalObject.getPopulationList()) {
			if (person.getNic().equals(_id)) {
				txtPatientName.setText(person.getFirstName() + " " + person.getLastName());
				txtAge.setText("" + person.getAge());
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}

	/**
	 * ======================= End of Hospital Screen=================================
	 */

	/**
	 * ======================= Start of Ministry Screen=================================
	 */
	/**
	 * Responsible for poplating Ministry screen
	 */
	public AnchorPane Ministry() {
		Ministry ministryObject = new Ministry();
		AnchorPane anchorPane = new AnchorPane();
		/**
		 * Sidebar
		 */
		VBox sideBar = new VBox();
		sideBar.setPrefSize(250, Double.MAX_VALUE);
		sideBar.setSpacing(10);
		sideBar.setAlignment(Pos.CENTER);
		sideBar.getStyleClass().add("ministry_sidebar");
		AnchorPane.setLeftAnchor(sideBar, 1.0);
		AnchorPane.setTopAnchor(sideBar, 1.0);
		AnchorPane.setBottomAnchor(sideBar, 1.0);

		Button btnStatistics = new Button("Statistics");
		btnStatistics.setPrefHeight(35);
		btnStatistics.setMaxWidth(Double.MAX_VALUE);
		btnStatistics.getStyleClass().add("nrc_btnAddNew");
		btnStatistics.setAlignment(Pos.CENTER);
		btnStatistics.setDefaultButton(false);

		ImageView statisticsIcon = new ImageView(new Image("icons/stats.png"));
		statisticsIcon.setFitHeight(35);
		statisticsIcon.setFitWidth(35);
		statisticsIcon.setPickOnBounds(true);
		statisticsIcon.setPreserveRatio(true);
		btnStatistics.setGraphic(statisticsIcon);

		Button btnBack = new Button("Back");
		btnBack.setPrefHeight(35);
		btnBack.setMaxWidth(Double.MAX_VALUE);
		btnBack.getStyleClass().add("nrc_btnAddNew");
		HBox.setHgrow(btnBack, Priority.ALWAYS);

		ImageView icon = new ImageView(new Image("icons/back.png"));
		icon.setFitHeight(35);
		icon.setFitWidth(35);
		icon.setPickOnBounds(true);
		icon.setPreserveRatio(true);
		btnBack.setGraphic(icon);

		sideBar.getChildren().addAll(btnStatistics, btnBack);
		/**
		 * Center
		 */
		BorderPane pnlStatistics = new BorderPane();
		AnchorPane.setLeftAnchor(pnlStatistics, 250.0);
		AnchorPane.setTopAnchor(pnlStatistics, 1.0);
		AnchorPane.setRightAnchor(pnlStatistics, 1.0);
		AnchorPane.setBottomAnchor(pnlStatistics, 1.0);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
		barChart.setTitle("Patients");
		xAxis.setLabel("Departments");
		yAxis.setLabel("Patients");

		btnStatistics.setOnAction(e -> {
			viewStatistics(pnlStatistics, barChart, ministryObject);
			btnStatistics.setDisable(true);
		});
		btnBack.setOnAction(e -> {
			displayDashboard();
		});
		

		anchorPane.getChildren().addAll(sideBar, pnlStatistics);
		return anchorPane;
	}

	/**
	 * will call when statistics button clicked on Ministry Screen
	 */
	public void viewStatistics(BorderPane pnlStatistics, BarChart<String, Number> barChart, Ministry ministryObject) {
		int emergency = 0;
		int interal_medicine = 0;
		int physiotherapy = 0;
		int neurology = 0;
		int cardiology = 0;
		int ent = 0;
		int orthopedic = 0;
		int dental = 0;
		int dermatology = 0;
		int ophalmology = 0;
		barChart.getData().clear();
		for (Hospital hospitalPatient : ministryObject.getPatientsList()) {
			if (hospitalPatient.getDepartment().equals("Emergency")) {
				emergency++;

			} else if (hospitalPatient.getDepartment().equals("Internal Medicine")) {
				interal_medicine++;
			} else if (hospitalPatient.getDepartment().equals("Physiotherapy")) {
				physiotherapy++;
			} else if (hospitalPatient.getDepartment().equals("Neurology")) {
				neurology++;
			} else if (hospitalPatient.getDepartment().equals("Cardiology")) {
				cardiology++;
			} else if (hospitalPatient.getDepartment().equals("ENT")) {
				ent++;
			} else if (hospitalPatient.getDepartment().equals("Orthopedic")) {
				orthopedic++;
			} else if (hospitalPatient.getDepartment().equals("Dental")) {
				dental++;
			} else if (hospitalPatient.getDepartment().equals("Dermatology")) {
				dermatology++;
			} else if (hospitalPatient.getDepartment().equals("Ophthalmology")) {
				ophalmology++;
			}

		}

		XYChart.Series<String, Number> series = new XYChart.Series<>();

		series.getData().add(new XYChart.Data<>("Emergency", emergency));
		series.getData().add(new XYChart.Data<>("Internal Medicine", interal_medicine));
		series.getData().add(new XYChart.Data<>("Physiotherapy", physiotherapy));
		series.getData().add(new XYChart.Data<>("Neurology", neurology));
		series.getData().add(new XYChart.Data<>("Cardiology", cardiology));
		series.getData().add(new XYChart.Data<>("ENT", ent));
		series.getData().add(new XYChart.Data<>("Orthopedic", orthopedic));
		series.getData().add(new XYChart.Data<>("Dental", dental));
		series.getData().add(new XYChart.Data<>("Dermatology", dermatology));
		series.getData().add(new XYChart.Data<>("Ophthalmology", ophalmology));
		
		barChart.getData().add(series);
		pnlStatistics.getChildren().clear();
		pnlStatistics.setCenter(barChart);

	}
	
public void checkStatus() {
		Ministry ministryObject = new Ministry();
		int max=3;
		int emergency = 0;
		int interal_medicine = 0;
		int physiotherapy = 0;
		int neurology = 0;
		int cardiology = 0;
		int ent = 0;
		int orthopedic = 0;
		int dental = 0;
		int dermatology = 0;
		int ophalmology = 0;

		for (Hospital hospitalPatient : ministryObject.getPatientsList()) {
			if (hospitalPatient.getDepartment().equals("Emergency")) {
				emergency++;

			} else if (hospitalPatient.getDepartment().equals("Internal Medicine")) {
				interal_medicine++;
			} else if (hospitalPatient.getDepartment().equals("Physiotherapy")) {
				physiotherapy++;
			} else if (hospitalPatient.getDepartment().equals("Neurology")) {
				neurology++;
			} else if (hospitalPatient.getDepartment().equals("Cardiology")) {
				cardiology++;
			} else if (hospitalPatient.getDepartment().equals("ENT")) {
				ent++;
			} else if (hospitalPatient.getDepartment().equals("Orthopedic")) {
				orthopedic++;
			} else if (hospitalPatient.getDepartment().equals("Dental")) {
				dental++;
			} else if (hospitalPatient.getDepartment().equals("Dermatology")) {
				dermatology++;
			} else if (hospitalPatient.getDepartment().equals("Ophthalmology")) {
				ophalmology++;
			}

		}
		String departments="";
		if(emergency>=max) {
			departments=departments+"-Emergency\n";
		}
		if(interal_medicine>=max) {
			departments=departments+"-Internal Medicine\n";
		}
		
		if(physiotherapy>=max) {
			departments=departments+"-Physiotherapy\n";
		}
		else if(neurology>=max) {
			departments=departments+"-Neurology\n";
		
		}
		 if(cardiology>=max) {
			 departments=departments+"-Cardiology\n";
	
		}
		 if(ent>=max) {
			 departments=departments+"-ENT\n";
			
		}
		 if(orthopedic>=max) {
			 departments=departments+"-Orthopedic\n";
			
		}
		 if(dental>=max) {
			 departments=departments+"-Dental\n";
			
		}
		 if(dermatology>=max) {
			 departments=departments+"-Dermatology\n";
		}
		 if(ophalmology>=max) {
			 departments=departments+"-Ophthalmology\n";
		
		}
		if(!departments.isEmpty() && departments!="") {
			departments="Following Departments has reached to maximum Patitents.\n"+departments;
			Alert alert=new Alert(AlertType.WARNING);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setResizable(true);
			alert.setContentText(departments);
			alert.show();
		}
	}
	/**
	 * ======================= End of Ministry
	 * Screen=================================
	 */

	/**
	 * Generic textField Listener which changes the textfield border to default its
	 * basically used to change textField border from red to default
	 */
	public void addListener(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			textField.setStyle("-fx-border-width:0px");
		});

	}

	/**
	 * This listener is for textFields which only allows to get Numeric input
	 * TextField for NIC and age implements this listener Because NIC and age field
	 * should only contains numeric values
	 */
	public void addNumericListener(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			textField.setStyle("-fx-border-width:0px");
			if (!newValue.matches("\\d*")) {
				textField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
	}

	/**
	 * Used to clear the text of node (TextFields,DatePicker,ComboBox)
	 */
	@SuppressWarnings("rawtypes")
	public void clear(Node node) {
		if (node instanceof TextField) {
			((TextField) node).setText("");
			;
		} else if (node instanceof DatePicker) {
			((DatePicker) node).setValue(null);
		} else if (node instanceof ComboBox) {
		
			
			((ComboBox) node).getSelectionModel().select(0);
		}

	}

	/**
	 * this class extends converter class which is used to convert the data of
	 * Department Object to String to populate Departments ComboBox
	 */
	public class DepartmentConverter extends StringConverter<Department> {

		@Override
		public String toString(Department object) {
			if (object != null) {
				return object.getName();
			} else {
				return "";
			}

		}

		@Override
		public Department fromString(String string) {
			
			return null;

		}
	}
}
