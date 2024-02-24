package com.lamim.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {
	private String email;
	private String nome;
	private String senha;
}
