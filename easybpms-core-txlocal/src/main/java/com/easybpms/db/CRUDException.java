package com.easybpms.db;

public class CRUDException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String message = null;
	private Throwable internalException = null;
	
	public CRUDException(String message)
	{
		this.message = message;
	}
	
	public CRUDException(String message, Throwable internalException)
	{
		this.message = message;
		this.internalException = internalException;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
	
	@Override
	public Throwable getCause()
	{
		return this.internalException;
	}
	
	public static CRUDException getException(String message, Exception exception) throws CRUDException {
		
		if (exception.getClass().equals(CRUDException.class)) {
			throw (CRUDException)exception;
		} else {
			throw new CRUDException(message, exception.getCause());
		}
	}

}
