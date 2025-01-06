package LogicFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Class User that represents the user entity on the database.
 * 
 */
@Entity
@Table(name = "users") // Certifica o nome da tabela
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Certifica o mapeamento da chave primária
    private int id;

    private String username;
    private String hash;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
