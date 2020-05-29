package com.moderngas.jpaentity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role")
public class RoleEntity {

    private Long id;

    private String role;
}
