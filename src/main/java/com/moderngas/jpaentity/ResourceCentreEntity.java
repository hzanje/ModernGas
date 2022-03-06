package com.moderngas.jpaentity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@Entity
@SQLDelete(sql = "UPDATE resource_centre a SET active_flag = 0 WHERE id = ? ")
@Where(clause = "active_flag = 1")
@Table(name = "resource_centre")
public class ResourceCentreEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    public ResourceCentreEntity(Long id, String name, String alias, UserEntity entity) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.userEntity = entity;
    }
}
