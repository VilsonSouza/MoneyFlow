package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import controller.MoneyFlowController;
import model.dao.ConexaoBD;
import model.vo.UsuarioVO;

public class MenuVisao extends JFrame {

	private MoneyFlowController controller;

	private JDesktopPane desktop;

	private JMenu financeiroMenu;
	private JMenu aplicacaoMenu;
	private JMenu relatoriosMenu;

	private JMenuItem quadroItem;
	private JMenuItem bancoItem;
	private JMenuItem perfilItem;
	private JMenuItem categoriaItem;
	private JMenuItem alertaItem;
	private JMenuItem metaItem;
	private JMenuItem finalizarItem;
	private JMenuItem deslogarItem;
	private JMenuItem calculadoraFinanceiraItem;

	private JMenuItem relatorioAnualItem;
	private JMenuItem relatorioCategoriaItem;

	private JMenuBar barra;

	private Color backgroundTelas;

	private String email;

	private ImageIcon logo;

	public MenuVisao(MoneyFlowController controller, String email, ImageIcon logo, Color backgroundTelas) {
		// Construtor

		super("MoneyFlow");

		this.controller = controller;
		this.email = email;
		this.logo = logo;
		this.backgroundTelas = backgroundTelas;

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Java Swing Nimbus

		} catch (Exception e) {
		}

		inicializaComponentes();
		
		gerenciarAlertas();

		financeiroMenu.add(alertaItem);
		financeiroMenu.addSeparator();
		financeiroMenu.add(quadroItem);
		financeiroMenu.addSeparator();
		financeiroMenu.add(categoriaItem);
		financeiroMenu.addSeparator();
		financeiroMenu.add(bancoItem);
		financeiroMenu.addSeparator();
		financeiroMenu.add(metaItem);
		financeiroMenu.addSeparator();
		financeiroMenu.add(calculadoraFinanceiraItem);

		relatoriosMenu.add(relatorioAnualItem);
		relatoriosMenu.addSeparator();
		relatoriosMenu.add(relatorioCategoriaItem);

		aplicacaoMenu.add(perfilItem);
		aplicacaoMenu.addSeparator();
		aplicacaoMenu.add(deslogarItem);
		aplicacaoMenu.addSeparator();
		aplicacaoMenu.add(finalizarItem);

		barra.add(financeiroMenu);
		barra.add(relatoriosMenu);
		barra.add(aplicacaoMenu);

		setJMenuBar(barra);
		
		alertaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gerenciarAlertas();
			}
		});

		quadroItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gerenciarQuadros();
			}
		});

		categoriaItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gerenciarCategorias();
			}
		});

		bancoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gerenciarBancos();
			}
		});

		metaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gerenciarMetas();
			}
		});
		perfilItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editaPerfil();
			}
		});

		finalizarItem.addActionListener(new ActionListener() { // classe interna anonima
			public void actionPerformed(ActionEvent event) {
				fechar();
			}
		});

		deslogarItem.addActionListener(new ActionListener() { // classe interna anonima
			public void actionPerformed(ActionEvent event) {
				deslogar();
			}
		});

		relatorioAnualItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarRelatorioAnual();
			}
		});

		relatorioCategoriaItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarRelatorioCategoria();
			}
		});
		
		calculadoraFinanceiraItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculadoraFinanceira();
			}
		});
	}

	public void inicializaComponentes() {
		// Metodo responsavel por inicializar itens do menu

		financeiroMenu = new JMenu("Financeiro");
		aplicacaoMenu = new JMenu("Aplicação");
		relatoriosMenu = new JMenu("Relatórios");

		alertaItem = new JMenuItem("Gerenciar Alertas");
		quadroItem = new JMenuItem("Gerenciar Quadros");
		bancoItem = new JMenuItem("Gerenciar Bancos");
		categoriaItem = new JMenuItem("Gerenciar Categorias");
		perfilItem = new JMenuItem("Editar Perfil");
		finalizarItem = new JMenuItem("Finalizar Programa");
		deslogarItem = new JMenuItem("Deslogar");
		metaItem = new JMenuItem("Gerenciar Metas");
		calculadoraFinanceiraItem = new JMenuItem("Calculadora Financeira");

		relatorioAnualItem = new JMenuItem("Relatório Anual de Movimentações");
		relatorioCategoriaItem = new JMenuItem("Relatório de Categorias");

		barra = new JMenuBar();

		desktop = new JDesktopPane() {
			Image im = (new ImageIcon("./images/suaImagem.jpg")).getImage();

			public void paintComponent(Graphics g) {
				g.drawImage(im, 0, 0, this);
			}
		};
		

		add(desktop);		
	}
	
	private void gerenciarAlertas() {
		GerenciamentoAlertaVisao g = new GerenciamentoAlertaVisao(desktop, backgroundTelas, controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void gerenciarQuadros() {
		GerenciamentoQuadroVisao g = new GerenciamentoQuadroVisao(desktop, backgroundTelas, controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void gerenciarMetas() {
		GerenciamentoMetasVisao g = new GerenciamentoMetasVisao(desktop, backgroundTelas, controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void gerenciarCategorias() {
		GerenciamentoCategoriaVisao g = new GerenciamentoCategoriaVisao(desktop, backgroundTelas, controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void gerenciarBancos() {
		GerenciamentoBancoVisao g = new GerenciamentoBancoVisao(desktop, backgroundTelas, controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void gerarRelatorioAnual() {
		PlotRelatorioAnualVisao g = new PlotRelatorioAnualVisao(controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}
	
	private void gerarRelatorioCategoria() {
		PlotRelatorioCategoriaVisao g = new PlotRelatorioCategoriaVisao(controller, email);
		g.setBounds(0, 0, getTamanhoTela().width-40, getTamanhoTela().height-100);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void editaPerfil() {
		GerenciamentoPerfilVisao g = new GerenciamentoPerfilVisao(desktop, controller, email, backgroundTelas, MenuVisao.this);
		g.setBounds(0, 0, 350, 450);
		g.setClosable(true);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
		
		getTamanhoTela();
	}

	private void calculadoraFinanceira() {
		CalculadoraVisao c = new CalculadoraVisao();
		c.setBounds(0, 0, 400, 250);
		c.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		c.getContentPane().setBackground(backgroundTelas);

		desktop.add(c);
		c.setPosicao();
		c.setVisible(true);
	}

	private void deslogar() {
		this.dispose();

		LoginVisao loginVisao = new LoginVisao(logo, controller, backgroundTelas, null);
		loginVisao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginVisao.setBounds(100, 100, 570, 500);
		loginVisao.setIconImage(logo.getImage());
		loginVisao.setLocationRelativeTo(null);
		loginVisao.getContentPane().setBackground(backgroundTelas);
		
		loginVisao.setVisible(true);
	}

	private Dimension getTamanhoTela() {
		Dimension tamanho = MenuVisao.this.getSize();
		
		return tamanho;
	}

	private void fechar() {
		ConexaoBD.closeCoxexaoBD();
		System.exit(0);
	}
}
