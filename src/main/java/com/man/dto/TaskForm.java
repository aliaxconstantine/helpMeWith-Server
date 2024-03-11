package com.man.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TaskForm {
    private String[] imageUrl;
    private String name;
    private String description;
    private Double price;
    private String[] type;
    private String bigType;
    private Date dueTime;
    private Double x;
    private Double y;
    private String city;
}
