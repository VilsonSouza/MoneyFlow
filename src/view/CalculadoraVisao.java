package view;

import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

import javax.swing.JInternalFrame;

//Calculadora utilizando Frames
public class CalculadoraVisao extends JInternalFrame implements ActionListener {
	Label L1, L2, L3;
	Button B1, B2, B3, B4, bt_log, bt_sen, bt_cos, bt_rar_quad, bt_rar_cub, bt_inv, bt_limpar, bt_sair;
	TextField T1, T2, T3;

	public CalculadoraVisao() {
		setTitle("Calculadora");
		setBackground(new Color(150, 150, 150));
		setLayout(new GridLayout(9, 2));
		L1 = new Label("Num.1");
		L1.setForeground(Color.white);
		L2 = new Label("Num.2");
		L2.setForeground(Color.white);
		L3 = new Label("Total");
		L3.setForeground(Color.white);
		L1.setFont(new Font("", Font.BOLD, 14));
		L2.setFont(new Font("", Font.BOLD, 14));
		L3.setFont(new Font("", Font.BOLD, 14));
		B1 = new Button("+");
		B1.addActionListener(this);
		B2 = new Button("-");
		B2.addActionListener(this);
		B3 = new Button("x");
		B3.addActionListener(this);
		B4 = new Button("/");
		B4.addActionListener(this);
		
		bt_log = new Button("Log");
		bt_log.addActionListener(this);
		
		bt_sen = new Button("sin");
		bt_sen.addActionListener(this);
		
		bt_cos = new Button("cos");
		bt_cos.addActionListener(this);
		
		bt_rar_quad = new Button("^2");
		bt_rar_quad.addActionListener(this);
		
		bt_rar_cub = new Button("^3");
		bt_rar_cub.addActionListener(this);
		
		bt_inv = new Button("^-1");
		bt_inv.addActionListener(this);
		
		bt_limpar = new Button("Limpar");
		bt_limpar.addActionListener(this);
		bt_limpar.setBackground(Color.black);
		bt_limpar.setForeground(Color.white);
		
		bt_sair = new Button("Sair");
		bt_sair.addActionListener(this);
		
		T1 = new TextField();
		T2 = new TextField();
		T3 = new TextField();
		T3.setEditable(false); // define que o textField como somente leitura
		add(L1);
		add(T1);
		add(L2);
		add(T2);
		add(L3);
		add(T3);
		add(B1);
		add(B2);
		add(B3);
		add(B4);
		add(bt_log);
		add(bt_sen);
		add(bt_cos);
		add(bt_rar_quad);
		add(bt_rar_cub);
		add(bt_inv);
//		add(bt_tang);
//		add(bt_sim);
		add(bt_limpar);
		add(bt_sair);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bt_limpar) {
			T1.setText("");
			T2.setText("");
			T3.setText("");
			return;
		}
		if (e.getSource() == bt_sair) {
			T1.setText("");
			T2.setText("");
			T3.setText("");
			this.dispose();
			return;
		}

		double N1 = 0, N2 = 0, result = 0;

		try {		
			if (T1.getText().equals(""))
				N1 = 0;
			else
				N1 = Float.parseFloat(T1.getText());
			
			if (T2.getText().equals(""))
				N2 = 0;
			else
				N2 = Float.parseFloat(T2.getText());
		} catch (NumberFormatException erro) {
			T3.setText("Erro");
			return;
		}

		if (e.getSource() == B1) {
			result = N1 + N2;
		} else if (e.getSource() == B2) {
			result = N1 - N2;
		} else if (e.getSource() == B3) {
			result = N1 * N2;
		} else if (e.getSource() == B4) {
			result = N1 / N2;
		} else if (e.getSource() == bt_log) {
			result = Math.log10(N1);
			T2.setText("");
		} else if (e.getSource() == bt_sen) {
			result =  Math.sin(Math.toRadians(N1));
			T2.setText("");
		}else if (e.getSource() == bt_cos) {
			result =  Math.cos(Math.toRadians(N1));
			T2.setText("");
		}else if (e.getSource() == bt_rar_quad) {
			result =  Math.pow(N1, 2);
			T2.setText("");
		}else if (e.getSource() == bt_rar_cub) {
			result =  Math.pow(N1, 3);
			T2.setText("");
		}else if (e.getSource() == bt_inv) {
			result =  Math.pow(N1, -1) * N2;
		}
		
		T3.setText("" + result);
	}
	
	public void setPosicao() {
		Dimension d = this.getDesktopPane().getSize();
		this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);
	}
}
