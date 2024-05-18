package com.adrian.webapp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
	private HotelService hotelService;

    @GetMapping("/booking/view")
    public String viewBooking(Model model) {
    	List<Booking> bookingList = bookingRepository.findAll();
    	model.addAttribute("bookingList", bookingList);
    	return "viewBooking";
    }
    
    @GetMapping("/booking/create")
    public String showBookingForm(Model model, @RequestParam("roomId") int roomId,@RequestParam("hotelId") int hotelId) {
    	Hotel hotel = hotelService.getHotelById(hotelId);
    	List<Room> hotelRooms = hotel.getRooms();
    	List<Room> selectedRoom = hotelRooms.stream()
						                .filter(rooms -> rooms.getRoomNumber() == roomId)
						                .collect(Collectors.toList());
    	Room room = selectedRoom.get(0);
    	model.addAttribute("booking", new Booking());
    	return "bookingForm";
    }
    @PostMapping("/booking/create")
    public String submitBooking(@ModelAttribute("booking") Booking booking) {
    	booking.setTimestamp(LocalDateTime.now());
        bookingRepository.save(booking);
    	return "redirect:/booking/confirmation";
    }
 
    @GetMapping("/booking/delete")
    public String deleteFeedback(@RequestParam int id, Model model) {
	  	Optional<Booking> optionalBooking = bookingRepository.findById(id);
	    Booking booking = optionalBooking.get();
	    LocalDateTime bookingTime = booking.getTimestamp();
	    LocalDateTime currentTime = LocalDateTime.now();
	    Duration duration = Duration.between(bookingTime, currentTime);
	    long hoursDifference = duration.toHours();
	    if (hoursDifference < 2) {
	    	// If less than 2 hours, delete the booking
	    	bookingRepository.deleteById(id);
	    } else {
	        // If more than 2 hours, do not delete and redirect with a message
	        model.addAttribute("message", "Cannot delete booking older than 2 hours.");
	        }
	    return "redirect:/booking/view";
	    } 
    
    @GetMapping("/booking/confirmation")
    public String showBookingConfiramtion() {
    	return "bookingConfirmation";
    }
    
}
