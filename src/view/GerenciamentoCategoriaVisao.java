package view;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import controller.MoneyFlowController;
import model.vo.CategoriaVO;
import view.GerenciamentoBancoVisao.TableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GerenciamentoCategoriaVisao extends JInternalFrame {

	private MoneyFlowController controller;

	private JTextField textPesquisa;

	private JComboBox<String> comboBoxFiltros;

	private JButton buttonAddCategoria;
	private JButton buttonAlterCategoria;
	private JButton buttonDelCategoria;
	private JButton buttonVoltar;

	private DefaultTableModel tableModel;
	private ImageIcon iconAdd;
	private ImageIcon iconAlter;
	private ImageIcon iconDelete;
	private ImageIcon iconVoltar;

	private JTable table;

	private JScrollPane barraRolagem;

	private JDesktopPane desktop;
	
	private TableRowSorter tableSorter;

	private Color backgroundTelas;

	private ArrayList<CategoriaVO> categorias;

	private String emailUsuario;

	public GerenciamentoCategoriaVisao(JDesktopPane desktop, Color backgroundTelas, MoneyFlowController controller,
			String emailUsuario) {
		super("Gerenciar Categorias");

		this.desktop = desktop;
		this.backgroundTelas = backgroundTelas;
		this.controller = controller;
		this.emailUsuario = emailUsuario;

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		criaJTable();
		inicializaComponentes();
		atualizaTabela();

		this.getContentPane().add(this.montaPainel());

		buttonAddCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarCategoria();
			}
		});

		buttonAlterCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarCategoria();
			}
		});

		buttonDelCategoria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarCategoria();
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
				if (table.getSelectedRowCount() == 1) {
					buttonAlterCategoria.setEnabled(true);
					buttonDelCategoria.setEnabled(true);
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
				if(e.getClickCount() == 2)
					alterarCategoria();
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

	// metodo responsavel por criar a JTable
	private void criaJTable() {
		// inicializa a JTable
		table = new JTable();

		// mas a JTable precisa de algo para manipular seus dados(inserir linha,
		// excluir...)
		tableModel = new DefaultTableModel() {
			// por padrao o DefaultTableModel permite fazer alteracoes na JTable, por isso
			// precisamos
			// sobrecarregar seu metodo dizendo que nenhuma das linhas podera ser alterada
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table.setDefaultRenderer(Object.class, new TableRenderer());

		table.setRowHeight(23);

		tableSorter = new TableRowSorter(tableModel);
		table.setRowSorter(tableSorter);

		table.getColumnModel().getSelectionModel()
				.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// definindo que o tableModel seta responsavel por manipular os dados da JTable
		table.setModel(tableModel);

		// adicionando todas as colunas da tabela
		tableModel.addColumn("Código");
		tableModel.addColumn("Descrição");

		// setando o numero de linhas da JTable inicialmente como 0
		tableModel.setNumRows(0);
	}

	private void inicializaComponentes() {
		barraRolagem = new JScrollPane(table);

		textPesquisa = new JTextField();

		comboBoxFiltros = new JComboBox<>();
		comboBoxFiltros.addItem("Código");
		comboBoxFiltros.addItem("Descrição");

		buttonAddCategoria = new JButton();
		buttonAlterCategoria = new JButton();
		buttonDelCategoria = new JButton();
		buttonVoltar = new JButton();

		iconVoltar = new ImageIcon("icons/voltar.png");
		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");

		buttonAddCategoria.setIcon(iconAdd);
		buttonAlterCategoria.setIcon(iconAlter);
		buttonDelCategoria.setIcon(iconDelete);
		buttonVoltar.setIcon(iconVoltar);

		buttonAddCategoria.setToolTipText("Adicionar Nova Categoria");
		buttonAlterCategoria.setToolTipText("Alterar Categoria Existente");
		buttonDelCategoria.setToolTipText("Deletar Categoria Existente");
	}

	private JComponent montaPainel() {
		FormLayout layout = new FormLayout("5dlu, p, 5dlu, p:grow, 5dlu, p:grow, 150dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, p, 5dlu");

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		CellConstraints cc = new CellConstraints();

		builder.addLabel("Pesquisa:", cc.xy(2, 2));
		builder.add(textPesquisa, cc.xy(4, 2));
		builder.add(comboBoxFiltros, cc.xy(6, 2));

		builder.add(barraRolagem, cc.xywh(2, 4, 6, 7));
		builder.add(buttonAddCategoria, cc.xy(9, 4));
		builder.add(buttonAlterCategoria, cc.xy(9, 6));
		builder.add(buttonDelCategoria, cc.xy(9, 8));
		builder.add(buttonVoltar, cc.xy(9, 10));

		return builder.getPanel();
	}

	public void atualizaTabela() {
		String pesquisa = textPesquisa.getText();

		// Verifica se a pesquisa é nula e define como vazia se necessário
		if (pesquisa == null)
			pesquisa = "";
		String filtro = "";
		// Define o filtro com base na seleção do comboBox
		if (comboBoxFiltros.getSelectedIndex() == 0) {
			filtro = "codigo";
		} else {
			filtro = "descricao";
		}

		// Desabilita os botões de alterar e deletar categoria
		buttonAlterCategoria.setEnabled(false);
		buttonDelCategoria.setEnabled(false);

		// Limpa o modelo da tabela
		tableModel.setRowCount(0);

		// Obtém as categorias do controller com base na pesquisa e filtro
		categorias = controller.getCategorias(emailUsuario, pesquisa, filtro);

		// Adiciona as categorias encontradas à tabela
		for (CategoriaVO c : categorias) {
			tableModel.addRow(new Object[] { c.getCodigo(), c.getDescricao() });
		}
	}

	private void adicionarCategoria() {
		AdicionarCategoriaVisao a = new AdicionarCategoriaVisao(GerenciamentoCategoriaVisao.this, controller,
				emailUsuario);
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();
	}

	private void alterarCategoria() {
		AlterarCategoriaVisao a = new AlterarCategoriaVisao(GerenciamentoCategoriaVisao.this, controller,
				categorias.get(table.getSelectedRow()));
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();
	}

	private void deletarCategoria() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoCategoriaVisao.this,
				"Tem certeza que deseja excluir a Categoria?\nAs Movimentações associadas serão deletadas", "",
				JOptionPane.WARNING_MESSAGE);

		if (opc == 0) {
			int codigoCategoria = categorias.get(table.getSelectedRow()).getCodigo();

			controller.atualizaValoresBanco(emailUsuario);
			controller.atualizaValoresMeta(emailUsuario);
			controller.atualizaValoresQuadro(emailUsuario);

			controller.delMovimentacaoCategoria(emailUsuario, codigoCategoria);
			if (controller.delCategoria(codigoCategoria)) {
				JOptionPane.showMessageDialog(GerenciamentoCategoriaVisao.this, "Categoria deletada com sucesso", "",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(GerenciamentoCategoriaVisao.this, "Erro ao deletar Categoria", "",
						JOptionPane.ERROR_MESSAGE);
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

	public class TableRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (row % 2 == 0)
				comp.setBackground(Color.WHITE);
			else
				comp.setBackground(new Color(242, 242, 242));

			comp.setForeground(Color.BLACK);
			comp.setFont(new Font("Arial", Font.PLAIN, 12));

			if (isSelected) {
				comp.setBackground(new Color(57, 105, 138));
				comp.setForeground(Color.WHITE);
				comp.setFont(new Font("Arial", Font.BOLD, 12));
			}

			return comp;
		}
	}
}