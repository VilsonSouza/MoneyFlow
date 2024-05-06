package model.vo;

public class MetaVO {
	private int codigo;
	private String descricao;
	private float valorEsperado;
	private float valorArrecadado;
	private float valorRestante;
	
	public MetaVO(int codigo, String descricao) {
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
	public float getValorEsperado() {
		return valorEsperado;
	}
	public void setValorEsperado(float valorEsperado) {
		this.valorEsperado = valorEsperado;
	}
	public float getValorArrecadado() {
		return valorArrecadado;
	}
	public void setValorArrecadado(float valorArrecadado) {
		this.valorArrecadado = valorArrecadado;
	}
	public float getValorRestante() {
		return valorRestante;
	}
	public void setValorRestante(float valorRestante) {
		this.valorRestante = valorRestante;
	}
}
