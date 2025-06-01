package com.fxexchange.fx.repository;

import com.fxexchange.fx.dao.CurrencyConversion;
import com.fxexchange.fx.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CurrencyConversionRepositoryImpl implements CurrencyConversionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CurrencyConversion> findByCriteria(String transactionId, LocalDate transactionDate, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Query for data
        CriteriaQuery<CurrencyConversion> cq = cb.createQuery(CurrencyConversion.class);
        Root<CurrencyConversion> root = cq.from(CurrencyConversion.class);

        Predicate predicate = cb.conjunction();

        if (transactionId != null && !transactionId.isEmpty()) {
            predicate = cb.and(predicate, cb.equal(root.get("transactionId"), transactionId));
        }

        if (transactionDate != null) {
            LocalDateTime startOfDay = transactionDate.atStartOfDay();
            LocalDateTime endOfDay = transactionDate.atTime(LocalTime.MAX);
            predicate = cb.and(predicate,
                    cb.between(root.get("transactionDate"), startOfDay, endOfDay));
        }

        cq.where(predicate);
        cq.orderBy(cb.desc(root.get("transactionDate")));

        List<CurrencyConversion> results = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No conversion found for the given criteria.");
        }

        // Query for total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CurrencyConversion> countRoot = countQuery.from(CurrencyConversion.class);
        countQuery.select(cb.count(countRoot));

        Predicate countPredicate = cb.conjunction();

        if (transactionId != null && !transactionId.isEmpty()) {
            countPredicate = cb.and(countPredicate, cb.equal(countRoot.get("transactionId"), transactionId));
        }

        if (transactionDate != null) {
            LocalDateTime startOfDay = transactionDate.atStartOfDay();
            LocalDateTime endOfDay = transactionDate.atTime(LocalTime.MAX);
            countPredicate = cb.and(countPredicate,
                    cb.between(countRoot.get("transactionDate"), startOfDay, endOfDay));
        }

        countQuery.where(countPredicate);

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }
}
