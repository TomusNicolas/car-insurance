package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.web.dto.CarHistoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CarHistoryService {

    private final CarRepository carRepository;
    private final InsuranceClaimRepository claimRepo;

    public CarHistoryService(CarRepository carRepository, InsuranceClaimRepository claimRepo) {
        this.carRepository = carRepository;
        this.claimRepo = claimRepo;
    }

    public List<CarHistoryResponse> getHistory(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        List<InsuranceClaim> claims = claimRepo.findByCarId(car.getId());

        List<CarHistoryResponse> events = new ArrayList<>();
        for (InsuranceClaim claim : claims) {
            events.add(new CarHistoryResponse(claim.getClaimDate(), claim.getDescription(), claim.getAmount()));
        }

        events.sort(Comparator.comparing(CarHistoryResponse::date));
        return events;
    }

}
