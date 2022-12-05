package com.school.project.schooldbproject.order.repository;

import com.school.project.schooldbproject.order.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Optional;

public class PaymentJpaRepositoryImpl implements PaymentRepository {
    private final EntityManager em;

    @Autowired
    public PaymentJpaRepositoryImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public Payment save(Payment catalogue) {
        return null;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.empty();
    }
}
