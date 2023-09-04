package com.lamim.minhasfinancas.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamim.minhasfinancas.api.dto.UsuarioDTO;
import com.lamim.minhasfinancas.exception.ErroAutenticacao;
import com.lamim.minhasfinancas.exception.RegraNegocioException;
import com.lamim.minhasfinancas.model.entity.Usuario;
import com.lamim.minhasfinancas.service.LancamentoService;
import com.lamim.minhasfinancas.service.UsuarioService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class) // Usado para testar apenas os controles
@AutoConfigureMockMvc
public class UsuarioRepositoryTest {
	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	MockMvc mvc;

	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;

	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		String email = "email@email.com";
		String senha = "1234";
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

		Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar")).accept(JSON)
				.contentType(JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		String email = "email@email.com";
		String senha = "1234";
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();

		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar")).accept(JSON)
				.contentType(JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		String email = "email@email.com";
		String senha = "1234";
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON)
				.contentType(JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Test
	public void deveRetonarBadRequestAoTentarCriarUmNovoUsuarioInvalido() throws Exception {
		String email = "email@email.com";
		String senha = "1234";
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();

		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON)
				.contentType(JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
