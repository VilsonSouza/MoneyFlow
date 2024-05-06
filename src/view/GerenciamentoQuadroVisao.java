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
import model.vo.QuadroVO;

public class GerenciamentoQuadroVisao extends JInternalFrame{
	
	MoneyFlowController controller;
	
	private JTextField textPesquisa;
	
	private JComboBox<String> comboBoxFiltros;

	private JButton buttonAddQuadro;
	private JButton buttonAlterQuadro;
	private JButton buttonDelQuadro;
	private JButton buttonAddDelMovimentacao;

	private ImageIcon iconAdd;
	private ImageIcon iconAlter;
	private ImageIcon iconDelete;
	private ImageIcon iconMovimentacoes;

	private DefaultTableModel tableModel;

	private JTable table;

	private JScrollPane barraRolagem;
	
	private JDesktopPane desktop;
	
	private Color backgroundTelas;
	
	private ArrayList<QuadroVO> quadros;
	
	private String emailUsuario;

	//construtor
	public GerenciamentoQuadroVisao(JDesktopPane desktop, Color backgroundTelas, MoneyFlowController controller, String emailUsuario) {
		super("Gerenciar Quadros");
		
		this.controller = controller;
		
		this.desktop = desktop;
		this.backgroundTelas = backgroundTelas;
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
		
		buttonAddDelMovimentacao.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gerenciarEntradasSaidas();
			}
		});
		
		buttonAddQuadro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarQuadro();
			}
		});
		
		buttonAlterQuadro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarQuadro();
			}
		});
		
		buttonDelQuadro.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarQuadro();
			}
		});
		
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(table.getSelectedRowCount() == 1) {
					buttonAlterQuadro.setEnabled(true);
					buttonDelQuadro.setEnabled(true);
					buttonAddDelMovimentacao.setEnabled(true);
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
		tableModel.addColumn("Total de Saídas (R$)");
		tableModel.addColumn("Total de Entradas (R$)");		

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
	
		buttonAddQuadro = new JButton();
		buttonAlterQuadro = new JButton();
		buttonDelQuadro = new JButton();
		buttonAddDelMovimentacao = new JButton();
		
		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");
		iconMovimentacoes = new ImageIcon("icons/list.png");
		
		buttonAddQuadro.setIcon(iconAdd);
		buttonAlterQuadro.setIcon(iconAlter);
		buttonDelQuadro.setIcon(iconDelete);
		buttonAddDelMovimentacao.setIcon(iconMovimentacoes);

		buttonAddQuadro.setToolTipText("Adicionar Novo Quadro");
		buttonAlterQuadro.setToolTipText("Alterar Quadro Existente");
		buttonDelQuadro.setToolTipText("Deletar Entrada Existente");
		buttonAddDelMovimentacao.setToolTipText("Gerenciar Entradas e Saídas");
	}

	//metodo responsavel por montar o painel 
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"5dlu, p, 5dlu, p:grow, 5dlu, p:grow, 50dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 150dlu:grow, 5dlu");

		// Agrupe as colunas para que tenham o mesmo tamanho
        layout.setColumnGroups(new int[][]{{4, 6}});
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(barraRolagem, cc.xywh(2, 4, 6, 8));

		builder.addLabel("Pesquisa:", cc.xy(2, 2));
		builder.add(textPesquisa, cc.xy(4, 2));
		
		builder.add(comboBoxFiltros, cc.xy(6, 2));

		builder.add(buttonAddDelMovimentacao, cc.xy(9, 4));
		builder.add(buttonAddQuadro, cc.xy(9, 6));
		builder.add(buttonAlterQuadro, cc.xy(9, 8));
		builder.add(buttonDelQuadro, cc.xy(9, 10));

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
		
		buttonAlterQuadro.setEnabled(false);
		buttonDelQuadro.setEnabled(false);
		buttonAddDelMovimentacao.setEnabled(false);
		
		tableModel.setNumRows(0);
		
		quadros = controller.getQuadros(emailUsuario, pesquisa, filtro);
		
		for(QuadroVO q : quadros)
			tableModel.addRow(new Object[] {
					q.getCodigo(), q.getDescricao(), q.getTotalSaida(), q.getTotalEntrada()
			});
	}
	
	private void gerenciarEntradasSaidas() {
		GerenciamentoEntradaSaidaVisao g = new GerenciamentoEntradaSaidaVisao(desktop, backgroundTelas, GerenciamentoQuadroVisao.this, quadros.get(table.getSelectedRow()), controller, emailUsuario);
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}
	
	private void adicionarQuadro() {
		AdicionarQuadroVisao a = new AdicionarQuadroVisao(GerenciamentoQuadroVisao.this, controller, emailUsuario);
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();
	}
	
	private void alterarQuadro() {
		AlterarQuadroVisao a = new AlterarQuadroVisao(GerenciamentoQuadroVisao.this, controller, quadros.get(table.getSelectedRow()));
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();		
	}
	
	private void deletarQuadro() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoQuadroVisao.this, "Tem certeza que deseja excluir o Quadro?\n Algumas Movimentações podem ser Excluídas", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) {
    		int codigoQuadro = quadros.get(table.getSelectedRow()).getCodigo();
			controller.delMovimentacaoQuadro(emailUsuario, codigoQuadro);
    		if(controller.delQuadro(codigoQuadro)) {
    			
    			JOptionPane.showMessageDialog(GerenciamentoQuadroVisao.this, "Quadro deletado com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    		}else {
    			JOptionPane.showMessageDialog(GerenciamentoQuadroVisao.this, "Erro ao deletar Quadro", "", JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	
    	atualizaTabela();
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}