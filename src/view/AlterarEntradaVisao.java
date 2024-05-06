package view;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import controller.MoneyFlowController;
import model.vo.BancoVO;
import model.vo.CategoriaVO;
import model.vo.MetaVO;
import model.vo.MovimentacaoVO;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;

public class AlterarEntradaVisao extends JInternalFrame {
	
	private MoneyFlowController controller;
	
	private MovimentacaoVO movimentacaoVO;

    private JTextField textCodigo;
    private JTextField textDescricao;
    private JTextField textValor;
    private JDateChooser dateChooserEntrada;

    private JComboBox<String> comboBanco;
    private JComboBox<String> comboMeta;
    private JComboBox<String> comboCategoria;
    
    private ArrayList<BancoVO> bancos;
    private ArrayList<MetaVO> metas;
    private ArrayList<CategoriaVO> categorias;

    private JButton buttonCancelar;
    private JButton buttonSalvar;
    
    private GerenciamentoEntradaSaidaVisao gerenciamentoEntradaSaidaVisao;
    
    private int codigoQuadro;
    
    private String emailUsuario;

    public AlterarEntradaVisao(GerenciamentoEntradaSaidaVisao gerenciamentoEntradaSaidaVisao, MoneyFlowController controller, MovimentacaoVO movimentacaoVO, int codigoQuadro, String emailUsuario) {
        super("Alterar Entrada");
        
        this.gerenciamentoEntradaSaidaVisao = gerenciamentoEntradaSaidaVisao;
        this.controller = controller;
        this.movimentacaoVO = movimentacaoVO;
        this.codigoQuadro = codigoQuadro;
        this.emailUsuario = emailUsuario;

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Java Swing Nimbus
        } catch (Exception e) {
        }

        // Chama o método responsável por inicializar os componentes
        inicializaComponentes();
        
        // Chama o método responsável por completar os campos
        preencherCampos();

        /* Monta todo o layout e em seguida o adiciona */
        this.getContentPane().add(this.montaPainel());

        buttonSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvar(); // Fecha o JFrame
            }
        });

        buttonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelar(); // Fecha o JFrame
            }
        });
        
        textValor.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(!controller.ehNumero(textValor.getText())) {
					JOptionPane.showMessageDialog(AlterarEntradaVisao.this, "Entre com um valor numérico", "", JOptionPane.ERROR_MESSAGE);
					textValor.requestFocus();
					textValor.selectAll();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
    }

    private void inicializaComponentes() {
        textCodigo = new JTextField();
        textDescricao = new JTextField();
        textValor = new JTextField();
        
        textCodigo.setEnabled(false);

        textCodigo.setEditable(false);

        buttonCancelar = new JButton("Cancelar");
        buttonSalvar = new JButton("Salvar");

        comboBanco = new JComboBox<>();
        comboMeta = new JComboBox<>();
        comboCategoria = new JComboBox<>();
        
        bancos = controller.getBancos(emailUsuario, "", "descricao");
        metas = controller.getMetas(emailUsuario, "", "descricao");
        categorias = controller.getCategorias(emailUsuario, "", "descricao");
        
        metas.add(new MetaVO(-1, "Sem Meta"));
        
        for(BancoVO b : bancos)
        	comboBanco.addItem(b.getDescricao());
        
        for(MetaVO m : metas)
        	comboMeta.addItem(m.getDescricao());
        
        for(CategoriaVO c : categorias)
        	comboCategoria.addItem(c.getDescricao());

        dateChooserEntrada = new JDateChooser();
        dateChooserEntrada.setDate(new Date());
    }

    private JComponent montaPainel() {
        FormLayout layout = new FormLayout(
        		"10dlu, p:grow, 10dlu",
                "10dlu:grow, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 10dlu:grow");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        CellConstraints cc = new CellConstraints();

        builder.addLabel("Código:", cc.xy(2, 2));
        builder.add(textCodigo, cc.xy(2, 4));

        builder.addLabel("Descrição:", cc.xy(2, 6));
        builder.add(textDescricao, cc.xy(2, 8));

        builder.addLabel("Valor:", cc.xy(2, 10));
        builder.add(textValor, cc.xy(2, 12));

        builder.addLabel("Banco:", cc.xy(2, 14));
        builder.add(comboBanco, cc.xy(2, 16));

        builder.addLabel("Meta:", cc.xy(2, 18));
        builder.add(comboMeta, cc.xy(2, 20));
        
        builder.addLabel("Categoria:", cc.xy(2, 22));
        builder.add(comboCategoria, cc.xy(2, 24));

        builder.addLabel("Data Entrada:", cc.xy(2, 26));
        builder.add(dateChooserEntrada, cc.xy(2, 28));

        builder.add(buttonCancelar, cc.xy(2, 30));
        builder.add(buttonSalvar, cc.xy(2, 32));

        return builder.getPanel();
    }
    
    private void preencherCampos() {
    	movimentacaoVO = controller.getMovimentacao(movimentacaoVO.getCodigo());
    	
    	textCodigo.setText(movimentacaoVO.getCodigo() + "");
    	textDescricao.setText(movimentacaoVO.getDescricao());
    	textValor.setText(movimentacaoVO.getValor_total() + "");
    	
    	int aux = 0;
    	for(BancoVO b : bancos){
    		if(b.getCodigo() == movimentacaoVO.getCodigoBanco()) {
    			comboBanco.setSelectedIndex(aux);
    			break;
    		}
    		
    		aux++;
    	}
        
    	aux = 0;
    	for(MetaVO m : metas){
    		if(m.getCodigo() == movimentacaoVO.getCodigoMeta()) {
    			comboMeta.setSelectedIndex(aux);
    			break;
    		}
    		
    		aux++;
    	}
    	
    	aux = 0;
    	for(CategoriaVO c : categorias){
    		if(c.getCodigo() == movimentacaoVO.getCodigoCategoria()) {
    			comboCategoria.setSelectedIndex(aux);
    			break;
    		}
    		
    		aux++;
    	}
    	
    	dateChooserEntrada.setDate(movimentacaoVO.getDataOcorrencia());
    }

    private void salvar() {
    	if(!controller.ehNumero(textValor.getText()))
    		return;
    	
    	if (textDescricao.getText().isEmpty()) {
			JOptionPane.showMessageDialog(AlterarEntradaVisao.this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
    	
    	int codigoBanco = bancos.get(comboBanco.getSelectedIndex()).getCodigo();
    	int codigoCategoria = categorias.get(comboCategoria.getSelectedIndex()).getCodigo();
    	int codigoMeta = metas.get(comboMeta.getSelectedIndex()).getCodigo();
    	
    	movimentacaoVO = new MovimentacaoVO();
    	
    	movimentacaoVO.setDescricao(textDescricao.getText());
    	movimentacaoVO.setDataOcorrencia(dateChooserEntrada.getDate());

    	String valor = textValor.getText();

		valor = valor.replace(",", ".");
		valor = valor.trim();
		
    	movimentacaoVO.setValor_total(Float.parseFloat(valor));
    	
    	movimentacaoVO.setEntrada(true);
    	movimentacaoVO.setCodigoQuadro(codigoQuadro);
    	movimentacaoVO.setCodigoCategoria(codigoCategoria);
    	movimentacaoVO.setCodigoBanco(codigoBanco);
    	movimentacaoVO.setCodigoMeta(codigoMeta);
    	movimentacaoVO.setCodigo(Integer.parseInt(textCodigo.getText()));
    	
    	if(controller.alterMovimentacao(emailUsuario, movimentacaoVO))
    		JOptionPane.showMessageDialog(AlterarEntradaVisao.this, "Entrada alterada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    	else
    		JOptionPane.showMessageDialog(AlterarEntradaVisao.this, "Erro ao alterar Entrada", "", JOptionPane.ERROR_MESSAGE);
    	
    	voltar();
    }
    
    private void cancelar() {
    	int opc = JOptionPane.showConfirmDialog(AlterarEntradaVisao.this, "Tem certeza que deseja cancelar?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) 
    		voltar();
    }
    
    private void voltar() {
    	this.dispose();
    	gerenciamentoEntradaSaidaVisao.atualizaValores();
    	gerenciamentoEntradaSaidaVisao.atualizaTabela();
    	gerenciamentoEntradaSaidaVisao.setVisible(true);
    }
    
    public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}