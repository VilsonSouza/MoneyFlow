package view;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.MoneyFlowController;
import model.dao.ConexaoBD;
import model.vo.UsuarioVO;

public class MoneyFlow_App {

	public static void main(String[] args) {	
		//cor base de fundo
		Color backgroundTelas = Color.WHITE;
		
		//inicializando conexao com banco
		ConexaoBD.getConexaoBD();
		
		//inicializando controle de aplicacao
		MoneyFlowController controller = new MoneyFlowController();
		ImageIcon logo = new ImageIcon("icons/logo_app.png");
		
		//inicializando aplicacao
		LoginVisao l = new LoginVisao(logo, controller, backgroundTelas, new UsuarioVO("gshayashida@gmail.com", "senha", "guga"));
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setBounds(100, 100, 570, 500);
		l.setIconImage(logo.getImage());
		l.setLocationRelativeTo(null);
		l.getContentPane().setBackground(backgroundTelas);
		l.setVisible(true);
	}

}