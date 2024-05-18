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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import controller.MoneyFlowController;
import model.vo.MetaVO;
import model.vo.RelatorioAnualVO;
import view.GerenciamentoCategoriaVisao.TableRenderer;

public class PlotRelatorioAnualVisao extends JInternalFrame{
	
	private MoneyFlowController controller;

	private JButton buttonVoltar;
	
	private ImageIcon iconVoltar;
	
	private JDateChooser calendarDe;
	private JDateChooser calendarAte;
	
	private String emailUsuario;
	
	private ArrayList<RelatorioAnualVO> dados;

    private DefaultCategoryDataset dataset;

    private JFreeChart chart;
    
    private ChartPanel chartPanel;

	//construtor
	public PlotRelatorioAnualVisao(MoneyFlowController controller, String emailUsuario) {
		super("Relatório Anual de Movimentações");
		
		this.controller = controller;
		this.emailUsuario = emailUsuario;

		try { 

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	
		} 
		catch (Exception e) 
		{ }

		inicializaComponentes();
		
		updateData();

		this.getContentPane().add(this.montaPainel());
		
		buttonVoltar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		
		calendarAte.addPropertyChangeListener("date", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("date".equals(evt.getPropertyName())) {
					updateData();
				}
			}
		});

		calendarDe.addPropertyChangeListener("date", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("date".equals(evt.getPropertyName())) {
					updateData();
				}
			}
		});
	}

	//metodo responsavel por inicializar os componentes
	private void inicializaComponentes() {
		buttonVoltar = new JButton();

		iconVoltar = new ImageIcon("icons/voltar.png");
		
		dataset = new DefaultCategoryDataset();
		
		chart = createChart(dataset);
		
		chartPanel = new ChartPanel(chart);
		
		buttonVoltar.setIcon(iconVoltar);
		
		calendarDe = new JDateChooser();
		calendarAte = new JDateChooser();

		Calendar cal = Calendar.getInstance();
		
		// Obtenha o ano atual
        int currentYear = cal.get(Calendar.YEAR);

        // Defina a data para 1 de janeiro do ano atual
        cal.set(currentYear, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        calendarDe.setDate(cal.getTime());

		// Subtrai um mês da data atual
		cal.add(Calendar.YEAR, +1);

		calendarAte.setDate(cal.getTime());
	}

	//metodo responsavel por montar o painel 
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout(
				"5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, 5dlu, p, 5dlu",
				"5dlu, p, 5dlu, 150dlu:grow, p, 5dlu");
		
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		CellConstraints cc = new CellConstraints();

		builder.add(chartPanel, cc.xywh(2, 4, 8, 2));

		builder.addLabel("De:", cc.xy(2, 2));
		builder.add(calendarDe, cc.xy(4, 2));
		
		builder.addLabel("Até:", cc.xy(6, 2));
		builder.add(calendarAte, cc.xy(8, 2));
		
		builder.add(buttonVoltar, cc.xy(11, 5));

		return builder.getPanel();
	}
	
	private JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart(
        		"Lucro total por mês",
    			"Mês",
    			"Total (R$)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private void updateData() {
    	Date de = calendarDe.getDate();
		Date ate = calendarAte.getDate();
		
    	dados = controller.getRelatorioAnual(emailUsuario, de, ate);
    	
		dataset.clear();

		for (RelatorioAnualVO r : dados) {
			String mes = r.getNumeroMes() + "";
			float valorTotal = r.getValorTotalMes();
			dataset.addValue(valorTotal, "Total (R$)", mes);
		}		
    }
	
	private void voltar() {
		this.dispose();
	}

	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}