package com.hotel.HotelManagementApplication.Conffig;

import com.hotel.HotelManagementApplication.Entitys.Booking;
import com.hotel.HotelManagementApplication.Repo.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class BookingCleanupTask {

    @Autowired
    private BookingRepo bookingRepo;

    @Scheduled(cron = "0 0 1 * * *") // Every day at 1 AM
    public void deleteExpiredBookings() {
        List<Booking> expired = bookingRepo.findAll().stream()
                .filter(b -> b.getCheckOutDate().isBefore(LocalDate.now()))
                .toList();

        bookingRepo.deleteAll(expired);
    }
}

