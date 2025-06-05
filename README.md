# 📝 Notes App

A modern Android notes application built with **Jetpack Compose** and **Room Database**. Create, organize, and secure your notes with a beautiful Material Design 3 interface.

## ✨ Features

- 📝 **Create & Edit Notes** - Rich text input with title and description
- 🔍 **Smart Search** - Find notes instantly by title or content
- 🔒 **Password Protection** - Secure sensitive notes with custom passwords
- ⭐ **Favorites System** - Star important notes for quick access
- 🎨 **Colorful Interface** - Each note gets a unique background color
- 🌙 **Dark Theme** - Eye-friendly dark mode design
- ⚡ **Real-time Updates** - Automatic timestamps and live filtering

## 📱 Screenshots

<table>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/b980d0a9-2bef-41df-9c30-4a325dc4a876" 
           alt="View Note" width="250" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/7c9a91ed-e898-4726-b3bc-2f67ba0e971a" 
           alt="Add Note" width="250" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/c5c29f61-23c6-44a2-8c04-5d57ab6b2ac0" 
           alt="Home Screen" width="250" />
    </td>
    
    
  </tr>
  <tr>
    <td align="center"><b>Home Screen</b></td>
    <td align="center"><b>Add Note</b></td>
    <td align="center"><b>Locked Note</b></td>
  </tr>
</table>

## 🛠️ Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Room Database** - Local data persistence
- **Navigation Compose** - Screen navigation
- **MVVM Architecture** - Clean architecture pattern
- **Coroutines** - Asynchronous programming
- **StateFlow** - Reactive state management

## 🏗️ Architecture

```
📁 app/
├── 📁 data/           # Database, entities, repository, Room setup
├── 📁 navigation/     # Navigation components and screen routing
├── 📁 screens/        # Compose UI screens (Home, Add, Edit, View)
├── 📁 ui/             # UI components and theme
├── 📁 uistates/       # UI state management classes
├── 📁 viewmodels/     # ViewModels for business logic
└── MainActivity.kt    # Application entry point
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Android SDK 24+
- Kotlin 1.8+

### Quick Start
```bash
# Clone the repository
git clone https://github.com/yourusername/notes-app.git

# Open in Android Studio
cd notes-app

# Build and run
./gradlew build
```

### Key Dependencies
```kotlin
// Compose & Material 3
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.material3:material3:1.1.2"

// Room Database
implementation "androidx.room:room-ktx:2.5.0"

// Navigation
implementation "androidx.navigation:navigation-compose:2.7.4"
```

## 📖 How to Use

1. **Create Notes**: Tap the ➕ button to add new notes
2. **Search**: Use the search bar to find specific notes
3. **Secure Notes**: Toggle password protection in note settings
4. **Organize**: Star important notes for quick access
5. **Edit**: Tap any note to view or edit its content

## 🔐 Security Features

- **Password Protection**: Lock sensitive notes with custom passwords
- **Temporary Unlock**: Access locked notes temporarily during session
- **Smart Filtering**: Search only shows accessible notes

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Support

If you like this project, please consider:
- ⭐ Starring the repository
- 🐛 Reporting issues
- 💡 Suggesting new features
- 🤝 Contributing code


<div align="center">
  <b>Made with ❤️ using Jetpack Compose</b>
</div>
