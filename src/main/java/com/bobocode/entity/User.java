package com.bobocode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
@Entity
@Table(schema = "db_performance_test", name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(schema = "db_performance_test", name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "user")
    private Set<Ticket> tickets = new HashSet<>();

    @JsonIgnore
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Comment> comments= new HashSet<>();

    @JsonIgnore
    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(mappedBy = "users")
    private Set<Board> boards = new HashSet<>();

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
