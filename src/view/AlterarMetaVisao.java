package view;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.MetaVO;

import javax.swing.*;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AlterarMetaVisao extends JInternalFrame {
	
	private MoneyFlowController controller;
	
	private MetaVO metaVO;

    private JTextField textCodigo;
    private JTextField textDescricao;
    private JTextField textValorEsperado;
    private JTextField textValorArrecadado;
    
    private JButton buttonCancelar;
    private JButton buttonSalvar;
    
	private ImageIcon iconCancelar;
	private ImageIcon iconSalvar;
    
    private GerenciamentoMetasVisao gerenciamentoMetasVisao;
    
    private String emailUsuario;

    public AlterarMetaVisao(GerenciamentoMetasVisao gerenciamentoMetasVisao, MoneyFlowController controller, MetaVO metaVO, String emailUsuario) {
        super("Alterar Meta");
                
        this.controller = controller;
        this.gerenciamentoMetasVisao = gerenciamentoMetasVisao;
        this.metaVO = metaVO;
        this.emailUsuario = emailUsuario;
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	// Java Swing Nimbus
        } catch (Exception e) {
        }

        // Chama o método responsável por inicializar os componentes
        inicializaComponentes();

        /* Monta todo o layout e em seguida o adiciona */
        this.getContentPane().add(this.montaPainel());
        
        // Chama o método responsável por completar os campos
        preencherCampos();
        
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
        
        textValorArrecadado.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(!controller.ehNumero(textValorArrecadado.getText())) {
					JOptionPane.showMessageDialog(AlterarMetaVisao.this, "Entre com um valor numérico", "", JOptionPane.ERROR_MESSAGE);
					textValorArrecadado.requestFocus();
					textValorArrecadado.selectAll();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        textValorEsperado.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(!controller.ehNumero(textValorEsperado.getText())) {
					JOptionPane.showMessageDialog(AlterarMetaVisao.this, "Entre com um valor numérico", "", JOptionPane.ERROR_MESSAGE);
					textValorEsperado.requestFocus();
					textValorEsperado.selectAll();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
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
        textValorEsperado = new JTextField();
        textValorArrecadado = new JTextField();
        
        textCodigo.setEnabled(false);
        textValorArrecadado.setEnabled(false);
        
        buttonCancelar = new JButton();
        buttonSalvar = new JButton();
        
        buttonCancelar.setToolTipText("Cancelar");
        buttonSalvar.setToolTipText("Salvar");
        
		iconCancelar = new ImageIcon("icons/voltar.png");
		iconSalvar = new ImageIcon("icons/save.png");

		buttonCancelar.setIcon(iconCancelar);
		buttonSalvar.setIcon(iconSalvar);
        
        textCodigo.setEnabled(false);
    }

    private JComponent montaPainel() {
    	FormLayout layout = new FormLayout(
                "10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 15dlu, p, 10dlu:grow");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Código:", cc.xy(2, 2));
        builder.add(textCodigo, cc.xy(2, 4));

        builder.addLabel("Descrição:", cc.xy(2, 6));
        builder.add(textDescricao, cc.xy(2, 8));

        builder.addLabel("Valor Esperado:", cc.xy(2, 10));
        builder.add(textValorEsperado, cc.xy(2, 12));
        
        builder.addLabel("Valor Arrecadado:", cc.xy(2, 14));
        builder.add(textValorArrecadado, cc.xy(2, 16));

        builder.add(montaBarraBotao(), cc.xy(2, 18));

        return builder.getPanel();
    }
    
    private void preencherCampos() {
    	textCodigo.setText(metaVO.getCodigo() + "");
    	textDescricao.setText(metaVO.getDescricao());
    	textValorArrecadado.setText(metaVO.getValorArrecadado() + "");
    	textValorEsperado.setText(metaVO.getValorEsperado() + "");
    }
    
    private void salvar() {
    	if(!(controller.ehNumero(textValorArrecadado.getText()) || controller.ehNumero(textValorEsperado.getText())))
    		return;
    	
    	if (textDescricao.getText().isEmpty() || textValorArrecadado.getText().isEmpty() || textValorEsperado.getText().isEmpty()) {
			JOptionPane.showMessageDialog(AlterarMetaVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
    	
    	metaVO.setDescricao(textDescricao.getText());
    	metaVO.setValorEsperado(Float.parseFloat(textValorEsperado.getText()));
    	
    	if(controller.alterMeta(emailUsuario, metaVO))
    		JOptionPane.showMessageDialog(AlterarMetaVisao.this, "Meta alterada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    	else
    		JOptionPane.showMessageDialog(AlterarMetaVisao.this, "Erro ao alterar Meta", "", JOptionPane.ERROR_MESSAGE);
    	
    	voltar();
    }
    
    private void cancelar() {
    	int opc = JOptionPane.showConfirmDialog(AlterarMetaVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) 
    		voltar();
    }
    
    private void voltar() {
    	this.dispose();
    	gerenciamentoMetasVisao.atualizaTabela();
    	gerenciamentoMetasVisao.setVisible(true);
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