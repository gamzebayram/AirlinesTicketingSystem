package io.upschool.repository;


import io.upschool.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByTicketNumber(@Param("ticketNumber") String ticketNumber);
    Ticket findByTicketNumber(String ticketNumber);
}
