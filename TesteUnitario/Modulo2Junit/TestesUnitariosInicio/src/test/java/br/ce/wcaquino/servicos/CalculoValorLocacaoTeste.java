package br.ce.wcaquino.servicos;

import br.ce.wcaquino.br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import daos.LocacaoDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static builders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private SPCService spc;


    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorAlocacao;

    @Parameterized.Parameter(value = 2)
    public String cenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    private static Filme filme1 = umFilme().agora();
    private static Filme filme2 = umFilme().agora();
    private static Filme filme3 = umFilme().agora();
    private static Filme filme4 = umFilme().agora();
    private static Filme filme5 = umFilme().agora();
    private static Filme filme6 = umFilme().agora();
    private static Filme filme7 = umFilme().agora();

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2), 8.0, "3 FIlmes: Sem desconto"},
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 FIlmes: 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 FIlmes: 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 FIlmes: 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme6), 14.0, "6 FIlmes: 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme6, filme7), 14.0, "7 FIlmes: Sem desconto no sétimo filme"}
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDesContos() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        //acao
        Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
        //verificacao
        Assert.assertThat(resultado.getValor(), is(valorAlocacao));
    }


    @Test
    public void print() {
        System.out.println(valorAlocacao);
    }
}
