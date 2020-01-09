package com.jogeen.barrage.web.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_auth")
public class Auth {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * uid
     */
    private Integer uid;

    /**
     * third_id
     */
    private Integer thirdId;

    /**
     * third_type
     */
    private String thirdType;

    /**
     * name
     */
    private String name;

    /**
     * email
     */
    private String email;

    /**
     * login
     */
    private String login;
}
