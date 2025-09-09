package com.example.carins.web;

import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.service.InsuranceClaimService;
import com.example.carins.web.dto.InsuranceClaimDto;
import com.example.carins.web.dto.InsuranceClaimResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InsuranceClaimController {

    private final InsuranceClaimRepository claimRepository;
    private final InsuranceClaimService claimService;

    public InsuranceClaimController(InsuranceClaimRepository claimRepository, InsuranceClaimService claimService) {
        this.claimRepository = claimRepository;
        this.claimService = claimService;
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<InsuranceClaimResponse> createClaim( @PathVariable Long carId, @RequestBody InsuranceClaimDto req) {

        InsuranceClaim claim = new InsuranceClaim(null, req.getClaimDate(), req.getDescription(), req.getAmount());

        InsuranceClaim saved = claimService.create(carId, claim);

        InsuranceClaimResponse response = new InsuranceClaimResponse(
                saved.getId(),
                saved.getCar().getId(),
                saved.getClaimDate(),
                saved.getDescription(),
                saved.getAmount());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
