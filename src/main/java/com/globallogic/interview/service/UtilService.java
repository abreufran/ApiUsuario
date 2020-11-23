package com.globallogic.interview.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class UtilService {
	private static volatile SecureRandom numberGenerator = null;
    private static final long MSB = 0x8000000000000000L;

    public String generarUUID() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }

        return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
    }  
}
