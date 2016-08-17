package com.easybpms.bd;

public class CRUDException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String message = null;
	private Throwable excecaoInterna = null;
	
	public CRUDException(String mensagem)
	{
		this.message = mensagem;
	}
	
	public CRUDException(String message, Throwable excecaoInterna)
	{
		this.message = message;
		this.excecaoInterna = excecaoInterna;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
	
	@Override
	public Throwable getCause()
	{
		return this.excecaoInterna;
	}
	
	public static CRUDException getExcecao(String message, Exception excecao) throws CRUDException {
		
		if (excecao.getClass().equals(CRUDException.class)) {
			throw (CRUDException)excecao;
		} else {
			throw new CRUDException(message, excecao.getCause());
		}
	}
	
	public static String getInconformidadeCadastrar(String nomeEntidade) {
		return "Inconformidade ao cadastrar " + nomeEntidade + "!";
	}
	
	public static String getInconformidadeAlterar(String nomeEntidade) {
		return "Inconformidade ao alterar " + nomeEntidade + "!";
	}
	
	public static String getInconformidadeConsultar(String nomeEntidade) {
		return "Inconformidade ao consultar " + nomeEntidade + "!";
	}
	
	public static String getInconformidadeExcluir(String nomeEntidade) {
		return "Inconformidade ao excluir " + nomeEntidade + "!";
	}

}
