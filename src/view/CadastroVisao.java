package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.QuadroVO;
import model.vo.UsuarioVO;

public class CadastroVisao extends JFrame {
	
	private MoneyFlowController controller;
	
	private JButton buttonGoogle;
	private JButton buttonFacebook;
	private JButton buttonLogin;
	
	private JTextField textNome;
	private JTextField textEmail;
	private JPasswordField textSenha;
	
	private JLabel labelCadastro;
	private JLabel labelLogin;	

	private ImageIcon iconGoogle;
	private ImageIcon iconFacebook;
	
	private ImageIcon logo;
	
	private UsuarioVO usuarioVO;

	public CadastroVisao(ImageIcon logo, MoneyFlowController controller) {
		super("Tela de Cadastro");
		
		this.logo = logo;
		this.controller = controller;
		
		try { 
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	// Java Swing Nimbus
		} 
		catch (Exception e) 
		{ }

		//chama o metodo rsponsavel por inicializar os componentes
		inicializaComponentes();

		//monta todo o layout e em seguida o adiciona
		this.getContentPane().add(this.montaPainel());
		
		buttonLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logar();
			}
		});

		buttonGoogle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logarGoogle();
			}
		});
		
		buttonFacebook.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logarFacebook();
			}
		});
		
		labelCadastro.addMouseListener(new MouseListener() {
			
			public void mouseEntered(MouseEvent e) {
				labelCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Define o cursor para a mão
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	labelCadastro.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Define o cursor padrão
            }

			@Override
			public void mouseClicked(MouseEvent e) {
				login();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void inicializaComponentes() {
		buttonFacebook = new JButton("Criar com Facebook");
		buttonGoogle = new JButton("Criar com Google");
		buttonLogin = new JButton("Cadastrar");
		
		iconGoogle = new ImageIcon("icons/logo_google.png");
		buttonGoogle.setIcon(iconGoogle);
		
		iconFacebook = new ImageIcon("icons/logo_facebook.png");
		buttonFacebook.setIcon(iconFacebook);
		
		textNome = new JTextField();
		textEmail = new JTextField();
		textSenha = new JPasswordField();
		
		labelCadastro = new JLabel("Ja possui conta? Faça o login");
		labelLogin = new JLabel("Criar conta - MoneyFlow");
	}
	
	//metodo responsavel por montar o painel 
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"10dlu:grow, p, center:p:grow, p, 10dlu:grow",
				"10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 10dlu:grow");

		// Agrupe as colunas para que tenham o mesmo tamanho
        layout.setColumnGroups(new int[][]{{2, 4}});
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(labelLogin, cc.xy(3, 2));

		builder.add(buttonGoogle, cc.xyw(2, 4, 3));
		builder.add(buttonFacebook, cc.xyw(2, 6, 3));
		
		builder.addSeparator("Outra forma de Cadastro", cc.xyw(2, 8, 3));
		
		builder.addLabel("Nome:", cc.xy(2, 10));
		builder.add(textNome, cc.xyw(2, 12, 3));
		
		builder.addLabel("Email:", cc.xy(2, 14));
		builder.add(textEmail, cc.xyw(2, 16, 3));
		
		builder.addLabel("Senha:", cc.xy(2, 18));
		builder.add(textSenha, cc.xyw(2, 20, 3));
		
		builder.add(buttonLogin, cc.xyw(2, 22, 3));
		
		builder.add(labelCadastro, cc.xy(3, 24));

		return builder.getPanel();
	}

	private void logar() {
		String email = textEmail.getText();
		String senhaDigitada = new String(textSenha.getPassword());
		String nome = textNome.getText();
		
		usuarioVO = new UsuarioVO(email, senhaDigitada, nome);
		
		if(controller.verificaUsuario(email)) {
			JOptionPane.showMessageDialog(this, "Email Já Cadastrado", "Erro", JOptionPane.WARNING_MESSAGE);
		}else {
			if(controller.addUsuario(usuarioVO)) {
				JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso", "Erro", JOptionPane.INFORMATION_MESSAGE);
				
				LoginVisao l = new LoginVisao(logo, controller, usuarioVO);
				l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				l.setBounds(100, 100, 320, 450);
				l.setIconImage(logo.getImage());
				
				this.dispose();
				l.setVisible(true);
			}else {
				JOptionPane.showMessageDialog(this, "Erro ao cadastrar Usuário", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}



	private void logarGoogle() {
		logar();
	}

	private void logarFacebook() {
		logar();
	}
	
	private void login() {
		LoginVisao l = new LoginVisao(logo, controller, null);
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setBounds(100, 100, 320, 450);
		l.setIconImage(logo.getImage());
		
		this.dispose();
		l.setVisible(true);
	}
}
