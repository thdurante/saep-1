package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;

/**
 * Classe que representa o repositório de Pareceres.
 */
public class RepositorioDePareceres implements ParecerRepository {

    /**
     * Representação da collection Pareceres advinda do banco de dados.
     */
    private MongoCollection pareceresCollection;

    /**
     * Representação da collection Radocs advinda do banco de dados.
     */
    private MongoCollection radocsCollection;

    /**
     * Objeto para serialização e parse de documentos JSON.
     */
    private Gson gson;

    /**
     * Cria um novo repositório de Pareceres e Radocs, já inicializando o
     * serializador/parser Gson. A conexão com o banco de dados será aberta
     * em um momento posterior.
     */
    public RepositorioDePareceres() {
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void adicionaNota(String id, Nota nota) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNota(String id, Avaliavel original) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persisteParecer(Parecer parecer) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void atualizaFundamentacao(String parecer, String fundamentacao) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parecer byId(String id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeParecer(String id) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Radoc radocById(String identificador) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String persisteRadoc(Radoc radoc) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRadoc(String identificador) {

    }

    /**
     * Método chamado no tearDown dos testes para limpar a base de dados.
     */
    public void clearDB() {
        //pareceresCollection.deleteMany(new Document());
        //radocsCollection.deleteMany(new Document());
    }
}
