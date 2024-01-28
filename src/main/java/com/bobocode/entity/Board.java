package com.bobocode.entity;

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
@EqualsAndHashCode(of = "name")
@Entity
@Table(schema = "db_performance_test", name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_id_seq")
    @SequenceGenerator(schema = "db_performance_test", name = "board_id_seq", sequenceName = "board_id_seq", allocationSize = 1)
    private Long id;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private Set<Ticket> tickets = new HashSet<>();


    @Setter(AccessLevel.PRIVATE)
    @ManyToMany
    @JoinTable(name = "user_board",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public Board(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getBoards().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getBoards().remove(this);
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setBoard(this);
    }

    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setBoard(null);
    }
}
