package com.hotel.HotelManagementApplication.Repo;
import com.hotel.HotelManagementApplication.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

