package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import controller.MoneyFlowController;
import model.vo.RelatorioCategoriaVO;

public class PlotRelatorioCategoriaVisao extends JInternalFrame {

	private MoneyFlowController controller;

	private JButton buttonVoltar;

	private ImageIcon iconVoltar;

	private JDateChooser calendarDe;
	private JDateChooser calendarAte;

	private String emailUsuario;

	private ArrayList<RelatorioCategoriaVO> dados;

	private DefaultCategoryDataset dataset;

	private JFreeChart chart;

	private ChartPanel chartPanel;

	// construtor
	public PlotRelatorioCategoriaVisao(MoneyFlowController controller, String emailUsuario) {
		super("Relatório de Categorias");

		this.controller = controller;
		this.emailUsuario = emailUsuario;

		try {

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}

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

	// metodo responsavel por inicializar os componentes
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

	// metodo responsavel por montar o painel
	private JComponent montaPainel() {
		FormLayout layout = new FormLayout("5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 50dlu:grow, 5dlu, p, 5dlu",
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
		return ChartFactory.createBarChart("Lucro por Categoria", "Categoria", "Total (R$)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
	}

	private void updateData() {
		Date de = calendarDe.getDate();
		Date ate = calendarAte.getDate();

		dados = controller.getRelatorioCategoria(emailUsuario, de, ate);

		dataset.clear();

		for (RelatorioCategoriaVO r : dados) {
			String categoria = r.getCategoriaDescricao();
			float valorTotal = r.getValor();
			dataset.addValue(valorTotal, "Total (R$)", categoria);
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