# Add project specific ProGuard rules here.
-keep class com.marcingantkowski.tolkienquizapp.domain.model.** { *; }
-keep class com.marcingantkowski.tolkienquizapp.data.remote.dto.** { *; }

# Keep Composables
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class * implements androidx.compose.ui.tooling.preview.PreviewParameterProvider

# Keep Hilt generated classes
-keep class * extends dagger.hilt.internal.aggregatedroot.AggregatedRoot
-keep class * extends dagger.hilt.internal.processedrootsentinel.ProcessedRootSentinel
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager.FragmentComponentBuilder

# Keep Retrofit/Gson
-keep class retrofit2.Call
-keepclasseswithmembers,allowshrinking class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Room entities and converters
-keep class androidx.room.TypeConverter { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static final java.lang.String NAME;
}
-keepclassmembers class **.R$* {
    public static final int[] <fields>;
}
