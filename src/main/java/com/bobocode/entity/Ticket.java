package com.bobocode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.HashSet;
import java.util.Set;

@ToString
@NoArgsConstructor
@Getter
@Entity
@Table(schema = "db_performance_test", name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_seq")
    @SequenceGenerator(schema = "db_performance_test", name = "ticket_id_seq", sequenceName = "ticket_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(nullable = false)
    private TicketStatus status;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id")
    private Board board;

    @JsonIgnore
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "ticket", orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    public Ticket(String title, String description, TicketStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTicket(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setTicket(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return id != null && id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum TicketStatus {
        TODO, IN_PROGRESS, DONE
    }
}
