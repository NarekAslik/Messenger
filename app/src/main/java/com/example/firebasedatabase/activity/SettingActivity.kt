package com.example.firebasedatabase.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedatabase.constants.NOTIFICATION_INTENT_KEY
import com.example.firebasedatabase.dataClasses.NotificationData
import com.example.firebasedatabase.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getSerializableExtra(NOTIFICATION_INTENT_KEY) as NotificationData
        binding.apply {
            senderNameText.text = result.sender
            contentText.text = result.text
        }
    }
}