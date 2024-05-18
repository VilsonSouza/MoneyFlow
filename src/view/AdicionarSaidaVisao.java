package view;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;
import com.toedter.components.JSpinField;

import controller.MoneyFlowController;
import model.vo.BancoVO;
import model.vo.CategoriaVO;
import model.vo.MovimentacaoVO;

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
import java.util.ArrayList;
import java.util.Date;

public class AdicionarSaidaVisao extends JInternalFrame {
	
	private MoneyFlowController controller;

    private JTextField textCodigo;
    private JTextField textDescricao;
    private JTextField textValor;

    private JSpinField spinParcelas;
    
    private JDateChooser dateChooserDataSaida;

    private JButton buttonCancelar;
    private JButton buttonSalvar;
    
	private ImageIcon iconCancelar;
	private ImageIcon iconSalvar;
    
    private JComboBox<String> comboBanco;
    private JComboBox<String> comboCategoria;
    
    private ArrayList<BancoVO> bancos;
    private ArrayList<CategoriaVO> categorias;

    private GerenciamentoEntradaSaidaVisao gerenciamentoEntradaSaidaVisao;
    
    private int codigoQuadro;
    
    private MovimentacaoVO movimentacaoVO;
    
    private String emailUsuario;
    
    public AdicionarSaidaVisao(GerenciamentoEntradaSaidaVisao gerenciamentoEntradaSaidaVisao, MoneyFlowController controller, int codigoQuadro, String emailUsuario) {
        super("Adicionar Saída");
        
        this.gerenciamentoEntradaSaidaVisao = gerenciamentoEntradaSaidaVisao;
        this.controller = controller;
        this.codigoQuadro = codigoQuadro;
        this.emailUsuario = emailUsuario;

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	// Java Swing Nimbus
        } catch (Exception e) {
        }

        // Chama o método responsável por inicializar os componentes
        inicializaComponentes();

        /* Monta todo o layout e em seguida o adiciona */
        this.getContentPane().add(this.montaPainel());

        buttonSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvar();
            }
        });
        
        buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});
        
    	textValor.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(!controller.ehNumero(textValor.getText())) {
					JOptionPane.showMessageDialog(AdicionarSaidaVisao.this, "Entre com um valor numérico", "", JOptionPane.ERROR_MESSAGE);
					textValor.requestFocus();
					textValor.selectAll();
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
        textValor = new JTextField();
        
        textCodigo.setEnabled(false);
        textValor.setText("0");

        spinParcelas = new JSpinField();
        spinParcelas.setMinimum(1);
        spinParcelas.setValue(1);
        
        buttonCancelar = new JButton();
        buttonSalvar = new JButton();
        
        buttonCancelar.setToolTipText("Cancelar");
        buttonSalvar.setToolTipText("Salvar");
        
		iconCancelar = new ImageIcon("icons/voltar.png");
		iconSalvar = new ImageIcon("icons/save.png");

		buttonCancelar.setIcon(iconCancelar);
		buttonSalvar.setIcon(iconSalvar);
        
        comboBanco = new JComboBox<>();
        comboCategoria = new JComboBox<>();
        
        bancos = controller.getBancos(emailUsuario, "", "descricao");
        categorias = controller.getCategorias(emailUsuario, "", "descricao");
        
        for(BancoVO b : bancos)
        	comboBanco.addItem(b.getDescricao());
        
        for(CategoriaVO c : categorias)
        	comboCategoria.addItem(c.getDescricao());

        dateChooserDataSaida = new JDateChooser();
        dateChooserDataSaida.setDate(new Date());
    }

    private JComponent montaPainel() {
        FormLayout layout = new FormLayout(
                "10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 15dlu, p, 10dlu:grow");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Código:", cc.xy(2, 2));
        builder.add(textCodigo, cc.xy(2, 4));

        builder.addLabel("Descrição:", cc.xy(2, 6));
        builder.add(textDescricao, cc.xy(2, 8));
        
        builder.addLabel("Valor Total:", cc.xy(2, 10));
        builder.add(textValor, cc.xy(2, 12));
        
        builder.addLabel("Banco:", cc.xy(2, 14));
        builder.add(comboBanco, cc.xy(2, 16));
        
        builder.addLabel("Parcelas:", cc.xy(2, 18));
        builder.add(spinParcelas, cc.xy(2, 20));
        
        builder.addLabel("Categoria:", cc.xy(2, 22));
        builder.add(comboCategoria, cc.xy(2, 24));

        builder.addLabel("Data de Saída (Primeira Parcela):", cc.xy(2, 26));
        builder.add(dateChooserDataSaida, cc.xy(2, 28));

        builder.add(montaBarraBotao(), cc.xy(2, 30));

        return builder.getPanel();
    }

    private void salvar() {
    	if(!controller.ehNumero(textValor.getText()))
    		return;
    	
    	if (textDescricao.getText().isEmpty()) {
			JOptionPane.showMessageDialog(AdicionarSaidaVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
    	
    	int codigoBanco = bancos.get(comboBanco.getSelectedIndex()).getCodigo();
    	int codigoCategoria = categorias.get(comboBanco.getSelectedIndex()).getCodigo();
    	
    	movimentacaoVO = new MovimentacaoVO();
    	
    	movimentacaoVO.setDescricao(textDescricao.getText());
    	
    	String valor = textValor.getText();

		valor = valor.replace(",", ".");
		valor = valor.trim();
		
    	movimentacaoVO.setValor_total(Float.parseFloat(valor));
    	movimentacaoVO.setCodigoBanco(codigoBanco);
    	movimentacaoVO.setCodigoCategoria(codigoCategoria);
    	movimentacaoVO.setEntrada(false);
    	movimentacaoVO.setDataOcorrencia(dateChooserDataSaida.getDate());
    	movimentacaoVO.setQtdParcelas(spinParcelas.getValue());
    	movimentacaoVO.setCodigoQuadro(codigoQuadro);
    	
    	if(controller.addMovimentacao(emailUsuario, movimentacaoVO))
    		JOptionPane.showMessageDialog(AdicionarSaidaVisao.this, "Saída adicionada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    	else
    		JOptionPane.showMessageDialog(AdicionarSaidaVisao.this, "Erro ao adicionar Saída", "", JOptionPane.ERROR_MESSAGE);
    	
    	voltar();
    }
    
    private void cancelar() {
    	int opc = JOptionPane.showConfirmDialog(AdicionarSaidaVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) 
    		voltar();
    }

    private void voltar() {
        this.dispose();
        gerenciamentoEntradaSaidaVisao.atualizaValores();
        gerenciamentoEntradaSaidaVisao.atualizaTabela();
        gerenciamentoEntradaSaidaVisao.setVisible(true);
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
