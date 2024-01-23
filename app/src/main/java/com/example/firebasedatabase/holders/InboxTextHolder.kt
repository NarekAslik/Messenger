package com.example.firebasedatabase.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedatabase.dataClasses.MessageData
import com.example.firebasedatabase.dataClasses.NotificationData
import com.example.firebasedatabase.databinding.InboxTextViewBinding
import com.example.firebasedatabase.`interface`.TextItemClickListener

class InboxTextHolder(private val binding: InboxTextViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var textItemClickListener: TextItemClickListener? = null
    fun bind(messageData: MessageData) {
        binding.apply {
            inboxText.text = messageData.text
            inboxTextSender.text = messageData.sender
            inboxText.setOnLongClickListener {
                textItemClickListener?.deleteItem(messageData)
                true
            }
        }
    }

    fun setTextItemClickListener(listener: TextItemClickListener?) {
        textItemClickListener = listener
    }
}