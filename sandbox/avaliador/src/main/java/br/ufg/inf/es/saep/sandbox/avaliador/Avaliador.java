package br.ufg.inf.es.saep.sandbox.avaliador;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementação do serviço de avaliação de regra usando
 * Java Expression Evaluator (https://github.com/uklimaschewski/EvalEx).
 */
public class Avaliador implements AvaliaRegraService {

    /**
     * Depósito de valores produzidos pela avaliação de expressões.
     * A chave de acesso é o identificador do atributo (nome)
     * associado à expressão avaliada.
     */
    private Map<String,BigDecimal> contexto = new HashMap<String, BigDecimal>();

    /**
     * Serviço de acesso às informações de configuração do SAEP.
     * Possivelmente deverá ser "injetado".
     */
    private Regras regras = new Regras();

    public Avaliador(Regras regras) {
        this.regras = regras;
    }

    public Pontuacao avalia(List<Relato> relatos, ItemAvaliado item) {
        // Cardinalidade do conjunto de relatos
        int total = relatos.size();

        // Expressão a ser avaliada
        String expressao = item.getRegra().getExpressao().getExpressao();
        Expression exp = new Expression(expressao);

        // Assume expressão constante, sem necessidade de definir contexto
        double resultado = exp.eval().doubleValue();

        return new Pontuacao(item, resultado);
    }

    /**
     * Avalia o conjunto de registros conforme a sentença
     * cujo código é fornecido.
     *
     * @param registros Conjunto de registros a ser avaliado.
     *
     * @param codigo Código único da sentença a ser utilizada
     *               na avaliação dos registros.
     *
     * @return Pontuação obtida pela avaliação.
     */
    public float avalia(List<Registro> registros, int codigo) {

        // Recupera variável associado ao resultado
        // (identificador do depósito do resultado)
        String variavel = regras.getVariavel(codigo);

        switch (regras.getTipo(codigo)) {
            case Regras.PONTOS_POR_RELATO:
                float ppr = regras.getPontosPorRelato(codigo);
                float pontos = ppr * registros.size();

                // Atualiza contexto
                contexto.put(variavel, new BigDecimal(pontos));
                return pontos;

            case Regras.EXPRESSAO:
                BigDecimal valor = avaliaExpressao(codigo);

                // Deposita resultado produzido no contexto
                contexto.put(variavel, valor);

                return valor.floatValue();

            case Regras.CONDICIONAL:
                BigDecimal condicao = avaliaExpressao(codigo);
                int cexpr = condicao == BigDecimal.ZERO
                        ? regras.getCodigoSentencaSenao(codigo)
                        : regras.getCodigoSentencaEntao(codigo);

                // Avalia "então" ou "senão"
                BigDecimal resultado = avaliaExpressao(cexpr);

                // Deposita resultado produzido no contexto
                contexto.put(variavel, resultado);

                return resultado.floatValue();
        }

        return 0.1f;
    }

    /**
     * Avalia expressão.
     *
     * @param codigo O identificador único da expressão.
     *
     * @return O valor da expressão.
     */
    private BigDecimal avaliaExpressao(int codigo) {
        // Recupera a expressão
        String sentenca = regras.getSentenca(codigo);
        Expression exp = new Expression(sentenca);

        // Variáveis utilizadas na avaliação da expressão
        List<String> utilizadas = regras.getDependencias(codigo);

        // Recuperar o contexto
        for (String dependeDe : utilizadas) {
            BigDecimal valor = contexto.get(dependeDe);
            exp.setVariable(dependeDe, valor);
        }

        // Avalia
        BigDecimal resultadoSentenca = exp.eval();
        return resultadoSentenca;
    }
}