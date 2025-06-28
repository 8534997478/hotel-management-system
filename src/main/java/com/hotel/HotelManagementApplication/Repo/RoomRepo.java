package com.hotel.HotelManagementApplication.Repo;

import com.hotel.HotelManagementApplication.Entitys.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepo extends JpaRepository<Room, Long> {
    boolean existsByRoomNumber(String roomNumber);
    List<Room> findByAvailable(boolean available);

}

