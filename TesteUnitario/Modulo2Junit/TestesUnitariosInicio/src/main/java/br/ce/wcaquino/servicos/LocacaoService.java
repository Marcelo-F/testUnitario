package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import daos.LocacaoDAO;



public class LocacaoService {

	private LocacaoDAO locacaoDAO;

	private SPCService spcService;

	private EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {


		if(usuario == null){
			throw  new LocadoraException("Usuario vazio");
		}
		if(filmes == null || filmes.isEmpty()) {
			throw  new LocadoraException("Filme vazio");
		}
		for (Filme f: filmes){
			if(f.getEstoque() == 0){
				throw  new FilmeSemEstoqueException("Filme sem estoque");
			}

		}
		boolean negativado;
		try {
			negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			if (negativado){
				throw  new LocadoraException("Usuário negativado");
			}
		} catch (Exception e) {
			throw new LocadoraException("Problemas com o spc, tente novamente");
		}
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(obterData());
		Double valorTotal = calcularValorLocacao(filmes);
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);

		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		locacaoDAO.salvar(locacao);

		return locacao;
	}

	protected Date obterData() {
		return new Date();
	}

	private Double calcularValorLocacao(List<Filme> filmes) {
		Double valorTotal = 0d;
		for (int i = 0; i< filmes.size(); i++){
				Double valorFilme = filmes.get(i).getPrecoLocacao();
				switch (i){
					case 2:valorFilme = valorFilme*0.75; break;
					case 3:valorFilme = valorFilme*0.50; break;
					case 4:valorFilme = valorFilme*0.25; break;
					case 5:valorFilme = valorFilme*0.0; break;

				}

			valorTotal+= valorFilme;
		}
		return valorTotal;
	}

	public  void notificarAtrasos(){
		List<Locacao>locacaoList = locacaoDAO.obterLocacoesPendentes();

		for (Locacao locacao : locacaoList){
			if (locacao.getDataRetorno().before(new Date())){
				emailService.notificarAtraso(locacao.getUsuario());
			}

		}

	}

	public void prorrogarLocacao(Locacao locacao, int dias){
		Locacao novaLocacao = new Locacao();

		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		locacaoDAO.salvar(novaLocacao);

	}

}