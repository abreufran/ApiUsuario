package com.globallogic.interview;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.globallogic.interview.dao.IUsuarioRepo;
import com.globallogic.interview.model.Telefono;
import com.globallogic.interview.model.Usuario;

@SpringBootApplication
public class ApiUsuarioApplication {
	
	@Autowired
	private IUsuarioRepo usuarioRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ApiUsuarioApplication.class, args);
	}
	
	@PostConstruct
	public void inicializarUsuarios() {
		List<Telefono> telefonos1 = Stream.of(
				new Telefono(0, "9876656", "2", "57"),
				new Telefono(0, "9345556", "2", "57")
				).collect(Collectors.toList());
		
		List<Telefono> telefonos2 = Stream.of(
				new Telefono(0, "33476656", "2", "57"),
				new Telefono(0, "55554556", "2", "57")
				).collect(Collectors.toList());
		
		List<Telefono> telefonos3 = Stream.of(
				new Telefono(0, "7876656", "2", "57"),
				new Telefono(0, "77588556", "2", "57")
				).collect(Collectors.toList());
		
		
		List<Usuario> usuarios = Stream.of(
				new Usuario(0, "Javier Martinez", "javier.martinez@prueba.com", passwordEncoder.encode("Javier01"), telefonos1),
				new Usuario(0, "Fernando Alvarez", "fernando.alvarez@prueba.com", passwordEncoder.encode("Fernando02"), telefonos2),
				new Usuario(0, "Administrador", "admin@prueba.com", passwordEncoder.encode("Admin03"), telefonos3)
				).collect(Collectors.toList());
		
		usuarioRepo.saveAll(usuarios);
	}

}
