package com.globallogic.interview.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.globallogic.interview.dao.IUsuarioRepo;
import com.globallogic.interview.model.SolicitudAutenticacion;
import com.globallogic.interview.model.Usuario;

@Service
public class UsuarioService {
	
	private static Logger logger = LogManager.getLogger(UsuarioService.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UtilService utilService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager autenticationManager;
	
	@Autowired
	private IUsuarioRepo usuarioRepo;
	
	public ResponseEntity<Map<String, Object>> crearUsuario(Usuario usuario) {
		try {
			Map<String, Object> hm = new HashMap<>();
			
			if(!usuarioValido(usuario, hm)) {
				return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
			}
			else {
				usuario.setUuid(utilService.generarUUID());
				
				Usuario u = crear(usuario);
				
				hm.put("usuario", u);
				
				hm.put("id", u.getUuid());
				String fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
				hm.put("created", fecha);
				hm.put("modified", fecha);
				hm.put("last_login", fecha);
				hm.put("token", jwtService.generarToken(u.getEmail()));
				hm.put("isactive", true);
				
				return new ResponseEntity<>(hm, HttpStatus.CREATED);
			}
		}
		catch (Exception e) {
			Map<String, Object> hm = new HashMap<>();
			
			logger.error("Error al crear el usuario", e);
			hm.put("error", "Error al crear el usuario");
			return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
		}
	}
	
	private boolean usuarioValido(Usuario usuario, Map<String, Object> hm) {
		boolean usuarioValido = true;
		if(!emailValido(usuario.getEmail())) {
			hm.put("error", "El formato del email es incorrecto.");
			usuarioValido = false;
		}
		if(usuarioValido && buscarPorEmail(usuario.getEmail()) != null) {
			hm.put("error", "El correo ya esta registrado.");
			usuarioValido = false;
		}
		if(usuarioValido && !passwordValido(usuario.getPassword())) {
			hm.put("error", "El formato del password es invalido. El password debe comenzar con una (1) mayuscula, seguido de letras minusculas y debe terminar con (2) numeros.");
			usuarioValido = false;
		}
		return usuarioValido;
	}
	
	private boolean emailValido(String email) {
		// Patr�n para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
 
        Matcher mather = pattern.matcher(email);
 
        if (mather.find()) {
            return true;
        } 

        return false;
	}
	
	private boolean passwordValido(String password) {
		// Patr�n para validar el password
		Pattern pattern = Pattern.compile("^[A-Z]{1}[a-z]+[0-9]{2}$");
 
        Matcher mather = pattern.matcher(password);
 
        if (mather.find()) {
            return true;
        } 

        return false;
	}
	
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		try {
			List<Usuario> usuarios = listar();
			if(usuarios.isEmpty()) {
				logger.debug("No se encontraron usuarios");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else {
				logger.debug("Se encontraron ( " + usuarios.size()+ " ) usuarios");
				return new ResponseEntity<>(usuarios, HttpStatus.OK);
			}
		}
		catch (Exception e) {
			logger.error("Error al listar los usuarios", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<Map<String, String>> generarToken(SolicitudAutenticacion solicitudAutenticacion) {
		try {
			logger.debug("Autenticando a traves de Spring Security UserName: " + solicitudAutenticacion.getEmail() + " / Password: " + solicitudAutenticacion.getPassword());
			autenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(solicitudAutenticacion.getEmail(), solicitudAutenticacion.getPassword()));
			logger.debug("Usuario Autenticado Correctamente!!!");
		}
		catch (Exception e) {
			logger.error("username/password invalido", e);
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		Map<String, String> hm = new HashMap<>();
		logger.debug("Generando Token JWT");
		String token = jwtService.generarToken(solicitudAutenticacion.getEmail());
		logger.debug("Token JWT generado satisfactoriamente: " + token);
		hm.put("token", token);
		return new ResponseEntity<>(hm, HttpStatus.OK);
	}
	
	public Usuario crear(Usuario usuario) {
		logger.debug("INICIO: Crear Usuario");
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		Usuario u = usuarioRepo.save(usuario);
		logger.debug("FIN: Crear Usuario");
		return u;
	}
	
	public List<Usuario> listar() {
		logger.debug("INICIO: listar usuario");
		List<Usuario> usuarios = new ArrayList<>();
		usuarioRepo.findAll().forEach(usuarios::add);
		logger.debug("FIN: listar usuario");
		return usuarios;
	}
	
	public Usuario buscarPorEmail(String email) {
		return usuarioRepo.findByEmail(email);
	}
}
