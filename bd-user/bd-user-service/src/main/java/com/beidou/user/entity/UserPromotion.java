package com.beidou.user.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Table(name = "E_USER_PROMOTION")
@Data
public class UserPromotion {


    @Id
    private Long id;

    @Column(name = "my_address")
    private String myAddress;

    @Column(name = "other_address")
    private String otherAddress;


    @Column(name = "createon")
    private Long createon;

    @Column(name = "updateon")
    private Long updateon;

    @Transient
    private String createTime;




}