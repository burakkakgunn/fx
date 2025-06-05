package com.fxexchange.fx.repository;

import com.fxexchange.fx.dao.CurrencyConversion;
import com.fxexchange.fx.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyConversionRepositoryImplTest {

    private CurrencyConversionRepositoryImpl repository;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        repository = new CurrencyConversionRepositoryImpl();

        Field field = CurrencyConversionRepositoryImpl.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void testFindByCriteria_returnsPage() {
        // Arrange
        String transactionId = "abc123";
        LocalDate transactionDate = LocalDate.of(2024, 1, 1);
        PageRequest pageable = PageRequest.of(0, 10);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<CurrencyConversion> cq = mock(CriteriaQuery.class);
        Root<CurrencyConversion> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);
        TypedQuery<CurrencyConversion> typedQuery = mock(TypedQuery.class);
        TypedQuery<Long> countQuery = mock(TypedQuery.class);

        CurrencyConversion mockConversion = new CurrencyConversion();
        mockConversion.setTransactionId(transactionId);
        mockConversion.setTransactionDate(LocalDateTime.now());
        mockConversion.setAmount(BigDecimal.TEN);

        // Mocks for data query
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(CurrencyConversion.class)).thenReturn(cq);
        when(cq.from(CurrencyConversion.class)).thenReturn(root);
        when(cb.conjunction()).thenReturn(predicate);
        when(cb.equal(any(), any())).thenReturn(predicate);
        when(cb.and(any(), any())).thenReturn(predicate);
        when(cq.where(predicate)).thenReturn(cq);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(mockConversion));

        // Mocks for count query
        CriteriaQuery<Long> cqCount = mock(CriteriaQuery.class);
        Root<CurrencyConversion> countRoot = mock(Root.class);
        when(cb.createQuery(Long.class)).thenReturn(cqCount);
        when(cqCount.from(CurrencyConversion.class)).thenReturn(countRoot);
        when(cqCount.select(any())).thenReturn(cqCount);
        when(cqCount.where((Expression<Boolean>) any())).thenReturn(cqCount);
        when(entityManager.createQuery(cqCount)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act
        Page<CurrencyConversion> result = repository.findByCriteria(transactionId, transactionDate, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(transactionId, result.getContent().get(0).getTransactionId());
    }

    @Test
    void testFindByCriteria_emptyResult_throwsException() {
        // Arrange
        String transactionId = "abc123";

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<CurrencyConversion> cq = mock(CriteriaQuery.class);
        Root<CurrencyConversion> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);
        TypedQuery<CurrencyConversion> typedQuery = mock(TypedQuery.class);
        TypedQuery<Long> countQuery = mock(TypedQuery.class);


        // Mocks for data query
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(CurrencyConversion.class)).thenReturn(cq);
        when(cq.from(CurrencyConversion.class)).thenReturn(root);
        when(cb.conjunction()).thenReturn(predicate);
        when(cb.equal(any(), any())).thenReturn(predicate);
        when(cb.and(any(), any())).thenReturn(predicate);
        when(cq.where(predicate)).thenReturn(cq);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of());

        // Mocks for count query
        CriteriaQuery<Long> cqCount = mock(CriteriaQuery.class);
        Root<CurrencyConversion> countRoot = mock(Root.class);
        when(cb.createQuery(Long.class)).thenReturn(cqCount);
        when(cqCount.from(CurrencyConversion.class)).thenReturn(countRoot);
        when(cqCount.select(any())).thenReturn(cqCount);
        when(cqCount.where((Expression<Boolean>) any())).thenReturn(cqCount);
        when(entityManager.createQuery(cqCount)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                repository.findByCriteria("abc", LocalDate.now(), PageRequest.of(0, 5)));
    }
}
