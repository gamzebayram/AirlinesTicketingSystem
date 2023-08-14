package io.upschool.repository;


import io.upschool.entity.Flight;
import io.upschool.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByTicketNumber(String ticketNumber);
    Ticket findByTicketNumber(String ticketNumber);
}
