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
import model.vo.BancoVO;
import model.vo.QuadroVO;

public class GerenciamentoBancoVisao extends JInternalFrame{
	
	private MoneyFlowController controller;
	
	private JTextField textPesquisa;
	
	private JComboBox<String> comboBoxFiltros;

	private JButton buttonAddBanco;
	private JButton buttonAlterBanco;
	private JButton buttonDelBanco;
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
	
	private ArrayList<BancoVO> bancos;
	
	private String emailUsuario;
	
	//construtor
	public GerenciamentoBancoVisao(JDesktopPane desktop, Color backgroundTelas, MoneyFlowController controller, String emailUsuario) {
		super("Gerenciar Bancos");
		
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
		
		buttonAddBanco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarBanco();
			}
		});
		
		buttonAlterBanco.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarBanco();
			}
		});
		
		buttonDelBanco.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarBanco();
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
					buttonAlterBanco.setEnabled(true);
					buttonDelBanco.setEnabled(true);
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
		tableModel.addColumn("Valor Total (R$)");

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
	
		buttonAddBanco = new JButton();
		buttonAlterBanco = new JButton();
		buttonDelBanco = new JButton();
		buttonVoltar = new JButton();

		iconVoltar = new ImageIcon("icons/voltar.png");
		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");
		
		buttonAddBanco.setIcon(iconAdd);
		buttonAlterBanco.setIcon(iconAlter);
		buttonDelBanco.setIcon(iconDelete);
		buttonVoltar.setIcon(iconVoltar);

		buttonAddBanco.setToolTipText("Adicionar Novo Banco");
		buttonAlterBanco.setToolTipText("Alterar Banco Existente");
		buttonDelBanco.setToolTipText("Deletar Banco Existente");
	}

	//metodo responsavel por montar o painel 
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"5dlu, p, 5dlu, p:grow, 5dlu, p:grow, 150dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, p, 5dlu");
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(barraRolagem, cc.xywh(2, 4, 6, 7));

		builder.addLabel("Pesquisa:", cc.xy(2, 2));
		builder.add(textPesquisa, cc.xy(4, 2));
		
		builder.add(comboBoxFiltros, cc.xy(6, 2));

		builder.add(buttonAddBanco, cc.xy(9, 4));
		builder.add(buttonAlterBanco, cc.xy(9, 6));
		builder.add(buttonDelBanco, cc.xy(9, 8));
		builder.add(buttonVoltar, cc.xy(9, 10));

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
		
		buttonAlterBanco.setEnabled(false);
		buttonDelBanco.setEnabled(false);
		
		tableModel.setNumRows(0);
		
		bancos = controller.getBancos(emailUsuario, pesquisa, filtro);
		
		for(BancoVO b : bancos)
			tableModel.addRow(new Object[] {
					b.getCodigo(), b.getDescricao(), b.getValorTotal()
			});
	}
	
	private void adicionarBanco() {
		AdicionarBancoVisao a = new AdicionarBancoVisao(GerenciamentoBancoVisao.this, controller, emailUsuario);
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();
	}
	
	private void alterarBanco() {
		AlterarBancoVisao a = new AlterarBancoVisao(GerenciamentoBancoVisao.this, controller, bancos.get(table.getSelectedRow()));
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();		
	}
	
	private void deletarBanco() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoBancoVisao.this, "Tem certeza que deseja excluir o Banco?\nAs Movimentações associadas serão deletadas", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) {
    		int codigoBanco = bancos.get(table.getSelectedRow()).getCodigo();
    		
    		controller.atualizaValoresMeta(emailUsuario);
    		controller.atualizaValoresQuadro(emailUsuario);
    		
    		controller.delMovimentacaoBanco(emailUsuario, codigoBanco);
    		if(controller.delBanco(codigoBanco)) {
    			JOptionPane.showMessageDialog(GerenciamentoBancoVisao.this, "Banco deletado com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    		}else {
    			JOptionPane.showMessageDialog(GerenciamentoBancoVisao.this, "Erro ao deletar Banco", "", JOptionPane.ERROR_MESSAGE);
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