package io.upschool.service;

import io.upschool.dto.flight.FlightSaveResponse;
import io.upschool.dto.route.RouteSaveRequest;
import io.upschool.entity.Route;
import io.upschool.enums.CabinClassType;
import io.upschool.exception.*;
import io.upschool.utils.CreditCardUtil;
import io.upschool.dto.ticket.TicketSaveRequest;
import io.upschool.dto.ticket.TicketSaveResponse;
import io.upschool.entity.Flight;
import io.upschool.entity.Ticket;
import io.upschool.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;


@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FlightService flightService;


    @Transactional
    public TicketSaveResponse createTicket(TicketSaveRequest ticketDTO) {

        checkIsPassengerAlreadyBuy(ticketDTO);

        Ticket ticketResponse = buildTicketAndSaveAndUpdateSeatCount(ticketDTO);
        return TicketSaveResponse.builder()
                .id(ticketResponse.getId())
                .ticketNumber(ticketResponse.getTicketNumber())
                .isDelete(ticketResponse.isDelete())
                .passengerName(ticketResponse.getPassengerName())
                .cardNumber(ticketResponse.getCardNumber())
                .ticketPrice(ticketResponse.getTicketPrice())
                .flightNumber(ticketResponse.getFlight().getFlightNumber())
                .build();
    }

    @Transactional(readOnly = true)
    public TicketSaveResponse getTicketByTicketNumber(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found.");
        }
        return TicketSaveResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .isDelete(ticket.isDelete())
                .passengerName(ticket.getPassengerName())
                .cardNumber(ticket.getCardNumber())
                .ticketPrice(ticket.getTicketPrice())
                .flightNumber(ticket.getFlight().getFlightNumber())
                .build();

    }


    @Transactional
    public TicketSaveResponse delete(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket not found.");
        }
        ticket.setDelete(true);
        ticketRepository.save(ticket);
        return TicketSaveResponse.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .isDelete(ticket.isDelete())
                .passengerName(ticket.getPassengerName())
                .cardNumber(ticket.getCardNumber())
                .ticketPrice(ticket.getTicketPrice())
                .flightNumber(ticket.getFlight().getFlightNumber())
                .build();

    }


    private Ticket buildTicketAndSaveAndUpdateSeatCount(TicketSaveRequest ticketDTO) {
        Flight flightByFlightNumber = flightService.findFlightByFlightNumber(ticketDTO.getFlightNumber());
        if (flightByFlightNumber == null) {
            throw new FlightNotFoundException("Flight not found.");
        }
        if (flightByFlightNumber.getSeatCapacity() > 0) {
            flightByFlightNumber.setSeatCapacity(flightByFlightNumber.getSeatCapacity() - 1);
            flightService.save(flightByFlightNumber);
        } else {
            throw new SeatNotAvailableException("No available seats for this flight.");
        }


        String maskedCardNumber = CreditCardUtil.maskCardNumber(ticketDTO.getCardNumber());
        String ticketNumber = generateUniqueTicketNumber();
        double ticketPrice = calculateTicketPrice(flightByFlightNumber, ticketDTO.getTicketClassType());

        Ticket newTicket = Ticket.builder()
                .ticketNumber(ticketNumber)
                .passengerName(ticketDTO.getPassengerName())
                .cardNumber(maskedCardNumber)
                .ticketPrice(ticketPrice)
                .flight(flightByFlightNumber)
                .build();
        return ticketRepository.save(newTicket);
    }


    private String generateUniqueTicketNumber() {
        Random random = new Random();
        String ticketNumber;
        do {
            int randomNumber = random.nextInt(1000);
            ticketNumber = "PNR-" + randomNumber;
        } while (ticketRepository.existsByTicketNumber(ticketNumber));

        return ticketNumber;
    }


    private double calculateTicketPrice(Flight flight, String ticketClassType) {
        double basePrice = flight.getBasePrice();
        double priceMultiplier = 1.0;

        if (ticketClassType.equalsIgnoreCase(CabinClassType.FIRST.name())) {
            priceMultiplier *= 3.5;
        }
        if (ticketClassType.equalsIgnoreCase(CabinClassType.BUSINESS.name())) {
            priceMultiplier *= 2.0;
        }
        return basePrice * priceMultiplier;

    }


    private void checkIsPassengerAlreadyBuy(TicketSaveRequest ticketDTO) {
        Long findId = ticketRepository.findFlightIdByFlightNumber(ticketDTO.getFlightNumber());
        if (findId == null) {
            throw new FlightNotFoundException("Flight not found.");
        }
        Ticket alreadyBuyTicket = ticketRepository.findTicketByFlight_IdAndPassengerName(findId, ticketDTO.getPassengerName());
        if (alreadyBuyTicket != null ) {
            throw new TicketAlreadySavedException("Passenger already buy ticket for this flight.");
        }
    }


}





