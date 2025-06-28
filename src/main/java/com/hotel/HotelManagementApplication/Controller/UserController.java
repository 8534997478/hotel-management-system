package com.hotel.HotelManagementApplication.Controller;

import com.hotel.HotelManagementApplication.Entitys.Booking;
import com.hotel.HotelManagementApplication.Entitys.Room;
import com.hotel.HotelManagementApplication.Entitys.User;
import com.hotel.HotelManagementApplication.Repo.RoomRepo;
import com.hotel.HotelManagementApplication.service.BookingService;
import com.hotel.HotelManagementApplication.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Sign Up - Hotel Management");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/do_register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!agreement) {
            redirectAttributes.addFlashAttribute("error", "You must agree to the terms and conditions.");
            return "redirect:/user/signup";
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "signup";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Successfully Registered!");
        return "/login";
    }
    @GetMapping("/dashboard")
    public String userDashboard(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get()); // 👈 Add user to model
        }

        return "user/dashboard";
    }


    @GetMapping("/profile")
    public String viewProfile(Principal principal, Model model) {
        String username = principal.getName();
        userService.getUserByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        return "user/profile"; // Create profile.html
    }

    // Update profile
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFullName(updatedUser.getFullName());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            user.setGender(updatedUser.getGender());
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "Profile updated.");
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/book-room")
    public String showBookingForm(Model model) {
        List<Room> availableRooms = roomRepo.findByAvailable(true);
        model.addAttribute("rooms", availableRooms);
        model.addAttribute("booking", new Booking());
        return "user/book-room";
    }
    @GetMapping("/book-room/form/{roomId}")
    public String bookRoomForm(@PathVariable Long roomId, Model model) {
        roomRepo.findById(roomId).ifPresent(room -> model.addAttribute("room", room));
        model.addAttribute("booking", new Booking());
        return "user/booking-form";
    }


    @PostMapping("/book-room")
    public String processBooking(@ModelAttribute Booking booking,
                                 @RequestParam Long roomId,
                                 @RequestParam String paymentId,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.getUserByUsername(principal.getName());
        Optional<Room> room = roomRepo.findById(roomId);

        if (user.isPresent() && room.isPresent()) {
            if (!bookingService.isRoomAvailable(roomId, booking.getCheckInDate(), booking.getCheckOutDate())) {
                redirectAttributes.addFlashAttribute("error", "Room not available for selected dates!");
                return "redirect:/user/book-room";
            }

            booking.setRoom(room.get());
            booking.setCustomerName(user.get().getFullName());
            booking.setPhone(user.get().getPhone());
            booking.setEmail(user.get().getEmail());
            booking.setStatus("Confirmed");
            booking.setPaymentId(paymentId);  // ✅ save payment ID

            bookingService.saveBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Room booked and paid successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Booking failed!");
        }

        return "redirect:/user/my-bookings";
    }



    @GetMapping("/my-bookings")
    public String viewBookings(Model model, Principal principal) {
        String email = userService.getUserByUsername(principal.getName()).get().getEmail();
        List<Booking> bookings = bookingService.findByEmail(email);
        model.addAttribute("bookings", bookings);
        return "user/my-bookings";
    }
    @PostMapping("/create-order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        int amount = Integer.parseInt(data.get("amount").toString());

        RazorpayClient client = new RazorpayClient("rzp_test_15dGbJgD4ajD6F", "finsneMvkCuCfKT2NiOH5qgG");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount); // in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "room_booking_" + UUID.randomUUID());

        Order order = client.orders.create(orderRequest);  // ✅ THIS IS CORRECT FOR v1.4.4+
        return order.toString();  // JSON string will be sent to frontend
    }




}
