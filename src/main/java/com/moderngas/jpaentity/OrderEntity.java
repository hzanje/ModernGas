package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "is_refill", columnDefinition = "tinyint(1) DEFAULT 0" )
    private boolean isRefill;

    @Column(name = "refill_count")
    private int refillCount;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "status_id")
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @Column(name = "cylinder_type")
    @Enumerated(EnumType.ORDINAL)
    private CylinderType cylinderType;

    @OneToOne
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private GasMaster gasMaster;

    @Column(name = "delivery_vehicle")
    private Long deliveryVehicleNumber;

    @Column(name = "loaded_date")
    private Date loadedDate;

    @Column(name = "delivered_date")
    private Date deliveredDate;

    @Column(name = "cancellation_date")
    private Date cancellationDate;

    @Column(name = "price")
    private int price;

}
