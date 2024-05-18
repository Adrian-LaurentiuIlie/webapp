package com.adrian.webapp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public HotelService(HotelRepository hotelRepository, ObjectMapper objectMapper) {
        this.hotelRepository = hotelRepository;
        this.objectMapper = objectMapper;
    }

    public void saveHotels(List<Hotel> hotels) {
        hotelRepository.saveAll(hotels);
    }
    
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public List<Hotel> readHotelsFromJsonFile(String filePath) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, new TypeReference<List<Hotel>>() {});
    }
    
    public Hotel getHotelById(int id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        return hotel.orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
    }
}
