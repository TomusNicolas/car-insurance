package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.service.CarService;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.CarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final InsurancePolicyService policyService;

    public CarController(CarService service, InsurancePolicyService policyService) {
        this.service = service;
        this.policyService = policyService;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {

        if (!service.carExists(carId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "car not found");
        }

        LocalDate d;
        try {
            d = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "invalid date format (ISO YYYY-MM-DD)");
        }

        if (d.isBefore(LocalDate.of(2002, 2, 25)) || d.isAfter(LocalDate.of(2520, 02, 02))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "impossible date [2002-02-25, 2520-02-02]");
        }

        boolean valid = service.isInsuranceValid(carId, d);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
