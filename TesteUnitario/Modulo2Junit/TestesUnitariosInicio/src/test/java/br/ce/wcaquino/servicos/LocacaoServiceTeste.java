package br.ce.wcaquino.servicos;

import br.ce.wcaquino.br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import daos.LocacaoDAO;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static builders.FilmeBuilder.umFIlmeSemEstoque;
import static builders.FilmeBuilder.umFilme;
import static builders.LocacaoBuilder.umLocacao;
import static builders.UsuarioBuilder.umUsuario;
import static matchers.MatcherProprios.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class LocacaoServiceTeste {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @InjectMocks @Spy
    private LocacaoService service;

    @Mock
    private SPCService spc;

    @Mock
    private LocacaoDAO dao;

    @Mock
    private EmailService email;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

    }


    @Test
    public void deveAlugarFilme() throws Exception {

        //cenario

        Usuario usuario = umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());


        //acao
        Mockito.doReturn(DataUtils.obterData(28,4,2017)).when(service).obterData();
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        //error.checkThat(Assert.assertThat(locacao.getValor(), is(5.0)));// usando error.checkThat, ele roda todo o teste e no final ele mostra todos os erros
        assertThat(locacao.getValor(), is(5.0));
//        assertThat(locacao.getValor(), is(not(6.0)));
//        Assert.assertEquals(5.0, locacao.getValor(), 0.01);
        // Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        //Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
        //error.checkThat(locacao.getDataRetorno(), MatcherProprios.ehHojeComDiferencaDias(1));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
    }

    //Forma elegante
    @Test(expected = FilmeSemEstoqueException.class)
    public void deveLancarExcecaoAoAlugarFIlmeSemEstoque() throws Exception {
        //cenario

        Usuario usuario = umUsuario().agora();
        ;

        List<Filme> filmes = Arrays.asList(umFIlmeSemEstoque().agora());

        //acao

        service.alugarFilme(usuario, filmes);

    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        //cenario


        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        try {
            service.alugarFilme(null, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }


    }

    @Test
    public void naoDeveAlugarFIlmeSemEstoque() throws LocadoraException, FilmeSemEstoqueException {
        //cenario

        Usuario usuario = umUsuario().agora();

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);

    }

    @Test
    public void testeLocaçãoFilmeSemEstoque2() {
        //cenario

        Usuario usuario = umUsuario().agora();
        ;


        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        //acao

        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }

    }

    @Test
    public void testeLocaçãoFilmeSemEstoque3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario


        Usuario usuario = umUsuario().agora();
        ;


        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

        expectedException.expect(FilmeSemEstoqueException.class);
        expectedException.expectMessage("Filme sem estoque");

        //acao

        service.alugarFilme(usuario, filmes);


    }

    @Test
    @Ignore
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    @Ignore
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);
        //verificacao
        assertThat(resultado.getValor(), is(13.0));
    }


    @Test
    @Ignore
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();
        ;
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);
        //verificacao
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    @Ignore
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();
        ;
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);
        //verificacao
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Locacao retorno = service.alugarFilme(usuario, filmes);

        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {

        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.doReturn(DataUtils.obterData(29,4,2017)).when(service).obterData();

        PowerMockito.verifyStatic(Mockito.times(2));

    }

    @Test
    public void naoDeveAlugarFIlmeParaNegativadoSPC() throws Exception {

        Usuario usuario = umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().agora());


        Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);


        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário negativado"));
        }

        verify(spc).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEMailParaLocacoesAtrasadas() {
        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Outro Atrasado").agora();
        List<Locacao> locacaoList = Arrays.asList(
                umLocacao().atrasado().comUsuario(usuario).agora(),
                umLocacao().comUsuario(usuario2).agora(),
                umLocacao().atrasado().comUsuario(usuario3).agora(),
                umLocacao().atrasado().comUsuario(usuario3).agora());
        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacaoList);
        //acao
        service.notificarAtrasos();

        //verificacao

        verify(email, Mockito.times(3)).notificarAtraso(Mockito.<Usuario>any(Usuario.class));
        verify(email).notificarAtraso(usuario);
        verify(email, Mockito.times(2)).notificarAtraso(usuario3);
        verify(email, Mockito.never()).notificarAtraso(usuario2);
        Mockito.verifyNoMoreInteractions(email);

    }

    @Test
    public void deveTratarErrosNoSpc() throws Exception {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catrastrófica"));

        expectedException.expect(LocadoraException.class);
//        expectedException.expectMessage("Falha catrastrófica");
        expectedException.expectMessage("Problemas com o spc, tente novamente");

        service.alugarFilme(usuario, filmes);


    }

    @Test
    public void deveProrrogarUmaLocacao() {
        Locacao locacao = umLocacao().agora();

        service.prorrogarLocacao(locacao, 3);


        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(dao).salvar(argumentCaptor.capture());
        Locacao locacaoRetorno = argumentCaptor.getValue();

        error.checkThat(locacaoRetorno.getValor(), is(12.0));
        error.checkThat(locacaoRetorno.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetorno.getDataRetorno(), ehHojeComDiferencaDias(3));
    }




    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        //acao

        Class<LocacaoService> clazz = LocacaoService.class;
        Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
        metodo.setAccessible(true);
        Double valor = (Double) metodo.invoke(service, filmes);
        //Chamando os metodos privados diretamente
        //Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);


        //verificacao

        Assert.assertThat(valor, is(4.0));


    }
}
