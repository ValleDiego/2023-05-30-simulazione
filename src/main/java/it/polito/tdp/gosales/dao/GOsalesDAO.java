package it.polito.tdp.gosales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.gosales.model.Arco;
import it.polito.tdp.gosales.model.DailySale;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;

public class GOsalesDAO {
	
	
	/**
	 * Metodo per leggere la lista di tutti i rivenditori dal database
	 * @return
	 */

	public List<Retailers> getAllRetailers(){
		String query = "SELECT * from go_retailers";
		List<Retailers> result = new ArrayList<Retailers>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	/**
	 * Metodo per leggere la lista di tutti i prodotti dal database
	 * @return
	 */
	public List<Products> getAllProducts(){
		String query = "SELECT * from go_products";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	/**
	 * Metodo per leggere la lista di tutte le vendite nel database
	 * @return
	 */
	public List<DailySale> getAllSales(){
		String query = "SELECT * from go_daily_sales";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantity"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public List<String> getNazioni() {
		String query = "SELECT DISTINCT r.Country\r " + 
				"FROM go_retailers AS r\r " + 
				"ORDER BY r.Country";
		List<String> result = new ArrayList<String>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString("r.Country"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public Map<Integer, Retailers> getVertici(String nazione) {
		String query = "SELECT DISTINCT r.*\r " + 
				"FROM go_retailers AS r\r " + 
				"WHERE r.Country = ?\r " + 
				"ORDER BY r.Retailer_name";
		Map<Integer, Retailers> result = new HashMap<Integer, Retailers>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nazione);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.put(rs.getInt("Retailer_code"),new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}


	public List<Arco> getArchi(String nazione, Integer anno, int nMin) {
		String query = "SELECT s1.Retailer_code, s2.Retailer_code, COUNT(DISTINCT s1.Product_number) AS peso\r " + 
				"FROM go_daily_sales AS s1, go_daily_sales AS s2\r " + 
				"WHERE s1.Product_number = s2.Product_number\r " + 
				"AND YEAR(s1.Date) = ? AND YEAR(s1.Date) = YEAR(s2.Date)\r " + 
				"AND s1.Retailer_code > s2.Retailer_code\r " + 
				"AND s1.Retailer_code IN (SELECT DISTINCT r.Retailer_code\r " + 
				"								FROM go_retailers AS r\r " + 
				"								WHERE r.Country = ?)\r " + 
				"AND s2.Retailer_code IN (SELECT DISTINCT r.Retailer_code\r " + 
				"								FROM go_retailers AS r\r " + 
				"								WHERE r.Country = ?)\r " + 
				"GROUP BY s1.Retailer_code, s2.Retailer_code\r " + 
				"HAVING peso>=?\r " +
				"ORDER BY peso";
		List<Arco> result = new ArrayList<Arco>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, anno);
			st.setString(2, nazione);
			st.setString(3, nazione);
			st.setInt(4, nMin);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Arco(rs.getInt("s1.Retailer_code"), rs.getInt("s2.Retailer_code"), rs.getInt("peso")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
}
