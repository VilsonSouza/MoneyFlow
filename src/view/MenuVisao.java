package view;

import java.awt.Color;
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
	private JMenuItem metaItem;
	private JMenuItem finalizarItem;
	private JMenuItem calculadoraFinanceiraItem;

	private JMenuItem relatorioAnualItem;

	private JMenuBar barra;

	private Color backgroundTelas;
	
	private UsuarioVO usuarioVO;

	public MenuVisao(MoneyFlowController controller, UsuarioVO usuarioVO) {
		// Construtor

		super("MoneyFlow");
		
		this.controller = controller;
		this.usuarioVO = usuarioVO;

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Java Swing Nimbus

		} catch (Exception e) {
		}

		inicializaComponentes();
		
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
		relatoriosMenu.add(new JMenuItem("Relatório 2"));
		relatoriosMenu.add(new JMenuItem("Relatório 3"));
		relatoriosMenu.add(new JMenuItem("Relatório 4"));
		
		aplicacaoMenu.add(perfilItem);
		aplicacaoMenu.addSeparator();
		aplicacaoMenu.add(finalizarItem);

		barra.add(financeiroMenu);
		barra.add(relatoriosMenu);
		barra.add(aplicacaoMenu);

		setJMenuBar(barra);

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
		
		relatorioAnualItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gerarRelatorioAnual();
			}
		});
	}

	public void inicializaComponentes() {
		// Metodo responsavel por inicializar itens do menu

		backgroundTelas = Color.WHITE;
		
		financeiroMenu = new JMenu("Financeiro");
		aplicacaoMenu = new JMenu("Aplicação");
		relatoriosMenu = new JMenu("Relatórios");

		quadroItem = new JMenuItem("Gerenciar Quadros");
		bancoItem = new JMenuItem("Gerenciar Bancos");
		categoriaItem = new JMenuItem("Gerenciar Categorias");
		perfilItem = new JMenuItem("Configurações de Perfil");
		finalizarItem = new JMenuItem("Finalizar Programa");
		metaItem = new JMenuItem("Gerenciar Metas");
		calculadoraFinanceiraItem = new JMenuItem("Calculadora Financeira");
		
		relatorioAnualItem = new JMenuItem("Relatório Anual");

		barra = new JMenuBar();

		desktop = new JDesktopPane() {
			Image im = (new ImageIcon("./images/suaImagem.jpg")).getImage();

			public void paintComponent(Graphics g) {
				g.drawImage(im, 0, 0, this);
			}
		};

		add(desktop);
	}

	private void gerenciarQuadros() {
		GerenciamentoQuadroVisao g = new GerenciamentoQuadroVisao(desktop, backgroundTelas, controller, usuarioVO.getEmail());
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}
	
	private void gerenciarMetas() {
		GerenciamentoMetasVisao g = new GerenciamentoMetasVisao(desktop, backgroundTelas, controller, usuarioVO.getEmail());
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}
	
	private void gerenciarCategorias() {
		GerenciamentoCategoriaVisao g = new GerenciamentoCategoriaVisao(desktop, backgroundTelas, controller, usuarioVO.getEmail());
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}

	private void gerenciarBancos() {
		GerenciamentoBancoVisao g = new GerenciamentoBancoVisao(desktop, backgroundTelas, controller, usuarioVO.getEmail());
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}
	
	private void gerarRelatorioAnual() {
		RelatorioAnualVisao g = new RelatorioAnualVisao(controller, usuarioVO.getEmail());
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		g.setClosable(true);
		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);
		g.setPosicao();
	}
	private void editaPerfil() {
		GerenciamentoPerfilVisao g = new GerenciamentoPerfilVisao(desktop,controller, usuarioVO.getEmail(),usuarioVO);
		g.setBounds(0, 0, 1050, 650);
		g.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		g.getContentPane().setBackground(backgroundTelas);

		desktop.add(g);
		g.setVisible(true);

	}

	private void fechar() {
		ConexaoBD.closeCoxexaoBD();
		System.exit(0);
	}
}
