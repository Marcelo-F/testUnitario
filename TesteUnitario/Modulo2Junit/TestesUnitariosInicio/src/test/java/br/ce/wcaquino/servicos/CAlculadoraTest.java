package br.ce.wcaquino.servicos;

import br.ce.wcaquino.br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.servicos.runners.ParallelRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ParallelRunner.class)
public class CAlculadoraTest {

    private Calculadora calculadora;

    @Before
    public void setup(){
        calculadora = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        //cenario
        int a = 5;
        int b = 3;

        //ação

        int result = calculadora.somar(a,b);
        //verificação

        Assert.assertEquals(8, result);

    }


    @Test
    public void deveSubtrairDoisValores(){
        //cenario
        int a =8;
        int b =5;
        //ação
        int resultado = calculadora.subtrair(a,b);
        //verificação
        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a= 6;
        int b =  3;

        //ação
        int resultado = calculadora.divide(a,b);

        //verificação

        Assert.assertEquals(2,resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        //cenario
        int a= 10;
        int b =  0;

        //ação
        int resultado = calculadora.divide(a,b);

        //verificação

        Assert.assertEquals(2,resultado);

    }

    @Test
    public void deveDividir(){

        String a = "6";
        String b = "3";
        int resultado = calculadora.divide(a,b);
        Assert.assertEquals(2, resultado);
        }
}
