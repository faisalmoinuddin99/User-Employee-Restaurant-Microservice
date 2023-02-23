package com.kakashi.userservice.service;

import com.kakashi.userservice.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final List<Employee> employeeList = new ArrayList<>();


    static {


        Employee emp1 = new Employee(4531,"63ef20966a7bf6fccffb44b6" ,"Ram Prasad", "+91 8765435657",
                "waiter", 15000, "Indian", "UP", 14,
                new ArrayList<>(List.of("playing cricket", "dancing", "cooking")));

        Employee emp2 = new Employee(4532,"63ef20966a7bf6fccffb44b6","Manhor Lal Khatar", "+91 8765435617",
                "chef", 20000, "Indian", "Bihar", 35,
                Arrays.asList("playing football", "travelling", "cooking"));

        Employee emp3 = new Employee(4533,"63f206b72f439cae736cbeba", "Chandan Kumar Shrivastav", "+91 8765435627",
                "Manager", 25000, "British Indian", "Bihar", 15,
                Arrays.asList("playing chess", "solo traveller"));


        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);
    }


    @Override
    public Employee addEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeList;
    }

    @Override
    public List<Employee> getEmployeesByRestaurantId(String restaurantId) {

        // will get all the employees working in the particular restaurant
        // uri:  http://localhost:8080/v1/api/employee/restaurant/63ef20966a7bf6fccffb44b6
        return employeeList.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Employee>> getAllEmployeesByRestaurantId() {
        return employeeList.stream().collect(Collectors.groupingBy(Employee::getRestaurantId));
    }


}
