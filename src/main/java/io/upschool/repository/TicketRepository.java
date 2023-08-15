package io.upschool.repository;


import io.upschool.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByTicketNumber(@Param("ticketNumber") String ticketNumber);

    Ticket findByTicketNumber(String ticketNumber);

   // @Query(value = "select t.flight.id from Ticket t where t.flight.flightNumber = :flightNumber")
    //Long findFlightIdByFlightNumber(@Param("flightNumber") String flightNumber);

    Ticket findTicketByFlight_IdAndPassengerName(Long flightId, String passengerName);

    List<Ticket> getAllByFlight_FlightNumber(@Param("flightNumber") String flightNumber);


}
