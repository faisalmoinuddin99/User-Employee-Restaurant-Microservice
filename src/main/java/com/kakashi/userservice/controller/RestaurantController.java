package com.kakashi.userservice.controller;

import com.kakashi.userservice.dto.RestaurantDTO;
import com.kakashi.userservice.model.Employee;
import com.kakashi.userservice.service.EmployeeService;
import com.kakashi.userservice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/restaurants")
    public ResponseEntity<Flux<RestaurantDTO>> getRestaurantDetails() {
        return ResponseEntity.ok(restaurantService.fetchRestaurantCompleteDetails());
    }


    @GetMapping("/restaurants/delhi")
    public ResponseEntity<Mono<List<RestaurantDTO>>> getRestaurantFromDelhi() {
        return ResponseEntity.ok(restaurantService.getAllRestaurantPresentInDelhi());
    }

    @GetMapping("/restaurant/{city}")
    public ResponseEntity<Mono<List<RestaurantDTO>>> getRestaurantByCity(@PathVariable String city) {
        return ResponseEntity.ok(restaurantService.getRestaurantByCity(city));
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Flux<RestaurantDTO>> getRestaurantWithEmployees(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.fetchAllRestaurantWithEmployees(id));
    }


    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @GetMapping("/employee/restaurant/{id}")
    public ResponseEntity<List<Employee>> getEmployeesOfRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeesByRestaurantId(id));
    }

    @GetMapping("/employee/restId")
    public ResponseEntity<Map<String, List<Employee>>> getEmployeesByRestId() {
        return ResponseEntity.ok(employeeService.getAllEmployeesByRestaurantId());
    }
}
