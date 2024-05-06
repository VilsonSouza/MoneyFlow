package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import controller.MoneyFlowController;
import model.vo.MetaVO;

public class GerenciamentoMetasVisao extends JInternalFrame{
	
	private MoneyFlowController controller;
	
	private JTextField textPesquisa;
	
	private JComboBox<String> comboBoxFiltros;

	private JButton buttonAddMeta;
	private JButton buttonAlterMeta;
	private JButton buttonDelMeta;
	private JButton buttonVoltar;
	
	private ImageIcon iconAdd;
	private ImageIcon iconAlter;
	private ImageIcon iconDelete;
	private ImageIcon iconVoltar;

	private DefaultTableModel tableModel;

	private JTable table;

	private JScrollPane barraRolagem;
	
	private JDesktopPane desktop;
	
	private Color backgroundTelas;
	
	private ArrayList<MetaVO> metas;
	
	private String emailUsuario;

	//construtor
	public GerenciamentoMetasVisao(JDesktopPane desktop, Color backgroundTelas, MoneyFlowController controller, String emailUsuario) {
		super("Gerenciar Metas");
		
		this.desktop = desktop;
		this.backgroundTelas = backgroundTelas;
		this.controller = controller;
		this.emailUsuario = emailUsuario;

		try { 

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	
		} 
		catch (Exception e) 
		{ }

		criaJTable();

		inicializaComponentes();
		
		atualizaTabela();

		this.getContentPane().add(this.montaPainel());
		
		buttonAddMeta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarMeta();
			}
		});
		
		buttonAlterMeta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarMeta();
			}
		});
		
		buttonDelMeta.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarMeta();
			}
		});
		
		buttonVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(table.getSelectedRowCount() == 1) {
					buttonAlterMeta.setEnabled(true);
					buttonDelMeta.setEnabled(true);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		textPesquisa.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				atualizaTabela();
			}

			public void keyPressed(KeyEvent e) {
			}
		});
		
		comboBoxFiltros.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				atualizaTabela();
			}
		});
	}

	//metodo responsavel por criar a JTable
	private void criaJTable() {
		//inicializa a JTable
		table = new JTable();

		//mas a JTable precisa de algo para manipular seus dados(inserir linha, excluir...)
		tableModel = new DefaultTableModel() {
			//por padrao o DefaultTableModel permite fazer alteracoes na JTable, por isso precisamos 
			//sobrecarregar seu metodo dizendo que nenhuma das linhas podera ser alterada
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//definindo que o tableModel seta responsavel por manipular os dados da JTable
		table.setModel(tableModel);

		//adicionando todas as colunas da tabela
		tableModel.addColumn("Código");
		tableModel.addColumn("Descrição");
		tableModel.addColumn("Valor da Meta (R$)");
		tableModel.addColumn("Valor Arrecadado (R$)");	
		tableModel.addColumn("Valor Restante (R$)");		

		//setando o numero de linhas da JTable inicialmente como 0
		tableModel.setNumRows(0);
	}

	//metodo responsavel por inicializar os componentes
	private void inicializaComponentes() {
		//inicializando e adicionando a tabela a um ScrollPane, case precise de uma barra de rolagem
		barraRolagem = new JScrollPane(table);
		
		textPesquisa = new JTextField();
		
		comboBoxFiltros = new JComboBox<>();
		comboBoxFiltros.addItem("Código");
		comboBoxFiltros.addItem("Descrição");
	
		buttonAddMeta = new JButton();
		buttonAlterMeta = new JButton();
		buttonDelMeta = new JButton();
		buttonVoltar = new JButton();

		iconVoltar = new ImageIcon("icons/voltar.png");
		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");
		
		buttonAddMeta.setIcon(iconAdd);
		buttonAlterMeta.setIcon(iconAlter);
		buttonDelMeta.setIcon(iconDelete);
		buttonVoltar.setIcon(iconVoltar);

		buttonAddMeta.setToolTipText("Adicionar Nova Meta");
		buttonAlterMeta.setToolTipText("Alterar Meta Existente");
		buttonDelMeta.setToolTipText("Deletar Meta Existente");
	}

	//metodo responsavel por montar o painel 
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"5dlu, p, 5dlu, p:grow, 5dlu, p:grow, 150dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 150dlu:grow, 5dlu");
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(barraRolagem, cc.xywh(2, 4, 6, 6));

		builder.addLabel("Pesquisa:", cc.xy(2, 2));
		builder.add(textPesquisa, cc.xy(4, 2));
		
		builder.add(comboBoxFiltros, cc.xy(6, 2));

		builder.add(buttonAddMeta, cc.xy(9, 4));
		builder.add(buttonAlterMeta, cc.xy(9, 6));
		builder.add(buttonDelMeta, cc.xy(9, 8));

		return builder.getPanel();
	}

	public void atualizaTabela() {
		String pesquisa = textPesquisa.getText();

		if (pesquisa == null)
			pesquisa = "";
		
		String filtro = "";
		if(comboBoxFiltros.getSelectedIndex() == 0)
			filtro = "codigo";
		else
			filtro = "descricao";
		
		buttonAlterMeta.setEnabled(false);
		buttonDelMeta.setEnabled(false);
		
		tableModel.setNumRows(0);
		
		metas = controller.getMetas(emailUsuario, pesquisa, filtro);
		
		for(MetaVO m : metas) {
			tableModel.addRow(new Object[] {
					m.getCodigo(), m.getDescricao(), m.getValorEsperado(), m.getValorArrecadado(), m.getValorRestante()
			});
		}
	}
	
	private void adicionarMeta() {
		AdicionarMetaVisao a = new AdicionarMetaVisao(GerenciamentoMetasVisao.this, controller, emailUsuario);
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();
	}
	
	private void alterarMeta() {
		AlterarMetaVisao a = new AlterarMetaVisao(GerenciamentoMetasVisao.this, controller, metas.get(table.getSelectedRow()), emailUsuario);
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();		
	}
	
	private void deletarMeta() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoMetasVisao.this, "Tem certeza que deseja excluir a Meta?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) {
    		int codigoMeta = metas.get(table.getSelectedRow()).getCodigo();
    		
    		controller.alterMovimentacaoMeta(emailUsuario, codigoMeta);
    		if(controller.delMeta(emailUsuario, codigoMeta)) {
    			JOptionPane.showMessageDialog(GerenciamentoMetasVisao.this, "Meta deletada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    		}else {
    			JOptionPane.showMessageDialog(GerenciamentoMetasVisao.this, "Erro ao deletar Meta", "", JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	
    	atualizaTabela();
	}
	
	private void voltar() {
		this.dispose();
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}