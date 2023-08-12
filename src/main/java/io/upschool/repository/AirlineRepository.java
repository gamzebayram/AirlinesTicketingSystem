package io.upschool.repository;

import io.upschool.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Airline findByIcaoCodeIs(String icaoCode);
}