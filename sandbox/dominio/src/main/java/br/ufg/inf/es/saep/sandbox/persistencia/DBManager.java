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

    /**
     * Identificador da base de dados.
     */
    private String database;

    /**
     * Retorna uma nova instância de DBManager.
     * @param database O nome da base de dados.
     */
    public DBManager(String database) {
        this.database = database;
    }

    /**
     * Abre uma conexão com o servidor de banco de dados e retorna a Collection desejada.
     * @return A collection identificada pela nome informado no construtor.
     */
    public MongoCollection abrirConexao (String collection) {
        MongoClient mongoClient = new MongoClient(new ServerAddress(servidor, porta));
        MongoDatabase db = mongoClient.getDatabase(database);

        return db.getCollection(collection);
    }
}
