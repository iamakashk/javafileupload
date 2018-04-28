package fileupload.datamanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import fileupload.connectionConfig.DatabaseConnection;

public class FileUploadDataManager {
	static DatabaseConnection dbConn = new DatabaseConnection();
	public boolean isFileExist(HashMap param){
		Connection dbConnection = null;
		Statement statement = null;
		ResultSet rs = null;
		StringBuilder query  = new StringBuilder();
		query.append("SELECT COUNT(*) CNT FROM FILE_MASTER ");
		query.append(" WHERE fm_doc_name like '% ");
		query.append(param.get("FILE_NAME")+"%'");
		boolean isExist = false;
		try {
			System.out.println("is file exist quqery_______________" + query);
			dbConnection = (Connection) dbConn.getConnection();
			statement = dbConnection.createStatement();
			 rs = statement.executeQuery(query.toString());
			 while(rs.next()){
				 if(rs.getInt("CNT") >0){
					 isExist = true;
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isExist;
	}
	
	public void insertRecordIntoTable(HashMap param) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String insertTableSQL = "INSERT INTO FILE_MASTER"
				+ "(FM_DOC_NAME , FM_DOC_PATH) VALUES"
				+ "(?,?)";

		try {
			dbConnection =(Connection) dbConn.getConnection();
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, param.get("FILE_NAME").toString());
			preparedStatement.setString(2,  param.get("FILE_PATH").toString());
			
			// execute insert SQL stetement
			preparedStatement.executeUpdate();


		} catch (SQLException e) {
				e.printStackTrace();

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}
	public void updateRecordToTable(HashMap param) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String updateTableSQL = "UPDATE FILE_MASTER SET FM_DOC_NAME = ?, FM_DOC_PATH = ? "
				                  + " WHERE FM_DOC_NAME = ?";

		try {
			dbConnection =(Connection) dbConn.getConnection();
			preparedStatement = dbConnection.prepareStatement(updateTableSQL);

			preparedStatement.setString(1, param.get("FILE_NAME").toString());
			preparedStatement.setString(2, param.get("FILE_PATH").toString());
			preparedStatement.setString(3, param.get("FILE_NAME").toString());

			// execute update SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Record is updated to  table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}
}
