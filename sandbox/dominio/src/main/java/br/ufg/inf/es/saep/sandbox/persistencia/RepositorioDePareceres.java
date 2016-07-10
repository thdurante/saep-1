package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

/**
 * Classe que representa o repositório de Pareceres.
 */
public class RepositorioDePareceres implements ParecerRepository {

    /**
     * Representação da base de dados.
     */
    private DBManager database;

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
        this.database = new DBManager("saep-sandbox");
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
        MongoCollection pareceresCollection = database.abrirConexao("pareceres");

        if (byId(parecer.getId()) != null) {
            throw new IdentificadorExistente("já persistido");
        }

        Document parecerDocument = Document.parse(gson.toJson(parecer));
        parecerDocument.put("_id", parecerDocument.get("id"));
        parecerDocument.remove("id");

        pareceresCollection.insertOne(parecerDocument);
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
        MongoCollection pareceresCollection = database.abrirConexao("pareceres");
        Document document = null;

        MongoCursor cursor = pareceresCollection.find(eq("_id", id)).iterator();
        if (!cursor.hasNext()) {
            return null;
        }

        while (cursor.hasNext()) {
            document = (Document) cursor.next();
            document.put("id", document.get("_id").toString());
            document.remove("_id");
        }

        return gson.fromJson(document.toJson(), Parecer.class);
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
