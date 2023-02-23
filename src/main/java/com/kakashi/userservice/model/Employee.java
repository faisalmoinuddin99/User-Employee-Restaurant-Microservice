package com.kakashi.userservice.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {
    private long empId;
    private String restaurantId ;
    private String empName;
    private String phoneNumber;
    private String jobPosition;
    private double salary;
    private String nationality;
    private String nativeState;
    private int workExp;
    private List<String> hobby = new ArrayList<>();

}
