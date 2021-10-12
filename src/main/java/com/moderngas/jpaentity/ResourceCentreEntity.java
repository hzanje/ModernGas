package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "resource_centre")
public class ResourceCentreEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    /*@Column(name = "user_id")
    private Long userId;*/

    public ResourceCentreEntity(Long id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }
}
