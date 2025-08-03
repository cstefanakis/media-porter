package org.sda.mediaporter.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_ids", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_ids", referencedColumnName = "id")
    )
    private Set<Role> roles;
}