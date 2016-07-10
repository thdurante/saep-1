package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import com.sun.tools.corba.se.idl.constExpr.Equal;
import org.junit.*;
import org.junit.rules.ExpectedException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
    private Parecer getParecerValido(String id) {
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
                new Valor(false)    // valor
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
    public void persisteParecerComIdDuplicado() {
        thrown.expect(IdentificadorExistente.class);
        thrown.expectMessage("já persistido");

        String id = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(id));
        repositorioDePareceres.persisteParecer(getParecerValido(id));
    }

    @Test
    public void persisteParecerValido() {
        Parecer parecer = getParecerValido(UUID.randomUUID().toString());
        repositorioDePareceres.persisteParecer(parecer);
    }

    @Test
    public void persisteParecerValidoComNotas() {

    }

    @Test
    public void recuperaParecerPorId() {
        String id = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(id));
        Parecer parecer = repositorioDePareceres.byId(id);

        assertEquals("id deve ser igual", id, parecer.getId());
        assertEquals("resolucao deve ser igual", "CONSUNI No 32/2013", parecer.getResolucao());
        assertEquals("deve possuir 1 radoc", 1, parecer.getRadocs().size());
        assertEquals("fundamentacao deve ser igual", "Fundamentação do parecer", parecer.getFundamentacao());
        assertEquals("deve possuir 0 notas", 0, parecer.getNotas().size());
        assertEquals("deve possuir 3 pontuacoes", 3, parecer.getPontuacoes().size());
        assertNotNull("as pontuacoes não devem ser null", parecer.getPontuacoes().get(0));
        assertNotNull("as pontuacoes não devem ser null", parecer.getPontuacoes().get(1));
        assertNotNull("as pontuacoes não devem ser null", parecer.getPontuacoes().get(2));
        assertEquals("o atributo da pontuacao 0 deve coincidir", "pontosAPGrad", parecer.getPontuacoes().get(0).getAtributo());
        assertEquals("o atributo da pontuacao 1 deve coincidir", "pontosAEADGrad", parecer.getPontuacoes().get(1).getAtributo());
        assertEquals("o atributo da pontuacao 2 deve coincidir", "aprovadoProm", parecer.getPontuacoes().get(2).getAtributo());
        assertThat("o valor da pontuacao 0 deve coincidir", parecer.getPontuacoes().get(0).getValor().getFloat(), is((float) 150));
        assertThat("o valor da pontuacao 1 deve coincidir", parecer.getPontuacoes().get(1).getValor().getFloat(), is((float) 560));
        assertThat("o valor da pontuacao 2 deve coincidir", parecer.getPontuacoes().get(2).getValor().getBoolean(), is(false));
        //assertThat("o valor da pontuacao 0 deve coincidir pelo método get", parecer.getPontuacoes().get(0).get("pontosAPGrad"), is((float) 150));
        //assertThat("o valor da pontuacao 1 deve coincidir pelo método get", parecer.getPontuacoes().get(1).get("pontosAPGrad"), is((float) 560));
        //assertThat("o valor da pontuacao 2 deve coincidir pelo método get", parecer.getPontuacoes().get(2).get("pontosAPGrad"), is(false);
    }
}
