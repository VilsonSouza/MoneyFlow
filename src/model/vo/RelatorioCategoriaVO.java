package model.vo;

public class RelatorioCategoriaVO {
	private String categoriaDescricao;
	private int categoriaCodigo;
	private float valor;
	
	public String getCategoriaDescricao() {
		return categoriaDescricao;
	}
	public void setCategoriaDescricao(String categoriaDescricao) {
		this.categoriaDescricao = categoriaDescricao;
	}
	public int getCategoriaCodigo() {
		return categoriaCodigo;
	}
	public void setCategoriaCodigo(int categoriaCodigo) {
		this.categoriaCodigo = categoriaCodigo;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
}
