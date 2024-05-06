package model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.vo.MovimentacaoVO;

public class MovimentacaoDAO {

	public ArrayList<MovimentacaoVO> getMovimentacoes(int codigoQuadro, String emailUsuario, String pesquisa, String filtro, Date de, Date ate) {
		MovimentacaoVO movimentacaoVO;
		ArrayList<MovimentacaoVO> movimentacoes = new ArrayList<MovimentacaoVO>();

		String comandoSQL = "SELECT m.codigo, m.descricao, m.valor_total, m.data_ocorrencia, m.codigoQuadro, m.entrada FROM movimentacao m WHERE codigoQuadro = "
				+ codigoQuadro
				+ " AND m."
				+ filtro
				+ " LIKE '"
				+ pesquisa
				+ "%' AND m.data_ocorrencia BETWEEN '"
				+ transformaDateString(de)
				+ "' AND '"
				+ transformaDateString(ate)
				+ "' AND emailUsuario = '"
				+ emailUsuario
				+ "' ORDER BY m."
				+ filtro
				+ ";";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {				
				movimentacaoVO = new MovimentacaoVO();
				movimentacaoVO.setCodigo(resultado.getInt("codigo"));
				movimentacaoVO.setDescricao(resultado.getString("descricao"));
				movimentacaoVO.setValor_total(resultado.getFloat("valor_total"));
				movimentacaoVO.setDataOcorrencia(resultado.getDate("data_ocorrencia"));
				movimentacaoVO.setEntrada(resultado.getBoolean("entrada"));
				
				movimentacoes.add(movimentacaoVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return movimentacoes;
	}

	public boolean addMovimentacao(String emailUsuario, MovimentacaoVO movimentacaoVO) {
        
		if(movimentacaoVO.isEntrada())
	        if(addMovimentacaoUnid(emailUsuario, movimentacaoVO))
	        	return true;
	    	else
	    		return false;   
		
		else
			if(addSaidas(emailUsuario, movimentacaoVO))
				return true;
			else
				return false;
    
	}
	
	public boolean addMovimentacaoUnid(String emailUsuario, MovimentacaoVO movimentacaoVO) {
		
		int linhasAfetadas = 0;

        String comandoSQL = "INSERT INTO movimentacao (codigo, descricao, valor_total, data_ocorrencia, entrada, codigoQuadro, codigoCategoria, codigoBanco, codigoMeta, emailUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement comando = ConexaoBD.getConexaoBD().prepareStatement(comandoSQL)) {
            comando.setInt(1, getCodigoValido());
            comando.setString(2, movimentacaoVO.getDescricao());
            comando.setFloat(3, movimentacaoVO.getValor_total());
            comando.setDate(4, (new java.sql.Date(movimentacaoVO.getDataOcorrencia().getTime())));
            comando.setBoolean(5, movimentacaoVO.isEntrada());
            comando.setInt(6, movimentacaoVO.getCodigoQuadro());
            comando.setInt(7, movimentacaoVO.getCodigoCategoria());
            comando.setInt(8, movimentacaoVO.getCodigoBanco());
            
            if(!movimentacaoVO.isEntrada() || movimentacaoVO.getCodigoMeta() == -1)
            	comando.setString(9, null);
            else
            	comando.setInt(9, movimentacaoVO.getCodigoMeta());

            comando.setString(10, emailUsuario);

            linhasAfetadas = comando.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar usuário no banco de dados.");
            e.printStackTrace();
        }
        
        if(linhasAfetadas > 0) {
        	return true;
        }else {
    		return false;  
        }
	}
	
	public boolean addSaidas(String emailUsuario, MovimentacaoVO movimentacaoVO) {
		
		Calendar cal = Calendar.getInstance();

        Date d = movimentacaoVO.getDataOcorrencia();

        cal.setTime(d);
        
        int n_parcelas = movimentacaoVO.getQtdParcelas();
        float valor = movimentacaoVO.getValor_total();

        // Loop para calcular e adicionar as parcelas
        for (int aux = 0; aux < n_parcelas; aux++) {
        	movimentacaoVO.setValor_total(valor/n_parcelas);
        	movimentacaoVO.setDataOcorrencia(cal.getTime());
        	
        	if(!addMovimentacaoUnid(emailUsuario, movimentacaoVO))
        		return false;
       
            cal.add(Calendar.MONTH, 1);
        }
        
        return true;
	}

	public MovimentacaoVO getMovimentacao(int codigo) {
		MovimentacaoVO movimentacaoVO = null;

		String comandoSQL = "SELECT codigo, descricao, valor_total, data_ocorrencia, entrada, codigoQuadro, codigoCategoria, codigoBanco, codigoMeta FROM movimentacao WHERE codigo = "
				+ codigo
				+ ";";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {				
				movimentacaoVO = new MovimentacaoVO();
				movimentacaoVO.setCodigo(resultado.getInt("codigo"));
				movimentacaoVO.setDescricao(resultado.getString("descricao"));
				movimentacaoVO.setValor_total(resultado.getFloat("valor_total"));			
				movimentacaoVO.setDataOcorrencia(resultado.getDate("data_ocorrencia"));
				movimentacaoVO.setEntrada(resultado.getBoolean("entrada"));
				movimentacaoVO.setCodigoMeta(resultado.getInt("codigoMeta"));				
				movimentacaoVO.setCodigoQuadro(resultado.getInt("codigoQuadro"));
				movimentacaoVO.setCodigoCategoria(resultado.getInt("codigoCategoria"));
				movimentacaoVO.setCodigoBanco(resultado.getInt("codigoBanco"));
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return movimentacaoVO;
	}

	public boolean alterMovimentacao(String emailUsuario, MovimentacaoVO movimentacaoVO) {
		int linhasAfetadas = 0;
		
		String comandoSQL = "";

		if(movimentacaoVO.isEntrada())
			if(movimentacaoVO.getCodigoMeta() != -1) {
		        comandoSQL = "UPDATE movimentacao SET descricao = '"
		        		+ movimentacaoVO.getDescricao()
		        		+ "', valor_total = "
		        		+ movimentacaoVO.getValor_total()
		        		+ ", data_ocorrencia = '"
		        		+ new java.sql.Date(movimentacaoVO.getDataOcorrencia().getTime())
		        		+ "', codigoCategoria = "
		        		+ movimentacaoVO.getCodigoCategoria()
		        		+ ", codigoBanco = "
		        		+ movimentacaoVO.getCodigoBanco()
		        		+ ", codigoMeta = "
		        		+ movimentacaoVO.getCodigoMeta()
		        		+ " WHERE codigo = "
		        		+ movimentacaoVO.getCodigo()
		        		+ ";";
			}else {		
			comandoSQL = "UPDATE movimentacao SET descricao = '"
	        		+ movimentacaoVO.getDescricao()
	        		+ "', valor_total = "
	        		+ movimentacaoVO.getValor_total()
	        		+ ", data_ocorrencia = '"
	        		+ new java.sql.Date(movimentacaoVO.getDataOcorrencia().getTime())
	        		+ "', codigoCategoria = "
	        		+ movimentacaoVO.getCodigoCategoria()
	        		+ ", codigoBanco = "
	        		+ movimentacaoVO.getCodigoBanco()
	        		+ ", codigoMeta = "
	        		+ null
	        		+ " WHERE codigo = "
	        		+ movimentacaoVO.getCodigo()
	        		+ ";";
			}
		else
			comandoSQL = "UPDATE movimentacao SET descricao = '"
	        		+ movimentacaoVO.getDescricao()
	        		+ "', valor_total = "
	        		+ movimentacaoVO.getValor_total()
	        		+ ", data_ocorrencia = '"
	        		+ new java.sql.Date(movimentacaoVO.getDataOcorrencia().getTime())
	        		+ "', codigoCategoria = "
	        		+ movimentacaoVO.getCodigoCategoria()
	        		+ ", codigoBanco = "
	        		+ movimentacaoVO.getCodigoBanco()
	        		+ " WHERE codigo = "
	        		+ movimentacaoVO.getCodigo()
	        		+ ";";
			
        try (PreparedStatement comando = ConexaoBD.getConexaoBD().prepareStatement(comandoSQL)) {
            
            linhasAfetadas = comando.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar usuário no banco de dados.");
            e.printStackTrace();
        }
        
        if(linhasAfetadas > 0) {
        	return true;
        } else {
    		return false;  
        }
	}
	
	public int getCodigoValido() {
		int codigo = 1;

		String comandoSQL = "SELECT max(codigo) as codigo FROM movimentacao;";

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

	public boolean delMovimentacao(int codigo) {
		String comandoSQL = "DELETE FROM movimentacao WHERE codigo = "
				+ codigo
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean delMovimentacaoQuadro(String emailUsuario, int codigoQuadro) {
		String comandoSQL = "DELETE FROM movimentacao WHERE codigoQuadro = "
				+ codigoQuadro
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean delMovimentacaoBanco(String emailUsuario, int codigoBanco) {
		String comandoSQL = "DELETE FROM movimentacao WHERE codigoBanco = "
				+ codigoBanco
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean delMovimentacaoCategoria(String emailUsuario, int codigoCategoria) {
		String comandoSQL = "DELETE FROM movimentacao WHERE codigoCategoria = "
				+ codigoCategoria
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean alterMovimentacaoMeta(String emailUsuario, int codigoMeta) {
		String comandoSQL = "UPDATE movimentacao SET codigoMeta = null WHERE codigoMeta = "
				+ codigoMeta
				+ ";";
		
		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			int resultado = comando.executeUpdate(comandoSQL);

			comando.close();
			
			if(resultado != 0) {
				return true;
			}

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o meta "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean ehNumero(String numero) {
		boolean ehNumero = true;

		numero = numero.trim();
		numero = numero.replace(",", "");
		numero = numero.replace(".", "");

		try {
			float n = Float.parseFloat(numero);
		}catch(NumberFormatException e){
			ehNumero = false;
		}

		return ehNumero;
	}
	
	public String transformaDateString(Date dataBanco) {
		SimpleDateFormat formatoUsual = new SimpleDateFormat("yyyy-MM-dd");

		String dataString = formatoUsual.format(dataBanco);

		return dataString;
	}

}
