package com.jogeen.barrage.web.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_user")
@Data
public class User {
    @Id
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String type;
}
