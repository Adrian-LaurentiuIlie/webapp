package com.adrian.webapp;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final HotelService hotelService;

    public DataInitializer(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Hotel> hotels = hotelService.readHotelsFromJsonFile("src/main/resources/data/hotels.json");
        hotelService.saveHotels(hotels);
    }
}
