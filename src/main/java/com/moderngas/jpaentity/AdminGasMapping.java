package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

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

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Float price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "admin_gas_id", referencedColumnName = "id", nullable = false)
    private Set<AdminGasCylinderTypeMapping> adminGasCylinderTypeMapping;

}
