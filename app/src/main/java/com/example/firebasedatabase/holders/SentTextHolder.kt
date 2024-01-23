package com.example.firebasedatabase.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedatabase.dataClasses.MessageData
import com.example.firebasedatabase.databinding.SentTextViewBinding
import com.example.firebasedatabase.`interface`.TextItemClickListener

class SentTextHolder(private val binding: SentTextViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var textItemClickListener: TextItemClickListener? = null
    fun bind(messageData: MessageData) {
        binding.apply {
            sentText.text = messageData.text
            sentTextSender.text = messageData.sender
            sentText.setOnLongClickListener {
                textItemClickListener?.deleteItem(messageData)
                true
            }
        }
    }

    fun setTextItemClickListener(listener: TextItemClickListener?) {
        textItemClickListener = listener
    }
}