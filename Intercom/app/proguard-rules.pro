# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/twiceYuan/AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# Material EditText
-keep class com.rengwuxian.materialedittext.** {*;}

-keepattributes InnerClasses

# common adapter
-keepattributes *Annotation*
-keepclassmembers class * extends com.twiceyuan.commonadapter.library.holder.CommonHolder {
    public <init>(...);
}

# android support
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

# firebase
-keepattributes Signature
-keep class com.google.firebase.** { *; }
-keep class com.firebase.** { *; }
-keep interface com.google.firebase.** { *; }
-keep class com.twiceyuan.intercom.model.local.** { *; }
-keepclassmembers class * extends android.support.v7.widget.RecyclerView$ViewHolder {
  *;
}

-keep class com.google.** { *; }
# google service
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
  public static final *** NULL;
}

-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final *** CREATOR;
}

-keep @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <methods>;
}

-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keep @interface com.google.android.gms.common.util.DynamiteApi
-keep public @com.google.android.gms.common.util.DynamiteApi class * {
  public <fields>;
  public <methods>;
}

-dontwarn android.security.NetworkSecurityPolicy

# Rx
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keep class rx.** { *; }
-dontwarn java.lang.invoke.*
-dontwarn rx.internal.**
-dontwarn java.io.**