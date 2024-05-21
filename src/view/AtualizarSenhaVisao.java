package view;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import controller.MoneyFlowController;
import model.vo.UsuarioVO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtualizarSenhaVisao extends JFrame {

    private MoneyFlowController controller;

    private JPasswordField textSenha;
    private JPasswordField textConfirmarSenha;

    private JButton buttonCancelar;
    private JButton buttonSalvar;

    private LoginVisao loginVisao;

    private String emailUsuario;
    private UsuarioVO usuarioVO;

    public AtualizarSenhaVisao(LoginVisao loginVisao, MoneyFlowController controller, String emailUsuario) {
        super("Atualizar Senha");

        this.loginVisao = loginVisao;
        this.controller = controller;
        this.emailUsuario = emailUsuario;

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");    // Java Swing Nimbus
        } catch (Exception e) {
        }

        //chama o metodo rsponsavel por inicializar os componentes
        inicializaComponentes();

        //monta todo o layout e em seguida o adiciona
        this.getContentPane().add(this.montaPainel());

        buttonSalvar.addActionListener(new ActionListener() {
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
        textSenha = new JPasswordField();
        textConfirmarSenha = new JPasswordField();
        buttonCancelar = new JButton("Cancelar");
        buttonSalvar = new JButton("Salvar");


    }

    private JComponent montaPainel() {
        FormLayout layout = new FormLayout(
                "10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 10dlu:grow"
        );

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Senha:", cc.xy(2, 2));
        builder.add(textSenha, cc.xy(2, 4));
        builder.addLabel("Confirmar Senha:", cc.xy(2, 6));
        builder.add(textConfirmarSenha, cc.xy(2, 8));


        builder.add(buttonCancelar, cc.xy(2, 10));
        builder.add(buttonSalvar, cc.xy(2, 12));

        return builder.getPanel();
    }

    private void salvar() {
        if (textSenha.getText().isEmpty() || textConfirmarSenha.getText().isEmpty()) {
            JOptionPane.showMessageDialog(AtualizarSenhaVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!textSenha.getText().equals(textConfirmarSenha.getText())) {
            JOptionPane.showMessageDialog(AtualizarSenhaVisao.this, "Por favor, digite senhas iguais.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UsuarioVO usuario = controller.getUsuario(emailUsuario);
        if (usuario != null) {
            usuario.setSenha(textSenha.getText());
            controller.alterUsuario(usuario);
            JOptionPane.showMessageDialog(AtualizarSenhaVisao.this, "Senha atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            voltar();
        } else {
            JOptionPane.showMessageDialog(AtualizarSenhaVisao.this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cancelar() {
        int opc = JOptionPane.showConfirmDialog(AtualizarSenhaVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);

        if(opc == 0)
            voltar();
    }

    private void voltar() {
        this.dispose();
        loginVisao.setVisible(true);
    }
}
