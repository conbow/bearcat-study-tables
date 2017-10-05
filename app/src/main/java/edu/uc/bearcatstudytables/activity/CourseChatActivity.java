package edu.uc.bearcatstudytables.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.databinding.ActivityCourseChatBinding;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;

public class CourseChatActivity extends BaseActivity {

    private static final String TAG = "CourseChatActivity";

    public final static String KEY_COURSE_ID = "COURSE_ID";
    public final static String KEY_COURSE_NAME = "COURSE_NAME";

    private String mCourseId;
    private ActivityCourseChatBinding mBinding;
    private ChatMessageDTO mChatMessage;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(KEY_COURSE_NAME));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_course_chat);
        mChatMessage = new ChatMessageDTO();
        mBinding.setChatMessage(mChatMessage);
        mCourseId = getIntent().getStringExtra(KEY_COURSE_ID);
        initializeChatMessageList();
    }

    private void initializeChatMessageList() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("course-chat/" + mCourseId)
                .limitToLast(50);

        FirebaseRecyclerOptions<ChatMessageDTO> options =
                new FirebaseRecyclerOptions.Builder<ChatMessageDTO>()
                        .setQuery(query, ChatMessageDTO.class)
                        .build();

        mAdapter = new FirebaseRecyclerAdapter<ChatMessageDTO, CourseChatMessageViewHolder>(options) {
            @Override
            public CourseChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CourseChatMessageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_chat_message, parent, false));
            }

            @Override
            protected void onBindViewHolder(CourseChatMessageViewHolder viewHolder, int position,
                                            final ChatMessageDTO model) {
                viewHolder.bind(mUser, model);
            }
        };
        mRecyclerView = findViewById(R.id.course_chat_message_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    public void onSendMessageButtonClick(final View view) {
        // Check input validation and attempt to send message
        if (!mChatMessage.getMessage().isEmpty()) {
            mChatMessage.setDate(new Date());
            mChatMessage.setCourseId(mCourseId);
            mChatMessage.setFrom(mUser);
            mDatabase.getReference().child("course-chat/" + mCourseId).push().setValue(mChatMessage)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                scrollToBottom();
                                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                            } else {
                                Snackbar.make(view,
                                        getString(R.string.error_sending_message), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
        scrollToBottom();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private static class CourseChatMessageViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout mChatMessageContainer;
        private final FrameLayout mChatMessageLeft;
        private final FrameLayout mChatMessageRight;
        private final TextView mChatMessage;

        private CourseChatMessageViewHolder(View view) {
            super(view);
            mChatMessageContainer = view.findViewById(R.id.chat_message_container);
            mChatMessage = view.findViewById(R.id.chat_message);
            mChatMessageLeft = view.findViewById(R.id.chat_message_left);
            mChatMessageRight = view.findViewById(R.id.chat_message_right);
        }

        private void bind(UserDTO currentUser, ChatMessageDTO chatMessage) {
            boolean isFromCurrentUser = chatMessage.getFrom().getEmail().equals(currentUser
                    .getEmail());

            String userName = chatMessage.getFrom().getName();
            String messagePrefix = !userName.isEmpty() ? userName + ": " : "";
            setChatMessage(isFromCurrentUser, messagePrefix + chatMessage.getMessage());
        }

        private void setChatMessage(boolean isFromCurrentUser, String chatMessage) {
            // Set chat bubble to the left or right
            if (isFromCurrentUser) {
                mChatMessageContainer.setGravity(Gravity.RIGHT);
                mChatMessageRight.setVisibility(View.GONE);
            } else {
                mChatMessageContainer.setGravity(Gravity.LEFT);
                mChatMessageLeft.setVisibility(View.GONE);
            }

            // Set chat bubble text
            mChatMessage.setText(chatMessage);
        }
    }
}
