package com.inghubs.digiwall.service;

import com.inghubs.digiwall.config.JwtTokenProvider;
import com.inghubs.digiwall.model.dto.LoginRequest;
import com.inghubs.digiwall.model.entity.Customer;
import com.inghubs.digiwall.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public Map<String, Object> login(LoginRequest request) {
        Optional<Customer> customerOpt = customerRepository.findByUsername(request.getUsername());
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Kullanıcı bulunamadı");
        }
        Customer customer = customerOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Şifre hatalı");
        }
        String token = jwtTokenProvider.createToken(customer);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", customer.getRole());
        response.put("customerId", customer.getId());
        return response;
    }
}
