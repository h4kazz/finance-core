package com.home.finance.user.model;

import com.home.finance.account.model.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "app_users",
        uniqueConstraints = @UniqueConstraint(name = "uk_app_users_email", columnNames = "email")
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Not separate entity, only a collection of values
    @ElementCollection(fetch = FetchType.EAGER)
    // Store roles in separate table and links it with user Id
    @CollectionTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "userId")
    )
    // Store enum as text, not as index
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<UserRole> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    List<Account> accounts;

    // Needed for JPA - when user is loaded from DB, empty object is created and filled with values from DB
    protected User() {};

    public User(String name, String email, String password, Set<UserRole> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = new LinkedHashSet<>(roles);
    }

    public User(String email) {
        this.email = email;
    }
}
