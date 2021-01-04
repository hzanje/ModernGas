package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    @Column(name = "cylinder_type")
    private String cylinderType;

    @Column(name = "is_refill", columnDefinition = "tinyint(1) DEFAULT 1")
    private boolean isRefill;

    @Column(name = "refill_count")
    private int refillCount;

    @Column(name = "userId")
    private Long userId;

    @OneToOne
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private GasMaster gasMaster;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;
}
