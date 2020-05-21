package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class FinalTest extends Application {
	private PreparedStatement preparedStatement;
	private String queryString;
	private static TextField tfEmpId = new TextField();
	private static TextField tfEmpName = new TextField();
	private static TextField tfEmpGrSal = new TextField();
	private static TextField tfEmpNetSal = new TextField();
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane pane = new GridPane();
			pane.setAlignment(Pos.CENTER);
			pane.setPadding(new Insets(11.5,12.5,13.5,14.5));
			pane.setHgap(5.5);
			pane.setVgap(5.5);
			
			// Create labels and Text Fields in the Pane
			pane.add(new Label("Employee Id: "),0,0);
			pane.add(tfEmpId,1,0);
			pane.add(new Label("Employee Name: "),0,1);
			tfEmpName.setEditable(false);
			pane.add(tfEmpName,1,1);
			pane.add(new Label("Gross Salary: "),0,2);
			tfEmpGrSal.setEditable(false);
			pane.add(tfEmpGrSal,1,2);
			pane.add(new Label("Net Salary: "),0,3);
			tfEmpNetSal.setEditable(false);
			pane.add(tfEmpNetSal,1,3);
			//Create Buttons in the Pane
			Button btnDisplayRecord = new Button("Display Info");
			Button btnClear = new Button("Clear");
			Button btnDelete = new Button("Delete");
			Button btnExit = new Button("Exit");
			//set actions to buttons
			btnDisplayRecord.setOnAction(e -> DisplayInfo());
			btnClear.setOnAction(e -> ClearFields());
			btnDelete.setOnAction(e -> DeleteEmp());
			btnExit.setOnAction(e -> Platform.exit());
			//Insert Buttons to Pane
			pane.add(btnDisplayRecord, 3, 4);
			pane.add(btnClear, 2, 4);
			pane.add(btnDelete, 1, 4);
			pane.add(btnExit, 0, 4);
			
			Scene scene = new Scene(pane);
			primaryStage.setTitle("Final Test - Employees");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void ClearFields() {
		tfEmpId.setText("");
		tfEmpName.setText("");
		tfEmpGrSal.setText("");
		tfEmpNetSal.setText("");
	}
	public void DisplayInfo() {
		try {
			queryString = "select empId, empName, empGrSal from EMPLOYEE where empId = ?";
			//load jdbc driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver loaded.");
			//establish a connection
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","finaljava", "password");
			System.out.println("Database connected.");
			//create a statement
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, tfEmpId.getText());
			ResultSet rset = preparedStatement.executeQuery();
			if(rset.next())
			{
				int empId = rset.getInt(1);
				String empName = rset.getString(2);
				double empGrSal = rset.getDouble(3);
				double empNetSal = empGrSal - (100*3);
				//display result in a label
				tfEmpId.setText(Integer.toString(empId));
				tfEmpName.setText(empName);
				tfEmpGrSal.setText(Double.toString(empGrSal));
				tfEmpNetSal.setText(Double.toString(empNetSal));
			}
			else
			{
				tfEmpName.setText("Not Found");
				tfEmpGrSal.setText("Not Found");
				tfEmpNetSal.setText("Not Found");
			}	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	public void DeleteEmp() {
		try {
			queryString = "delete from EMPLOYEE where empId = ?";
			//load jdbc driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver loaded.");
			//establish a connection
			Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","finaljava", "password");
			System.out.println("Database connected.");
			//create a statement
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, tfEmpId.getText());
			ResultSet rset = preparedStatement.executeQuery();
			JOptionPane.showMessageDialog(null, "Record deleted");
			ClearFields();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		launch(args);
	}
}
