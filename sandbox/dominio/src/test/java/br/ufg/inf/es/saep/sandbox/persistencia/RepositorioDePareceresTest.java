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
import static org.junit.Assert.*;

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
        listaDeNotas.add(new Nota(
                new Pontuacao("aprovadoProm", new Valor(false)),
                new Pontuacao("aprovadoProm", new Valor(true)),
                "Alteração realizada porque um cálculo tinha sido feito de maneira errada."
        ));

        return listaDeNotas;
    }

    /**
     * Recupera uma Nota qualquer a ser adicionada a um Parecer.
     * @return A nota a ser adicionada a um Parecer.
     */
    private Nota getSampleNota() {
        return new Nota(
                new Pontuacao("nomeAtributo", new Valor("valor")),
                new Pontuacao("nomeAtributo", new Valor("novoValor")),
                "justificativa alteração"
        );
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
        // RepositorioDePareceres.clearDB();
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
        String id = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(id));

        Parecer parecer = repositorioDePareceres.byId(id);
        assertNotNull("parecer não deve ser null", parecer);
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
        assertNotNull("o get do item Avaliavel 0 não pode ser null", parecer.getPontuacoes().get(0).get("pontosAPGrad"));
        assertNotNull("o get do item Avaliavel 1 não pode ser null", parecer.getPontuacoes().get(1).get("pontosAEADGrad"));
        assertNotNull("o get do item Avaliavel 2 não pode ser null", parecer.getPontuacoes().get(2).get("aprovadoProm"));
        assertNull("o get do item Avaliavel 0 deve ser null caso o atributo não exista", parecer.getPontuacoes().get(0).get("atributoInexistente"));
        assertThat("o valor da pontuacao 0 deve coincidir pelo método get da interface Avaliavel", parecer.getPontuacoes().get(0).get("pontosAPGrad").getFloat(), is((float) 150));
        assertThat("o valor da pontuacao 1 deve coincidir pelo método get da interface Avaliavel", parecer.getPontuacoes().get(1).get("pontosAEADGrad").getFloat(), is((float) 560));
        assertThat("o valor da pontuacao 2 deve coincidir pelo método get da interface Avaliavel", parecer.getPontuacoes().get(2).get("aprovadoProm").getBoolean(), is(false));
        assertEquals("o tamanho da lista de notas deve ser igual a 1", 1, parecer.getNotas().size());
        assertEquals("o valor do item original de Notas[0] deve coincidir", false, parecer.getNotas().get(0).getItemOriginal().get("aprovadoProm").getBoolean());
        assertEquals("o valor do item alterado de Notas[0] deve coincidir", true, parecer.getNotas().get(0).getItemNovo().get("aprovadoProm").getBoolean());
        assertEquals("a justificativa de Notas[0] deve coincidir", "Alteração realizada porque um cálculo tinha sido feito de maneira errada.", parecer.getNotas().get(0).getJustificativa());
    }

    @Test
    public void adicionaNotaEmParecerInexistente() {
        thrown.expect(IdentificadorDesconhecido.class);
        thrown.expectMessage("id desconhecido");

        String parecerId = "id inexistente";
        repositorioDePareceres.adicionaNota(
                parecerId,
                new Nota(
                        new Pontuacao("nome", new Valor("valor")),
                        new Pontuacao("nome", new Valor("novoValor")),
                        "justificativa"
                )
        );
    }

    @Test
    public void adicionaNota() {
        Parecer parecer;

        String parecerId = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(parecerId));

        parecer = repositorioDePareceres.byId(parecerId);
        assertNotNull("lista de notas antes da iserção deve ser diferente de null", parecer.getNotas());
        assertEquals("lista de notas antes da inserção deve ter tamanho 1", 1, parecer.getNotas().size());

        repositorioDePareceres.adicionaNota(
                parecerId,
                getSampleNota()
        );

        parecer = repositorioDePareceres.byId(parecerId);
        assertEquals("id deve coincidir", parecerId, parecer.getId());
        assertNotNull("lista de notas deve ser diferente de null", parecer.getNotas());
        assertEquals("lista de notas deve ter tamanho 2", 2, parecer.getNotas().size());
        assertEquals("justificativa da Nota 0 deve coincidir", "Alteração realizada porque um cálculo tinha sido feito de maneira errada.", parecer.getNotas().get(0).getJustificativa());
        assertEquals("valor original para o atributo ('nomeAtributo') da Nota 0 deve coincidir", false, parecer.getNotas().get(0).getItemOriginal().get("aprovadoProm").getBoolean());
        assertEquals("novo valor para o atributo ('nomeAtributo') da Nota 0 deve coincidir", true, parecer.getNotas().get(0).getItemNovo().get("aprovadoProm").getBoolean());
        assertEquals("justificativa da Nota 1 deve coincidir", "justificativa alteração", parecer.getNotas().get(1).getJustificativa());
        assertEquals("valor original para o atributo ('nomeAtributo') da Nota 1 deve coincidir", "valor", parecer.getNotas().get(1).getItemOriginal().get("nomeAtributo").getString());
        assertEquals("novo valor para o atributo ('nomeAtributo') da Nota 1 deve coincidir", "novoValor", parecer.getNotas().get(1).getItemNovo().get("nomeAtributo").getString());
    }

    @Test
    public void removeNotaEmParecerInexistente() {
        thrown.expect(IdentificadorDesconhecido.class);
        thrown.expectMessage("id desconhecido");

        String parecerId = "id inexistente";
        repositorioDePareceres.removeNota(
                parecerId,
                new Pontuacao("nome", new Valor("valor"))
        );
    }

    @Test
    public void removeNota() {
        Parecer parecer;

        // Cria um parecer com uma nota, adiciona uma segunda nota
        String parecerId = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(parecerId));
        repositorioDePareceres.adicionaNota(parecerId, getSampleNota());

        // Recupera parecer antes da remoção
        parecer = repositorioDePareceres.byId(parecerId);
        assertNotNull("lista de notas antes da remoção deve ser diferente de null", parecer.getNotas());
        assertEquals("lista de notas antes da remoção deve ter tamanho 2", 2, parecer.getNotas().size());

        // Remove
        repositorioDePareceres.removeNota(parecerId, new Pontuacao("aprovadoProm", new Valor(false)));

        // Recupera parecer depois da remoção
        parecer = repositorioDePareceres.byId(parecerId);
        assertNotNull("lista de notas depois da remoção deve ser diferente de null", parecer.getNotas());
        assertEquals("lista de notas depois da remoção deve ter tamanho 1", 1, parecer.getNotas().size());
        assertEquals("a justificativa da Nota[0] depois da remoção deve coincidir", "justificativa alteração", parecer.getNotas().get(0).getJustificativa());
    }

    @Test
    public void atualizaFundamentacaoEmParecerInvalido() {
        thrown.expect(IdentificadorDesconhecido.class);
        thrown.expectMessage("id desconhecido");

        String parecerId = "id inexistente";
        repositorioDePareceres.atualizaFundamentacao(parecerId, "nova fundamentação");
    }

    @Test
    public void atualizaFundamentacao() {
        Parecer parecer;

        // Cria e persiste um parecer
        String parecerId = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(parecerId));

        // Recupera parecer antes da atualização da fundamentação
        parecer = repositorioDePareceres.byId(parecerId);
        assertEquals("fundamentacao antes da atualização deve coincidir", "Fundamentação do parecer", parecer.getFundamentacao());

        // Atualiza fundamentação
        repositorioDePareceres.atualizaFundamentacao(parecerId, "Nova fundamentação");

        // Recupera parecer depois da atualização da fundamentação
        parecer = repositorioDePareceres.byId(parecerId);
        assertEquals("fundamentacao depois da atualização deve coincidir", "Nova fundamentação", parecer.getFundamentacao());
    }

    @Test
    public void removeParecer() {
        Parecer parecer;

        // Cria e persiste um parecer
        String parecerId = UUID.randomUUID().toString();
        repositorioDePareceres.persisteParecer(getParecerValido(parecerId));

        // Recupera parecer por ID antes da remoção
        parecer = repositorioDePareceres.byId(parecerId);
        assertNotNull("parecer recuperado por ID antes da remoção não deve ser null", parecer);

        // Remove parecer
        repositorioDePareceres.removeParecer(parecerId);

        // Recupera parecer por ID depois da remoção
        parecer = repositorioDePareceres.byId(parecerId);
        assertNull("parecer recuperado por ID depois da remoção deve ser null", parecer);

        // Remove parecer que não existe
        repositorioDePareceres.removeParecer("id de um parecer que não existe");
    }
}
