package com.inghubs.digiwall;

import com.inghubs.digiwall.model.entity.Customer;
import com.inghubs.digiwall.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DigiwallApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigiwallApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedCustomers(CustomerRepository customerRepository,
			BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			if (customerRepository.count() == 0) {
				Customer employee = new Customer();
				employee.setName("Ali");
				employee.setSurname("Yılmaz");
				employee.setTckn("11111111111");
				employee.setRole("EMPLOYEE");
				employee.setUsername("employee1");
				employee.setPassword(passwordEncoder.encode("password"));
				customerRepository.save(employee);

				Customer customer1 = new Customer();
				customer1.setName("Ayşe");
				customer1.setSurname("Demir");
				customer1.setTckn("22222222222");
				customer1.setRole("CUSTOMER");
				customer1.setUsername("customer1");
				customer1.setPassword(passwordEncoder.encode("password"));
				customerRepository.save(customer1);

				Customer customer2 = new Customer();
				customer2.setName("Mehmet");
				customer2.setSurname("Kaya");
				customer2.setTckn("33333333333");
				customer2.setRole("CUSTOMER");
				customer2.setUsername("customer2");
				customer2.setPassword(passwordEncoder.encode("password"));
				customerRepository.save(customer2);
			}
		};
	}
}
