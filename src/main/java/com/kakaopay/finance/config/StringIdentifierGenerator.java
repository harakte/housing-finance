package com.kakaopay.finance.config;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class StringIdentifierGenerator implements IdentifierGenerator, Configurable {

    private String prefix;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = params.getProperty("prefix");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String query = String.format("select %s from %s",
                session.getEntityPersister(object.getClass().getName(), object).getIdentifierPropertyName(),
                object.getClass().getSimpleName());
        Long max = session.createQuery(query).stream()
                .map(o -> ((String)o).replace(prefix + "-",""))
                .mapToLong(num -> Long.parseLong((String)num))
                .max()
                .orElse(0L);
        return prefix + "-" +(max + 1);
    }
}
