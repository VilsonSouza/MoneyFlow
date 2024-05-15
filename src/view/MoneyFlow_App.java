package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import controller.MoneyFlowController;
import model.dao.ConexaoBD;
import model.vo.UsuarioVO;

public class MoneyFlow_App {

	public static void main(String[] args) {		
		ConexaoBD.getConexaoBD();
		MoneyFlowController controller = new MoneyFlowController();
		ImageIcon logo = new ImageIcon("icons/logo_app.png");
		
		LoginVisao l = new LoginVisao(logo, controller, new UsuarioVO("gustavo.gp520@gmail.com", "senha", "guga"));
		l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l.setBounds(100, 100, 320, 450);
		l.setIconImage(logo.getImage());
		l.setLocationRelativeTo(null);
		l.setVisible(true);
	}

}