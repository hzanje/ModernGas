package com.moderngas.service.serviceImpl;

import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

public class MyGenerator implements IdentifierGenerator {

    public static final String generatorName = "myGenerator";

    @SneakyThrows
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        Random random = SecureRandom.getInstanceStrong();
        int number = random.nextInt(9999999);

        return "REEK_" + String.format("%06d", number);
    }
}
