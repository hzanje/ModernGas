package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cylinder")
public class CylinderEntity extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "cylinder_status_id")
    @Enumerated(EnumType.ORDINAL)
    private CylinderStatus cylinderStatus;
}
