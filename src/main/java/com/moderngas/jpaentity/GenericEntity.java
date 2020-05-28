package com.moderngas.jpaentity;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Long id;

    @Column(name = "active_flag", columnDefinition = "tinyint(1) DEFAULT 1" )
    protected boolean activeFlag;

    @Column(name = "created_date")
    protected Date createdDate;

    @Column(name = "updated_date")
    protected Date updatedDate;


}
