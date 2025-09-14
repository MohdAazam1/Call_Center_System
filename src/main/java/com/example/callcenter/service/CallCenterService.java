package com.example.callcenter.service;

import com.example.callcenter.model.Agent;
import com.example.callcenter.model.Call;
import com.example.callcenter.repository.AgentRepository;
import com.example.callcenter.repository.CallRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CallCenterService {
    private final CallRepository callRepo;
    private final AgentRepository agentRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private int customerId = 1;
    private final Random rnd = new Random();

    public CallCenterService(CallRepository callRepo, AgentRepository agentRepo,
                             SimpMessagingTemplate messagingTemplate) {
        this.callRepo = callRepo;
        this.agentRepo = agentRepo;
        this.messagingTemplate = messagingTemplate;
        startSimulation();
        startDispatcher();
    }

    public void newCall() {
        Call call = new Call();
        call.setCustomerId(customerId++);
        call.setStatus("WAITING");
        callRepo.save(call);
        messagingTemplate.convertAndSend("/topic/newCall", call);
    }

    private void answerCall(Agent agent) throws InterruptedException {
        List<Call> waiting = callRepo.findByStatus("WAITING");
        if (!waiting.isEmpty()) {
            Call call = waiting.get(0);
            call.setStatus("ACTIVE");
            call.setAssignedAgent(agent);
            callRepo.save(call);

            messagingTemplate.convertAndSend("/topic/answerCall",
                    "Agent " + agent.getUsername() + " answering Customer " + call.getCustomerId());

            Thread.sleep(rnd.nextInt(3000) + 2000);

            call.setStatus("FINISHED");
            callRepo.save(call);

            messagingTemplate.convertAndSend("/topic/finishCall",
                    "Agent " + agent.getUsername() + " finished with Customer " + call.getCustomerId());
        }
    }

    private void startSimulation() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(rnd.nextInt(4000) + 2000);
                    newCall();
                }
            } catch (Exception ignored) {}
        }).start();
    }

    private void startDispatcher() {
        new Thread(() -> {
            try {
                while (true) {
                    List<Agent> agents = agentRepo.findAll();
                    for (Agent a : agents) {
                        if (a.isAvailable()) {
                            answerCall(a);
                        }
                    }
                    Thread.sleep(500);
                }
            } catch (Exception ignored) {}
        }).start();
    }
}
