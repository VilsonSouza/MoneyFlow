package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dao.ConexaoBD;
import model.vo.MetaVO;

public class MetaDAO {

	public ArrayList<MetaVO> getMetas(String emailUsuario, String pesquisa, String filtro) {
		MetaVO metaVO;
		ArrayList<MetaVO> metas = new ArrayList<MetaVO>();

		String comandoSQL = "SELECT m.codigo, m.descricao, m.valor_esperado, m.valor_arrecadado FROM meta m WHERE m."
				+ filtro
				+ " LIKE '"
				+ pesquisa
				+ "%' AND emailUsuario = '"
				+ emailUsuario
				+ "' ORDER BY m."
				+ filtro
				+ ";";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				metaVO = new MetaVO(resultado.getInt("codigo"), resultado.getString("descricao"));
				metaVO.setValorEsperado(resultado.getFloat("valor_esperado"));
				metaVO.setValorArrecadado(resultado.getFloat("valor_arrecadado"));
				metaVO.setValorRestante(metaVO.getValorEsperado() - metaVO.getValorArrecadado());
				if(metaVO.getValorRestante() < 0)
					metaVO.setValorRestante(0);

				metas.add(metaVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return metas;
	}
	
	public int getCodigoValido() {
		int codigo = 1;

		String comandoSQL = "SELECT max(codigo) as codigo FROM meta;";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				codigo = resultado.getInt("codigo") + 1;
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return codigo;
	}

	public int addMeta(String emailUsuario, MetaVO metaVO) {
		int codigo = getCodigoValido();

		String comandoSQL = "INSERT INTO meta (codigo, descricao, valor_esperado, valor_arrecadado, emailUsuario) VALUES ( "
				+ codigo
				+ ",'"
				+ metaVO.getDescricao()
				+ "', "
				+ metaVO.getValorEsperado()
				+ ", 0, '"
				+ emailUsuario
				+ "');";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				atualizaValores(emailUsuario);
				return codigo;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return -1;
	}
	
	public boolean alterMeta(String emailUsuario, MetaVO metaVO) {
		String comandoSQL = "UPDATE meta SET descricao = '"
				+ metaVO.getDescricao()
				+ "', valor_esperado = "
				+ metaVO.getValorEsperado()
				+ ", valor_arrecadado = "
				+ metaVO.getValorArrecadado()
				+ " WHERE codigo = "
				+ metaVO.getCodigo()
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				atualizaValores(emailUsuario);
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean alterValorMeta(String codigo, float valor) {
		String comandoSQL = "UPDATE meta SET valor_arrecadado = valor_arrecadado +"
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
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}

	public boolean delMeta(String emailUsuario, int codigo) {
		String comandoSQL = "DELETE FROM meta WHERE codigo = "
				+ codigo
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				atualizaValores(emailUsuario);
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean atualizaValores(String emailUsuario) {
		String comandoSQL = "UPDATE meta m SET m.valor_arrecadado = (SELECT SUM(CASE WHEN mov.entrada = 1 THEN mov.valor_total ELSE 0 END) FROM movimentacao mov WHERE mov.codigoMeta = m.codigo AND mov.emailUsuario = '"
				+ emailUsuario
				+ "') WHERE m.emailUsuario = '"
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

	public int getQtdMetas(String emailUsuario) {
		int qtd = 0;

		String comandoSQL = "SELECT count(*)  FROM meta WHERE emailUsuario = '"
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
