package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contact_person")
public class ContactPersonEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    private Long mobileNumber;
}
