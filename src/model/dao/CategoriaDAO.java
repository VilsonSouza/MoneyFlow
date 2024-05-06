package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dao.ConexaoBD;
import model.vo.CategoriaVO;

public class CategoriaDAO {

	public ArrayList<CategoriaVO> getCategoria(String emailUsuario, String pesquisa, String filtro) {
		CategoriaVO categoriaVO;
		ArrayList<CategoriaVO> categorias = new ArrayList<CategoriaVO>();

		String comandoSQL = "SELECT codigo, descricao FROM categoria WHERE "
				+ filtro
				+ " LIKE '"
				+ pesquisa
				+ "%' AND emailUsuario = '"
				+ emailUsuario
				+ "' ORDER BY "
				+ filtro
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				categoriaVO = new CategoriaVO(resultado.getInt("codigo"), resultado.getString("descricao"));

				categorias.add(categoriaVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return categorias;
	}
	
	public int getCodigoValido() {
		int codigo = 1;

		String comandoSQL = "SELECT max(codigo) as codigo FROM categoria;";

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

	public int addCategoria(String emailUsuario, CategoriaVO categoriaVO) {
		int codigo = getCodigoValido();

		String comandoSQL = "INSERT INTO categoria (codigo, descricao, emailUsuario) VALUES ( "
				+ codigo
				+ ",'"
				+ categoriaVO.getDescricao()
				+ "', '"
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
	
	public boolean alterCategoria(CategoriaVO categoriaVO) {
		String comandoSQL = "UPDATE categoria SET descricao = '"
				+ categoriaVO.getDescricao()
				+ "' WHERE codigo = "
				+ categoriaVO.getCodigo()
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

	public boolean delCategoria(int codigo) {
		String comandoSQL = "DELETE FROM categoria WHERE codigo = "
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

	public int getQtdCategorias(String emailUsuario) {
		int qtd = 0;

		String comandoSQL = "SELECT count(*)  FROM categoria WHERE emailUsuario = '"
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
