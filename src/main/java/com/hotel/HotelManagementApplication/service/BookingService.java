package com.hotel.HotelManagementApplication.service;

import com.hotel.HotelManagementApplication.Entitys.Booking;
import com.hotel.HotelManagementApplication.Repo.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public List<Booking> findByEmail(String email) {
        return bookingRepo.findByEmail(email); // 👈 this line makes the controller work
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepo.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepo.deleteById(id);
    }
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> existing = bookingRepo.findByRoomIdAndStatus(roomId, "Confirmed");
        return existing.stream().noneMatch(b ->
                !(b.getCheckOutDate().isBefore(checkIn) || b.getCheckInDate().isAfter(checkOut))
        );
    }





}


