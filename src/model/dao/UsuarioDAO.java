package model.dao;

import java.security.Key;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

import model.dao.ConexaoBD;
import model.vo.MovimentacaoVO;
import model.vo.RelatorioAnualVO;
import model.vo.UsuarioVO;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class UsuarioDAO {
	
	private static final String CHAVE = "0qoVmtNcUUvhLhPZ";
    
    public boolean verificaUsuario(UsuarioVO usuarioVO) {
    	
    	String senhaEncriptografada = criptografar(usuarioVO.getSenha());
    	
    	int qtd = 0;

        String comandoSQL = "SELECT count(*) FROM usuario WHERE email = '"
        		+ usuarioVO.getEmail()
        		+ "' AND senha = '"
        		+ senhaEncriptografada
        		+ "';";

        try (Statement comando = ConexaoBD.getConexaoBD().createStatement();
             ResultSet resultado = comando.executeQuery(comandoSQL)) {

            while (resultado.next()) {
                qtd = resultado.getInt(1);
            }
            
            resultado.close();
            comando.close();

        } catch (SQLException e) {
            System.err.println("Erro ao realizar conexão com o banco de dados.");
            e.printStackTrace();
        }

        if(qtd != 0)
        	return true;
        else
        	return false;
    }
    
    public boolean verificaUsuario(String email) {
    	
    	int qtd = 0;

        String comandoSQL = "SELECT count(*) FROM usuario WHERE email = '"
        		+ email
        		+ "';";

        try (Statement comando = ConexaoBD.getConexaoBD().createStatement();
             ResultSet resultado = comando.executeQuery(comandoSQL)) {

            while (resultado.next()) {
                qtd = resultado.getInt(1);
            }
            
            resultado.close();
            comando.close();

        } catch (SQLException e) {
            System.err.println("Erro ao realizar conexão com o banco de dados.");
            e.printStackTrace();
        }

        if(qtd != 0)
        	return true;
        else
        	return false;
    }

    public boolean addUsuario(UsuarioVO usuarioVO) {
    	
    	int linhasAfetadas = 0;

        String comandoSQL = "INSERT INTO usuario (email, senha, nome) VALUES (?, ?, ?);";

        try (PreparedStatement comando = ConexaoBD.getConexaoBD().prepareStatement(comandoSQL)) {
            comando.setString(1, usuarioVO.getEmail());
            comando.setString(2, criptografar(usuarioVO.getSenha()));
            comando.setString(3, usuarioVO.getNome());

            linhasAfetadas = comando.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar usuário no banco de dados.");
            e.printStackTrace();
        }
        
        if(linhasAfetadas > 0)
        	return true;
    	else
    		return false;        		
    }
    public boolean alterUsuario(UsuarioVO usuarioVO) {
        int linhasAfetadas = 0;

        String comandoSQL = "UPDATE usuario SET senha = ?, nome = ? WHERE email = ?;";

        try (PreparedStatement comando = ConexaoBD.getConexaoBD().prepareStatement(comandoSQL)) {
            comando.setString(1, criptografar(usuarioVO.getSenha())); // Criptografa a nova senha
            comando.setString(2, usuarioVO.getNome());
            comando.setString(3, usuarioVO.getEmail());

            linhasAfetadas = comando.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao alterar usuário no banco de dados.");
            e.printStackTrace();
        }

        return linhasAfetadas > 0;
    }
    public UsuarioVO getUsuario(UsuarioVO usuarioVO) {
        UsuarioVO usuarioRetornado = null;

        String comandoSQL = "SELECT * FROM usuario WHERE email = ?";

        try (PreparedStatement comando = ConexaoBD.getConexaoBD().prepareStatement(comandoSQL)) {
            comando.setString(1, usuarioVO.getEmail());
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                usuarioRetornado = new UsuarioVO();
                usuarioRetornado.setEmail(resultado.getString("email"));
                usuarioRetornado.setNome(resultado.getString("nome"));

                // Descriptografa a senha recuperada do banco de dados
                String senhaCriptografada = resultado.getString("senha");
                String senhaDescriptografada = descriptografar(senhaCriptografada);
                usuarioRetornado.setSenha(senhaDescriptografada);
            }

            resultado.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter usuário do banco de dados.");
            e.printStackTrace();
        }

        return usuarioRetornado;
    }
    public String descriptografar(String senhaCriptografada) {
        try {
            SecretKey chaveSecreta = new SecretKeySpec(CHAVE.getBytes(), "AES");
            Cipher cifra = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cifra.init(Cipher.DECRYPT_MODE, chaveSecreta);

            byte[] textoDescriptografado = cifra.doFinal(Base64.getDecoder().decode(senhaCriptografada));

            return new String(textoDescriptografado);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean delUsuario(UsuarioVO usuarioVO) {
        int linhasAfetadas = 0;

        // Exclua primeiro os registros relacionados nas tabelas filhas
        try (Connection conexao = ConexaoBD.getConexaoBD()) {
            // Desative o autocommit para garantir que as exclusões possam ser revertidas se algo der errado
            conexao.setAutoCommit(false);

            // Exclua os registros relacionados à movimentacao
            String comandoMovimentacao = "DELETE FROM movimentacao WHERE emailUsuario = ?";
            try (PreparedStatement stmtMovimentacao = conexao.prepareStatement(comandoMovimentacao)) {
                stmtMovimentacao.setString(1, usuarioVO.getEmail());
                stmtMovimentacao.executeUpdate();
            }

            // Exclua os registros relacionados ao banco
            String comandoBanco = "DELETE FROM banco WHERE emailUsuario = ?";
            try (PreparedStatement stmtBanco = conexao.prepareStatement(comandoBanco)) {
                stmtBanco.setString(1, usuarioVO.getEmail());
                stmtBanco.executeUpdate();
            }

            // Exclua os registros relacionados à categoria
            String comandoCategoria = "DELETE FROM categoria WHERE emailUsuario = ?";
            try (PreparedStatement stmtCategoria = conexao.prepareStatement(comandoCategoria)) {
                stmtCategoria.setString(1, usuarioVO.getEmail());
                stmtCategoria.executeUpdate();
            }

            // Exclua os registros relacionados à meta
            String comandoMeta = "DELETE FROM meta WHERE emailUsuario = ?";
            try (PreparedStatement stmtMeta = conexao.prepareStatement(comandoMeta)) {
                stmtMeta.setString(1, usuarioVO.getEmail());
                stmtMeta.executeUpdate();
            }

            // Exclua os registros relacionados ao quadro
            String comandoQuadro = "DELETE FROM quadro WHERE emailUsuario = ?";
            try (PreparedStatement stmtQuadro = conexao.prepareStatement(comandoQuadro)) {
                stmtQuadro.setString(1, usuarioVO.getEmail());
                stmtQuadro.executeUpdate();
            }

            // Exclua os registros relacionados ao alerta
            String comandoAlerta = "DELETE FROM alerta WHERE emailUsuario = ?";
            try (PreparedStatement stmtAlerta = conexao.prepareStatement(comandoAlerta)) {
                stmtAlerta.setString(1, usuarioVO.getEmail());
                stmtAlerta.executeUpdate();
            }

            // Exclua o registro do usuário principal
            String comandoUsuario = "DELETE FROM usuario WHERE email = ?";
            try (PreparedStatement stmtUsuario = conexao.prepareStatement(comandoUsuario)) {
                stmtUsuario.setString(1, usuarioVO.getEmail());
                linhasAfetadas = stmtUsuario.executeUpdate();
            }

            // Se chegou até aqui sem lançar exceções, confirme as alterações
            conexao.commit();
        } catch (SQLException e) {
            // Se algo der errado, faça rollback das alterações e registre o erro
            System.err.println("Erro ao excluir usuário do banco de dados.");
            e.printStackTrace();

        }

        return linhasAfetadas > 0;
    }





    private String criptografar(String senha) {
        try {

            SecretKey chaveSecreta = new SecretKeySpec(CHAVE.getBytes(), "AES");
            Cipher cifra = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cifra.init(Cipher.ENCRYPT_MODE, chaveSecreta);


            byte[] textoCriptografado = cifra.doFinal(new String(senha).getBytes());

            return Base64.getEncoder().encodeToString(textoCriptografado);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public ArrayList<RelatorioAnualVO> getRelatorioAnual(String emailUsuario) {
		RelatorioAnualVO relatorioVO;
		ArrayList<RelatorioAnualVO> dados = new ArrayList<RelatorioAnualVO>();

		String comandoSQL = "SELECT MONTH(m.data_ocorrencia) AS mes, SUM(m.valor_total) AS valorTotal FROM movimentacao m JOIN usuario u ON m.emailUsuario = u.email WHERE YEAR(m.data_ocorrencia) = YEAR(CURRENT_DATE()) AND u.email = '"
				+ emailUsuario
				+ "' GROUP BY MONTH(m.data_ocorrencia) ORDER BY mes;";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while(resultado.next()) {				
				relatorioVO = new RelatorioAnualVO();
				relatorioVO.setNumeroMes(resultado.getInt("mes"));
				relatorioVO.setValorTotalMes(resultado.getFloat("valorTotal"));
				
				dados.add(relatorioVO);
			}

			resultado.close();
			comando.close();

		}catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco "
					+ "verifique a url de conexão");
			e.printStackTrace();
		}

		return dados;
	}
}
