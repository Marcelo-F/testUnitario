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
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static builders.FilmeBuilder.umFilme;
import static builders.LocacaoBuilder.umLocacao;
import static builders.UsuarioBuilder.umUsuario;
import static matchers.MatcherProprios.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTeste_PowerMock {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @InjectMocks
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

        service = PowerMockito.spy(service);

    }

    @Test
    public void deveAlugarFilme() throws Exception {

        //cenario

        Usuario usuario = umUsuario().agora();

        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        Mockito.doReturn(DataUtils.obterData(28,4,2017)).when(service).obterData();
        //acao

        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao

        assertThat(locacao.getValor(), is(5.0));

        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
    }

    //Forma elegante


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

        // PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();3
        PowerMockito.verifyStatic(Mockito.times(2));
        new Date();
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
    public void edveAlugarFilme_SemCalcularValor() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());
        Locacao locacao = new Locacao();
        // Para chamar o metodo privado
        PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
        //acao
        service.alugarFilme(usuario, filmes);
        //verificação

        Assert.assertThat(locacao.getValor(), is(1.0));
        PowerMockito.verifyPrivate(service).invoke();
    }


}
