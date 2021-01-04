package com.moderngas.jpaentity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "category_master")
public class CategoryMaster extends BaseEntity {

    private String name;
}
