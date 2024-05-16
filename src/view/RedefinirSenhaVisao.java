package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.QuadroVO;

public class RedefinirSenhaVisao extends JFrame {
	
	private MoneyFlowController controller;
	
	private JTextField textEmail;

    private JButton buttonCancelar;
    private JButton buttonEnviarCodigo;
    
    private LoginVisao loginVisao;
    
    private String emailUsuario;

    public RedefinirSenhaVisao(LoginVisao loginVisao, MoneyFlowController controller) {
    	super("Redefinir Senha");
    	
    	this.loginVisao = loginVisao;
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
        
        buttonEnviarCodigo.addActionListener(new ActionListener() {
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
    }

    private void inicializaComponentes() {
        textEmail = new JTextField();
        buttonCancelar = new JButton("Cancelar");
        buttonEnviarCodigo = new JButton("Enviar Código");
        
        
    }

    private JComponent montaPainel() {
        FormLayout layout = new FormLayout(
                "10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 10dlu:grow"
        );
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Email:", cc.xy(2, 2));
        builder.add(textEmail, cc.xy(2, 4));


        builder.add(buttonCancelar, cc.xy(2, 6));
        builder.add(buttonEnviarCodigo, cc.xy(2, 8));

        return builder.getPanel();
    }
    
    private void salvar() {
    	if (textEmail.getText().isEmpty()) {
			JOptionPane.showMessageDialog(RedefinirSenhaVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
    	
    	String codigo = controller.enviarRedefinicaoSenha(textEmail.getText());
    	
    	this.setVisible(false);

		String userInput = JOptionPane.showInputDialog(null, "Digite o código enviado no seu email:");

		if (userInput != null && !userInput.isEmpty() && codigo != null) {
			System.out.println("Deu certo");
			
		}else {
			JOptionPane.showMessageDialog(this, "Erro ao redefinir senha\nRe-envie o código e tente novamente",
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    private void cancelar() {
    	int opc = JOptionPane.showConfirmDialog(RedefinirSenhaVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) 
    		voltar();
    }
    
    private void voltar() {
    	this.dispose();
    	loginVisao.setVisible(true);
    }
}

