package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

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
import com.toedter.calendar.JDateChooser;

import controller.MoneyFlowController;
import model.vo.MovimentacaoVO;
import model.vo.QuadroVO;

public class GerenciamentoEntradaSaidaVisao extends JInternalFrame {

	private MoneyFlowController controller;

	private JTextField textPesquisa;
	private JComboBox<String> comboBoxFiltros;

	private JButton buttonAddEntrada;
	private JButton buttonAlterarEntrada;
	private JButton buttonDelEntrada;
	private JButton buttonAddSaida;
	private JButton buttonAlterarSaida;
	private JButton buttonDelSaida;
	private JButton buttonVoltar;

	private ImageIcon iconVoltar;
	private ImageIcon iconAdd;
	private ImageIcon iconAlter;
	private ImageIcon iconDelete;

	private JDateChooser calendarDe;
	private JDateChooser calendarAte;

	private DefaultTableModel tableModel;

	private JTable table;

	private JScrollPane barraRolagem;

	private QuadroVO quadroVO;

	private JDesktopPane desktop;

	private Color backgroundTelas;

	private GerenciamentoQuadroVisao gerenciarQuadroVisao;

	private String emailUsuario;

	// construtor
	public GerenciamentoEntradaSaidaVisao(JDesktopPane desktop, Color backgroundTelas,
			GerenciamentoQuadroVisao gerenciarQuadroVisao, QuadroVO quadroVO, MoneyFlowController controller,
			String emailUsuario) {
		super("Gerenciar Entradas e Saídas - " + quadroVO.getDescricao());

		this.controller = controller;
		this.desktop = desktop;
		this.backgroundTelas = backgroundTelas;
		this.gerenciarQuadroVisao = gerenciarQuadroVisao;
		this.quadroVO = quadroVO;
		this.emailUsuario = emailUsuario;

		try {

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}

		criaJTable();

		inicializaComponentes();

		atualizaTabela();

		this.getContentPane().add(this.montaPainel());

		buttonVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});

		buttonAddEntrada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarEntrada();
			}
		});

		buttonAlterarEntrada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarEntrada();
			}
		});

		buttonDelEntrada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarEntrada();
			}
		});

		buttonAddSaida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarSaida();
			}
		});

		buttonAlterarSaida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarSaida();
			}
		});

		buttonDelSaida.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarSaida();
			}
		});

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				buttonAlterarEntrada.setEnabled(false);
				buttonAlterarSaida.setEnabled(false);
				buttonDelEntrada.setEnabled(false);
				buttonDelSaida.setEnabled(false);
				
				if (table.getSelectedRowCount() == 1) {
					if (quadroVO.getMovimentacoes().get(table.getSelectedRow()).isEntrada()) {
						buttonAlterarEntrada.setEnabled(true);
						buttonDelEntrada.setEnabled(true);
					} else {
						buttonAlterarSaida.setEnabled(true);
						buttonDelSaida.setEnabled(true);
					}
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
		
		calendarAte.addPropertyChangeListener("date", new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
		        if ("date".equals(evt.getPropertyName())) {
		            atualizaTabela();
		        }
		    }
		});
		
		calendarDe.addPropertyChangeListener("date", new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
		        if ("date".equals(evt.getPropertyName())) {
		            atualizaTabela();
		        }
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

		// definindo que o tableModel seta responsavel por manipular os dados da JTable
		table.setModel(tableModel);

		// adicionando todas as colunas da tabela
		tableModel.addColumn("Código");
		tableModel.addColumn("Descrição");
		tableModel.addColumn("Valor (R$)");
		tableModel.addColumn("Data");

		// setando o numero de linhas da JTable inicialmente como 0
		tableModel.setNumRows(0);
	}

	// metodo responsavel por inicializar os componentes
	private void inicializaComponentes() {
		// inicializando e adicionando a tabela a um ScrollPane, case precise de uma
		// barra de rolagem
		barraRolagem = new JScrollPane(table);

		textPesquisa = new JTextField();
		textPesquisa.setBackground(new Color(240, 240, 240)); // define a cor de fundo para o JTextField

		comboBoxFiltros = new JComboBox<>();
		comboBoxFiltros.addItem("Código");
		comboBoxFiltros.addItem("Descrição");

		buttonAddEntrada = new JButton();
		buttonAddSaida = new JButton();
		buttonAlterarEntrada = new JButton();
		buttonAlterarSaida = new JButton();
		buttonDelEntrada = new JButton();
		buttonDelSaida = new JButton();
		buttonVoltar = new JButton();

		iconVoltar = new ImageIcon("icons/voltar.png");
		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");

		buttonVoltar.setIcon(iconVoltar);
		buttonAddEntrada.setIcon(iconAdd);
		buttonAddSaida.setIcon(iconAdd);
		buttonAlterarEntrada.setIcon(iconAlter);
		buttonAlterarSaida.setIcon(iconAlter);
		buttonDelEntrada.setIcon(iconDelete);
		buttonDelSaida.setIcon(iconDelete);

		buttonAddEntrada.setToolTipText("Adicionar Nova Entrada");
		buttonAddSaida.setToolTipText("Adicionar Nova Saída");
		buttonAlterarEntrada.setToolTipText("Alterar Entrada Existente");
		buttonAlterarSaida.setToolTipText("Alterar Saída Existente");
		buttonDelEntrada.setToolTipText("Deletar Entrada Existente");
		buttonDelSaida.setToolTipText("Deletar Saída Existente");
		buttonVoltar.setToolTipText("Voltar Para Tela Anterior");

		calendarDe = new JDateChooser();
		calendarAte = new JDateChooser();

		calendarAte.setDate(new Date());

		Calendar cal = Calendar.getInstance();

		// Subtrai um mês da data atual
		cal.add(Calendar.MONTH, -1);

		calendarDe.setDate(cal.getTime());
	}

	// metodo responsavel por montar o painel
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"5dlu, p, 5dlu, p:grow, 5dlu, p:grow, 5dlu, p, 5dlu, p:grow, 5dlu, p, 5dlu, p:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, p, 5dlu");

		// Agrupe as colunas para que tenham o mesmo tamanho
		layout.setColumnGroups(new int[][] { { 4, 6, 10, 14 } });

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(barraRolagem, cc.xywh(2, 4, 13, 17));

		builder.addLabel("Pesquisa:", cc.xy(2, 2));
		builder.add(textPesquisa, cc.xy(4, 2));

		builder.add(comboBoxFiltros, cc.xy(6, 2));

		builder.addLabel("De:", cc.xy(8, 2));
		builder.add(calendarDe, cc.xy(10, 2));

		builder.addLabel("Até:", cc.xy(12, 2));
		builder.add(calendarAte, cc.xy(14, 2));

		builder.addSeparator("Entradas", cc.xy(16, 4));
		builder.add(buttonAddEntrada, cc.xy(16, 6));
		builder.add(buttonAlterarEntrada, cc.xy(16, 8));
		builder.add(buttonDelEntrada, cc.xy(16, 10));

		builder.addSeparator("Saídas", cc.xy(16, 12));
		builder.add(buttonAddSaida, cc.xy(16, 14));
		builder.add(buttonAlterarSaida, cc.xy(16, 16));
		builder.add(buttonDelSaida, cc.xy(16, 18));
		builder.add(buttonVoltar, cc.xy(16, 20));

		// Configuração de cores
		builder.getPanel().setBackground(new Color(240, 240, 240)); // Cor de fundo do painel

		return builder.getPanel();
	}

	public void atualizaTabela() {
		String pesquisa = textPesquisa.getText();

		if (pesquisa == null)
			pesquisa = "";

		String filtro = "";
		if (comboBoxFiltros.getSelectedIndex() == 0)
			filtro = "codigo";
		else
			filtro = "descricao";

		Date de = calendarDe.getDate();
		Date ate = calendarAte.getDate();

		tableModel.setNumRows(0);

		buttonAlterarEntrada.setEnabled(false);
		buttonAlterarSaida.setEnabled(false);
		buttonDelEntrada.setEnabled(false);
		buttonDelSaida.setEnabled(false);

		quadroVO.setMovimentacoes(controller.getMovimentacoes(quadroVO.getCodigo(), emailUsuario, pesquisa, filtro, de, ate));

		for (MovimentacaoVO m : quadroVO.getMovimentacoes()) {
			tableModel.addRow(
					new Object[] { m.getCodigo(), m.getDescricao(), m.getValor_total(), m.getDataOcorrencia() });
		}
	}

	private void adicionarEntrada() {
		if (controller.getQtdBancos(emailUsuario) != 0) {
			if (controller.getQtCategorias(emailUsuario) != 0) {
				AdicionarEntradaVisao a = new AdicionarEntradaVisao(GerenciamentoEntradaSaidaVisao.this, controller,
						quadroVO.getCodigo(), emailUsuario);
				a.setBounds(0, 0, 350, 600);
				a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				a.setClosable(true);
				a.getContentPane().setBackground(backgroundTelas);
				this.setVisible(false);

				desktop.add(a);
				a.setVisible(true);
				a.setPosicao();
			} else {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
						"É necessário ao menos uma Categoria cadastrada", "", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
					"É necessário ao menos um Banco cadastrado", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void alterarEntrada() {
		if (controller.getQtdBancos(emailUsuario) != 0) {
			if (controller.getQtCategorias(emailUsuario) != 0) {
					AlterarEntradaVisao a = new AlterarEntradaVisao(GerenciamentoEntradaSaidaVisao.this, controller,
							quadroVO.getMovimentacoes().get(table.getSelectedRow()), quadroVO.getCodigo(), emailUsuario);
					a.setBounds(0, 0, 350, 600);
					a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					a.setClosable(true);
					a.getContentPane().setBackground(backgroundTelas);
					this.setVisible(false);

					desktop.add(a);
					a.setVisible(true);
					a.setPosicao();
			} else {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
						"É necessário ao menos uma Categoria cadastrada", "", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
					"É necessário ao menos um Banco cadastrado", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void deletarEntrada() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoEntradaSaidaVisao.this,
				"Tem certeza que deseja excluir a Entrada?", "", JOptionPane.WARNING_MESSAGE);

		if (opc == 0)
			if (controller.delMovimentacao(quadroVO.getMovimentacoes().get(table.getSelectedRow()).getCodigo())) {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this, "Entrada deletada com sucesso", "",
						JOptionPane.INFORMATION_MESSAGE);
				
				atualizaValores();
			}else {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this, "Erro ao deletar Entrada", "",
						JOptionPane.ERROR_MESSAGE);
			}

		atualizaTabela();
	}

	private void adicionarSaida() {
		if (controller.getQtdBancos(emailUsuario) != 0) {
			if (controller.getQtCategorias(emailUsuario) != 0) {
				AdicionarSaidaVisao a = new AdicionarSaidaVisao(GerenciamentoEntradaSaidaVisao.this, controller,
						quadroVO.getCodigo(), emailUsuario);
				a.setBounds(0, 0, 350, 600);
				a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				a.setClosable(true);
				a.getContentPane().setBackground(backgroundTelas);
				this.setVisible(false);

				desktop.add(a);
				a.setVisible(true);
				a.setPosicao();
			} else {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
						"É necessário ao menos uma Categoria cadastrada", "", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
					"É necessário ao menos um Banco cadastrado", "", JOptionPane.WARNING_MESSAGE);
		}

	}

	private void alterarSaida() {
		if (controller.getQtdBancos(emailUsuario) != 0) {
			if (controller.getQtCategorias(emailUsuario) != 0) {
				AlterarSaidaVisao a = new AlterarSaidaVisao(GerenciamentoEntradaSaidaVisao.this, controller,
						quadroVO.getMovimentacoes().get(table.getSelectedRow()), quadroVO.getCodigo(), emailUsuario);
				a.setBounds(0, 0, 350, 600);
				a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				a.setClosable(true);
				a.getContentPane().setBackground(backgroundTelas);
				this.setVisible(false);

				desktop.add(a);
				a.setVisible(true);
				a.setPosicao();
			} else {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
						"É necessário ao menos uma Categoria cadastrada", "", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this,
					"É necessário ao menos um Banco cadastrado", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void deletarSaida() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoEntradaSaidaVisao.this,
				"Tem certeza que deseja excluir a Saída?", "", JOptionPane.WARNING_MESSAGE);

		if (opc == 0) {
			if (controller.delMovimentacao(quadroVO.getMovimentacoes().get(table.getSelectedRow()).getCodigo())) {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this, "Saída deletada com sucesso", "",
						JOptionPane.INFORMATION_MESSAGE);
				
				atualizaValores();
			}
			else {
				JOptionPane.showMessageDialog(GerenciamentoEntradaSaidaVisao.this, "Erro ao deletar Saída", "",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		atualizaTabela();
	}
	
	public void atualizaValores() {
		controller.atualizaValoresBanco(emailUsuario);
		controller.atualizaValoresMeta(emailUsuario);
		controller.atualizaValoresQuadro(emailUsuario);
	}

	private void voltar() {
		this.dispose();
		gerenciarQuadroVisao.atualizaTabela();
		gerenciarQuadroVisao.setVisible(true);
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}
