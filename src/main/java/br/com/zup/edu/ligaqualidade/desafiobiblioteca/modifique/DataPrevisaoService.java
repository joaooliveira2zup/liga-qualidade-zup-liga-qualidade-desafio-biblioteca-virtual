package br.com.zup.edu.ligaqualidade.desafiobiblioteca.modifique;

import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosDevolucao;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosEmprestimo;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.EmprestimoConcedido;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.DadosExemplar;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.DadosLivro;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.DadosUsuario;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.TipoExemplar;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DataPrevisaoService {

	public static LocalDate getDataPrevistaDevolucao(DadosUsuario usuario, DadosExemplar exemplar, LocalDate dataParaSerConsideradaNaExpiracao) {
		if(Objects.isNull(usuario)){
			return null;
		}

		switch (usuario.padrao){
			case PADRAO:
				return getDataPrevistaDevolucaoUsuarioPadrao(exemplar, dataParaSerConsideradaNaExpiracao);
			case PESQUISADOR:
				return getDataPrevistaDevolucaoUsuarioPesquisador(exemplar, dataParaSerConsideradaNaExpiracao);
			default:
				return null;
		}

	}

	private static LocalDate getDataPrevistaDevolucaoUsuarioPesquisador(DadosExemplar exemplar, LocalDate dataVencimento) {
		if ( Objects.isNull(dataVencimento)){
			return LocalDate.now().plusDays(60L);
		}

		long until = LocalDate.now().until(dataVencimento, ChronoUnit.DAYS);
		return (until > 60L) ? LocalDate.now().plusDays(60L) : dataVencimento;
	}

	private static LocalDate getDataPrevistaDevolucaoUsuarioPadrao(DadosExemplar exemplar, LocalDate dataVencimento) {
		if( Objects.isNull(dataVencimento)
				|| TipoExemplar.RESTRITO.equals(exemplar.tipo)){
			return null;
		}

		if( LocalDate.now().until(dataVencimento, ChronoUnit.DAYS) > 60L ){
			return LocalDate.now().plusDays(60L);
		}

		return dataVencimento;
	}

}
