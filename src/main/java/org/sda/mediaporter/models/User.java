package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "names")
    private String name;

    @Column(name = "usernames", nullable = false, unique = true)
    private String username;

    @Column(name = "emails", nullable = false, unique = true)
    private String email;

    @Column(name = "passwords", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_ids", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_ids", referencedColumnName = "id")
    )
    private Set<Role> roles;
}