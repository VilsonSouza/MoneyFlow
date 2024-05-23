package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import controller.MoneyFlowController;
import model.vo.AlertaVO;
import model.vo.UsuarioVO;

public class GerenciamentoAlertaVisao extends JInternalFrame {

	private MoneyFlowController controller;

	private JButton buttonAddAlerta;
	private JButton buttonAlterAlerta;
	private JButton buttonDelAlerta;
	private JButton buttonVoltar;

	private ImageIcon iconAdd;
	private ImageIcon iconAlter;
	private ImageIcon iconDelete;
	private ImageIcon iconVoltar;

	private JDateChooser calendarDe;
	private JDateChooser calendarAte;

	private JScrollPane barraRolagem;

	private JDesktopPane desktop;

	private Tabela table;

	private AlertaModelListener alertaModelListener;

	private TableRowSorter tableSorter;

	private Color backgroundTelas;

	private ArrayList<AlertaVO> alertas;

	private String emailUsuario;
	
	private UsuarioVO usuarioVO;

	// construtor
	public GerenciamentoAlertaVisao(JDesktopPane desktop, Color backgroundTelas, MoneyFlowController controller,
			String emailUsuario) {
		super("Gerenciamento de Alertas");

		this.desktop = desktop;
		this.backgroundTelas = backgroundTelas;
		this.controller = controller;
		this.emailUsuario = emailUsuario;

		try {

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}

		criaJTable();

		inicializaComponentes();

		atualizaTabela();

		this.getContentPane().add(this.montaPainel());
		
		buttonAddAlerta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adicionarAlerta();
			}
		});
		
		buttonAlterAlerta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				alterarAlerta();
			}
		});
		
		buttonDelAlerta.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				deletarAlerta();
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
				// TODO Auto-generated method stub
				
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
			
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					alterarAlerta();
				}
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
		table = new Tabela(alertaModelListener);

		alertaModelListener = new AlertaModelListener();

		// definindo que o tableModel seta responsavel por manipular os dados da JTable
		table.setModel(alertaModelListener);

		table.setRowHeight(23);

		tableSorter = new TableRowSorter(alertaModelListener);
		table.setRowSorter(tableSorter);

		table.getColumnModel().getColumn(0).setPreferredWidth(5);
		table.getColumnModel().getColumn(1).setPreferredWidth(2000);
		table.getColumnModel().getColumn(2).setPreferredWidth(2000);
		table.getColumnModel().getColumn(3).setPreferredWidth(2000);

		table.setDefaultRenderer(Object.class, new TableRenderer());
	}

	// metodo responsavel por inicializar os componentes
	private void inicializaComponentes() {
		// inicializando e adicionando a tabela a um ScrollPane, case precise de uma
		// barra de rolagem
		barraRolagem = new JScrollPane(table);
		
		buttonAddAlerta = new JButton();
		buttonAlterAlerta = new JButton();
		buttonDelAlerta = new JButton();
		buttonVoltar = new JButton();

		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");
		iconVoltar = new ImageIcon("icons/voltar.png");
		
		buttonAddAlerta.setIcon(iconAdd);
		buttonAlterAlerta.setIcon(iconAlter);
		buttonDelAlerta.setIcon(iconDelete);
		buttonVoltar.setIcon(iconVoltar);

		buttonAddAlerta.setToolTipText("Adicionar Novo Alerta");
		buttonAlterAlerta.setToolTipText("Alterar Alerta Existente");
		buttonDelAlerta.setToolTipText("Deletar Alerta Existente");
		
		calendarDe = new JDateChooser();
		calendarAte = new JDateChooser();
		
		calendarDe.setDate(new Date());

		Calendar cal = Calendar.getInstance();

		// Subtrai um mês da data atual
		cal.add(Calendar.MONTH, 1);
		
		calendarAte.setDate(cal.getTime());
		
	}

	// metodo responsavel por montar o painel
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout("5dlu, p, 5dlu, p:grow, 5dlu, p, 5dlu, p:grow, 150dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, p, 5dlu");

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(barraRolagem, cc.xywh(2, 4, 8, 7));
		
		builder.addLabel("De:", cc.xy(2, 2));
		builder.add(calendarDe, cc.xy(4, 2));
		
		builder.addLabel("Ate:", cc.xy(6, 2));
		builder.add(calendarAte, cc.xy(8, 2));

		builder.add(buttonAddAlerta, cc.xy(11, 4));
		builder.add(buttonAlterAlerta, cc.xy(11, 6));
		builder.add(buttonDelAlerta, cc.xy(11, 8));
		builder.add(buttonVoltar, cc.xy(11, 10));

		return builder.getPanel();
	}

	public void atualizaTabela() {
		Date de = calendarDe.getDate();
		Date ate = calendarAte.getDate();
		
		alertas = controller.getAlertas(emailUsuario, de, ate);
		
		for(AlertaVO a : alertas)
			alertaModelListener.addRow(a);
	}
	
	private void adicionarAlerta() {
		AdicionarAlertaVisao a = new AdicionarAlertaVisao(GerenciamentoAlertaVisao.this, controller, emailUsuario);
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();
	}
	
	private void alterarAlerta() {
//		AlterarAlertaVisao a = new AlterarAlertaVisao(GerenciamentoAlertaVisao.this, controller, bancos.get(table.getSelectedRow()));
//		a.setBounds(0, 0, 350, 400);
//		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		a.setClosable(true);
//		a.getContentPane().setBackground(backgroundTelas);
//		this.setVisible(false);
//
//		desktop.add(a);
//		a.setVisible(true);
//		a.setPosicao();		
	}
	
	private void deletarAlerta() {
//		int opc = JOptionPane.showConfirmDialog(GerenciamentoAlertaVisao.this, "Tem certeza que deseja excluir o Alerta?\nAs Movimentações associadas serão deletadas", "", JOptionPane.WARNING_MESSAGE);
//    	
//    	if(opc == 0) {
//    		int codigoAlerta = bancos.get(table.getSelectedRow()).getCodigo();
//    		
//    		controller.atualizaValoresMeta(emailUsuario);
//    		controller.atualizaValoresQuadro(emailUsuario);
//    		
//    		controller.delMovimentacaoAlerta(emailUsuario, codigoAlerta);
//    		if(controller.delAlerta(codigoAlerta)) {
//    			JOptionPane.showMessageDialog(GerenciamentoAlertaVisao.this, "Alerta deletada com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
//    		}else {
//    			JOptionPane.showMessageDialog(GerenciamentoAlertaVisao.this, "Erro ao deletar Alerta", "", JOptionPane.ERROR_MESSAGE);
//    		}
//    	}
//    	
//    	atualizaTabela();
	}
	
	private void voltar() {
		this.dispose();
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}

	private static class RendererData extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public RendererData() {
			super();
		}

		@Override
		protected void setValue(Object o) {
			String dataFormatada = "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataFormatada = sdf.format(o);
			super.setValue(dataFormatada);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			int linha = row;

			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (linha % 2 == 0)
				comp.setBackground(Color.WHITE);
			else
				comp.setBackground(new Color(242, 242, 242));

			comp.setForeground(Color.RED);
			comp.setFont(new Font("Arial", Font.BOLD, 12));

			if (isSelected) {
				comp.setBackground(new Color(57, 105, 138));
				comp.setForeground(Color.WHITE);
				comp.setFont(new Font("Arial", Font.BOLD, 12));
			}

			return comp;
		}
	}

	private static class Tabela extends JTable {
		private static final long serialVersionUID = 1L;
		private RendererData formatadorData = new RendererData();

		public Tabela(TableModel modeloDaTabela) {
			super(modeloDaTabela);
		}

		@Override
		public TableCellRenderer getCellRenderer(int row, int column) {
			if (column == 3)
				return formatadorData;
			return super.getCellRenderer(row, column);
		}
	}

	public class TableRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			int linha = row;

			row = tableSorter.convertRowIndexToModel(row);

			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (column == 0) {
				int status = alertas.get(row).getStatus();

				if (status == 1)
					comp.setBackground(new Color(91, 234, 153));
				else if (status == 0)
					comp.setBackground(new Color(135, 206, 255));
				else
					comp.setBackground(Color.WHITE);

			} else {
				if (linha % 2 == 0)
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
			}

			return comp;
		}
	}

	public class AlertaModelListener extends AbstractTableModel {

		public ArrayList<AlertaVO> dados;
		private Object[] colunas = { "", "Código", "Descrição", "Data" };

		public AlertaModelListener() {
			dados = new ArrayList<AlertaVO>();
		}

		public void addRow(AlertaVO m) {
			this.dados.add(m);
			this.fireTableDataChanged();
		}

		public String getColumnName(int num) {
			return this.colunas[num].toString();
		}

		public int getRowCount() {
			return dados.size();
		}

		public int getColumnCount() {
			return colunas.length;
		}

		public Class getColumnClass(int coluna) {
			if (coluna == 3)
				return Date.class;
			else if (coluna == 1)
				return Integer.class;
			else
				return String.class;
		}

		public Object getValueAt(int linha, int coluna) {
			switch (coluna) {
			case 0:
				return "";
			case 1:
				return dados.get(linha).getCodigo();
			case 2:
				return dados.get(linha).getDescricao();
			case 3:
				return dados.get(linha).getData_alerta();
			}
			return null;
		}

		public void removeRow(int linha) {
			this.dados.remove(linha);
			this.fireTableRowsDeleted(linha, linha);
		}

		public AlertaVO get(int linha) {
			return this.dados.get(linha);
		}

		public boolean isCellEditable(int linha, int coluna) {
			return false;
		}

		public void limpaDados() {
			dados.clear();

			this.fireTableDataChanged();
		}
	}
}