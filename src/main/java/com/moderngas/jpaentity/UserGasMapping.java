package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_gas_mapping")
public class UserGasMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "gas_id")
    private Long gasId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "admin_id")
    private Long adminId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_gas_id", referencedColumnName = "id", nullable = false)
    private Set<UserGasCylinderTypeMapping> userGasCylinderTypeMapping;




}
