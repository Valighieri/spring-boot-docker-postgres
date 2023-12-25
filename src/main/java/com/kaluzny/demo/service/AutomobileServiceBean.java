package com.kaluzny.demo.service;

import com.kaluzny.demo.domain.Automobile;
import com.kaluzny.demo.domain.AutomobileRepository;
import jakarta.jms.JMSException;
import jakarta.jms.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class AutomobileServiceBean implements AutomobileService {

    private final AutomobileRepository repository;
    private final JmsTemplate jmsTemplate;

    Topic createTopic() throws JMSException {
        return Objects.requireNonNull(jmsTemplate.getConnectionFactory())
                .createConnection()
                .createSession()
                .createTopic("AutoTopic");

    }

    @Override
    public Automobile pushMessageAndSaveAutomobile(Automobile automobile) throws JMSException {
        Topic autoTopic = createTopic();
        Automobile savedAutomobile = repository.save(automobile);
        log.info("\u001B[32m" + "Sending Automobile with id: " + savedAutomobile.getId() + "\u001B[0m");
        jmsTemplate.convertAndSend(autoTopic, savedAutomobile);
        return savedAutomobile;
    }

    @Override
    public List<Automobile> getRedAutomobiles() throws JMSException {
        Topic autoTopic = createTopic();
        List<Automobile> automobileList = repository.findByColor("Red");
        automobileList.forEach(automobile -> {
            log.info("\u001B[32m" + "Sending Automobile with id: " + automobile.getId() + "\u001B[0m");
            jmsTemplate.convertAndSend(autoTopic, automobile);
        });
        return automobileList;
    }

}