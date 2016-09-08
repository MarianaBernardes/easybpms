package org.fixwo.domain;

public class Ocorrencia {
	private Long id;
	private String tenancy = "";
	private Boolean existeArea = false;
	private String status = "";
	private String setor = "";
	private String feedback = "";
	private Boolean avaliacao = false;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTenancy() {
		return tenancy;
	}
	public void setTenancy(String tenancy) {
		this.tenancy = tenancy;
	}
	public Boolean getExisteArea() {
		return existeArea;
	}
	public void setExisteArea(Boolean existeArea) {
		this.existeArea = existeArea;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSetor() {
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Boolean getAvaliacao() {
		return avaliacao;
	}
	public void setAvaliacao(Boolean avaliacao) {
		this.avaliacao = avaliacao;
	}
}
