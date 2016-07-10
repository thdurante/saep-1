package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import org.junit.*;
import org.junit.rules.ExpectedException;
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

    /**
     * Recupera um Parecer válido.
     * @return Um parecer válido.
     */
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
        listaDeRadocs.add(UUID.randomUUID().toString());

        return listaDeRadocs;
    }

    /**
     * Recupera a lista de Pontuações obtidas pelo Parecer.
     * @return A lista de pontuações.
     */
    private List<Pontuacao> getListaDePontuacoesObtidasPeloParecer() {
        List<Pontuacao> listaDePontuacoes = new ArrayList<>();
        listaDePontuacoes.add(new Pontuacao(
                "pontosAPGrad",     // nome
                new Valor(150)      // valor
        ));
        listaDePontuacoes.add(new Pontuacao(
                "pontosAEADGrad",   // nome
                new Valor(560)      // valor
        ));
        listaDePontuacoes.add(new Pontuacao(
                "aprovadoProm",     // nome
                new Valor(true)     // valor
        ));

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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    public void criaPontuacaoSemNome() {
        thrown.expect(CampoExigidoNaoFornecido.class);
        thrown.expectMessage("nome");
        new Pontuacao("", new Valor(12));
    }

    @Test
    public void criaPontuacaoSemValor() {
        thrown.expect(CampoExigidoNaoFornecido.class);
        thrown.expectMessage("valor");
        new Pontuacao("nome", null);
    }

    @Test
    public void criaNotaSemOrigem() {
        thrown.expect(CampoExigidoNaoFornecido.class);
        thrown.expectMessage("origem");
        new Nota(null, new Pontuacao("nome", new Valor("valor")), "justificativa");
    }

    @Test
    public void criaNotaSemDestino() {
        thrown.expect(CampoExigidoNaoFornecido.class);
        thrown.expectMessage("destino");
        new Nota(new Pontuacao("nome", new Valor("valor")), null, "justificativa");

    }

    @Test
    public void criaNotaSemJustificativa() {
        thrown.expect(CampoExigidoNaoFornecido.class);
        thrown.expectMessage("justificativa");
        new Nota(new Pontuacao("nome", new Valor("valor")), new Pontuacao("nome", new Valor("valor")), null);
    }

    @Test
    @Ignore
    public void persisteParecerValido() {
        Parecer parecer = getParecerValido();
        repositorioDePareceres.persisteParecer(parecer);
    }
}
