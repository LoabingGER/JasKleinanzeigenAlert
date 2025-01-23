package de.ebkbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQL {
	private static final String host = "192.168.188.21";
	private static final String port = "3306";
	private static final String database = "Accounts";
	private static final String username = "ebk";
	private static final String pass = "Kakamachen";
	
	private static Connection con;
	
	public static boolean isConnected() {
		return(con == null ? false:true);
	}
	
	public static void connect() {
		if(!isConnected());
		
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, pass);
				
				System.out.println("[MySQL] Verbunden!");
				
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				con.close();
				System.out.println("[MySQL] Verbindung Geschlossen!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void update(String qry) {
		try {
			PreparedStatement ps  = con.prepareStatement(qry);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> getDatabaseByID(int id){
		ArrayList<String> s = new ArrayList<String>();
		try {
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT * FROM KundenInformationen where id= '" + id + "';");

			while(rs.next()) {
				s.add(rs.getString(1).toString());
				s.add(rs.getString(2).toString());
				s.add(rs.getString(3).toString());
				s.add(rs.getDate(4).toString());
				s.add(rs.getDate(5).toString());
				s.add(rs.getString(6).toString());
			}

			return s;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}
	
	public static ArrayList<String> getDatabaseByChatID(int Chatid){
		ArrayList<String> s = new ArrayList<String>();
		try {
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT * FROM KundenInformationen where ChatID= '" + Chatid + "';");

			while(rs.next()) {
				s.add(rs.getString(1).toString());
				s.add(rs.getString(2).toString());
				s.add(rs.getString(3).toString());
				s.add(rs.getDate(4).toString());
				s.add(rs.getDate(5).toString());
				s.add(rs.getString(6).toString());
				s.add(rs.getString(7).toString());
			}

			return s;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}
	
	
	public static ArrayList<ArrayList<String>> getDatabase(String qry) {
		try {
			ArrayList<ArrayList<String>> s1 = new ArrayList<ArrayList<String>>();
			
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery(qry);
			while(rs.next()) {
				ArrayList<String> s2 = new ArrayList<String>();
				
				s2.add(rs.getString(1).toString());
				s2.add(rs.getString(2).toString());
				s2.add(rs.getString(3).toString());
				s2.add(rs.getDate(4).toString());
				s2.add(rs.getDate(5).toString());
				s2.add(rs.getString(6).toString());
				s2.add(rs.getString(7).toString());
				s1.add(s2);
			}
			st.close();
			return  s1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
	}

}
