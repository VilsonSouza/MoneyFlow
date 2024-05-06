package model.vo;

import java.util.Date;

public class MovimentacaoVO {
	private int codigo;
	private String descricao;
	private Date dataOcorrencia;
	private float valor_total;
	private boolean debitado;
	private boolean entrada;
	private int codigoCategoria;
	private int codigoMeta;
	private int codigoBanco;
	private int codigoQuadro;
	private int qtdParcelas;
	private String categoria;
	private String meta;
	private String banco;
	
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
	public Date getDataOcorrencia() {
		return dataOcorrencia;
	}
	public void setDataOcorrencia(Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}
	public float getValor_total() {
		return valor_total;
	}
	public void setValor_total(float valor_total) {
		this.valor_total = valor_total;
	}
	public boolean isDebitado() {
		return debitado;
	}
	public void setDebitado(boolean debitado) {
		this.debitado = debitado;
	}
	public boolean isEntrada() {
		return entrada;
	}
	public void setEntrada(boolean entrada) {
		this.entrada = entrada;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public int getCodigoCategoria() {
		return codigoCategoria;
	}
	public void setCodigoCategoria(int codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}
	public int getCodigoMeta() {
		return codigoMeta;
	}
	public void setCodigoMeta(int codigoMeta) {
		this.codigoMeta = codigoMeta;
	}
	public int getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(int codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public int getCodigoQuadro() {
		return codigoQuadro;
	}
	public void setCodigoQuadro(int codigoQuadro) {
		this.codigoQuadro = codigoQuadro;
	}
	public int getQtdParcelas() {
		return qtdParcelas;
	}
	public void setQtdParcelas(int qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}
}
