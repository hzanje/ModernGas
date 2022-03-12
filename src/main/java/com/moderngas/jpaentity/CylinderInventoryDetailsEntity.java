package com.moderngas.jpaentity;

import com.moderngas.enums.InventoryStatus;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity
@Where(clause = "active_flag = 1")
@Table(name = "cylinder_inventory_details")
public class CylinderInventoryDetailsEntity extends BaseEntity {

    @Column(name = "is_transit")
    private boolean isTransit;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_vehicle_id", referencedColumnName = "id")
    private DeliveryVehicleEntity deliveryVehicleEntity;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_centre_id", referencedColumnName = "id")
    private ResourceCentreEntity resourceCentreEntity;

    @Column(name = "inventory_status_id")
    @Enumerated(EnumType.ORDINAL)
    private InventoryStatus inventoryStatus;

}
