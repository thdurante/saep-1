package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Classe que representa o repositório de Resoluções.
 */
public class RepositorioDeResolucoes implements ResolucaoRepository {

    /**
     * Representação da collection específica advinda do banco de dados.
     */
    private MongoCollection resolucoesCollection;

    /**
     * Objeto para serialização e parse de documentos JSON.
     */
    private Gson gson;

    /**
     * Cria um novo repositório de Resoluções, já abrindo a conexão com o
     * banco de dados e inicializando o serializador/parser Gson.
     */
    public RepositorioDeResolucoes() {
        this.resolucoesCollection = new DBManager("resolucoes").abrirConexao();
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resolucao byId(String id) {
        Document document = null;
        ObjectId objectId = new ObjectId(id);

        MongoCursor cursor = resolucoesCollection.find(eq("_id", objectId)).iterator();
        if (!cursor.hasNext()) {
            return null;
        }

        while (cursor.hasNext()) {
            document = (Document) cursor.next();
            document.put("id", document.get("_id").toString());
            document.remove("_id");
        }

        return gson.fromJson(document.toJson(), Resolucao.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String persiste(Resolucao resolucao) {
        if (resolucao.getNome() == null || resolucao.getNome().isEmpty()) {
            throw new CampoExigidoNaoFornecido("nome");
        }

        if (resolucao.getDescricao() == null || resolucao.getDescricao().isEmpty()) {
            throw new CampoExigidoNaoFornecido("descricao");
        }

        if (resolucao.getDataAprovacao() == null) {
            throw new CampoExigidoNaoFornecido("dataAprovacao");
        }

        if (resolucao.getRegras() == null || resolucao.getRegras().size() < 1) {
            throw new CampoExigidoNaoFornecido("regras");
        }

        Document resolucaoDocument = Document.parse(gson.toJson(resolucao));

        ObjectId id = new ObjectId();
        resolucaoDocument.put("_id", id);
        resolucaoDocument.remove("id");

        resolucoesCollection.insertOne(resolucaoDocument);

        return id.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(String identificador) {
        ObjectId objectId = new ObjectId(identificador);
        DeleteResult deleteResult = resolucoesCollection.deleteOne(eq("_id", objectId));

        if (deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> resolucoes() {
        List<String> identificadoresDasResolucoes = new ArrayList<>();
        Document document;

        MongoCursor cursor = resolucoesCollection.find().iterator();
        while (cursor.hasNext()) {
            document = (Document) cursor.next();
            identificadoresDasResolucoes.add(document.get("_id").toString());
        }

        return identificadoresDasResolucoes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persisteTipo(Tipo tipo) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeTipo(String codigo) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tipo tipoPeloCodigo(String codigo) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tipo> tiposPeloNome(String nome) {
        return null;
    }
}
