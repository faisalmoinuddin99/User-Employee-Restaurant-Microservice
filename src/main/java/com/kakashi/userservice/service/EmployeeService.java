package com.kakashi.userservice.service;


import com.kakashi.userservice.model.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    public Employee addEmployee(Employee employee) ;

    public List<Employee> getEmployees() ;

    public List<Employee> getEmployeesByRestaurantId(String restaurantId) ;

    public Map<String, List<Employee>> getAllEmployeesByRestaurantId() ;
}
