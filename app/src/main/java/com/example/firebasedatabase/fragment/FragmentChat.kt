package com.example.firebasedatabase.fragment

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.PendingIntentCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasedatabase.R
import com.example.firebasedatabase.activity.SettingActivity
import com.example.firebasedatabase.adapter.ChatAdapter
import com.example.firebasedatabase.constants.DATABASE_NAME
import com.example.firebasedatabase.constants.NOTIFICATION_CHANNEL_ID
import com.example.firebasedatabase.constants.NOTIFICATION_ID
import com.example.firebasedatabase.constants.NOTIFICATION_INTENT_KEY
import com.example.firebasedatabase.constants.PERSON_INFO
import com.example.firebasedatabase.dataClasses.MessageData
import com.example.firebasedatabase.dataClasses.NotificationData
import com.example.firebasedatabase.databinding.ChatFragmentBinding
import com.example.firebasedatabase.`interface`.TextItemClickListener
import com.example.firebasedatabase.viewmodels.ChatFactoryViewModel
import com.example.firebasedatabase.viewmodels.FragmentChatViewModel
import kotlinx.coroutines.flow.collectLatest

@Suppress("DEPRECATION")
class FragmentChat : Fragment(R.layout.chat_fragment), TextItemClickListener {

    private lateinit var binding: ChatFragmentBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: FragmentChatViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatFragmentBinding.bind(view)

        viewModelInitialization()
        createNotificationChannel()
        getSHaredPreferences()
        adapterInitialization()
        getDataBaseInfo()
        sendDataToFireBase()
        setRcViewComponents()
        chatAdapter.setTextItemClickListener(this)
    }

    private fun getSHaredPreferences() {
        sharedPreferences =
            requireContext().getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun viewModelInitialization() {
        viewModel = ViewModelProvider(
            this,
            ChatFactoryViewModel(
                requireContext().getSharedPreferences(
                    DATABASE_NAME,
                    Context.MODE_PRIVATE
                )
            )
        )[FragmentChatViewModel::class.java]
    }

    private fun adapterInitialization() {
        chatAdapter = ChatAdapter(sharedPreferences.getString(PERSON_INFO, "").orEmpty())
    }

    private fun getDataBaseInfo() {
        viewModel.getDataFromFb()
        lifecycleScope.launchWhenStarted {
            viewModel.apply {
                messageListFlow.collectLatest {
                    chatAdapter.setMessageList(it)
                    binding.apply {
                        chatRcView.apply {
                            if (chatAdapter.itemCount == 0) {
                                stopScroll()
                            } else {
                                smoothScrollToPosition(it.lastIndex)
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.notificationListFlow.collectLatest {
                sendNotification(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sendDataToFireBase() {
        binding.apply {
            sendImView.setOnClickListener {
                viewModel.sendData(messageEdText.text.toString())
                messageEdText.setText("")
            }
        }
    }

    private fun setRcViewComponents() {
        binding.chatRcView.apply {
            adapter = chatAdapter
            layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    override fun deleteItem(messageData: MessageData) {
        viewModel.deleteMessage(messageData)
    }

    private fun createNotificationChannel() {
        val name = "Messenger Notifications"
        val descriptionText = "Allow access"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(notificationData: NotificationData) {
        if (notificationData.sender != sharedPreferences.getString(PERSON_INFO, "")) {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            intent.putExtra(NOTIFICATION_INTENT_KEY, notificationData)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val pendingIntent = PendingIntentCompat.getActivity(
                requireActivity().applicationContext,
                1,
                intent,
                PendingIntent.FLAG_ONE_SHOT,
                false
            )
            val builder = Notification.Builder(requireContext(), NOTIFICATION_CHANNEL_ID)
                .setContentText(notificationData.text)
                .setContentTitle(notificationData.title)
                .setSmallIcon(R.drawable.messenger_icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(requireActivity())) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity().applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        NOTIFICATION_ID
                    )
                } else {
                    notify(System.currentTimeMillis().toInt(), builder.build())
                }
            }
        }
    }
}