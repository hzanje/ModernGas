package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderStatus;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter @Setter
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
    @Column(name = "hydro_test_date")
    private Date hydroTestingDate;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "next_hydro_test_due_date")
    private Date nextHydroTestDueDate;

    @Column(name = "cylinder_status_id")
    @Enumerated(EnumType.ORDINAL)
    private CylinderStatus cylinderStatus;

    @Column(name = "gas_id")
    private Long gasId;

    @Column(name = "identifier")
    private String identifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cylinder_detail_id", referencedColumnName = "id")
    private CylinderInventoryDetailsEntity cylinderInventoryDetailsEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CylinderEntity that = (CylinderEntity) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code);
    }
}

