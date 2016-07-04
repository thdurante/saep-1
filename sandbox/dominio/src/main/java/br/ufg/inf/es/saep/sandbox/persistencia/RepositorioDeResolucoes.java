package br.ufg.inf.es.saep.sandbox.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.CampoExigidoNaoFornecido;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

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

    @Override
    public boolean remove(String identificador) {
        return false;
    }

    @Override
    public List<String> resolucoes() {
        return null;
    }

    @Override
    public void persisteTipo(Tipo tipo) {

    }

    @Override
    public void removeTipo(String codigo) {

    }

    @Override
    public Tipo tipoPeloCodigo(String codigo) {
        return null;
    }

    @Override
    public List<Tipo> tiposPeloNome(String nome) {
        return null;
    }
}
