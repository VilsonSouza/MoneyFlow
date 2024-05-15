package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
	
	private static Connection conexao = null;
	
	private ConexaoBD() {
		
	}

	public static Connection getConexaoBD() {

		
		if( conexao == null)
		{	
			try {
		
				
//				Class.forName("org.hsqldb.jdbcDriver"); //carrega o driver
				Class.forName("com.mysql.cj.jdbc.Driver"); //carrega o driver
					
				conexao = DriverManager.getConnection("jdbc:mysql://localhost/moneyflow","root","");
//				conexao = DriverManager.getConnection("jdbc:mysql://localhost/moneyflow?useTimezone=true&serverTimezone=UTC","root","");
				
			}		
			catch (ClassNotFoundException e) {
					System.err.println("Erro ao carregar o driver verifique o "
						+ "arquivo-hsqldb.jar no classpath");
						e.printStackTrace();
						//exce��o para criacao Statement e execucao do comando
			} 
			catch (SQLException e) {
						System.err.println("Erro ao realizar conexao com o banco "
						+ "verifique a url de conex�o");
						e.printStackTrace();
			}
		}
		
		return conexao;
	}
	
	/**
	 * 
	 */
	
	public static void closeCoxexaoBD() {
		
		try {
			conexao.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Erro ao realizar fechamento da conexao com o banco ");
			e.printStackTrace();
	
		}
	}

}
