package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    @Column(name = "cylinder_type")
    @Enumerated(EnumType.ORDINAL)
    private CylinderType cylinderType;

    @Column(name = "is_refill", columnDefinition = "tinyint(1) DEFAULT 1")
    private boolean isRefill;

    @Column(name = "refill_count")
    private int refillCount;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "admin_id")
    private Long adminId;

    @OneToOne
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private GasMaster gasMaster;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private float price;
}
