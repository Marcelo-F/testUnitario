package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Mock
    private EmailService emailService;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {

        //Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
        Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
        //Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
        Mockito.doReturn(5).when(calcSpy).somar(1,2);
        Mockito.doNothing().when(calcSpy).imprime();
        System.out.println("MOck: "+calcMock.somar(1, 5));
        System.out.println("Spy: "+calcSpy.somar(1, 5));// Não funciona com interface


        System.out.println("Mock");
        calcMock.imprime();
        System.out.println("Spy");
        calcMock.imprime();
    }

    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);
//        Mockito.when(calc.somar(Mockito.anyInt(),Mockito.anyInt())).thenReturn(5);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);

        Assert.assertEquals(5, calc.somar(1, 100000));

        System.out.println(argumentCaptor.getAllValues());
    }
}
