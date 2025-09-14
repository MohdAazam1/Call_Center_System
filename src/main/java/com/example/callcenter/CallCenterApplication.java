package com.example.callcenter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.callcenter.model.Agent;
import com.example.callcenter.repository.AgentRepository;

@SpringBootApplication
public class CallCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(CallCenterApplication.class, args);
    }

    // create a default agent at startup: username=agent1 password=password
    @Bean
    CommandLineRunner init(AgentRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("agent1").isEmpty()) {
                Agent a = new Agent();
                a.setUsername("agent1");
                a.setPassword(encoder.encode("password"));
                a.setAvailable(true);
                repo.save(a);
                System.out.println("Created default agent -> agent1 / password");
            }
        };
    }
}
