package com.kakashi.userservice.service;

import com.kakashi.userservice.dto.RestaurantDTO;
import com.kakashi.userservice.model.Employee;
import com.kakashi.userservice.response.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;


    @Override
    public Flux<RestaurantDTO> getAllRestaurantDetails() {


        return webClient.get()
                .uri("http://RESTAURANT-SERVICE/v1/api/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
//                .map(merge -> merge.withEmployees(allEmployee))
                .limitRate(10) // Limits the rate to 10 items per second [ BACKPRESSURE ]
                .delayElements(Duration.ofSeconds(0))
                .log();
    }


    @Override
    public Mono<List<RestaurantDTO>> getAllRestaurantPresentInDelhi() {


        return webClient.get()
                .uri("http://RESTAURANT-SERVICE/v1/api/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
                .filter(r -> r.getAddress().toLowerCase().contains("delhi")
                        || r.getCity().toLowerCase().contains("delhi"))
                .collectList()
                .log();
    }

    public Mono<List<RestaurantDTO>> getRestaurantByCity(String city) {
        return webClient.get()
                .uri("http://RESTAURANT-SERVICE/v1/api/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
                .filter(x -> x.getCity().toLowerCase().equals(city))
                .collectList()
                .log();
    }

    @Override
    public Flux<RestaurantDTO> fetchAllRestaurantWithEmployees(String restaurantId) {
        return webClient.get()
                .uri("http://RESTAURANT-SERVICE/v1/api/restaurant/" + restaurantId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
                .flatMap(restaurant -> {
                    Flux<EmployeeResponse> employeeList = webClient.get()
                            .uri("http://EMPLOYEE-SERVICE/v1/api/emp/restaurant/" + restaurantId)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToFlux(EmployeeResponse.class)
                            .log() ;
                    return employeeList.collectList()
                            .map(employees -> {
                                int employeeCount = employees.size();
                                return restaurant.withEmployees(employees)
                                        .withEmployeeCount(employeeCount);
                            })
                            .log();
                }) ;
    }

    @Override
    public Flux<RestaurantDTO> fetchRestaurantCompleteDetails() {
        return webClient.get()
                .uri("http://localhost:8085/v1/api/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
                .flatMap(restaurant -> {
                    Flux<EmployeeResponse> employeeList = webClient.get()
                            .uri("http://localhost:8081/v1/api/emp/restaurant/" + restaurant.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToFlux(EmployeeResponse.class)
                            .log();

                    return employeeList.collectList()
                            .map(employees -> {
                                int employeeCount = employees.size();
                                return restaurant.withEmployees(employees)
                                        .withEmployeeCount(employeeCount) ;
                            }).log() ;

                }) ;
    }


/*

    public Mono<List<RestaurantDTO>> getRestaurantsWithEmployeesPart1() {

        // Collect employees by restaurant ID
        Map<String, List<Employee>> employeesByRestaurantId = employeeService.getEmployees()
                .stream()
                .collect(Collectors.groupingBy(Employee::getRestaurantId));

        // Fetch all restaurants using WebClient
        Mono<Map<String, RestaurantDTO>> restaurantsById = webClient.get()
                .uri("http://localhost:8085/v1/api/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class)
                // Collect restaurants by ID
                .collect(Collectors.toMap(RestaurantDTO::getId, Function.identity()))
                .log();

        // Combine employees and restaurants
        Mono<List<RestaurantDTO>> mergedRestaurants = restaurantsById.map(restaurants -> {
            List<RestaurantDTO> result = new ArrayList<>();
            for (Map.Entry<String, RestaurantDTO> entry : restaurants.entrySet()) {
                String restaurantId = entry.getKey();
                RestaurantDTO restaurant = entry.getValue();
                List<Employee> employees = employeesByRestaurantId.get(restaurantId);
                if (employees != null) {
                    restaurant.setEmployees(employees);
                    restaurant.setEmployeeCount(employees.size()); // Set the employee count for the restaurant
                }
                result.add(restaurant);
            }
            return result;
        });


        // logic to count total number of employees in all restaurant
        Mono<Long> totalEmployees = mergedRestaurants.flatMapMany(Flux::fromIterable)
                .flatMap(r -> Flux.fromIterable(r.getEmployees()))
                .count();
        totalEmployees.subscribe(System.out::println); // 3

        return mergedRestaurants;
    }

    @Override
    public Mono<List<RestaurantDTO>> getRestaurantsWithEmployeesOptimizedCode() {

        // Fetch all employees using WebClient
        Flux<Employee> employees = webClient.get()
                .uri("http://localhost:8085/v1/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Employee.class);

        // Fetch all restaurants using WebClient
        Flux<RestaurantDTO> restaurants = webClient.get()
                .uri("http://localhost:8085/v1/api/restaurants")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(RestaurantDTO.class);

        // Combine employees and restaurants
        Mono<List<RestaurantDTO>> mergedRestaurants = restaurants.collectList().flatMap(rs -> {
            Map<String, RestaurantDTO> restaurantMap = rs.stream()
                    .collect(Collectors.toMap(RestaurantDTO::getId, Function.identity()));

            return employees.collect(Collectors.groupingBy(Employee::getRestaurantId)).map(employeesByRestaurantId -> {
                employeesByRestaurantId.forEach((restaurantId, employeeList) -> {
                    RestaurantDTO restaurant = restaurantMap.get(restaurantId);
                    if (restaurant != null) {
                        restaurant.setEmployees(employeeList);
                    }
                });
                return new ArrayList<>(restaurantMap.values());
            });
        });


        return mergedRestaurants;
    }


 */

}
