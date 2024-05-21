package model.vo;

import java.util.Date;

public class AlertaVO {
	private int codigo;
	private String descricao;
	private Date data_alerta;
	private int status;
	
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
	public Date getData_alerta() {
		return data_alerta;
	}
	public void setData_alerta(Date data_alerta) {
		this.data_alerta = data_alerta;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
