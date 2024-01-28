package com.bobocode.repository;

import com.bobocode.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select t from Ticket t where t.user.id = :userId")
    List<Ticket> findTicketsByUserId(Long userId);
}
