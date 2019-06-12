package com.example.androidworkmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class SendWorker(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    override fun doWork(): Result {

        // write the code to fetch the data
        val taskData: Data = inputData
        val taskDataString: String? = taskData.getString(MainActivity.MESSAGE_STATUS)

        showNotification("Work Manager", taskDataString ?: "Message Sent")

        // this output data will be fetch in MainActivity class
        val outputData: Data = Data.Builder().putString(WORK_RESULT,"Task Finished").build()

        return Result.success(outputData)
    }

    // create a function for generating a notification
    private fun showNotification(task: String, desc: String) {
        val manager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "message_channel"
        val channelName = "message_name"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)

        manager.notify(1, builder.build())
    }

    companion object {
        const val WORK_RESULT = "work_result"
    }
}