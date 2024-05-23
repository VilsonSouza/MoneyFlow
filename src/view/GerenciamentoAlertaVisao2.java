package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import controller.MoneyFlowController;
import model.vo.AlertaVO;
import model.vo.QuadroVO;
import view.GerenciamentoQuadroVisao.TableRenderer;

public class GerenciamentoAlertaVisao2 extends JInternalFrame{
	
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

	private DefaultTableModel tableModel;

	private JTable table;

	private JScrollPane barraRolagem;
	
	private JDesktopPane desktop;
	
	private TableRowSorter tableSorter;

	private Color backgroundTelas;
	
	private ArrayList<AlertaVO> alertas;
	
	private String emailUsuario;
	
	//construtor
	public GerenciamentoAlertaVisao2(JDesktopPane desktop, Color backgroundTelas, MoneyFlowController controller, String emailUsuario) {
		super("Gerenciar Alertas");
		
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
				if(table.getSelectedRowCount() == 1) {
					buttonAlterAlerta.setEnabled(true);
					buttonDelAlerta.setEnabled(true);
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
					alterarAlerta();
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
		
		table.setDefaultRenderer(Object.class, new TableRenderer());
		
		table.setRowHeight(23);

		tableSorter = new TableRowSorter(tableModel);
		table.setRowSorter(tableSorter);
		
		table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		//definindo que o tableModel seta responsavel por manipular os dados da JTable
		table.setModel(tableModel);

		//adicionando todas as colunas da tabela
		tableModel.addColumn("Código");
		tableModel.addColumn("Descrição");
		tableModel.addColumn("Data");

		//setando o numero de linhas da JTable inicialmente como 0
		tableModel.setNumRows(0);
	}

	//metodo responsavel por inicializar os componentes
	private void inicializaComponentes() {
		//inicializando e adicionando a tabela a um ScrollPane, case precise de uma barra de rolagem
		barraRolagem = new JScrollPane(table);
	
		buttonAddAlerta = new JButton();
		buttonAlterAlerta = new JButton();
		buttonDelAlerta = new JButton();
		buttonVoltar = new JButton();

		iconVoltar = new ImageIcon("icons/voltar.png");
		iconAdd = new ImageIcon("icons/add.png");
		iconAlter = new ImageIcon("icons/alter.png");
		iconDelete = new ImageIcon("icons/delete.png");
		
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

	//metodo responsavel por montar o painel 
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"5dlu, p, 5dlu, p:grow, 5dlu, p, 5dlu, p:grow, 150dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, p, 5dlu");
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(barraRolagem, cc.xywh(2, 4, 9, 7));

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
		buttonAlterAlerta.setEnabled(false);
		buttonDelAlerta.setEnabled(false);
		
		tableModel.setNumRows(0);
		
		alertas = controller.getAlertas(emailUsuario, calendarDe.getDate(), calendarAte.getDate());
		
		for(AlertaVO b : alertas)
			tableModel.addRow(new Object[] {
					b.getCodigo(), b.getDescricao(), b.getData_alerta()
			});
	}
	
	private void adicionarAlerta() {
		AdicionarAlertaVisao a = new AdicionarAlertaVisao(GerenciamentoAlertaVisao2.this, controller, emailUsuario);
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
		AlterarAlertaVisao a = new AlterarAlertaVisao(GerenciamentoAlertaVisao2.this, controller, alertas.get(table.getSelectedRow()));
		a.setBounds(0, 0, 350, 400);
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		a.setClosable(true);
		a.getContentPane().setBackground(backgroundTelas);
		this.setVisible(false);

		desktop.add(a);
		a.setVisible(true);
		a.setPosicao();		
	}
	
	private void deletarAlerta() {
		int opc = JOptionPane.showConfirmDialog(GerenciamentoAlertaVisao2.this, "Tem certeza que deseja excluir o Alerta?", "", JOptionPane.WARNING_MESSAGE);
    	
    	if(opc == 0) {
    		int codigoAlerta = alertas.get(table.getSelectedRow()).getCodigo();
    		
    		if(controller.delAlerta(codigoAlerta)) {
    			JOptionPane.showMessageDialog(GerenciamentoAlertaVisao2.this, "Alerta deletado com sucesso", "", JOptionPane.INFORMATION_MESSAGE);
    		}else {
    			JOptionPane.showMessageDialog(GerenciamentoAlertaVisao2.this, "Erro ao deletar Alerta", "", JOptionPane.ERROR_MESSAGE);
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