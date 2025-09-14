package com.example.callcenter.service;

import com.example.callcenter.model.Agent;
import com.example.callcenter.repository.AgentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AgentDetailsService implements UserDetailsService {

    private final AgentRepository repo;

    public AgentDetailsService(AgentRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agent agent = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(agent.getUsername())
                .password(agent.getPassword())
                .roles("AGENT")
                .build();
    }
}
