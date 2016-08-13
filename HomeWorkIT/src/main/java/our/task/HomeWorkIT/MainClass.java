package our.task.HomeWorkIT;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainClass {

	Connection connection = null;
	
	public static void main(String[] args) {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);
 
        // add handler
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase(".");
         
        server.setHandler(resource_handler);
 
        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        
        MainClass test = new MainClass();
        if (!test.loadDriver()) return;
        if (!test.getConnection()) return;
        
        test.createTable();
        test.fillTable();
        test.printTable();
        test.closeConnection();
        
	}

	private boolean loadDriver() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Драйвер не найден");
			e.printStackTrace();
			return false;
			}
		return true;
	}
	
	private boolean getConnection() {
		try {
			//String path = "mypath/";
			String dbname = "MyDB";
			String connectionString = "jdbc:hsqldb:mem:" + dbname;
			String login = "SA";
			String password = "";
			connection = DriverManager.getConnection(connectionString, login, password);
		} catch (SQLException e) {
			System.out.println("Соединение не создано");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void createTable() {
		Statement statement;
		String sql;
		try {
			statement = connection.createStatement();
			sql = "CREATE TABLE userTable (userId IDENTITY PRIMARY KEY, userName VARCHAR(255))";
			statement.executeUpdate(sql);
			sql = "CREATE TABLE diskTable (diskId IDENTITY PRIMARY KEY, diskName VARCHAR(255))";
			statement.executeUpdate(sql);
			sql = "CREATE TABLE takenItemTable (takenItemId IDENTITY PRIMARY KEY, userId INTEGER, diskId INTEGER)";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
		}
	}
	
	private void fillTable() {
		Statement statement;
		String sql;
		try {
			statement = connection.createStatement();		
			sql = "INSERT INTO userTable (userName) VALUES('Вася')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO userTable (userName) VALUES('Петя')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO userTable (userName) VALUES('Саша')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO userTable (userName) VALUES('Катя')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO userTable (userName) VALUES('Света')";
			statement.executeUpdate(sql);

			sql = "INSERT INTO diskTable (diskName) VALUES('Касаясь пустоты')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO diskTable (diskName) VALUES('Шпионский мост')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO diskTable (diskName) VALUES('Амели')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO diskTable (diskName) VALUES('Отель Гранд Будапешт')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO diskTable (diskName) VALUES('Стажер')";
			statement.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	private void printTable() {
		Statement statement;
		String sql;
		ResultSet resultSet;
		try {
			statement = connection.createStatement();
			sql = "SELECT userId, userName FROM userTable";
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				System.out.println(resultSet.getInt(1)+" " + resultSet.getString(2));
			}
			
			sql = "SELECT diskId, diskName FROM diskTable";
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				System.out.println(resultSet.getInt(1)+" " + resultSet.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void closeConnection() {
		Statement statement;
		try {
			statement = connection.createStatement();
			String sql = "SHUTDOWN";
			statement.execute(sql);
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	
	

}
