# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# wechat混淆
-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}
-keep public interface com.tencent.**

#阿里云一键登录混淆
-keep public class  R.drawable.authsdk*
-keep public class  R.layout.authsdk*
-keep public class  R.anim.authsdk*
-keep public class  R.id.authsdk*
-keep public class  R.string.authsdk*
-keep public class  R.style.authsdk*
-keep class org.json.** {
   *;
}
#微博混淆
-keep class com.sina.** {*;}
-dontwarn com.sina.**
#支付宝混淆
-keep class  com.alipay.share.sdk.** {
   *;
}
-keep class com.aidong.loginshare.bean.*