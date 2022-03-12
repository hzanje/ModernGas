package com.moderngas.jpaentity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_token")
public class UserTokenEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    protected Long id;

    @Lob
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expired_date")
    private Date expiredDate;

}
