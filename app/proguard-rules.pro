# ProGuard rules for Pixoji
# Disable obfuscation for better debugging
-dontobfuscate

# Keep all application classes
-keep class com.mashalmazen.pixoji.** { *; }

# Keep all Compose classes
-keep class androidx.compose.** { *; }

# Keep ViewModel classes
-keep class androidx.lifecycle.ViewModel { *; }
-keepclasseswithmembernames class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep Serialization classes
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * extends java.io.Serializable {
    <fields>;
    <methods>;
}

# Keep DataStore classes
-keep class androidx.datastore.** { *; }

# Keep WorkManager classes
-keep class androidx.work.** { *; }

# Keep Material Design classes
-keep class androidx.compose.material3.** { *; }
-keep class com.google.android.material.** { *; }

# Preserve line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep entry points
-keep public class com.mashalmazen.pixoji.presentation.MainActivity {
    <init>(...);  
}

# Keep service workers
-keep public class com.mashalmazen.pixoji.data.worker.** {
    <init>(...);  
}

# Preserve annotations
-keepattributes *Annotation*
-keep @interface *
-keep @com.mashalmazen.pixoji.** class * { *; }
