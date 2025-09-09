package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class InsuranceClaimService {

    private final CarRepository carRepository;
    private final InsuranceClaimRepository claimRepository;

    public InsuranceClaimService(CarRepository carRepository, InsuranceClaimRepository claimRepository) {
        this.carRepository = carRepository;
        this.claimRepository = claimRepository;
    }

    public InsuranceClaim create(Long carId, InsuranceClaim claim) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        claim.setCar(car);
        return claimRepository.save(claim);
    }
}
