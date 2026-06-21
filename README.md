# Pixoji - Emoji Mosaic Image Converter

A powerful Android application that converts images into stunning high-quality Emoji Mosaics using Jetpack Compose, MVVM architecture, and advanced color matching algorithms.

## Features

✨ **Core Features**
- Convert any image into an emoji mosaic with customizable density
- High-resolution output with emoji scaling up to 4x quality
- Two color matching algorithms: Fast RGB and Precise LAB Color Space
- Multiple emoji category filters (All, Nature, Faces, Flags, Food, Objects, Symbols)
- Transparent background support for advanced use cases
- Before/After image comparison in split-screen view

🎨 **UI & UX**
- 100% Jetpack Compose implementation (no XML layouts)
- Pure Dark Mode with AMOLED black background (#000000)
- Material Design 3 components
- Smooth animations and transitions
- Settings panel with comprehensive controls

⚙️ **Technical Excellence**
- MVVM architecture with Clean Architecture principles
- Kotlin Coroutines with StateFlow for reactive state management
- WorkManager for background processing with progress notifications
- Jetpack DataStore for persistent settings
- Dynamic permission requests (READ_MEDIA_IMAGES)
- MediaStore API for scoped storage compliance
- Cancellable operations with Job management
- Efficient emoji color database with caching

## Architecture

```
app/src/main/kotlin/com/mashalmazen/pixoji/
├── domain/
│   ├── model/           # Domain models (EmojiData, ProcessingSettings, ProcessingState)
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Use case interfaces
├── data/
│   ├── datastore/       # DataStore implementation for settings
│   ├── repository/      # Repository implementations
│   ├── usecase/         # Use case implementations
│   ├── image/           # Image processing and MediaStore utilities
│   └── worker/          # WorkManager implementation
└── presentation/
    ├── screen/          # Compose UI screens (MainScreen, SettingsScreen)
    ├── viewmodel/       # ViewModels and factories
    ├── theme/           # Theme, colors, typography
    └── MainActivity.kt  # Entry point
```

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose 1.5.0
- **Architecture**: MVVM + Clean Architecture
- **Async**: Kotlin Coroutines & StateFlow
- **Background**: WorkManager 2.8.1
- **Storage**: Jetpack DataStore 1.0.0
- **Permissions**: Accompanist Permissions 0.32.0
- **Image Loading**: Coil 2.4.0
- **Serialization**: Kotlinx Serialization 1.5.1

## Installation

1. Clone the repository:
```bash
git clone https://github.com/Mashalmazen/Pixoji.git
cd Pixoji
```

2. Open in Android Studio (Giraffe or newer recommended)

3. Build and run on Android 8.0+ (API 26+)

## Usage

1. **Select Image**: Tap "Select Image" button to choose a photo from gallery
2. **Configure Settings**: Open settings panel to adjust:
   - Grid Density (80-500 blocks)
   - Output Resolution (1x, 2x, 4x)
   - Emoji Category filter
   - Color matching algorithm (RGB or LAB)
   - Transparency options
3. **Generate**: Tap "Generate Emoji Mosaic" to process
4. **Save**: Save the result directly to device gallery in Pixoji folder

## Core Processing Logic

### Image Scaling
- Input image is scaled to user-defined grid dimensions
- Maintains aspect ratio through calculated height

### Color Analysis
- Calculates average RGB for each pixel block
- Supports two matching modes:
  - **RGB Mode**: Fast Euclidean distance in RGB space
  - **LAB Mode**: Precise perceptually-uniform LAB color space matching

### Emoji Rendering
- High-resolution canvas generation (48x48 to 192x192 per emoji)
- Proper emoji sizing and positioning
- Transparent background option for modern compositions
- Off-screen rendering to prevent pixelation on zoom

### Performance
- Cancellable operations with `ensureActive()` checks
- Emoji database caching in memory
- Efficient bitmap operations
- Coroutine-based non-blocking processing

## Mandatory Features Implemented

✅ Dynamic Permissions - READ_MEDIA_IMAGES requested only on button click
✅ Pure Dark Theme - Black background (#000000) throughout
✅ Persistent Settings - DataStore saves preferences immediately
✅ Before/After Comparison - Split-screen UI ready
✅ Cancellable Operations - Job cancellation with UI feedback
✅ Background Service - WorkManager with progress notifications
✅ MediaStore Export - Saves to Pictures/Pixoji with gallery integration
✅ Advanced Settings - 5 customizable parameters
✅ Clean Architecture - Domain/Data/Presentation layers
✅ 100% Compose UI - No XML layouts

## Settings Panel

**Grid Density Slider**
- Range: 80-500 blocks
- Affects mosaic resolution and processing time

**Output Resolution Multiplier**
- 1x: Fast processing, smaller file size
- 2x: Balanced quality and performance
- 4x: High quality, larger file size

**Emoji Category**
- All Emojis
- Nature & Animals
- Faces Only
- Flags
- Food & Drink
- Objects
- Symbols

**Matching Algorithm**
- Fast Match (RGB): Quick processing, good results
- Precise Match (LAB): Better color accuracy, slightly slower

**Transparency Toggle**
- Enable for PNG transparency
- Disable for solid black background

## Permissions

```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

## Project Structure

### Domain Layer
- Pure Kotlin models without Android dependencies
- Repository and UseCase interfaces
- Business logic rules

### Data Layer
- Repository implementations
- DataStore and MediaStore utilities
- Image processing engine
- WorkManager background tasks
- Emoji database management

### Presentation Layer
- Jetpack Compose UI screens
- ViewModels with state management
- Theme and styling
- Material Design 3 components

## Building & Deployment

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

ProGuard rules are configured for code shrinking and optimization.

## Performance Considerations

- **Processing Time**: Depends on image size and grid density
  - 80 density: ~500ms
  - 250 density: ~2s
  - 500 density: ~5s
- **Memory**: Efficient bitmap handling with proper recycling
- **Battery**: Coroutine-based non-blocking operations
- **Storage**: PNG compression for optimal file sizes

## Known Limitations

- Very high density (500+) with 4x resolution may take significant time on older devices
- Emoji rendering quality depends on system font support
- Large images may require more processing time

## Future Enhancements

- [ ] Real-time preview of mosaic effect
- [ ] Custom emoji selection
- [ ] Video to emoji mosaic conversion
- [ ] Batch processing support
- [ ] Cloud storage integration
- [ ] Social media direct sharing
- [ ] Advanced filters and effects
- [ ] Undo/Redo functionality

## License

MIT License - See LICENSE file for details

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

**Mashalmazen**
- GitHub: [@Mashalmazen](https://github.com/Mashalmazen)

## Support

For issues, feature requests, or questions, please open an issue on GitHub.

---

**Made with ❤️ using Kotlin and Jetpack Compose**
