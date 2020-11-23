package com.globallogic.interview;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import com.globallogic.interview.model.Telefono;
import com.globallogic.interview.model.Usuario;
import com.globallogic.interview.service.JwtService;
import com.globallogic.interview.service.MyUserDetailsService;
import com.globallogic.interview.service.UsuarioService;

@SpringBootTest(classes={ApiUsuarioApplication.class})
class UsuarioControllerTest {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;

    @Test
    public void crearUsuarioYValidarTokenJwtTest() {
    	
    	Telefono t = new Telefono(0, "9876656", "2", "57");
    	List<Telefono> telefonos = new ArrayList<>();
    	telefonos.add(t);

        Usuario u = new Usuario(0, "Usuario Prueba", "usuario.prueba@prueba.com", "Uprueba02", telefonos);
        Map<String, Object> mapUsuario = usuarioService.crearUsuario(u).getBody();
        
        Usuario usuarioCreado = (Usuario) mapUsuario.get("usuario");
        
        assertTrue(usuarioCreado != null && usuarioCreado.getId() != 0);
        
        String token = (String) mapUsuario.get("token");
        
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(usuarioCreado.getEmail());
        
        assertTrue(jwtService.validarToken(token, userDetails));
    }
    
    

    
}

