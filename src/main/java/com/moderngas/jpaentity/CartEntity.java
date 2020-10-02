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

@Data
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class CartEntity extends GenericEntity {

    @Column(name = "cylinder_type")
    private String cylinderType;

    @Column(name = "is_refill")
    private boolean isRefill;

    @Column(name = "userId")
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private GasMaster gasMaster;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;
}
