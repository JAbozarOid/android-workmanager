package com.example.androidworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // we want to create a request that execute once
        val workManager: WorkManager = WorkManager.getInstance()

        // we want to send the notification when the device is charging -> for this position we use constraint class , after that we should this to the "Request"
        val powerConstraint: Constraints = Constraints.Builder().setRequiresCharging(true).build()

        // set data to pass to our task
        val taskData: Data = Data.Builder().putString(MESSAGE_STATUS , "Message Sent Successfully").build()

        val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(SendWorker::class.java)
            .setConstraints(powerConstraint)
            .setInputData(taskData)
            .build()

        btnSend.setOnClickListener {
            workManager.enqueue(request)
        }

        workManager.getWorkInfoByIdLiveData(request.id).observe(this, Observer { workInfo ->
            workInfo?.let {

                if (it.state.isFinished){
                    val outputData: Data = it.outputData
                    val taskResult: String? = outputData.getString(SendWorker.WORK_RESULT)
                    tvStatus.append(taskResult + "\n")
                }

                val state: WorkInfo.State = workInfo.state
                tvStatus.append(state.toString() + "\n")
            }
        })
    }

    companion object {
        const val MESSAGE_STATUS = "message_status"
    }
}
