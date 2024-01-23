package com.example.firebasedatabase.viewmodels


import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.firebasedatabase.constants.MESSAGES
import com.example.firebasedatabase.constants.NOTIFICATIONS
import com.example.firebasedatabase.constants.PERSON_INFO
import com.example.firebasedatabase.dataClasses.MessageData
import com.example.firebasedatabase.dataClasses.NotificationData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class FragmentChatViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val database = Firebase.database
    private val myMessagesRef = database.getReference(MESSAGES)
    private val myNotificationsRef = database.getReference(NOTIFICATIONS)
    val messageListFlow = MutableSharedFlow<List<MessageData>>()
    val notificationListFlow = MutableSharedFlow<NotificationData>()

    fun sendData(messageText: String) {
        val time = System.currentTimeMillis().toString()
        val notificationData = NotificationData(
            title = sharedPreferences.getString(PERSON_INFO, "").orEmpty(),
            text = messageText,
            sender = sharedPreferences.getString(PERSON_INFO, "").orEmpty()
        )
        val messageData = MessageData(
            sharedPreferences.getString(PERSON_INFO, "").orEmpty(),
            messageText,
            time
        )
        myMessagesRef.child(time).setValue(messageData)
        myNotificationsRef.setValue(notificationData)
    }

    fun getDataFromFb() {
        myMessagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = ArrayList<MessageData>()
                snapshot.children.forEach {
                    messageList.add(
                        MessageData(
                            it.child("sender").value as String,
                            it.child("text").value as String,
                            it.child("time").value as String
                        )
                    )
                }
                CoroutineScope(IO).launch {
                    messageListFlow.emit(messageList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        myNotificationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("sender")) {
                    val notificationData = NotificationData(
                        sender = snapshot.child("sender").value.toString(),
                        text = snapshot.child("text").value.toString(),
                        title = snapshot.child("title").value.toString()
                    )
                    CoroutineScope(IO).launch {
                        notificationListFlow.emit(notificationData)
                        myNotificationsRef.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun deleteMessage(messageData: MessageData) {
        myMessagesRef.child(messageData.time).removeValue()
    }

}