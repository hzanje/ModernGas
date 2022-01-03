package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Data
@Entity
@SQLDelete(sql = "UPDATE resource_centre a SET active_flag = 0 WHERE id = ? ")
@Where(clause = "active_flag = 1")
@Table(name = "resource_centre")
public class ResourceCentreEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    public ResourceCentreEntity(Long id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }
}
