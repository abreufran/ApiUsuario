package com.globallogic.interview.model;


public class SolicitudAutenticacion {
	private String email;
	private String password;
	
	public SolicitudAutenticacion() {}
	
	public SolicitudAutenticacion(String email, String password) {
		super();
		this.setEmail(email);
		this.setPassword(password);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
