package com.jogeen.barrage.web.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class Question implements Serializable{
    private String id;
    private String title;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private String analysis;
    private Long timeLimit=1L;
    private Long score=0L;
}
