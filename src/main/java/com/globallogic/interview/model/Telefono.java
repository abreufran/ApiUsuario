package com.globallogic.interview.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Telefono {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String number;
	private String citycode;
	private String contrycode;
	
	public Telefono() {}
	
	public Telefono(int id, String number, String citycode, String contrycode) {
		super();
		this.id = id;
		this.number = number;
		this.citycode = citycode;
		this.contrycode = contrycode;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getContrycode() {
		return contrycode;
	}
	public void setContrycode(String contrycode) {
		this.contrycode = contrycode;
	}
	
	
}
