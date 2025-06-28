package com.hotel.HotelManagementApplication.Repo;

import com.hotel.HotelManagementApplication.Entitys.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    List<Booking> findByEmail(String email);

    List<Booking> findByRoomIdAndStatus(Long roomId, String confirmed);
    List<Booking> findByRoomId(Long roomId); // ✅ THIS LINE

}
