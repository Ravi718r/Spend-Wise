📱 Spend Wise – Personal Expense Tracker

Spend Wise is an Android app built using Kotlin and Jetpack Compose that helps users track expenses and income, set monthly limits, visualize spending trends using graphs and bar charts, and manage their finances effortlessly. It features Firebase authentication, Firestore database, notifications, and smooth navigation with MVVM architecture.

✨ Features

✅ User Authentication – Login & Logout using Firebase Auth
✅ Add Income & Expense – Track all your transactions easily
✅ Budget Management – Set monthly limits and get notified when exceeded
✅ Analytics Dashboard – Visualize spending using graphs & bar charts
✅ Notifications – Smart reminders for savings and limits
✅ Smooth Navigation – Powered by Jetpack Compose Navigation
✅ MVVM Architecture – Clean, scalable, and maintainable code
✅ Firestore Integration – Secure cloud-based data storage

🛠️ Tech Stack
Category	Technology Used
Language	Kotlin
UI Toolkit	Jetpack Compose
Architecture	MVVM (Model-View-ViewModel)
Navigation	Accompanist Navigation & Navigation-Compose
Database	Firebase Firestore
Authentication	Firebase Auth
State Management	ViewModel + StateFlow
Image Loading	Coil
Charts	Compose-based custom graphs & bar charts
Notifications	Firebase + Local Notifications
📦 Dependencies
// Jetpack Compose & Material
implementation("androidx.compose.material:material:1.6.8")
implementation("androidx.compose.material:material-icons-extended:1.5.4")
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

// Coil for Image Loading
implementation("io.coil-kt:coil-compose:2.4.0")

// Accompanist for Smooth Navigation
implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")

// Firebase
implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
implementation("com.google.firebase:firebase-auth:22.3.0")
implementation("com.google.firebase:firebase-firestore:24.10.0")
implementation("com.google.firebase:firebase-analytics")

🏛️ Architecture

The app follows MVVM (Model-View-ViewModel) for a clean, modular, and testable structure.

spend_wise/
│── model/             # Data models (Expense, Income, Budget, Notifications)
│── view/              # Jetpack Compose UI screens
│── viewmodel/         # ViewModels for state & data handling
│── navigation/        # App navigation setup
│── ui/theme/          # App theming & colors
│── repository/        # Data handling (Firestore, Local DB)
│── utils/             # Helper classes, constants
│── MainActivity.kt    # App entry point

📊 Screens & Features

Home Screen → Overview of expenses, income & budget

Add Transaction → Add income/expense easily

Monthly Summary → See bar charts & graphs for expenses

Budget Settings → Set limits and get notified when exceeded

Notifications → Alerts for budgets and reminders

Profile Management → Update profile, manage login/logout

🚀 Installation & Setup

Clone the Repository

git clone [https://github.com/your-username/spend-wise.git](https://github.com/Ravi718r/Spend-Wise/tree/master)


Open in Android Studio

Select File → Open → Spend Wise

Set up Firebase

Go to Firebase Console

Create a project & add google-services.json to app/

Sync Gradle & Run the App


🤝 Contributing

Contributions are welcome!
If you'd like to improve Spend Wise, feel free to fork the repo and submit a pull request.

📜 License

This project is licensed under the MIT License.
