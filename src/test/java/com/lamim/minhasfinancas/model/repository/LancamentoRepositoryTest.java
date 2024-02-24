package com.lamim.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.lamim.minhasfinancas.model.entity.Lancamento;
import com.lamim.minhasfinancas.model.enums.StatusLancamento;
import com.lamim.minhasfinancas.model.enums.TipoLacamento;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	@Autowired
	LancamentoRepository repository;

	@Autowired
	TestEntityManager entityManaget;
	


	@Test
	public void deveSalvarUmUsuario() {
		Lancamento lancamento = criarLancamento();

		lancamento = repository.save(lancamento);
		Assertions.assertThat(lancamento.getId()).isNotNull();
	}

	@Test
	public void deveDeletarUsuario() {
		Lancamento lancamento = criarEPersistirUmLancamento();

		lancamento = entityManaget.find(Lancamento.class, lancamento.getId());

		repository.delete(lancamento);

		Lancamento lancamentoInexistente = entityManaget.find(Lancamento.class, lancamento.getId());
		Assertions.assertThat(lancamentoInexistente).isNull();
	}
	
	@Test
	public void deveAtualisarLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		lancamento.setAno(2008);
		lancamento.setDescricao("Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		Lancamento lancamentoAtualizado = entityManaget.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2008);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Atualizar");
		Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId () {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
	
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	

	private Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLancamento();
		entityManaget.persist(lancamento);
		return lancamento;
	}

	public static Lancamento criarLancamento() {
		return Lancamento.builder().ano(2019).mes(3).descricao("Descricao").valor(BigDecimal.valueOf(10))
				.tipo(TipoLacamento.DESPESA).status(StatusLancamento.PENDENTE).dataCadastro(LocalDate.now()).build();
	}
}
