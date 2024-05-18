package com.adrian.webapp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	
	@Autowired
	private HotelService hotelService;
	//HardCoded Lat and Lon
	//private double latitude1 = 46.766984;
    //private double longitude1 = 23.626513;
    
	@GetMapping("/")
	public String home() {
		return "index";
	}

    public List<Hotel> getHotels(){
    	List<Hotel> hotels = hotelService.getAllHotels();
    	return hotels;
    }
    
    @PostMapping("/selectHotelsByDistance")
    public String filterHotelByDistance(@RequestParam("distance") double distance,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            Model model) {
    	List<Hotel> hotels = getHotels();
    	List<Hotel> filteredHotelsByDistance = hotels.stream()
    			.filter(hotel -> calculateDistance(latitude, longitude, hotel.getLatitude(), hotel.getLongitude()) <= distance)
				.collect(Collectors.toList());
    	model.addAttribute("index", filteredHotelsByDistance);
    	return "index";
    }
    
    @GetMapping("/availableRooms")
    public String getAvailableRooms(@RequestParam("id") int id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        List<Room> hotelRooms = hotel.getRooms();
        List<Room> availableRooms = hotelRooms.stream()
                                         .filter(rooms -> rooms.isAvailable() == true)
                                         .collect(Collectors.toList());
        model.addAttribute("availableRooms", availableRooms);
        return "availableRooms";
    }
         
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // convert to kilometers
    }
}
