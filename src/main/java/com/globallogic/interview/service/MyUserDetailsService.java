package com.globallogic.interview.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.globallogic.interview.dao.IUsuarioRepo;
import com.globallogic.interview.model.Usuario;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private IUsuarioRepo usuarioRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepo.findByEmail(email);
		
		return new User(usuario.getEmail(),usuario.getPassword(), new ArrayList<>());
	}

}
