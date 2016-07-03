package br.ufg.inf.es.saep.sandbox.persistencia;


import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Encapsula a conexão com o servidor de banco de dados.
 * Retorna uma Collection que vai ser trabalhada nos repositórios.
 */
public class DBManager {

    /**
     * Informações do servidor.
     */
    private final String servidor = "server.thiagodurante.com.br";
    private final int porta = 27017;
    private final String database = "saep";

    /**
     * Identificador da Collection a ser recuperada.
     */
    private String collectionName;

    /**
     * Retorna uma nova instância de DBManager.
     * @param collection O nome da Collection a ser recuperada.
     */
    public DBManager(String collection) {
        this.collectionName = collection;
    }

    /**
     * Abre uma conexão com o servidor de banco de dados e retorna a Collection desejada.
     * @return A collection identificada pela nome informado no construtor.
     */
    public MongoCollection abrirConexao () {
        MongoClient mongoClient = new MongoClient(new ServerAddress(servidor, porta));
        MongoDatabase db = mongoClient.getDatabase(database);

        return db.getCollection(collectionName);
    }
}
