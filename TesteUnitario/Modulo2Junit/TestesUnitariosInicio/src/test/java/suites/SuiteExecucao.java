package suites;

import br.ce.wcaquino.servicos.CalculoValorLocacaoTeste;
import br.ce.wcaquino.servicos.LocacaoServiceTeste;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //  CAlculadoraTest.class,
        CalculoValorLocacaoTeste.class, LocacaoServiceTeste.class
})
public class SuiteExecucao {

//    @BeforeClass
//    public static void before() {
//        System.out.println("BEFORE");
//    }
//
//    @AfterClass
//    public static void after() {
//        System.out.println("AFTER");
//    }
}
