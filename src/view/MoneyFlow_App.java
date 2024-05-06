package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.MoneyFlowController;
import model.dao.ConexaoBD;

public class MoneyFlow_App {

	public static void main(String[] args) {		
		ConexaoBD.getConexaoBD();
		MoneyFlowController controller = new MoneyFlowController();
		ImageIcon logo = new ImageIcon("icons/logo_app.png");
		
		LoginVisao l = new LoginVisao(logo, controller, null);
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setBounds(100, 100, 320, 450);
		l.setIconImage(logo.getImage());
		l.setVisible(true);
	}

}