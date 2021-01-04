package com.moderngas.jpaentity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "status_master")
public class StatusMaster extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "sequence")
    private int sequence;
}
