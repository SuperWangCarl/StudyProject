package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * 效率比较
 * 新增3000条员工记录
 * @author Administrator
 *
 */
public class TestBatchOperation {

	public void createEmpByStatement(){
		// ----------------------------
		Connection conn = null;
		Statement st = null;
			
		// ----------------------------
		try {
			// 1. load driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. set up connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			conn.setAutoCommit(false);
			// 3. create sql executor
			st = conn.createStatement();
			
			// 4. execute sql
			
			int result = 0;
			
			long begin = System.currentTimeMillis();
			
			for (int i = 0; i < 3000; i++){
				
				result += st.executeUpdate("INSERT INTO emp(empno, ename) VALUES(" + i +", 'TOM')");
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			
			long end = System.currentTimeMillis();
			
			if (result > 0){
				System.out.println("inserted: " + result);	
				System.out.println(end - begin);
			} 
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 5. close resources
				try {
					if (st != null){
						st.close();
					}
					
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	// finally
		
	}	// createEmpByStatement
	
	public void createEmpByPreparedStatementBatch(){
		// ----------------------------
		Connection conn = null;
		PreparedStatement psmt = null;
			
		// ----------------------------
		try {
			// 1. load driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. set up connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			conn.setAutoCommit(false);
			// 3. create sql executor	// !!
			String sql = 
				"INSERT INTO emp(empno, ename)" +
				" VALUES(?,?)";
			
			psmt = conn.prepareStatement(sql);
			// 4. execute sql
			
			int result = 0;
			
			long begin = System.currentTimeMillis();
			
			for (int i = 0; i < 3000; i++){
				psmt.setInt(1, i);
				psmt.setString(2, "tom");
				
				psmt.addBatch();
			}
			
			int[] aCount = psmt.executeBatch();
			
			result = aCount.length;
			
			conn.commit();
			conn.setAutoCommit(true);
			
			long end = System.currentTimeMillis();
			
			if (result > 0){
				System.out.println("inserted: " + result);	
				System.out.println(end - begin);
			} 
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 5. close resources
				try {
					if (psmt != null){
						psmt.close();
					}
					
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	// finally
		
	}	// createEmpByPreparedStatementBatch
	
	public void createEmpByPreparedStatement(){
		// ----------------------------
		Connection conn = null;
		PreparedStatement psmt = null;
			
		// ----------------------------
		try {
			// 1. load driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. set up connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
//			conn.setAutoCommit(false);
			// 3. create sql executor	// !!
			String sql = 
				"INSERT INTO emp(empno, ename)" +
				" VALUES(?,?)";
			
			psmt = conn.prepareStatement(sql);
			// 4. execute sql
			
			int result = 0;
			
			long begin = System.currentTimeMillis();
			
			for (int i = 0; i < 3000; i++){
				psmt.setInt(1, i);
				psmt.setString(2, "tom");
				
				result += psmt.executeUpdate();
			}
			
//			conn.commit();
//			conn.setAutoCommit(true);
			
			long end = System.currentTimeMillis();
			
			if (result > 0){
				System.out.println("inserted: " + result);	
				System.out.println(end - begin);
			} 
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 5. close resources
				try {
					if (psmt != null){
						psmt.close();
					}
					
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	// finally
		
	}	// createEmpByPreparedStatement
	
	public void createEmpByStatementBatch(){
		// ----------------------------
		Connection conn = null;
		Statement st = null;
			
		// ----------------------------
		try {
			// 1. load driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. set up connection
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			conn.setAutoCommit(false);
			// 3. create sql executor	// !!
			st = conn.createStatement();
			// 4. execute sql
			
			int result = 0;
			
			long begin = System.currentTimeMillis();
			
			for (int i = 0; i < 3000; i++){
				
				st.addBatch("INSERT INTO emp(empno, ename) VALUES(" + i +", 'TOM')");
			}
			
			int[] aCount = st.executeBatch();
			result = aCount.length;
			
			conn.commit();
			conn.setAutoCommit(true);
			
			long end = System.currentTimeMillis();
			
			if (result > 0){
				System.out.println("inserted: " + result);	
				System.out.println(end - begin);
			} 
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 5. close resources
				try {
					if (st != null){
						st.close();
					}
					
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	// finally
		
	}	// createEmpByStatementBatch
	
	
	public static void main(String[] args) {
		TestBatchOperation t = new TestBatchOperation();
	//	t.createEmpByStatement();				// 5719, 2266, 2218, 1047(手动)
	//	t.createEmpByPreparedStatement();		// 1937, 1921, 1906, 828(手动)
	//	t.createEmpByPreparedStatementBatch();	// 2078, 1875, 1922, 797(手动)
		t.createEmpByStatementBatch();			// 2172, 2266, 2156, 985(手动)
		
		
	}	// main
	
}	// TestBatchOperation
