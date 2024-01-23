package com.example.firebasedatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.firebasedatabase.R
import com.example.firebasedatabase.dataClasses.MessageData
import com.example.firebasedatabase.dataClasses.NotificationData
import com.example.firebasedatabase.databinding.InboxTextViewBinding
import com.example.firebasedatabase.databinding.SentTextViewBinding
import com.example.firebasedatabase.enums.MessageItemEnums
import com.example.firebasedatabase.holders.InboxTextHolder
import com.example.firebasedatabase.holders.SentTextHolder
import com.example.firebasedatabase.`interface`.TextItemClickListener
import java.util.ArrayList


class ChatAdapter(val name: String) : RecyclerView.Adapter<ViewHolder>() {
    private var messageList = ArrayList<MessageData>()
    private var textItemClickListener: TextItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sender == name) {
            MessageItemEnums.SENT_MESSAGE_DATA.ordinal
        } else {
            MessageItemEnums.INBOX_MESSAGE_DATA.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            MessageItemEnums.SENT_MESSAGE_DATA.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sent_text_view, parent, false)
                SentTextHolder(SentTextViewBinding.bind(view))
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inbox_text_view, parent, false)
                InboxTextHolder(InboxTextViewBinding.bind(view))
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = messageList[position]
        when (holder) {
            is SentTextHolder -> {
                holder.bind(data)
                holder.setTextItemClickListener(textItemClickListener)
            }

            is InboxTextHolder -> {
                holder.apply {
                    bind(data)
                    setTextItemClickListener(textItemClickListener)
                }
            }
        }
    }

    fun setMessageList(list: List<MessageData>) {
        messageList = arrayListOf()
        messageList.addAll(list)
        notifyDataSetChanged()
    }

    fun setTextItemClickListener(listener: TextItemClickListener?) {
        textItemClickListener = listener
    }
}