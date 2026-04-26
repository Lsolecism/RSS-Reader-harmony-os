package org.example.mycute.service;


public interface MQProducerService {
    void sendEmailTask(String email, String code);
}
