Spend Wise – Personal Expense Tracker

Spend Wise is an Android application developed using Kotlin and Jetpack Compose. It helps users track expenses and income, set monthly limits, visualize spending trends using graphs and bar charts, and manage their finances effectively. The app integrates Firebase Authentication, Firestore Database, and Analytics, with smooth navigation and a clean MVVM architecture.

1. Overview

Spend Wise simplifies personal finance management by providing insights into income and expenses, enabling users to plan better and save more. The app is designed with Jetpack Compose for a modern UI experience and follows MVVM architecture for clean and maintainable code.

2. Features

User Authentication → Secure login and logout using Firebase Auth

Add Income & Expenses → Track all financial transactions

Budget Management → Set monthly spending limits and get notified when exceeded

Data Visualization → Analyze spending patterns with graphs and bar charts

Notifications → Get alerts about budgets and savings

Smooth Navigation → Built with Jetpack Compose Navigation

MVVM Architecture → Ensures scalability, maintainability, and testability

Firestore Integration → Real-time cloud-based database support

3. Tech Stack
Category	Technology Used
Language	Kotlin
UI Toolkit	Jetpack Compose
Architecture	MVVM (Model-View-ViewModel)
Navigation	Accompanist Navigation + Navigation-Compose
Database	Firebase Firestore
Authentication	Firebase Auth
State Management	ViewModel + StateFlow
Image Loading	Coil
Notifications	Firebase + Local Notifications
4. Dependencies
// Jetpack Compose & Material Design
implementation("androidx.compose.material:material:1.6.8")
implementation("androidx.compose.material:material-icons-extended:1.5.4")
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

// Coil for Image Loading
implementation("io.coil-kt:coil-compose:2.4.0")

// Accompanist for Smooth Navigation
implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")

// Firebase Integration
implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
implementation("com.google.firebase:firebase-auth:22.3.0")
implementation("com.google.firebase:firebase-firestore:24.10.0")
implementation("com.google.firebase:firebase-analytics")

5. Architecture

The app follows the MVVM (Model-View-ViewModel) pattern for a clean, modular, and testable architecture.

spend_wise/
│── model/             # Data models (Expense, Income, Budget, Notifications)
│── view/              # Jetpack Compose UI screens
│── viewmodel/         # ViewModels for state & data handling
│── navigation/        # Navigation setup for Jetpack Compose
│── ui/theme/          # App theming and color management
│── repository/        # Data handling (Firestore, Local DB)
│── utils/             # Helper classes and constants
│── MainActivity.kt    # App entry point

6. Screens & Functionality

Home Screen → Overview of expenses, income, and budgets

Add Transaction → Add income or expense records

Monthly Summary → View graphs and bar charts for expenses

Budget Settings → Set monthly limits and receive notifications

Notifications → Alerts for budgets, savings, and reminders

Profile Management → Edit profile and manage account details

7. Installation & Setup

Clone the Repository

git clone https://github.com/your-username/spend-wise.git


Open in Android Studio

Go to File → Open → Spend Wise

Configure Firebase

Go to Firebase Console

Create a project & add google-services.json to app/

Sync Gradle and Run the App

8. Screenshots
Home Screen	Add Expense	Monthly Report
(Coming Soon)	(Coming Soon)	(Coming Soon)
9. Contributing

Contributions are always welcome!
If you'd like to improve Spend Wise, fork the repository and submit a pull request.

10. License

This project is licensed under the MIT License.
