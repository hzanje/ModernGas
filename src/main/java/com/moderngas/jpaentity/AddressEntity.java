package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@SQLDelete(sql = "UPDATE address a SET active_flag = 0 WHERE id = ?")
@Table(name = "address")
public class AddressEntity extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "pincode")
    private int pincode;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "is_primary")
    private boolean isPrimary;

}
