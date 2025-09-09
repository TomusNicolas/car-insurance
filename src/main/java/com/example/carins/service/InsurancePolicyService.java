package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;
    public InsurancePolicyService(InsurancePolicyRepository policyRepo) {
        this.policyRepository = policyRepo;
    }

    public List<InsurancePolicy> listPolicies() {
        return policyRepository.findAll();
    }
}
