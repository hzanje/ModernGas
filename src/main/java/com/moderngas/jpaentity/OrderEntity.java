package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity extends GenericEntity {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "is_refil")
    private boolean isRefil;

    @Column(name = "med_kit_refil_count")
    private int medKitRefilCount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private StatusMaster statusMaster;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private GasMaster gasMaster;

}
