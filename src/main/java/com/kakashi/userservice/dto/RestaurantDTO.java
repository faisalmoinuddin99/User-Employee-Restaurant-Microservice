package com.kakashi.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakashi.userservice.model.Employee;
import com.kakashi.userservice.response.EmployeeResponse;
import lombok.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@With
public class RestaurantDTO {

    @JsonProperty("_id")
    private String id ;
    private String name ;
    private String address ;

    private String city ;
    @JsonProperty("_phoneNumber")
    private String phoneNumber ;

    @JsonProperty("_isOpened")
    private boolean isOpened ;

    private List<EmployeeResponse> employees = new ArrayList<>() ;

    private int employeeCount;
}
