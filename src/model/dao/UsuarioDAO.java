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

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class UsuarioDAO {

	Session newSession = null;
	MimeMessage mimeMessage = new MimeMessage(newSession);

	private static final String CHAVE = "0qoVmtNcUUvhLhPZ";

	public UsuarioDAO() {
		setupServerProperties();
	}

	public boolean verificaUsuario(UsuarioVO usuarioVO) {

		String senhaEncriptografada = criptografar(usuarioVO.getSenha());

		int qtd = 0;

		String comandoSQL = "SELECT count(*) FROM usuario WHERE email = '" + usuarioVO.getEmail() + "' AND senha = '"
				+ senhaEncriptografada + "';";

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

		if (qtd != 0)
			return true;
		else
			return false;
	}

	public boolean verificaUsuario(String email) {

		int qtd = 0;

		String comandoSQL = "SELECT count(*) FROM usuario WHERE email = '" + email + "';";

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

		if (qtd != 0)
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

		if (linhasAfetadas > 0)
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

	public UsuarioVO getUsuario(String email) {
		UsuarioVO usuarioRetornado = null;

		String comandoSQL = "SELECT * FROM usuario WHERE email = ?";

		try (PreparedStatement comando = ConexaoBD.getConexaoBD().prepareStatement(comandoSQL)) {
			comando.setString(1, email);
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

	public boolean delUsuario(String email) {
		int linhasAfetadas = 0;

		// Exclua primeiro os registros relacionados nas tabelas filhas
		try (Connection conexao = ConexaoBD.getConexaoBD()) {
			// Desative o autocommit para garantir que as exclusões possam ser revertidas se
			// algo der errado
			conexao.setAutoCommit(false);

			// Exclua os registros relacionados à movimentacao
			String comandoMovimentacao = "DELETE FROM movimentacao WHERE emailUsuario = ?";
			try (PreparedStatement stmtMovimentacao = conexao.prepareStatement(comandoMovimentacao)) {
				stmtMovimentacao.setString(1, email);
				stmtMovimentacao.executeUpdate();
			}

			// Exclua os registros relacionados ao banco
			String comandoBanco = "DELETE FROM banco WHERE emailUsuario = ?";
			try (PreparedStatement stmtBanco = conexao.prepareStatement(comandoBanco)) {
				stmtBanco.setString(1, email);
				stmtBanco.executeUpdate();
			}

			// Exclua os registros relacionados à categoria
			String comandoCategoria = "DELETE FROM categoria WHERE emailUsuario = ?";
			try (PreparedStatement stmtCategoria = conexao.prepareStatement(comandoCategoria)) {
				stmtCategoria.setString(1, email);
				stmtCategoria.executeUpdate();
			}

			// Exclua os registros relacionados à meta
			String comandoMeta = "DELETE FROM meta WHERE emailUsuario = ?";
			try (PreparedStatement stmtMeta = conexao.prepareStatement(comandoMeta)) {
				stmtMeta.setString(1, email);
				stmtMeta.executeUpdate();
			}

			// Exclua os registros relacionados ao quadro
			String comandoQuadro = "DELETE FROM quadro WHERE emailUsuario = ?";
			try (PreparedStatement stmtQuadro = conexao.prepareStatement(comandoQuadro)) {
				stmtQuadro.setString(1, email);
				stmtQuadro.executeUpdate();
			}

			// Exclua os registros relacionados ao alerta
			String comandoAlerta = "DELETE FROM alerta WHERE emailUsuario = ?";
			try (PreparedStatement stmtAlerta = conexao.prepareStatement(comandoAlerta)) {
				stmtAlerta.setString(1, email);
				stmtAlerta.executeUpdate();
			}

			// Exclua o registro do usuário principal
			String comandoUsuario = "DELETE FROM usuario WHERE email = ?";
			try (PreparedStatement stmtUsuario = conexao.prepareStatement(comandoUsuario)) {
				stmtUsuario.setString(1, email);
				linhasAfetadas = stmtUsuario.executeUpdate();
			}

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
				+ emailUsuario + "' GROUP BY MONTH(m.data_ocorrencia) ORDER BY mes;";

		try {
			Statement comando = ConexaoBD.getConexaoBD().createStatement();
			ResultSet resultado = comando.executeQuery(comandoSQL);

			while (resultado.next()) {
				relatorioVO = new RelatorioAnualVO();
				relatorioVO.setNumeroMes(resultado.getInt("mes"));
				relatorioVO.setValorTotalMes(resultado.getFloat("valorTotal"));

				dados.add(relatorioVO);
			}

			resultado.close();
			comando.close();

		} catch (SQLException e) {
			System.err.println("Erro ao realizar conexao com o banco " + "verifique a url de conexão");
			e.printStackTrace();
		}

		return dados;
	}

	private MimeMessage draftEmail(String emailReceiptient, String emailSubject, String emailBody)
			throws AddressException, MessagingException {

		mimeMessage.addRecipients(Message.RecipientType.TO, emailReceiptient);

		mimeMessage.setSubject(emailSubject);

		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(emailBody, "text/html");
		MimeMultipart multipart = new MimeMultipart();
		multipart.addBodyPart(bodyPart);
		mimeMessage.setContent(multipart);
		return mimeMessage;
	}

	private void setupServerProperties() {
		Properties properties = System.getProperties();
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		newSession = Session.getDefaultInstance(properties, null);
	}

	public static String gerarStringAleatoria() {
		
		String caracteres = "0123456789";

		Random random = new Random();
		StringBuilder sb = new StringBuilder(6);

		for (int i = 0; i < 6; i++) {

			int index = random.nextInt(caracteres.length());

			sb.append(caracteres.charAt(index));
		}

		return sb.toString().toUpperCase();
	}

	public boolean enviarEmail(String emailReceiptient, String emailSubject, String emailBody) {
		try {
			mimeMessage = draftEmail(emailReceiptient, emailSubject, emailBody);

			// Credenciais SMTP:
			String fromUser = "flowmoneyads@gmail.com";
			String fromPassword = "zidn wshl lknr wvdv";
			String emailHost = "smtp.gmail.com";
			Transport transport = newSession.getTransport("smtp");
			transport.connect(emailHost, fromUser, fromPassword);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();

			return true;
		} catch (Exception e) {
			System.err.println("Erro ao enviar email");
			e.printStackTrace();
		}

		return false;
	}

	public String enviarConfirmacaoCadastro(String email, String nome) {

		String codigo = gerarStringAleatoria();

		String subject = "Confirmação de Cadastro MoneyFlow";
		String body = "Olá " + nome
				+ ",<br><br>Obrigado por se cadastrar no MoneyFlow, o seu aplicativo de gerenciamento financeiro pessoal!<br><br>Para concluir o processo de cadastro, por favor, insira o seguinte código de confirmação no aplicativo:<br><br> [Código de Confirmação]: "
				+ codigo
				+ "<br><br>Este código é necessário para ativar a sua conta e começar a utilizar todas as funcionalidades do MoneyFlow. Se você não iniciou o processo de cadastro, por favor, ignore este e-mail.<br><br>Se precisar de ajuda ou tiver alguma dúvida, não hesite em nos contatar.<br><br><br><br>Atenciosamente,<br><br>Equipe MoneyFlow";

		if (enviarEmail(email, subject, body))
			return codigo;
		else
			return null;
	}
	
	public String enviarConfirmacaoDelUsuario(String email, String nome) {

		String codigo = gerarStringAleatoria();

		String subject = "Confirmação de exclusão de conta MoneyFlow";
		String body = "Olá "
				+ nome
				+ ",<br><br>"
				+ "Você está recebendo este email para confirmar a exclusão da sua conta no MoneyFlow, o seu aplicativo de gerenciamento de finanças pessoais."
				+ "<br><br>"
				+ "Por favor, tome nota do seguinte código de confirmação:<br><br>"
				+ "[Código de Confirmação]: "
				+ codigo
				+ "<br><br>"
				+ "Se você solicitou a exclusão da sua conta, insira este código no aplicativo para confirmar a exclusão da sua conta. Se você não solicitou esta ação, por favor, entre em contato conosco imediatamente."
				+ "<br><br>"
				+ "Agradecemos por usar o MoneyFlow para gerenciar suas finanças e esperamos tê-lo(a) novamente conosco no futuro."
				+ "<br><br>"
				+ "Atenciosamente,<br><br>Equipe MoneyFlow";

		if (enviarEmail(email, subject, body))
			return codigo;
		else
			return null;
	}

	public String enviarRedefinicaoSenha(String email) {
		UsuarioVO usuarioVO = getUsuario(email);
		
		String codigo = gerarStringAleatoria();

		String subject = "Confirmação de exclusão de conta MoneyFlow";
		String body = "Olá "
				+ usuarioVO.getNome()
				+ ",<br><br>"
				+ "Você está recebendo este email para alterar a senha da sua conta no MoneyFlow, o seu aplicativo de gerenciamento de finanças pessoais."
				+ "<br><br>"
				+ "Por favor, tome nota do seguinte código de confirmação:<br><br>"
				+ "[Código de Confirmação]: "
				+ codigo
				+ "<br><br>"
				+ "Se você solicitou essa ação, insira este código no aplicativo para confirmar a redefinição da sua senha. Se você não solicitou esta ação, por favor, entre em contato conosco imediatamente."
				+ "<br><br>"
				+ "Atenciosamente,<br><br>Equipe MoneyFlow";

		if (enviarEmail(email, subject, body))
			return codigo;
		else
			return null;
	
	}
}
