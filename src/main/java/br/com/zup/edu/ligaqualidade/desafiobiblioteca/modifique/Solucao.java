package br.com.zup.edu.ligaqualidade.desafiobiblioteca.modifique;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosDevolucao;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosEmprestimo;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.EmprestimoConcedido;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.DadosExemplar;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.DadosLivro;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.DadosUsuario;

public class Solucao {

	/**
	 * Você precisa implementar o código para executar o fluxo
	 * o completo de empréstimo e devoluções a partir dos dados
	 * que chegam como argumento. 
	 * 
	 * Caso você queira pode adicionar coisas nas classes que já existem,
	 * mas não pode alterar nada.
	 */
	
	/**
	 * 
	 * @param livros dados necessários dos livros
	 * @param dadosExemplares tipos de dadosExemplares para cada livro
	 * @param dadosUsuarios tipos de dadosUsuarios
	 * @param dadosEmprestimos informações de pedidos de empréstimos
	 * @param dadosDevolucoes informações de devoluções, caso exista.
	 * @param dataParaSerConsideradaNaExpiracao aqui é a data que deve ser utilizada para verificar expiração
	 * @return
	 */
	public static Set<EmprestimoConcedido> executa(Set<DadosLivro> livros,
			Set<DadosExemplar> dadosExemplares,
			Set<DadosUsuario> dadosUsuarios, Set<DadosEmprestimo> dadosEmprestimos,
			Set<DadosDevolucao> dadosDevolucoes, LocalDate dataParaSerConsideradaNaExpiracao) {

		Set<EmprestimoConcedido> emprestimosRealizados = new HashSet<>();
		for(DadosLivro livro : livros){
			DadosExemplar exemplar = dadosExemplares.stream().filter( exemp -> exemp.idLivro == livro.id).findFirst().orElse(null);
			DadosEmprestimo emprestimo = dadosEmprestimos.stream().filter( emp -> emp.idLivro == livro.id).findFirst().orElse(null);
			DadosUsuario usuario = emprestimo != null ? dadosUsuarios.stream().filter(usr -> usr.idUsuario == emprestimo.idUsuario).findFirst().orElse(null) : null;

			LocalDate dataDevolucao = DataPrevisaoService.getDataPrevistaDevolucao(usuario, exemplar, dataParaSerConsideradaNaExpiracao);

			if( nothingIsNull(exemplar, dataDevolucao, emprestimo, usuario) && isTempoExcedente(emprestimo.tempo) && emprestimosRealizados.size() <= 5) {
				EmprestimoConcedido emprestimoRealizado = new EmprestimoConcedido(usuario.idUsuario, exemplar.idExemplar, dataDevolucao);
				emprestimosRealizados.add(emprestimoRealizado);
			}
		}

		return emprestimosRealizados;
	}

	private static boolean nothingIsNull( DadosExemplar exemplar, LocalDate dataDevolucao, DadosEmprestimo dadosEmprestimo, DadosUsuario usuario){
		return dataDevolucao != null && exemplar != null  && dadosEmprestimo != null && usuario != null;
	}

	private static boolean isTempoExcedente(int tempo){
		return tempo <= 60;
	}

}
