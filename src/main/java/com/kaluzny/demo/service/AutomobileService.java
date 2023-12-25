package com.kaluzny.demo.service;

import com.kaluzny.demo.domain.Automobile;
import jakarta.jms.JMSException;

import java.util.List;

public interface AutomobileService {
        Automobile pushMessageAndSaveAutomobile(Automobile automobile) throws JMSException;

        List<Automobile> getRedAutomobiles() throws JMSException;
}
