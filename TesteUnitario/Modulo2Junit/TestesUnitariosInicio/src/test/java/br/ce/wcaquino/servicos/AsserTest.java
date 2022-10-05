package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AsserTest {
    @Test
    public void Test(){
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals("Erro de comparação",1,1);
        Assert.assertEquals(0.51,0.51, 0.01);

        int i =5;
        Integer i2 = 5;
        Assert.assertEquals(Integer.valueOf(i),i2);
        Assert.assertEquals(i,i2.intValue());

        Assert.assertEquals("bola", "bola");
        Assert.assertNotEquals("bola", "rede");
        Assert.assertTrue("bola".equalsIgnoreCase( "Bola"));
        Assert.assertTrue("bola".startsWith( "bo"));

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = null;

        Assert.assertEquals(u1, u2);
       // Assert.assertSame(u1,u2); // Valida se é da mesma instancia
        Assert.assertNotSame(u1,u2);
        Assert.assertNull(u3);
        Assert.assertNotNull(u2);
    }
}
