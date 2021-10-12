package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderStatus;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cylinder")
public class CylinderEntity extends BaseEntity {

    @Column(name = "cylinder_code")
    private String code;

    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    @Column(name = "manufacturer")
    private String manufacturer;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "manufacturing_date", updatable = false)
    private Date manufacturingDate;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", updatable = false)
    private Date expiryDate;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_service")
    private Date lastService;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "next_service")
    private Date nextService;

    @Column(name = "cylinder_status_id")
    @Enumerated(EnumType.ORDINAL)
    private CylinderStatus cylinderStatus;
}
