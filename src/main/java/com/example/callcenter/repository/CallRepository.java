package com.example.callcenter.repository;

import com.example.callcenter.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {
    List<Call> findByStatus(String status);
}
