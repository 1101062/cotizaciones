package com.bolsadeideas.springboot.app.models.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    /*@Id
    @GenericGenerator(
            name = "SEQ_USER_AUTO_INCR",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "SEQ_USER_AUTO_INCR"),
                    @Parameter(name = "initial_value", value = "1000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(generator = "SEQ_USER_AUTO_INCR")*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, unique = true)
    private String username;

    @Column(length = 60)
    private String password;

    @Column(name = "enabled")
    private int enabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Authority> authorities;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gestor_id")
    private Gestor gestor;

    public User() {
    }

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

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public int getEnabled() {

        return enabled;
    }

    public void setEnabled(int enabled) {

        this.enabled = enabled;
    }

    public List<Authority> getRoles() {

        return authorities;
    }

    public void setRoles(List<Authority> authorities) {

        this.authorities = authorities;
    }

    public Gestor getCliente() {

        return gestor;
    }

    public void setCliente(Gestor gestor) {

        this.gestor = gestor;
    }

    private static final long serialVersionUID = 1L;
}
