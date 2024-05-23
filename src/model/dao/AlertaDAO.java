package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.vo.AlertaVO;

public class AlertaDAO {
	
	public ArrayList<AlertaVO> getAlertas(String emailUsuario, Date de, Date ate) {
		AlertaVO alertaVO;
		ArrayList<AlertaVO> alertas = new ArrayList<AlertaVO>();

		String comandoSQL = "SELECT a.codigo, a.descricao, a.data_alerta, CASE WHEN a.data_alerta < CURRENT_DATE THEN -1 WHEN a.data_alerta = CURRENT_DATE THEN 0 WHEN a.data_alerta > CURRENT_DATE THEN 1 END AS stat FROM alerta a WHERE emailUsuario = '"
				+ emailUsuario
				+ "' AND a.data_alerta BETWEEN '"
				+ transformaDateString(de)
				+ "' AND '"
				+ transformaDateString(ate)
				+ "' ORDER BY a.data_alerta;";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {
				alertaVO = new AlertaVO();
				
				alertaVO.setCodigo(resultado.getInt("codigo"));
				alertaVO.setDescricao(resultado.getString("descricao"));
				alertaVO.setData_alerta(resultado.getDate("data_alerta"));
				alertaVO.setStatus(resultado.getInt("stat"));
				
				alertas.add(alertaVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return alertas;
	}
	
	
	
	public int addAlerta(String emailUsuario, AlertaVO alertaVO) {
		int codigo = getCodigoValido();

		String comandoSQL = "INSERT INTO alerta (codigo, descricao, data_alerta, emailUsuario) VALUES ( "
				+ codigo
				+ ",'"
				+ alertaVO.getDescricao()
				+ "', '"
				+ transformaDateString(alertaVO.getData_alerta())
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
	
	public boolean alterAlerta(AlertaVO alertaVO) {
		String comandoSQL = "UPDATE alerta SET descricao = '"
				+ alertaVO.getDescricao()
				+ "', data_alerta = '"
				+ transformaDateString(alertaVO.getData_alerta())
				+ "' WHERE codigo = "
				+ alertaVO.getCodigo()
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
	
	public boolean delAlerta(int codigo) {
		String comandoSQL = "DELETE FROM alerta WHERE codigo = "
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
	
	public int getCodigoValido() {
		int codigo = 1;

		String comandoSQL = "SELECT max(codigo) as codigo FROM alerta;";

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
	
	private String transformaDateString(Date dataBanco) {
		SimpleDateFormat formatoUsual = new SimpleDateFormat("yyyy-MM-dd");

		String dataString = formatoUsual.format(dataBanco);

		return dataString;
	}
	
}
