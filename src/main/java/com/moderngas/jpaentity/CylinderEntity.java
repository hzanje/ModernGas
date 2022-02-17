package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderStatus;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Where(clause = "active_flag = 1")
@Table(name = "cylinder")
public class CylinderEntity extends BaseEntity {

    @Column(name = "cylinder_code")
    private String code;

    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    @Column(name = "assigned_user_name")
    private String assignedUserName;

    @Column(name = "manufacturer")
    private String manufacturer;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "manufacturing_date", updatable = false)
    private Date manufacturingDate;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", updatable = false)
    private Date expiryDate;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_service")
    private Date lastService;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "next_service")
    private Date nextService;

    @Column(name = "cylinder_status_id")
    @Enumerated(EnumType.ORDINAL)
    private CylinderStatus cylinderStatus;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cylinder_detail_id", referencedColumnName = "id")
    private CylinderInventoryDetailsEntity cylinderInventoryDetailsEntity;
}
