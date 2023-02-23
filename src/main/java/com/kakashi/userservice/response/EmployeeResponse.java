package com.kakashi.userservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class EmployeeResponse {
    private Long empId;
    private String restaurantId ;
    private String empName;
    private String phoneNumber;
    private String jobPosition;
    private Double salary;
    private String nationality;
    private String nativeState;
    private Integer workExp;
    private final List<String> hobby = new ArrayList<>();

}
