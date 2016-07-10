package br.ufg.inf.es.saep.sandbox.persistencia;

import org.junit.AfterClass;
import org.junit.Before;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class RepositorioDePareceresTest {

    RepositorioDePareceres repositorioDePareceres;
    PodamFactory factory;

    /*
    * PRIVATE METHODS
    * */

    /*
    * TESTS
    * */

    @Before
    public void setUp() {
        this.repositorioDePareceres = new RepositorioDePareceres();
        this.factory = new PodamFactoryImpl();
    }

    @AfterClass
    public static void tearDown() {
        new RepositorioDeResolucoes().clearDB();
    }
}
