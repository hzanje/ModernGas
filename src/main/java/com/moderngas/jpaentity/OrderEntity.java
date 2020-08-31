package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends GenericEntity {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "cylinder_type")
    private String cylinderType;

    @Column(name = "is_refill")
    private boolean isRefill;

    @Column(name = "refill_count")
    private int refillCount;

    @Column(name = "med_kit_refil_count")
    private int medKitRefilCount;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private StatusMaster statusMaster;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private GasMaster gasMaster;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "orderEntity")
    private List<OrderStatusEntity> orderStatusEntityList;

}
