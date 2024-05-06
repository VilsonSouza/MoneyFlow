package model.vo;

import java.util.ArrayList;

public class QuadroVO {
	private int codigo;
	private String descricao;
	private String emailUsuario;
	private float totalEntrada;
	private float totalSaida;
	private ArrayList<MovimentacaoVO> movimentacoes;
	
	public QuadroVO(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public float getTotalEntrada() {
		return totalEntrada;
	}
	public void setTotalEntrada(float totalEntrada) {
		this.totalEntrada = totalEntrada;
	}
	public float getTotalSaida() {
		return totalSaida;
	}
	public void setTotalSaida(float totalSaida) {
		this.totalSaida = totalSaida;
	}
	public ArrayList<MovimentacaoVO> getMovimentacoes() {
		return movimentacoes;
	}
	public void setMovimentacoes(ArrayList<MovimentacaoVO> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

}
