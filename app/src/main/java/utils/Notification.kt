package utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.teabuddy.R

object NotificationHelper {
    private const val CHANNEL_ID = "tea_buddy_channel"
    private const val CHANNEL_NAME = "Tea Buddy Notifications"
    private const val NOTIFICATION_ID = 1

    fun sendNotification(context: Context, title: String, content: String) {
        //Check premission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Notification is unavailable", Toast.LENGTH_SHORT).show()
            return
        }

        // NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Сповіщення від Tea Buddy"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        //Create and send Notif
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

            .setSmallIcon(R.drawable.notif_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT )
//            .setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notification_sound))
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }


}
