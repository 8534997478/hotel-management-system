package com.hotel.HotelManagementApplication.service;

import com.hotel.HotelManagementApplication.Entitys.Room;
import com.hotel.HotelManagementApplication.Repo.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepo roomRepo;

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    public void saveRoom(Room room) {
        roomRepo.save(room);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepo.findById(id);
    }

    public void deleteRoom(Long id) {
        roomRepo.deleteById(id);
    }
}

