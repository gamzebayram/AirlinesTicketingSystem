package io.upschool.controller;

import io.upschool.dto.BaseResponse;
import io.upschool.dto.airline.AirlineSaveRequest;
import io.upschool.dto.airline.AirlineSaveResponse;
import io.upschool.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

/*
    @PostMapping
    public ResponseEntity<Object> createAirline(@RequestBody AirlineSaveRequest request) {
        var airlineSaveResponse = airlineService.createAirline(request);
        var response =  BaseResponse.<AirlineSaveResponse>builder()
                .status(HttpStatus.CREATED.value())
                .isSuccess(true)
                .data(airlineSaveResponse)
                .build();
        return ResponseEntity.ok(response);
    }





 */

}
