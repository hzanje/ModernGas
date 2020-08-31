package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "order_status")
public class OrderStatusEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long id;

    @Column(name = "status")
    public String status;

    @Column(name = "date")
    public String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    public OrderEntity orderEntity;



}
