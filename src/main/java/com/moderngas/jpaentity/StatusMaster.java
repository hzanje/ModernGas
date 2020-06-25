package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "status_master")
public class StatusMaster extends GenericEntity {

    @Column(name = "status")
    private String status;

    @Column(name = "sequence")
    private int sequence;
}
