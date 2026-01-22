package at.ac.fhstp.awp_bad.groupxx.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "GeocacheUser")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String mail;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String pwhash;


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwhash() {
        return pwhash;
    }

    public void setPwhash(String pwhash) {
        this.pwhash = pwhash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
