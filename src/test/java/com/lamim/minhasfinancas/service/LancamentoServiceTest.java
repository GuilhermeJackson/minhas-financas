package com.lamim.minhasfinancas.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.lamim.minhasfinancas.exception.RegraNegocioException;
import com.lamim.minhasfinancas.model.entity.Lancamento;
import com.lamim.minhasfinancas.model.enums.StatusLancamento;
import com.lamim.minhasfinancas.model.repository.LancamentoRepository;
import com.lamim.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.lamim.minhasfinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
    LancamentoServiceImpl service;
	
    @MockBean
    LancamentoRepository repository;
    
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execução
        Lancamento lancamento = service.salvar(lancamentoASalvar);

        //
        Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
        
        Assertions.catchThrowableOfType(() -> service.	salvar(lancamentoASalvar), RegraNegocioException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1L);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
        Mockito.doNothing().when(service).validar(lancamentoSalvo);
        service.atualizar(lancamentoSalvo);
        
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}
	
	@Test
	public void deveRetornarErroAoTentarAtualizarUmLancamentoQueNaoFoiSalvo() {
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoSalvo), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).save(lancamentoSalvo);
	}
	
	@Test
	public void deveDeletarUmLancamentoComSucesso() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		service.deletar(lancamento);
		
		Mockito.verify(repository).delete(lancamento);
	}
	
	@Test
	public void naoDeveDeletarUmLancamentoQueNãoFoiSalvo() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		service.deletar(lancamento);
		
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}
}
