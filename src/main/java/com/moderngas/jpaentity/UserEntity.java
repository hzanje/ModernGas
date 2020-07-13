package com.moderngas.jpaentity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends GenericEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "password")
    private String password;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToOne
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    private AddressEntity addressEntity;

}
