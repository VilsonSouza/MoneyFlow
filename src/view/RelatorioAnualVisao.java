package view;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.MoneyFlowController;
import model.vo.RelatorioAnualVO;

public class RelatorioAnualVisao extends JInternalFrame {
	
	private ArrayList<RelatorioAnualVO> dados;
	private MoneyFlowController controller;
	private String emailUsuario;
	
	public RelatorioAnualVisao(MoneyFlowController controller, String emailUsuario) {
		super("Gráfico Anual");
		this.controller = controller;
		this.emailUsuario = emailUsuario;
		gerarGrafico();
	}
	
	private ChartPanel gerarGrafico() {
//		dados = controller.getRelatorioAnual(emailUsuario, de, ate);
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (RelatorioAnualVO r : dados) {
			String mes = r.getNumeroMes() + "";
			float valorTotal = r.getValorTotalMes();
			dataset.addValue(valorTotal, "Total", mes);
		}

		JFreeChart grafico = ChartFactory.createBarChart(
			"Comparação Anual por Mês",
			"Mês",
			"Total",
			dataset
		);
		
		ChartPanel painel = new ChartPanel(grafico);
		
		grafico.getCategoryPlot().getRenderer().setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		grafico.getCategoryPlot().getRenderer().setBaseItemLabelsVisible(true);
		
		return painel;
	}
	
	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}