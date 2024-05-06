package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dao.ConexaoBD;
import model.vo.BancoVO;

public class BancoDAO {

	public ArrayList<BancoVO> getBancos(String emailUsuario, String pesquisa, String filtro) {
		BancoVO bancoVO;
		ArrayList<BancoVO> bancos = new ArrayList<BancoVO>();

		String comandoSQL = "SELECT b.codigo, b.descricao, b.valor_total FROM banco b WHERE b."
				+ filtro
				+ " LIKE '"
				+ pesquisa
				+ "%' AND emailUsuario = '"
				+ emailUsuario
				+ "' ORDER BY b."
				+ filtro
				+ ";";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				bancoVO = new BancoVO(resultado.getInt("codigo"), resultado.getString("descricao"));
				bancoVO.setValorTotal(resultado.getFloat("valor_total"));

				bancos.add(bancoVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return bancos;
	}
	
	public int getCodigoValido() {
		int codigo = 1;

		String comandoSQL = "SELECT max(codigo) as codigo FROM banco;";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				codigo = resultado.getInt("codigo") + 1;
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return codigo;
	}

	public int addBanco(String emailUsuario, BancoVO bancoVO) {
		int codigo = getCodigoValido();

		String comandoSQL = "INSERT INTO banco (codigo, descricao, valor_total, emailUsuario) VALUES ( "
				+ codigo
				+ ",'"
				+ bancoVO.getDescricao()
				+ "', 0, '"
				+ emailUsuario
				+ "');";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0)
				return codigo;

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return -1;
	}
	
	public boolean alterBanco(BancoVO bancoVO) {
		String comandoSQL = "UPDATE banco SET descricao = '"
				+ bancoVO.getDescricao()
				+ "', valor_total = "
				+ bancoVO.getValorTotal()
				+ " WHERE codigo = "
				+ bancoVO.getCodigo()
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0)
				return true;

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean alterValorBanco(String codigo, float valor) {
		String comandoSQL = "UPDATE banco SET valor_total = valor_total +"
				+ valor
				+ " WHERE codigo = "
				+ codigo
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0)
				return true;

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}

	public boolean delBanco(int codigo) {
		String comandoSQL = "DELETE FROM banco WHERE codigo = "
				+ codigo
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0)
				return true;

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean atualizaValores(String emailUsuario) {
		String comandoSQL = "UPDATE banco b SET b.valor_total = (SELECT SUM(CASE WHEN m.entrada = 1 THEN m.valor_total ELSE -m.valor_total END) FROM movimentacao m WHERE m.codigoBanco = b.codigo AND m.emailUsuario = '"
				+ emailUsuario
				+ "') WHERE b.emailUsuario = '"
				+ emailUsuario
				+ "';";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0)
				return true;

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}
		
		return false;
	}

	public int getQtdBancos(String emailUsuario) {
		int qtd = 1;

		String comandoSQL = "SELECT count(*) FROM banco WHERE emailUsuario = '"
				+ emailUsuario
				+ "';";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				qtd = resultado.getInt(1);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return qtd;
	}
}
