package com.example.weiboxx.ui.message
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weiboxx.R
import com.example.weiboxx.data.model.Message

class MessageAdapter(
    private val context: Context,
    private var messageList: MutableList<Message> = mutableListOf()
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var onMessageClickListener: OnMessageClickListener? = null

    interface OnMessageClickListener {
        fun onMessageClick(message: Message, position: Int)
    }

    fun setOnMessageClickListener(listener: OnMessageClickListener) {
        this.onMessageClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message, position)
    }

    override fun getItemCount(): Int = messageList.size

    fun updateMessages(newMessages: List<Message>) {
        messageList.clear()
        messageList.addAll(newMessages)
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        private val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        private val imageViewArrow: ImageView = itemView.findViewById(R.id.imageViewArrow)
        private val imageViewVerified: ImageView = itemView.findViewById(R.id.imageViewVerified)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMessageClickListener?.onMessageClick(messageList[position], position)
                }
            }
        }

        fun bind(message: Message, position: Int) {
            textViewTitle.text = message.title
            textViewContent.text = message.content
            textViewTime.text = message.time

            // 设置头像
            setupAvatar(message)

            // 设置认证标识
            imageViewVerified.visibility = if (message.isVerified) View.VISIBLE else View.GONE
        }

        private fun setupAvatar(message: Message) {
            // 创建圆形背景
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(message.getAvatarBackgroundColor())
            }

            imageViewAvatar.background = drawable

            // 根据消息类型设置不同的图标
            val iconRes = getAvatarIconRes(message.type)
            if (iconRes != 0) {
                imageViewAvatar.setImageResource(iconRes)
                imageViewAvatar.scaleType = ImageView.ScaleType.CENTER
            } else if (message.avatarResId != 0) {
                imageViewAvatar.setImageResource(message.avatarResId)
                imageViewAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        private fun getAvatarIconRes(type: Message.MessageType): Int {
            return when (type) {
                Message.MessageType.PERSONAL -> R.drawable.ic_at // @ 符号
                Message.MessageType.COMMENT -> R.drawable.ic_comment2 // 评论图标
                Message.MessageType.LIKE -> R.drawable.ic_like2 // 点赞图标
                Message.MessageType.NEWS -> R.drawable.ic_sina_logo // 新浪logo
                Message.MessageType.NOTIFICATION -> R.drawable.ic_notification // 通知图标
                Message.MessageType.GROUP -> R.drawable.ic_group // 群组图标
                Message.MessageType.LIVE -> R.drawable.ic_live // 直播图标
                Message.MessageType.STORY -> R.drawable.ic_story // 小秘书图标
                Message.MessageType.SCHOOL -> R.drawable.ic_school // 学校图标
            }
        }
    }
}