package com.globallogic.interview;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.globallogic.interview.controller.UsuarioController;
import com.globallogic.interview.model.SolicitudAutenticacion;
import com.globallogic.interview.model.Telefono;
import com.globallogic.interview.model.Usuario;
import com.globallogic.interview.service.JwtService;
import com.globallogic.interview.service.MyUserDetailsService;
import com.globallogic.interview.service.UsuarioService;

@AutoConfigureMockMvc
@SpringBootTest(classes={ApiUsuarioApplication.class})
class UsuarioControllerTest {
	
	@Autowired
	private UsuarioController usuarioController;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
    private MockMvc mockMvc;
	
    @Test
    public void testFiltroDeSeguridad()  throws Exception {
    	MockHttpServletResponse response = mockMvc.perform(get("/usuario/listar-usuarios")
                .header("token", jwtService.generarToken("usuario.prueba@prueba.com"))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
    }
    
    @Test
    public void testCrearUsuarioInValido() {
    	Telefono t = new Telefono(0, "9876656", "2", "57");
    	List<Telefono> telefonos = new ArrayList<>();
    	telefonos.add(t);

        Usuario u = new Usuario(0, "Usuario Prueba", "usuario.prueba@prueba1.com", "Uprueba02", telefonos);
        
        u.setEmail("CORREO_INCORRECTO");
        ResponseEntity<Map<String, Object>> responseEntity = usuarioService.crearUsuario(u);
        assertThat(responseEntity.getStatusCode().value(), equalTo(HttpStatus.BAD_REQUEST.value()));
        Map<String, Object> mapUsuario = responseEntity.getBody();
        assertTrue(mapUsuario.get("error") != null);
        
        u = new Usuario(0, "Usuario Prueba", "usuario.prueba@prueba2.com", "Uprueba02", telefonos);
        
        //Correo ya registrado
        u.setEmail("admin@prueba.com");
        responseEntity = usuarioService.crearUsuario(u);
        assertThat(responseEntity.getStatusCode().value(), equalTo(HttpStatus.BAD_REQUEST.value()));
        mapUsuario = responseEntity.getBody();
        assertTrue(mapUsuario.get("error") != null);
        
        u = new Usuario(0, "Usuario Prueba", "usuario.prueba@prueba3.com", "Uprueba02", telefonos);
        
        u.setPassword("PASSWORD_INCORRECTO");
        responseEntity = usuarioService.crearUsuario(u);
        assertThat(responseEntity.getStatusCode().value(), equalTo(HttpStatus.BAD_REQUEST.value()));
        mapUsuario = responseEntity.getBody();
        assertTrue(mapUsuario.get("error") != null);
    }

    @Test
    public void crearUsuarioYValidarTokenJwtTest() {
    	
    	Telefono t = new Telefono(0, "9876656", "2", "57");
    	List<Telefono> telefonos = new ArrayList<>();
    	telefonos.add(t);

        Usuario u = new Usuario(0, "Usuario Prueba", "usuario.prueba@prueba.com", "Uprueba02", telefonos);
        ResponseEntity<Map<String, Object>> responseEntity = usuarioController.crear(u);
        assertThat(responseEntity.getStatusCode().value(), equalTo(HttpStatus.CREATED.value()));
        
        Map<String, Object> mapUsuario = responseEntity.getBody();
        
        Usuario usuarioCreado = (Usuario) mapUsuario.get("usuario");
        
        assertTrue(usuarioCreado != null && usuarioCreado.getId() != 0);
        
        String token = (String) mapUsuario.get("token");
        
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(usuarioCreado.getEmail());
        
        assertTrue(jwtService.validarToken(token, userDetails));
    }
    
    
    @Test
    public void listarUsuarios() {
    	SolicitudAutenticacion solicitudAutenticacion = new SolicitudAutenticacion("admin@prueba.com", "Admin03");
    	
    	ResponseEntity<Map<String, String>> responseEntityJwt = usuarioController.generarTokenJwt(solicitudAutenticacion);
    	assertThat(responseEntityJwt.getStatusCode().value(), equalTo(HttpStatus.OK.value()));
    	
    	Map<String, String> mapToken = responseEntityJwt.getBody();
    	ResponseEntity<List<Usuario>> responseEntityUsuario = usuarioController.listar(mapToken.get("token"));
    	assertThat(responseEntityUsuario.getStatusCode().value(), equalTo(HttpStatus.OK.value()));
    	
    	List<Usuario> usuarios = responseEntityUsuario.getBody();
    	
    	assertTrue(usuarios.size() > 0);
    }
    
}

