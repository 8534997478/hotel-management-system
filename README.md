# 🏨 Hotel Management System

A **full-stack Hotel Management System** built with **Spring Boot**, **Thymeleaf**, and **Bootstrap**, offering room booking, Razorpay payment integration, user authentication, and an admin dashboard for room and booking management.

---

## 🚀 Features

### 👤 User Functionality
- 🛏 View available rooms with images and prices
- 📅 Book rooms for specific check-in and check-out dates
- 💳 Pay securely via Razorpay (payment gateway integration)
- 🧾 View personal booking history and booking status
- 🔐 User login and registration

### 🔑 Admin Functionality
- 📊 Admin dashboard with total users, active bookings, and room availability
- 🧑 Manage users (edit/delete)
- 🏨 Manage rooms (add, update, delete)
- 📋 View and manage all bookings
- 📤 Upload room images and set price per night

---

## 🛠 Tech Stack

| Layer        | Technology           |
|--------------|----------------------|
| Backend      | Java, Spring Boot    |
| Frontend     | Thymeleaf, HTML, CSS, Bootstrap 5 |
| Database     | MySQL                |
| Security     | Spring Security, BCrypt             |
| Payment      | Razorpay API         |
| Build Tool   | Maven                |

---

## 🧱 Project Structure

```bash
HotelManagementApplication
├── src
│   ├── main
│   │   ├── java/com/hotel/HotelManagementApplication
│   │   │   ├── Controller
│   │   │   ├── Entitys
│   │   │   ├── Repo
│   │   │   ├── service
│   │   │   └── Conffig
│   │   └── resources
│   │       ├── templates
│   │       ├── static
│   │       └── application.properties
└── pom.xml
