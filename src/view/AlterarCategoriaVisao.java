package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.CategoriaVO;

public class AlterarCategoriaVisao extends JInternalFrame {
	
	private MoneyFlowController controller;

    private JTextField textCodigo;
    private JTextField textDescricao;
    private JButton buttonCancelar;
    private JButton buttonSalvar;
        
    private GerenciamentoCategoriaVisao gerenciarCategoriaVisao;
    
    private CategoriaVO categoriaVO;

    public AlterarCategoriaVisao(GerenciamentoCategoriaVisao gerenciarCategoriaVisao, MoneyFlowController controller, CategoriaVO categoriaVO) {
    	super("Alterar Categoria");
    	
    	this.gerenciarCategoriaVisao = gerenciarCategoriaVisao;
    	this.controller = controller;
    	this.categoriaVO = categoriaVO;

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
    }

    private void inicializaComponentes() {
        textCodigo = new JTextField();
        textDescricao = new JTextField();
        buttonCancelar = new JButton("Cancelar");
        buttonSalvar = new JButton("Salvar");
        
        textCodigo.setEnabled(false);
    }

    private JComponent montaPainel() {
        FormLayout layout = new FormLayout(
                "10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 10dlu:grow"
        );
        
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Código:", cc.xy(2, 2));
        builder.add(textCodigo, cc.xy(2, 4));

        builder.addLabel("Descrição:", cc.xy(2, 6));
        builder.add(textDescricao, cc.xy(2, 8));

        builder.add(buttonCancelar, cc.xy(2, 10));
        builder.add(buttonSalvar, cc.xy(2, 12));

        return builder.getPanel();
    }
    
    private void preencherCampos() {
    	textCodigo.setText(categoriaVO.getCodigo() + "");
    	textDescricao.setText(categoriaVO.getDescricao());
    }
    
    private void salvar() {
    	if (textDescricao.getText().isEmpty()) {
			JOptionPane.showMessageDialog(AlterarCategoriaVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
    	
    	categoriaVO.setCodigo(Integer.parseInt(textCodigo.getText()));
    	categoriaVO.setDescricao(textDescricao.getText());
    	
    	if(controller.alterCategoria(categoriaVO))
    		JOptionPane.showMessageDialog(AlterarCategoriaVisao.this, "Categoria alterada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    	else
    		JOptionPane.showMessageDialog(AlterarCategoriaVisao.this, "Erro ao alterar Categoria", "", JOptionPane.ERROR_MESSAGE);
    	
    	voltar();
    }
    
    private void cancelar() {
    	int opc = JOptionPane.showConfirmDialog(AlterarCategoriaVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) 
    		voltar();
    }
    
    private void voltar() {
    	this.dispose();
    	gerenciarCategoriaVisao.atualizaTabela();
    	gerenciarCategoriaVisao.setVisible(true);
    }
    
    public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}

