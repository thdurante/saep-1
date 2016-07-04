package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import org.junit.Before;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class RepositorioDeResolucoesTest {

    RepositorioDeResolucoes repositorioDeResolucoes;
    PodamFactory factory;

    /*
    * PRIVATE METHODS
    * */

    /**
     * Recupera a lista de regras da Resolução CONSUNI No 32/2013.
     * @return A lista de regras da Resolução.
     */
    private List<Regra> getListaDeRegras() {
        // REGRA 1
        List<String> dependenciasRegra1 = new ArrayList<>();
        dependenciasRegra1.add("cha = carga horária anual");

        Regra regra1 = new Regra(
            Regra.EXPRESSAO,                                // tipo
            "Aulas presenciais na graduação",               // descricao
            0,                                              // valorMaximo
            0,                                              // valorMinimo
            "pontosAPGrad",                                 // variavel
            "10 * (cha / 32)",                              // expressao
            null,                                           // entao
            null,                                           // senao
            null,                                           // tipoRelato
            0,                                              // pontosPorItem
            dependenciasRegra1                              // dependeDe
        );

        // REGRA 2
        List<String> dependenciasRegra2 = new ArrayList<>();
        dependenciasRegra2.add("cha = carga horária anual");

        Regra regra2 = new Regra(
                Regra.EXPRESSAO,                                // tipo
                "Aulas de EAD na graduação",                    // descricao
                0,                                              // valorMaximo
                0,                                              // valorMinimo
                "pontosAEADGrad",                               // variavel
                "10 * (cha / 32)",                              // expressao
                null,                                           // entao
                null,                                           // senao
                null,                                           // tipoRelato
                0,                                              // pontosPorItem
                dependenciasRegra2                              // dependeDe
        );

        List<Regra> regras = new ArrayList<>();
        regras.add(regra1);
        regras.add(regra2);

        return regras;
    }

    /**
     * Recupera a data de aprovação da Resolução CONSUNI No 32/2013.
     * @return A data de aprovação da Resolução.
     */
    private Date getDataAprovacao() {
        Calendar c = Calendar.getInstance();
        c.set(2013, Calendar.SEPTEMBER, 27);
        return c.getTime();
    }

    /**
     * Recupera uma Resolução válida, ou seja, que satisfaz a todas as condições
     * determinadas por seu construtor.
     * @return Resolução válida.
     */
    private Resolucao getResolucaoValida() {
        return new Resolucao(
            "CONSUNI No 32/2013",
            "Dispõe sobre normas para avaliação de pessoal docente em relação ao " +
            "estágio probatório, à progressão funcional e à promoção na Carreira " +
            "do Magistério Superior, e revoga as disposições em contrário.",
            getDataAprovacao(),
            getListaDeRegras()
        );
    }

    /*
    * TESTS
    * */

    @Before
    public void setUp() {
        this.repositorioDeResolucoes = new RepositorioDeResolucoes();
        this.factory = new PodamFactoryImpl();
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteResolucaoSemNenhumAtributoObrigatorio() {
        Resolucao resolucao = new Resolucao(null, null, null, null);
        repositorioDeResolucoes.persiste(resolucao);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteResolucaoSemId() {
        Resolucao resolucao = new Resolucao(
            null,
            "Dispõe sobre normas para avaliação de pessoal docente em relação ao " +
            "estágio probatório, à progressão funcional e à promoção na Carreira " +
            "do Magistério Superior, e revoga as disposições em contrário.",
            getDataAprovacao(),
            getListaDeRegras()
        );
        repositorioDeResolucoes.persiste(resolucao);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteResolucaoSemDescricao() {
        Resolucao resolucao = new Resolucao(
            "CONSUNI No 32/2013",
            null,
            getDataAprovacao(),
            getListaDeRegras()
        );
        repositorioDeResolucoes.persiste(resolucao);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteResolucaoSemDataAprovacao() {
        Resolucao resolucao = new Resolucao(
            "CONSUNI No 32/2013",
            "Dispõe sobre normas para avaliação de pessoal docente em relação ao " +
            "estágio probatório, à progressão funcional e à promoção na Carreira " +
            "do Magistério Superior, e revoga as disposições em contrário.",
            null,
            getListaDeRegras()
        );
        repositorioDeResolucoes.persiste(resolucao);
    }

    @Test(expected = CampoExigidoNaoFornecido.class)
    public void persisteResolucaoSemRegras() {
        Resolucao resolucao = new Resolucao(
            "CONSUNI No 32/2013",
            "Dispõe sobre normas para avaliação de pessoal docente em relação ao " +
            "estágio probatório, à progressão funcional e à promoção na Carreira " +
            "do Magistério Superior, e revoga as disposições em contrário.",
            getDataAprovacao(),
            null
        );
        repositorioDeResolucoes.persiste(resolucao);
    }

    @Test
    public void persisteResolucaoValida() {
        Resolucao resolucao = getResolucaoValida();
        String identificador = repositorioDeResolucoes.persiste(resolucao);
        assertNotNull("id retornado não deve ser null", identificador);
    }

    @Test
    public void recuperaPorId() {
        String id = "57799feae0e499143ca77b55";
        Resolucao resolucao = repositorioDeResolucoes.byId(id);

        assertNotNull("resolução retornada não deve ser null", resolucao);
        assertNotNull("dataAprovação não deve ser null", resolucao.getDataAprovacao());

        assertEquals("id deve ser igual", "57799feae0e499143ca77b55", resolucao.getId());
        assertEquals("nome deve ser igual", "CONSUNI No 32/2013", resolucao.getNome());
        assertEquals("deve possuir regras", 2, resolucao.getRegras().size());
    }

    @Test
    public void recuperaPorIdInexistente() {
        String id = "47799feae0e499143ca77b54";
        Resolucao resolucao = repositorioDeResolucoes.byId(id);

        assertNull("resolução não encontrada, retorno deve ser null", resolucao);
    }

    @Test
    public void listaIdsDeResolucoes() {
        List<String> identificadoresDasResolucoes = repositorioDeResolucoes.resolucoes();

        System.out.println("Total de Resoluções: " +identificadoresDasResolucoes.size());
        for (String s : identificadoresDasResolucoes) {
            System.out.println(s);
        }

        assertNotNull("a lista de ids não deve ser null", identificadoresDasResolucoes);
        assertNotEquals("a lista de ids não deve ser vazia", 0, identificadoresDasResolucoes.size());
    }

    @Test
    public void removeResolucaoValida() {
        String id = repositorioDeResolucoes.persiste(getResolucaoValida());
        Boolean resultadoRemocao = repositorioDeResolucoes.remove(id);
        assertEquals("deve ser true se a resolucao foi deletada", true, resultadoRemocao);
    }

    @Test
    public void removeResolucaoInexistente() {
        String id = "3779de2fe0e49916ef449f23";
        Boolean resultadoRemocao = repositorioDeResolucoes.remove(id);
        assertEquals("deve ser false se a resolucao não foi deletada", false, resultadoRemocao);
    }
}
