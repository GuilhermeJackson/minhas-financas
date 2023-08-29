package com.lamim.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lamim.minhasfinancas.api.dto.LancamentoDTO;
import com.lamim.minhasfinancas.exception.RegraNegocioException;
import com.lamim.minhasfinancas.model.entity.Lancamento;
import com.lamim.minhasfinancas.model.entity.Usuario;
import com.lamim.minhasfinancas.model.enums.StatusLancamento;
import com.lamim.minhasfinancas.model.enums.TipoLacamento;
import com.lamim.minhasfinancas.service.LancamentoService;
import com.lamim.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor 
public class LancamentoResource {
	private final LancamentoService lancamentoService;
	private final UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<Object> buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false)Integer ano,
			@RequestParam(value = "usuario") long idUsuario
			) {
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setAno(ano);
		lancamentoFiltro.setMes(mes);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		
		if(usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta");
		}
		if(!usuario.isPresent()) {
			lancamentoFiltro.setUsuario(usuario.get());			
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@PostMapping
	public ResponseEntity<Object> salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = lancamentoService.salvar(entidade)	;
			return new ResponseEntity<Object>(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto ) {
		return lancamentoService.obterPorId(id).map(entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(id);
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
				
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
		new ResponseEntity<String>("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}
	
	@DeleteMapping("{id}")
	private ResponseEntity<String> deletar (@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id).map(entity -> {
			try {
				lancamentoService.deletar(entity);
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
		new ResponseEntity<String>("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario_id())
		.orElseThrow(() ->  new RegraNegocioException("Usuário não encontrado para o ID informado!"));
		lancamento.setUsuario(usuario);

		if(dto.getTipo() != null) {		
			lancamento.setTipo(TipoLacamento.valueOf(dto.getTipo()));
		}
		
		if(dto.getStatus() != null) {			
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		return lancamento;
	}
}
