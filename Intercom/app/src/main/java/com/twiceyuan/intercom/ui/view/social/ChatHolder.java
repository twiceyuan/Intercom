package com.twiceyuan.intercom.ui.view.social;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twiceyuan.intercom.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 每条聊天消息的显示
 */
public class ChatHolder extends RecyclerView.ViewHolder {

    public TextView        messageTextView;
    public TextView        messengerTextView;
    public CircleImageView messengerImageView;

    public TextView        messageTextViewSelf;
    public TextView        messengerTextViewSelf;
    public CircleImageView messengerImageViewSelf;

    public LinearLayout messageLayout;
    public LinearLayout messageLayoutSelf;

    public TextView messageCreateAt;

    public ChatHolder(View v) {
        super(v);

        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
        messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);

        messageTextViewSelf = (TextView) itemView.findViewById(R.id.messageTextViewSelf);
        messengerTextViewSelf = (TextView) itemView.findViewById(R.id.messengerTextViewSelf);
        messengerImageViewSelf = (CircleImageView) itemView.findViewById(R.id.messengerImageViewSelf);

        messageLayout = (LinearLayout) itemView.findViewById(R.id.messageLayout);
        messageLayoutSelf = (LinearLayout) itemView.findViewById(R.id.messageLayoutSelf);

        messageCreateAt = (TextView) itemView.findViewById(R.id.messageCreateAt);
    }
}