package com.hotel.HotelManagementApplication.Controller;

import com.hotel.HotelManagementApplication.Entitys.Room;
import com.hotel.HotelManagementApplication.Entitys.User;
import com.hotel.HotelManagementApplication.service.BookingService;
import com.hotel.HotelManagementApplication.service.RoomService;
import com.hotel.HotelManagementApplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BookingService bookingService;

    // Show admin dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // Manage Users page
    @GetMapping("/user")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/manage-user";
    }

    // Delete user
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/user";
    }

    // Edit user (GET)
    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        userService.getUserById(id).ifPresent(user -> model.addAttribute("user", user));
        return "admin/edit-user";
    }

    // Update user (POST)
    @PostMapping("/user/update")
    public String updateUser(@ModelAttribute("user") User updatedUser) {
        userService.saveUser(updatedUser);
        return "redirect:/admin/user";
    }
    @GetMapping("/rooms")
    public String manageRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/manage-rooms";
    }

    @GetMapping("/rooms/add")
    public String addRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "admin/add-room";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute Room room,
                           @RequestParam("roomImage") MultipartFile file,
                           HttpServletRequest request) throws IOException {

        if (!file.isEmpty()) {
            String uploadDir = request.getServletContext().getRealPath("/uploads/");
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.write(path, file.getBytes());

            room.setImageUrl("/uploads/" + filename);
        } else if (room.getId() != null) {
            roomService.getRoomById(room.getId())
                    .ifPresent(existing -> room.setImageUrl(existing.getImageUrl()));
        }

        roomService.saveRoom(room);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/rooms/edit/{id}")
    public String editRoom(@PathVariable Long id, Model model) {
        roomService.getRoomById(id).ifPresent(room -> model.addAttribute("room", room));
        return "admin/add-room";
    }

    @PostMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/bookings")
    public String viewBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/manage-bookings";
    }

    @PostMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/admin/bookings";
    }


}


