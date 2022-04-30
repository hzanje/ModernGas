package com.moderngas.jpaentity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Where(clause = "active_flag = 1")
@Table(name = "category_master")
public class CategoryMaster extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "icon_url")
    private String iconURL;
}
