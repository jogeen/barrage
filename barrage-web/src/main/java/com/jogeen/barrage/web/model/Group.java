package com.jogeen.barrage.web.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_group")
public class Group {
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

    /**
     * num
     */
    private Integer num;

    private Long value;

}
