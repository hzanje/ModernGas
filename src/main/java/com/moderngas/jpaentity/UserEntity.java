package com.moderngas.jpaentity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseEntity {

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

    @Column(name = "is_onboarding", columnDefinition = "tinyint(1) DEFAULT 1" )
    protected boolean isOnboarding = true;

    @Column(name = "is_forget_password", columnDefinition = "tinyint(1) DEFAULT 0")
    private boolean isForgetPassword = false;

    @ElementCollection
    @CollectionTable(name = "user_admin_mapping", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "admin_id")
    private Set<Long> adminIdSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<AddressEntity> addressEntitySet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<ContactPersonEntity> contactPersonSet;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Set<UserRoleEntity> roleEntitySet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "admin_id", referencedColumnName = "id", nullable = false)
    private Set<AdminGasMapping> adminGasMappings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Set<CylinderEntity> cylinderEntitySet;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Set<ResourceCentreEntity> resourceCentreEntitySet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Set<UserTokenEntity> userTokenSet;



}
