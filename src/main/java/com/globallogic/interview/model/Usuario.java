package com.globallogic.interview.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@Column(unique=true)
	private String email;
	private String password;
	
	@JsonIgnore
	private String uuid;
	
	@OneToMany(targetEntity = Telefono.class, cascade=CascadeType.ALL)
	@JoinColumn(name="id_usuario", referencedColumnName = "id")
	private List<Telefono> phones;
	
	public Usuario() {}
	
	public Usuario(int id, String name, String email, String password, List<Telefono> phones) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phones = phones;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public List<Telefono> getPhones() {
		return phones;
	}
	public void setPhones(List<Telefono> phones) {
		this.phones = phones;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	
	
}
