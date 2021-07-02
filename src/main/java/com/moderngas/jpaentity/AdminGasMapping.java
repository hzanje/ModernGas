package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "admin_gas_mapping")
public class AdminGasMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "gas_id")
    private Long gasId;

    @Column(name = "gas_name")
    private String gasName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;


}
