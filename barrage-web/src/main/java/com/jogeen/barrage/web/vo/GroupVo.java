package com.jogeen.barrage.web.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupVo implements Serializable{

    private Integer id;

    private String name;

    private String leader;

    private Integer num;

    private Long score;

    private Long percentage;

}
