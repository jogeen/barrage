package com.jogeen.barrage.web.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "tb_group")
public class Group implements Serializable{
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * leader
     */
    private String leader;


    private Long score;

}
