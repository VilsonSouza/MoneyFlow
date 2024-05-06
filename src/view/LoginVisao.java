package view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.UsuarioVO;

public class LoginVisao extends JFrame {
	
	private MoneyFlowController controller;
	
	private JButton buttonGoogle;
	private JButton buttonFacebook;
	private JButton buttonLogin;
	
	private JTextField textEmail;
	private JPasswordField textSenha;
	
	private JLabel labelEsqueceuSenha;
	private JLabel labelCadastro;
	private JLabel labelLogin;	
	private JLabel labelBemVindo;

	private ImageIcon iconGoogle;
	private ImageIcon iconFacebook;
	
	private ImageIcon logo;
	
	private UsuarioVO usuarioVO;

	public LoginVisao(ImageIcon logo, MoneyFlowController controller, UsuarioVO usuarioVO) {
		super("Tela de Login");
		
		this.logo = logo;
		this.controller = controller;
		this.usuarioVO = usuarioVO;
		
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
		
		labelEsqueceuSenha.addMouseListener(new MouseListener() {
			
			public void mouseEntered(MouseEvent e) {
				labelEsqueceuSenha.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Define o cursor para a mão
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	labelEsqueceuSenha.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Define o cursor padrão
            }

			@Override
			public void mouseClicked(MouseEvent e) {
				esqueceuSenha();
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
				cadastro();
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
		
		if (usuarioVO != null) {
			textEmail.setText(usuarioVO.getEmail());
			textSenha.setText(usuarioVO.getSenha());
		}
	}
	
	private void inicializaComponentes() {
		buttonFacebook = new JButton("Login com Facebook");
		buttonGoogle = new JButton("Login com Google");
		buttonLogin = new JButton("Login");
		
		iconGoogle = new ImageIcon("icons/logo_google.png");
		buttonGoogle.setIcon(iconGoogle);
		
		iconFacebook = new ImageIcon("icons/logo_facebook.png");
		buttonFacebook.setIcon(iconFacebook);
		
		textEmail = new JTextField();
		textSenha = new JPasswordField();
		
		labelCadastro = new JLabel("Não possui conta? Cadastre-se");
		labelEsqueceuSenha = new JLabel("Esqueceu a senha?");
		labelLogin = new JLabel("Login MoneyFlow");
		labelBemVindo = new JLabel("Bem-vindo de volta!");
		

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
		builder.add(labelBemVindo, cc.xy(3, 4));

		builder.add(buttonGoogle, cc.xyw(2, 6, 3));
		builder.add(buttonFacebook, cc.xyw(2, 8, 3));
		
		builder.addSeparator("Outra forma de Login", cc.xyw(2, 10, 3));
		
		builder.addLabel("Email:", cc.xy(2, 12));
		builder.add(textEmail, cc.xyw(2, 14, 3));
		
		builder.addLabel("Senha:", cc.xy(2, 16));
		builder.add(textSenha, cc.xyw(2, 18, 3));
		
		builder.add(labelEsqueceuSenha, cc.xy(3, 20));
		
		builder.add(buttonLogin, cc.xyw(2, 22, 3));
		
		builder.add(labelCadastro, cc.xy(3, 24));

		return builder.getPanel();
	}

	private void logar() {
		String email = textEmail.getText();
		String senhaDigitada = new String(textSenha.getPassword());
		
		usuarioVO = new UsuarioVO(email, senhaDigitada, "");


		if (email.isEmpty() || senhaDigitada.isEmpty()) {
			JOptionPane.showMessageDialog(LoginVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (controller.verificaUsuario(usuarioVO)) {
			MenuVisao m = new MenuVisao(controller, usuarioVO);
			m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			m.setBounds(100, 100, 1150, 720);
			m.setResizable(false);
			m.setVisible(true);
			m.setIconImage(logo.getImage());

			this.dispose();
		} else {
			JOptionPane.showMessageDialog(LoginVisao.this, "Email ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void logarGoogle() {
		logar();
	}

	private void logarFacebook() {
		logar();
	}
	
	private void esqueceuSenha() {
		JOptionPane.showMessageDialog(LoginVisao.this, "Senha alterada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void cadastro() {
		CadastroVisao l = new CadastroVisao(logo, controller);
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setBounds(100, 100, 320, 450);
		l.setIconImage(logo.getImage());
		
		this.dispose();
		l.setVisible(true);
	}
}
