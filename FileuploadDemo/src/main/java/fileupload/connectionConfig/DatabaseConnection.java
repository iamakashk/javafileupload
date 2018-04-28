package fileupload.connectionConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

	public Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Oracle driver not found ");
			e.printStackTrace();
			return null; 
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_upload","root","root");
		} catch (Exception e) {
			System.out.println("CONNECTION FAILED PLEASE CHECK THE CONSOLE");
			e.printStackTrace();
			return null;
		}
		if(connection != null){
			System.out.println("CONNECTION SUCCESS");
			return connection;
		}else{
			System.out.println("CONNECTION FAILED");
			return null;
		}
	}

}
