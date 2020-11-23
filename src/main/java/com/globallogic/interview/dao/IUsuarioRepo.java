package com.globallogic.interview.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globallogic.interview.model.Usuario;

public interface IUsuarioRepo extends JpaRepository<Usuario, Integer>{
	public Usuario findByEmail(String email);
}
