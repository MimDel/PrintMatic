package com.example.printmatic.repository;

import com.example.printmatic.dto.response.UserOrderDTO;
import com.example.printmatic.enums.OrderStatus;
import com.example.printmatic.enums.SortBy;
import com.example.printmatic.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o where (o.status in :orderStatus)" +
            "ORDER BY CASE " +
            "WHEN :sortBy = 'DEADLINE' THEN o.deadline " +
            "WHEN :sortBy = 'CREATED' THEN o.createdAt " +
            "END ASC")
    Page<OrderEntity> findAllByStatusesInSorted(
            @Param("orderStatus") List<OrderStatus> statuses,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );


    Page<OrderEntity> findAllByOwnerIdOrderByCreatedAtDesc(
            Long ownerId,
            Pageable pageable
    );

    Optional<OrderEntity> findById(Long id);
}
