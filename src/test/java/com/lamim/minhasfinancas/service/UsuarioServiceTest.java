package com.lamim.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.lamim.minhasfinancas.exception.ErroAutenticacao;
import com.lamim.minhasfinancas.exception.RegraNegocioException;
import com.lamim.minhasfinancas.model.entity.Usuario;
import com.lamim.minhasfinancas.model.repository.UsuarioRepository;
import com.lamim.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;

	
	@Test(expected = Test.None.class)
	public void deveSalvarUsuario() {
		 Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		 Usuario usuario = Usuario.builder()
				 .id(1l)
				 .nome("nome")
				 .email("email@email.com")
				 .senha("senha")
				 .build();
		 Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		 
		 Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		 
		 Assertions.assertThat(usuarioSalvo).isNotNull();
		 Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		 Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		 Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		 Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUsuarioComEmailJaCadastrado() {
		String email = "emal@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		service.salvarUsuario(usuario);
		
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		String email = "email@email.com";
		String senha = "1234";
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario result = service.autenticar(email, senha);
		
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		String messageError = "Este e-mail não corresponde a nenhum cadastro!";
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		Throwable exeption = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "1234"));
		Assertions.assertThat(exeption).isInstanceOf(ErroAutenticacao.class).hasMessage(messageError);

	}
	
	@Test
	public void deveLancarErroQUandoASenhaEstiverErrado() {
		String messageError = "Senha inválida!";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		Throwable exeption = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "1234"));
		Assertions.assertThat(exeption).isInstanceOf(ErroAutenticacao.class).hasMessage(messageError);
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		service.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		service.validarEmail("email@email.com");
	}
}
