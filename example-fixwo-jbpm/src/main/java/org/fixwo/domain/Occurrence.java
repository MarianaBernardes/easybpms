package org.fixwo.domain;

public class Occurrence {
	
	private Long id;
	private String cliente;
	private Boolean existArea;
	private String status;
	private String sector;
	private String feedback;
	private Boolean evaluation;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public Boolean getExistArea() {
		return existArea;
	}
	public void setExistArea(Boolean existArea) {
		this.existArea = existArea;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Boolean getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(Boolean evaluation) {
		this.evaluation = evaluation;
	}
}