package com.moderngas.jpaentity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Where(clause = "active_flag = 1")
@Table(name = "anonymous_cylinder")
public class AnonymousCylinderEntity extends BaseEntity {

    @Column(name = "cylinder_code")
    private String code;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "admin_id")
    private Long adminId;

}
