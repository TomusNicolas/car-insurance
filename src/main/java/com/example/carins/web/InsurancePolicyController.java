package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.InsurancePolicyDto;
import com.example.carins.web.dto.InsurancePolicyResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InsurancePolicyController {

    private final InsurancePolicyService service;

    public InsurancePolicyController(InsurancePolicyService service) { this.service = service; }

    @GetMapping("/policies")
    public List<InsurancePolicyResponse> getPolicies() { return service.listPolicies().stream().map(this::toDto).toList(); }

    @PostMapping("/policies")
    public InsurancePolicyResponse create(@Valid @RequestBody InsurancePolicyDto req) {
        InsurancePolicy saved = service.create(req);
        return new InsurancePolicyResponse(
                        saved.getId(),
                        saved.getCar().getId(),
                        saved.getProvider(),
                        saved.getStartDate(),
                        saved.getEndDate()
        );
    }

    @PutMapping("/policies/{id}")
    public InsurancePolicyResponse update(@PathVariable Long id, @Valid @RequestBody InsurancePolicyDto req) {
        InsurancePolicy saved = service.update(id, req);
        return new InsurancePolicyResponse(
                        saved.getId(),
                        saved.getCar().getId(),
                        saved.getProvider(),
                        saved.getStartDate(),
                        saved.getEndDate()
        );
    }

    private InsurancePolicyResponse toDto(InsurancePolicy i) {
        return new InsurancePolicyResponse(i.getId(), i.getCar().getId(), i.getProvider(), i.getStartDate(), i.getEndDate());
    }

}
