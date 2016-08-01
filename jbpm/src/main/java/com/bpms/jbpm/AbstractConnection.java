package com.bpms.jbpm;

public abstract class AbstractConnection {
	
	private static AbstractConnection instance = null;
	
	public static void getConnection() {
		if (instance == null){
			try {
				Class<?> connectionBD = Class.forName("com.jbpm.bd.Connection");
				try {
					instance = (AbstractConnection)connectionBD.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
