package com.syberry.crossdelivery.performrequest.entity;

import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "performer_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @NotNull
    private Order order;
    @ManyToOne
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    @NotNull
    private User performer;
    private Boolean approved;
    private LocalDateTime createdAt = LocalDateTime.now();
}
