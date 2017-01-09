#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------1.实体类---------------------------------

-keep class com.truthso.ip360.bean.** { *; }
#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

-dontwarn android.support.**
-dontwarn com.alibaba.fastjson.**
-dontwarn com.alibaba.sdk.**
-dontwarn com.baidu.location.**
-dontwarn okio.**
-dontwarn com.loop.android.**
-dontwarn com.alipay.**
-dontwarn com.megvii.livenessdetection.**
-dontwarn com.nineoldandroids.**
-dontwarn okhttp3.**
-dontwarn com.nostra13.universalimageloader.**
-dontwarn  com.lidroid.xutils.**
-dontwarn net.tsz.afinal.**


-keep class com.loop.android.** {*;}
-keep class com.alibaba.fastjson.** { *; }
-keep class com.alibaba.sdk.** { *; }

-keep class com.baidu.location.** { *; }
-keep class okio.Okio.** { *; }
-keep class com.alipay.** { *; }
-keep class com.megvii.livenessdetection.** { *; }
-keep class com.nineoldandroids.** { *; }
-keep class okhttp3.** { *; }
-keep class com.nostra13.universalimageloader.** { *; }
-keep class com.lidroid.xutils.** { *; }
-keep class net.tsz.afinal.** { *; }