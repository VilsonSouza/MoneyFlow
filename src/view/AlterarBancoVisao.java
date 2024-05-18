package view;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.BancoVO;

public class AlterarBancoVisao extends JInternalFrame {
	
	private MoneyFlowController controller;
	
	private BancoVO bancoVO;

    private JTextField textCodigo;
    private JTextField textDescricao;
    private JTextField textValorTotal;
    
    private JButton buttonCancelar;
    private JButton buttonSalvar;
    
	private ImageIcon iconCancelar;
	private ImageIcon iconSalvar;
    
    private GerenciamentoBancoVisao gerenciarBancoVisao;

    public AlterarBancoVisao(GerenciamentoBancoVisao gerenciarBancoVisao, MoneyFlowController controller, BancoVO bancoVO) {
    	super("Alterar Banco");
    	
    	this.gerenciarBancoVisao = gerenciarBancoVisao;
    	this.controller = controller;
    	this.bancoVO = bancoVO;

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	// Java Swing Nimbus
        }
        catch (Exception e)
        { }


        //chama o metodo rsponsavel por inicializar os componentes
        inicializaComponentes();
        
        // Chama o método responsável por completar os campos
        preencherCampos();

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
        
        buttonSalvar.addMouseListener(new MouseListener() {

			public void mouseEntered(MouseEvent e) {
				buttonSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Define o cursor para a mão
			}

			@Override
			public void mouseExited(MouseEvent e) {
				buttonSalvar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Define o cursor padrão
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

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
        
        buttonCancelar.addMouseListener(new MouseListener() {

			public void mouseEntered(MouseEvent e) {
				buttonCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Define o cursor para a mão
			}

			@Override
			public void mouseExited(MouseEvent e) {
				buttonCancelar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Define o cursor padrão
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

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
        textCodigo = new JTextField();
        textDescricao = new JTextField();
        textValorTotal = new JTextField();
        
        buttonCancelar = new JButton();
        buttonSalvar = new JButton();
        
        buttonCancelar.setToolTipText("Cancelar");
        buttonSalvar.setToolTipText("Salvar");
        
		iconCancelar = new ImageIcon("icons/voltar.png");
		iconSalvar = new ImageIcon("icons/save.png");

		buttonCancelar.setIcon(iconCancelar);
		buttonSalvar.setIcon(iconSalvar);
        
        textCodigo.setEnabled(false);
        textValorTotal.setEnabled(false);
    }

    private JComponent montaPainel() {
        FormLayout layout = new FormLayout(
                "10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 15dlu, p, 10dlu:grow"
        );
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Código:", cc.xy(2, 2));
        builder.add(textCodigo, cc.xy(2, 4));

        builder.addLabel("Descrição:", cc.xy(2, 6));
        builder.add(textDescricao, cc.xy(2, 8));
        
        builder.addLabel("Valor Total:", cc.xy(2, 10));
        builder.add(textValorTotal, cc.xy(2, 12));

        builder.add(montaBarraBotao(), cc.xy(2, 14));

        return builder.getPanel();
    }
    
    private void preencherCampos() {
    	textCodigo.setText(bancoVO.getCodigo() + "");
    	textDescricao.setText(bancoVO.getDescricao());
    	textValorTotal.setText(bancoVO.getValorTotal() + "");
    }
    
    private void salvar() {
    	if (textDescricao.getText().isEmpty()) {
			JOptionPane.showMessageDialog(AlterarBancoVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
    	
    	bancoVO = new BancoVO(Integer.parseInt(textCodigo.getText()), textDescricao.getText());
    	bancoVO.setValorTotal(Float.parseFloat(textValorTotal.getText()));
    	
    	if(controller.alterBanco(bancoVO))
    		JOptionPane.showMessageDialog(AlterarBancoVisao.this, "Banco alterado com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    	else
    		JOptionPane.showMessageDialog(AlterarBancoVisao.this, "Erro ao alterar Banco", "", JOptionPane.ERROR_MESSAGE);
    	
    	voltar();
    }
    
    private void cancelar() {
    	int opc = JOptionPane.showConfirmDialog(AlterarBancoVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) 
    		voltar();
    }
    
    private void voltar() {
    	this.dispose();
    	gerenciarBancoVisao.atualizaTabela();
    	gerenciarBancoVisao.setVisible(true);
    }
    
    // metodo responsavel por montar a barra de botoes que sera adicionada ao final da janela
 	private Component montaBarraBotao() {
 		return ButtonBarBuilder.create().addButton(buttonSalvar, buttonCancelar).build();
 	}
    
    public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}

