# Pixoji - Emoji Mosaic Converter

## Project Overview

Pixoji is a complete Android application demonstrating best practices in:
- Jetpack Compose 100% UI implementation
- MVVM + Clean Architecture
- Kotlin Coroutines & StateFlow
- Advanced image processing with color algorithms
- Background processing with WorkManager
- Persistent data with DataStore
- Scoped Storage (MediaStore) compliance
- Material Design 3

## Building

### Prerequisites
- Android Studio Giraffe or newer
- JDK 17
- SDK min 26, target 34

### Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

## Project Structure

```
Pixoji/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/mashalmazen/pixoji/
│   │   │   ├── domain/          # Business logic & models
│   │   │   ├── data/            # Data sources & repositories
│   │   │   └── presentation/    # UI & ViewModels
│   │   ├── res/                 # Resources
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
└── settings.gradle.kts
```

## Key Implementation Details

### Image Processing
- Scales image to grid dimensions
- Calculates average RGB per block
- Matches colors using Euclidean distance or LAB color space
- Renders high-res emoji mosaic on off-screen canvas
- Supports cancellation with coroutine Job management

### Settings Persistence
- DataStore preferences for all user settings
- Immediate updates without database overhead
- Type-safe configuration with Flow collectors

### Background Processing
- WorkManager for long-running tasks
- Foreground service with progress notifications
- Persistent system notifications with progress bar
- Cancellable through UI interaction

### UI Architecture
- Single Activity with Compose navigation
- ViewModel-driven state management
- Bottom Sheet for settings
- Before/After comparison ready
- Loading, success, and error states

## Features Checklist

- [x] Dynamic permission requests
- [x] Dark theme with pure black background
- [x] Persistent settings with DataStore
- [x] Before/After image comparison UI
- [x] Cancellable operations with Job management
- [x] Background processing with WorkManager
- [x] MediaStore export to gallery
- [x] Settings panel with 5+ options
- [x] MVVM architecture
- [x] 100% Compose UI
- [x] LAB color space matching
- [x] Emoji category filtering
- [x] High-resolution output
- [x] Progress indication
- [x] Error handling

## Dependencies

All dependencies are defined in `app/build.gradle.kts`:
- Jetpack Compose 1.5.0
- Material Design 3 1.1.1
- Lifecycle 2.6.1
- WorkManager 2.8.1
- DataStore 1.0.0
- Accompanist Permissions 0.32.0
- Coil Image Loading 2.4.0
- Kotlinx Serialization 1.5.1

## Testing

Unit tests can be added to `app/src/test/` for:
- Color matching algorithms
- LAB color space conversion
- Settings DataStore operations
- Image processor logic

UI tests can be added to `app/src/androidTest/` using:
- Compose testing framework
- Espresso for legacy component testing

## Performance

- Efficient bitmap operations with proper recycling
- Emoji database cached in memory
- Coroutine-based non-blocking UI
- Optimized PNG compression for output
- Off-screen canvas rendering for quality

## Future Roadmap

- Video to mosaic conversion
- Real-time preview
- Batch processing
- Cloud storage integration
- Advanced filters
- Social media sharing
- Custom emoji selection

## License

MIT License

## Author

Mashalmazen - 2024
