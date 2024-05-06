package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dao.ConexaoBD;
import model.vo.QuadroVO;

public class QuadroDAO {
	
	private MovimentacaoDAO movimentacaoDAO;

	public ArrayList<QuadroVO> getQuadros(String emailUsuario, String pesquisa, String filtro) {
		QuadroVO quadroVO;
		ArrayList<QuadroVO> quadros = new ArrayList<QuadroVO>();
		
		String comandoSQL = "SELECT codigo, descricao, total_entradas, total_saidas FROM quadro q WHERE q."
				+ filtro
				+ " LIKE '"
				+ pesquisa
				+ "%' AND emailUsuario = '"
				+ emailUsuario
				+ "' ORDER BY q."
				+ filtro
				+ ";";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				quadroVO = new QuadroVO(resultado.getInt("codigo"), resultado.getString("descricao"));
				quadroVO.setTotalEntrada(resultado.getFloat("total_entradas"));
				quadroVO.setTotalSaida(resultado.getFloat("total_saidas"));

				quadros.add(quadroVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return quadros;
	}
	
	public int getCodigoValido() {
		int codigo = 1;

		String comandoSQL = "SELECT max(codigo) as codigo FROM quadro;";

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

	public int addQuadro(QuadroVO quadroVO) {
		int codigo = getCodigoValido();

		String comandoSQL = "INSERT INTO quadro (codigo, descricao, emailUsuario) VALUES ( "
				+ codigo
				+ ",'"
				+ quadroVO.getDescricao()
				+ "', '"
				+ quadroVO.getEmailUsuario()
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
	
	public boolean alterQuadro(QuadroVO quadroVO) {
		String comandoSQL = "UPDATE quadro SET descricao = '"
				+ quadroVO.getDescricao()
				+ "' WHERE codigo = "
				+ quadroVO.getCodigo()
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

	public boolean delQuadro(int codigo) {		
		String comandoSQL = "DELETE FROM quadro WHERE codigo = "
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
		String comandoSQL = "UPDATE quadro q SET q.total_entradas = (SELECT SUM(CASE WHEN m.entrada = 1 THEN m.valor_total ELSE 0 END) FROM movimentacao m WHERE m.codigoQuadro = q.codigo AND m.emailUsuario = '"
				+ emailUsuario
				+ "'), q.total_saidas = (SELECT SUM(CASE WHEN m.entrada = 0 THEN ABS(m.valor_total) ELSE 0 END) FROM movimentacao m WHERE m.codigoQuadro = q.codigo AND m.emailUsuario = '"
				+ emailUsuario
				+ "') WHERE q.emailUsuario = '"
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
}
