package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderType;
import com.moderngas.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "is_refill", columnDefinition = "tinyint(1) DEFAULT 0")
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

    @OneToOne
    @JoinColumn(name = "delivery_vehicle", referencedColumnName = "id")
    private DeliveryVehicleEntity deliveryVehicle;

    @Column(name = "loaded_date")
    private Date loadedDate;

    @Column(name = "delivered_date")
    private Date deliveredDate;

    @Column(name = "cancellation_date")
    private Date cancellationDate;

    @Column(name = "price")
    private float price;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity addressEntity;

}
