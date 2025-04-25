# 📱 Quote Collector - Android App

## Overview
**Quote Collector** is a mobile application built for Android that allows users to collect, manage, and organize their favorite quotes. The app features user authentication, quote management with CRUD operations, search functionality, and category-based filtering.

---

## 📸 Screenshots

<div align="center">
  <img src="https://github.com/DimalshaPerera/QuoteCollector--Tortued-Poets/raw/master/app/src/main/res/drawable/img.png" alt="Login Screen" width="250"/>
</div>

---

## ✨ Features

### 🔐 User Authentication
- Sign up and login functionality

### 📝 Quote Management
- Add new quotes  
- Delete quotes  
- View all quotes  
- Find quotes by category

### 🎨 UI Features
- Clean, intuitive interface  
- Dark mode aesthetic  
- Responsive design for different screen sizes

---

## 🛠️ Tech Stack

- **Language:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Architecture:** MVVM pattern  
- **Network:** Retrofit for API calls  
- **Backend:** MockAPI.io for mock REST API  
- **Data Storage:** SharedPreferences for user session

---

## 🔗 API Endpoints Used

- `GET /users` – User authentication  
- `POST /users` – User registration  
- `GET /quotes` – Fetch all quotes  
- `GET /quotes?userId={id}` – Get quotes by user  
- `GET /quotes?text={text}` – Search quotes  
- `GET /quotes?category={category}` – Filter quotes by category  
- `POST /quotes` – Create new quote  
- `PUT /quotes/{id}` – Update quote  
- `DELETE /quotes/{id}` – Delete quote  

---

## 🚀 Setup Instructions

1. Clone the repository  
2. Open the project in Android Studio  
3. Sync Gradle dependencies  
4. Run the app on an emulator or physical device

---

## 🔮 Future Enhancements

- Enhanced search functionality with advanced filters  
- Quote editing feature with better UI  
- Offline mode for viewing quotes without internet connection  
- Social sharing capabilities  
- User profile customization  
- Additional quote categories and tagging system  
- Favorite/bookmarking system for quotes  

---

## 🧱 UI Framework

This application uses **Jetpack Compose** for building the user interface.

---

## 📝 Note for Reviewers

This is the initial version of the application that demonstrates the core functionality required in the practical test. Some features like detailed editing UI and advanced search are planned for future iterations.
