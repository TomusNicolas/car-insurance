package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InsurancePolicyService {

    private static final Logger log = LoggerFactory.getLogger(InsurancePolicyService.class);
    private final InsurancePolicyRepository policyRepository;
    private final CarRepository carRepository;

    public InsurancePolicyService(InsurancePolicyRepository policyRepo, CarRepository carRepo) {
        this.policyRepository = policyRepo;
        this.carRepository = carRepo;
    }

    public InsurancePolicy create(InsurancePolicyDto req) {
        Car car = carRepository.findById(req.getCarId()).orElseThrow();

        InsurancePolicy p = new InsurancePolicy();
        p.setCar(car);
        p.setProvider(req.getProvider());
        p.setStartDate(req.getStartDate());
        p.setEndDate(req.getEndDate());

        return policyRepository.save(p);
    }

    public InsurancePolicy update(Long id, InsurancePolicyDto req) {
        InsurancePolicy p = policyRepository.findById(id).orElseThrow();
        Car car = carRepository.findById(req.getCarId()).orElseThrow();

        p.setCar(car);
        p.setProvider(req.getProvider());
        p.setStartDate(req.getStartDate());
        p.setEndDate(req.getEndDate());

        return policyRepository.save(p);
    }

    public List<InsurancePolicy> listPolicies() {
        return policyRepository.findAll();
    }

    public boolean isInsurancePolicyValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        return policyRepository.existsActiveOnDate(carId, date);
    }

    @Scheduled(cron = "0 * * * * *")
    public void logRecentlyExpiredPolicies() {
        LocalDate today = LocalDate.now();
        List<InsurancePolicy> expiredToday = policyRepository.findByEndDate(today);

        for (InsurancePolicy p : expiredToday) {
            log.info("Policy {} for car {} expired on {}", p.getId(), p.getCar().getId(), p.getEndDate());
            p.setLog(true);
       }
        policyRepository.saveAll(expiredToday);
    }
}
