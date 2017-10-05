package edu.uc.bearcatstudytables.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.databinding.ActivityCourseChatBinding;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;

public class CourseChatActivity extends BaseActivity {

    private static final String TAG = "CourseChatActivity";

    public final static String KEY_COURSE_ID = "COURSE_ID";
    public final static String KEY_COURSE_NAME = "COURSE_NAME";

    private String mCourseId;
    private ActivityCourseChatBinding mBinding;
    private ChatMessageDTO mChatMessage;
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
                .child("course-chat")
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
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                viewHolder.bind(model);
            }
        };
        RecyclerView recyclerView = findViewById(R.id.course_chat_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    public void onSendMessageButtonClick(final View view) {
        // Check input validation and attempt to create course
        if (!mChatMessage.getMessage().isEmpty()) {
            mDatabase.getReference().child("course-chat").push().setValue(mChatMessage)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mBinding.chatMessage.setText("");
                            } else {
                                Snackbar.make(view,
                                        getString(R.string.error_course_create), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        } else {
            Log.d(TAG, "error sending chat message");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private static class CourseChatMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView mUserName;
        private final TextView mChatMessage;

        private CourseChatMessageViewHolder(View view) {
            super(view);
            mUserName = view.findViewById(R.id.chat_message);
            //mUserName = view.findViewById(R.id.from_name);
            mChatMessage = view.findViewById(R.id.chat_message);
        }

        private void bind(ChatMessageDTO chatMessage) {
            //setUserName(chatMessage.getFrom().getEmail());
            setUserName("test");
            setChatMessage(chatMessage.getMessage());
        }

        private void setUserName(String userName) {
            mUserName.setText(userName);
        }

        private void setChatMessage(String chatMessage) {
            mChatMessage.setText(chatMessage);
        }
    }
}
