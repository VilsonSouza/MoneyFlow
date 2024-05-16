package view;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import controller.MoneyFlowController;
import model.vo.UsuarioVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GerenciamentoPerfilVisao extends JInternalFrame {
	private MoneyFlowController controller;
	private UsuarioVO usuarioVO;

	private JButton buttonEditar;
	private JButton buttonCancelar;
	private JButton buttonExcluir;

	private JTextField textNome;

	private JPasswordField textSenha;
	private JDesktopPane desktop;

	private JLabel labelLogin;

	private Color backgroundTelas;

	private ImageIcon logo;

	private String email;

	private MenuVisao menuVisao;

	public GerenciamentoPerfilVisao(JDesktopPane desktop, MoneyFlowController controller, String email,
			Color backgroundTelas, MenuVisao menuVisao) {
		super("Editar Perfil");

		this.logo = new ImageIcon("icons/logo_app.png");
		this.controller = controller;
		this.email = email;
		this.desktop = desktop;
		this.backgroundTelas = backgroundTelas;
		this.menuVisao = menuVisao;

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Java Swing Nimbus
		} catch (Exception e) {
		}

		// chama o metodo rsponsavel por inicializar os componentes
		inicializaComponentes();

		// monta todo o layout e em seguida o adiciona
		this.getContentPane().add(this.montaPainel());

		buttonEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});

		buttonCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});

		buttonExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				excluir();
			}
		});

		if (usuarioVO != null) {
			textNome.setText(this.usuarioVO.getNome());
			textSenha.setText(this.usuarioVO.getSenha());
		}

	}

	private void inicializaComponentes() {
		usuarioVO = controller.getUsuario(email);

		buttonEditar = new JButton("Salvar");
		buttonCancelar = new JButton("Cancelar");
		buttonExcluir = new JButton("Excluir Conta");

		textNome = new JTextField();
		textSenha = new JPasswordField();

		labelLogin = new JLabel("Edição do Perfil");

		textSenha.setText("senha");
		if (usuarioVO != null) {
			textNome = new JTextField(usuarioVO.getNome());
			textSenha = new JPasswordField();
		} else {
			textNome = new JTextField();
			textSenha = new JPasswordField();
		}

	}

	private JComponent montaPainel() {
		FormLayout layout = new FormLayout("10dlu:grow, p, center:p:grow, p, 10dlu:grow",
				"10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 10dlu:grow");

		// Agrupe as colunas para que tenham o mesmo tamanho
		layout.setColumnGroups(new int[][] { { 2, 4 } });

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(labelLogin, cc.xy(3, 2));

		builder.addSeparator("Digite os Novos Dados", cc.xyw(2, 10, 3));

		builder.addLabel("Nome:", cc.xy(2, 12));
		builder.add(textNome, cc.xyw(2, 14, 3));

		builder.addLabel("Senha:", cc.xy(2, 16));
		builder.add(textSenha, cc.xyw(2, 18, 3));

		builder.add(buttonEditar, cc.xyw(2, 22, 3));
		builder.add(buttonCancelar, cc.xyw(2, 24, 3));
		builder.add(buttonExcluir, cc.xyw(2, 25, 3));

		return builder.getPanel();
	}

	private void salvar() {
		String nome = textNome.getText();
		String senha = new String(textSenha.getPassword());

		if (nome.isEmpty() && senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha pelo menos um campo.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!nome.isEmpty()) {
			usuarioVO.setNome(nome);
		}

		if (!senha.isEmpty()) {
			usuarioVO.setSenha(senha);
		}

		if (controller.alterUsuario(usuarioVO)) {
			JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso.", "Sucesso",
					JOptionPane.INFORMATION_MESSAGE);
			voltar();
		} else {
			JOptionPane.showMessageDialog(this, "Falha ao atualizar o perfil.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void excluir() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoPerfilVisao.this,
				"Tem certeza que deseja excluir o perfil?", "", JOptionPane.WARNING_MESSAGE);

		if (opc == 0) {
			String codigo = controller.enviarConfirmacaoDelUsuario(email, usuarioVO.getNome());
			
			menuVisao.setVisible(false);

			String userInput = JOptionPane.showInputDialog(null, "Digite o código enviado no seu email:");

			if (userInput != null && !userInput.isEmpty() && codigo != null) {
				if (codigo.equals(userInput)) {
					// Excluir o perfil
					if (controller.delUsuario(usuarioVO.getEmail())) {
						JOptionPane.showMessageDialog(GerenciamentoPerfilVisao.this, "Perfil excluído com sucesso.",
								"Sucesso", JOptionPane.INFORMATION_MESSAGE);

						// Fechar a janela atual (GerenciamentoPerfilVisao)
						menuVisao.dispose();

						// Abrir a janela de LoginVisao
						LoginVisao loginVisao = new LoginVisao(logo, controller, backgroundTelas, null);
						loginVisao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						loginVisao.setBounds(100, 100, 570, 500);
						loginVisao.setIconImage(logo.getImage());
						loginVisao.getContentPane().setBackground(backgroundTelas);
						loginVisao.setLocationRelativeTo(null);
						
						loginVisao.setVisible(true);

						return;
					} else {
						JOptionPane.showMessageDialog(GerenciamentoPerfilVisao.this, "Erro ao excluir o perfil.",
								"Erro", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(this, "Erro ao excluir Usuário\nRe-envie o código e tente novamente",
							"Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			menuVisao.setVisible(false);
		}
	}

	private void cancelar() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoPerfilVisao.this, "Tem certeza que deseja cancelar?", "",
				JOptionPane.WARNING_MESSAGE);

		if (opc == 0)
			voltar();
	}

	private void voltar() {
		this.dispose();
	}

	public void setPosicao() {
		Dimension d = this.desktop.getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}

}
