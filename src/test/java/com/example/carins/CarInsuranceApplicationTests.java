package com.example.carins;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Autowired
    MockMvc mvc;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }

    @Test
    void endpoint_notFound_ifCarMissing() throws Exception {
        this.mvc.perform(get("/api/cars/999/insurance-valid").param("date", "2025-09-09"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("car not found"));
    }

    @Test
    void endpoint_badRequest_ifDateFormatWrong() throws Exception {
        this.mvc.perform(get("/api/cars/1/insurance-valid").param("date", "09-09-2025"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("invalid date format (ISO YYYY-MM-DD)"));
    }

    @Test
    void endpoint_badRequest_ifDateOutOfRange() throws Exception {
        this.mvc.perform(get("/api/cars/1/insurance-valid").param("date", "1800-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("impossible date [2002-02-25, 2520-02-02]"));
    }

    @Test
    void endpoint_ok_ifAllGood() throws Exception {
        this.mvc.perform(get("/api/cars/1/insurance-valid").param("date", "2025-09-09"))
                .andExpect(status().isOk());
    }

}
