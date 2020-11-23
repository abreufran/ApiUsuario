package com.globallogic.interview.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.globallogic.interview.model.SolicitudAutenticacion;
import com.globallogic.interview.model.Usuario;
import com.globallogic.interview.service.UsuarioService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;

	
	@PostMapping(value = "/usuario/crear-usuario", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "Crea un usuario.")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Error inesperado al crear el usuario", response = HttpStatus.class),
		@ApiResponse(code = 201, message = "Usuario Creado", response = HttpStatus.class)
		})
	public ResponseEntity<Map<String, Object>> crear(@RequestBody Usuario usuario) {
		return usuarioService.crearUsuario(usuario);
	}

	@GetMapping(value = "/usuario/listar-usuarios", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "Devuelve un listado con los usuarios. Para hacer uso de este servicio se debe hacer uso del servicio generar-token-jwt. "
			+ "Tambien es posible utilizar el token que devuelve el servicio crear-usuario")
	@ApiResponses({
		@ApiResponse(code = 404, message = "No se encontraron usuarios", response = HttpStatus.class),
		@ApiResponse(code = 200, message = "Listado de Usuarios encontrado", response = HttpStatus.class),
		@ApiResponse(code = 400, message = "Error inesperado al listar los usuarios", response = HttpStatus.class)
		})
	public ResponseEntity<List<Usuario>> listar(@RequestHeader(required = true, name = "token") String token) {
		return usuarioService.listarUsuarios();
	}
	
	@PostMapping(value = "/generar-token-jwt", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "Servicio que permite generar un token JWT. Este token permite ejecutar las operaciones de ApiUsuario durante 10 horas. "
			+ "Username: admin@prueba.com / Password: Admin03 necesario para que Spring Security permita generar el token")
	@ApiResponses({
		@ApiResponse(code = 403, message = "No esta Autorizado para generar token JWT", response = HttpStatus.class),
		@ApiResponse(code = 200, message = "Token JWT", response = HttpStatus.class)
		})
	public ResponseEntity<Map<String, String>> generarTokenJwt(@RequestBody SolicitudAutenticacion solicitudAutenticacion) throws Exception {
		return usuarioService.generarToken(solicitudAutenticacion);
	}
}
