package com.twiceyuan.intercom.ui.view.social;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.twiceyuan.intercom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by twiceYuan on 8/5/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 会话 ViewHolder
 */
public class ConversationHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.peerImageView)
    public CircleImageView peerImageView;

    @BindView(R.id.messengerTextView)
    public TextView messengerTextView;

    @BindView(R.id.messageTextView)
    public TextView messageTextView;

    public ConversationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
