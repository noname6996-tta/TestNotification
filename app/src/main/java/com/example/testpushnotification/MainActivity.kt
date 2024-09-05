package com.example.testpushnotification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val NOTIFICATION_PERMISSION_REQUEST_CODE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Với android 13 trở leen thì cần thêm quyền
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
        // Kiểm tra intent để xử lý thông báo khi ứng dụng được khởi chạy từ thông báo
        handleIntent(intent)

        // Đăng ký FCM token (không bắt buộc, nhưng hữu ích để kiểm tra)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Xử lý intent mới khi ứng dụng đang chạy và nhận được thông báo
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        // Kiểm tra nếu intent có dữ liệu từ thông báo
        val data = intent.extras
        data?.let {
            val title = it.getString("title")
            val message = it.getString("message")

            // Xử lý dữ liệu thông báo (ví dụ: hiển thị trong TextView hoặc mở một Fragment cụ thể)
            Log.d("FCM", "Notification Title: $title, Message: $message")
        }
    }
}