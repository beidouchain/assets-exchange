package com.beidou.exchange.base.entity;

import lombok.Data;
import javax.persistence.*;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {
    @Column(name = "created_on")
    private Long createdOn;
    @Column(name = "updated_on")
    private Long updatedOn;
}
