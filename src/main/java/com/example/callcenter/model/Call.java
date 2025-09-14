package com.example.callcenter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int customerId;
    private String status; // WAITING, ACTIVE, FINISHED

    @ManyToOne
    private Agent assignedAgent;
}
