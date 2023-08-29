package com.lamim.minhasfinancas.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import com.lamim.minhasfinancas.exception.ErroAutenticacao;
import com.lamim.minhasfinancas.exception.RegraNegocioException;
import com.lamim.minhasfinancas.model.entity.Usuario;
import com.lamim.minhasfinancas.model.repository.UsuarioRepository;
import com.lamim.minhasfinancas.service.UsuarioService;

@Service //Criado uma única instancia da classe (Singleton)
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Este e-mail não corresponde a nenhum cadastro!");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida!");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean isExiste = repository.existsByEmail(email);
		if(isExiste) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse e-mail");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		
		return repository.findById(id);
	}
}
