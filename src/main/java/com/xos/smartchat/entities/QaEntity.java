package com.xos.smartchat.entities;

import lombok.Data;

@Data
public class QaEntity {
    private int id;
    private String question;
    private String answer;
    private String keywords;
    private int hits;
}
