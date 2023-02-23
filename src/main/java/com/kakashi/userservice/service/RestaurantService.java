package com.kakashi.userservice.service;

import com.kakashi.userservice.dto.RestaurantDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface RestaurantService {

    public Flux<RestaurantDTO> getAllRestaurantDetails() ;



    public Mono<List<RestaurantDTO>> getAllRestaurantPresentInDelhi() ;

    public Mono<List<RestaurantDTO>> getRestaurantByCity(String city) ;

    public Flux<RestaurantDTO> fetchAllRestaurantWithEmployees(String restaurantId) ;

    public Flux<RestaurantDTO> fetchRestaurantCompleteDetails() ;

/*
    public Mono<List<RestaurantDTO>> getRestaurantsWithEmployeesPart1() ;
    public Mono<List<RestaurantDTO>> getRestaurantsWithEmployeesOptimizedCode() ;

 */


}
