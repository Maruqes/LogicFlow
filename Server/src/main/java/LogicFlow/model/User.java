package LogicFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
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
    private Long id;

    // Keep API field names used by controllers/repository, but map to the real DB columns.
    @Column(name = "email", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String hash;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
