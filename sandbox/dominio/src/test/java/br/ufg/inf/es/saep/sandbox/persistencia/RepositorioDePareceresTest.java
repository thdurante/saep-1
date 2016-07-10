package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepositorioDePareceresTest {

    RepositorioDePareceres repositorioDePareceres;
    PodamFactory factory;

    /*
    * PRIVATE METHODS
    * */

    private Parecer getParecerValido() {
        String id = UUID.randomUUID().toString();
        return new Parecer(
                id,                                             // id
                "CONSUNI No 32/2013",                           // resolucaoId
                getListaDeRadocsUtilizadosNoParecer(),          // radocsIds
                getListaDePontuacoesObtidasPeloParecer(),       // pontuacoes
                "Fundamentação do parecer",                     // fundamentacao
                getListaDeNotasDoParecer()                      // notas
        );
    }

    /**
     * Recupera os ids dos Radocs utilizados em um parecer.
     * @return A lista de ids de Radocs que foram utilizados em um Parecer.
     */
    private List<String> getListaDeRadocsUtilizadosNoParecer() {
        List<String> listaDeRadocs = new ArrayList<>();

        return listaDeRadocs;
    }

    /**
     * Recupera a lista de Pontuações obtidas pelo Parecer.
     * @return A lista de pontuações.
     */
    private List<Pontuacao> getListaDePontuacoesObtidasPeloParecer() {
        List<Pontuacao> listaDePontuacoes = new ArrayList<>();

        return listaDePontuacoes;
    }

    /**
     * Recupera a lista de Notas de um parecer.
     * @return Lista de Notas de um parecer.
     */
    private List<Nota> getListaDeNotasDoParecer() {
        List<Nota> listaDeNotas = new ArrayList<>();

        return listaDeNotas;
    }

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

    @Test
    public void persisteParecerValido() {

    }
}
