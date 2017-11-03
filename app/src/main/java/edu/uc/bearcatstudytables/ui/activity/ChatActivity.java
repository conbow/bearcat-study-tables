package edu.uc.bearcatstudytables.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.util.Date;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.ChatMessageDAO;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityChatBinding;
import edu.uc.bearcatstudytables.databinding.ListItemChatMessageBinding;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;

public class ChatActivity extends BaseActivity {

    private static final String TAG = "ChatActivity";

    public final static String KEY_CHAT_ID = "CHAT_ID";
    public final static String KEY_CHAT_NAME = "CHAT_NAME";

    private String mCourseId;
    private ActivityChatBinding mBinding;
    private ChatMessageDTO mChatMessage;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(KEY_CHAT_NAME));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        mChatMessage = new ChatMessageDTO();
        mBinding.setChatMessage(mChatMessage);
        mCourseId = getIntent().getStringExtra(KEY_CHAT_ID);
        initializeChatMessageList();
    }

    private void initializeChatMessageList() {

        Query query = ChatMessageDAO.getReference(mCourseId).limitToLast(50);

        FirebaseRecyclerOptions<ChatMessageDTO> options =
                new FirebaseRecyclerOptions.Builder<ChatMessageDTO>()
                        .setQuery(query, ChatMessageDTO.class)
                        .build();

        mAdapter = new FirebaseRecyclerAdapter<ChatMessageDTO, ChatMessageViewHolder>(options) {
            @Override
            public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                ListItemChatMessageBinding binding = ListItemChatMessageBinding
                        .inflate(layoutInflater, parent, false);
                return new ChatMessageViewHolder(binding);
                //binding.setUser();
                //return ListItemChatMessageBinding.inflate(LayoutInflater.from(parent.getContext());
                /*
                return new ChatMessageViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_chat_message, parent, false));
                        */
            }

            @Override
            protected void onBindViewHolder(ChatMessageViewHolder viewHolder, int position,
                                            final ChatMessageDTO model) {
                viewHolder.bind(mCurrentUser.get(), model);
            }

            @Override
            public void onDataChanged() {
                scrollToBottom();
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
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    public void onSendMessageButtonClick(final View view) {
        // Check input validation and attempt to send message
        if (!mChatMessage.getMessage().isEmpty()) {
            mChatMessage.setDate(new Date());
            mChatMessage.setCourseId(mCourseId);
            mChatMessage.setFrom(mCurrentUser.get());

            mChatService.sendMessage(mCourseId, mChatMessage, new IDataAccess.TaskCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onSuccess() {
                    mBinding.chatMessage.setText("");
                    // I shouldn't have to do this, the adapter's onDataChanged fn should be
                    // called, right? But it's not
                    scrollToBottom();
                }

                @Override
                public void onFailure(Exception e) {
                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private static class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        private ListItemChatMessageBinding mBinding;

        private ChatMessageViewHolder(ListItemChatMessageBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(UserDTO currentUser, ChatMessageDTO chatMessage) {
            boolean isFromCurrentUser = chatMessage.getFrom().getEmail().equals(currentUser
                    .getEmail());
            mBinding.setChatMessage(chatMessage);
            mBinding.setIsFromCurrentUser(isFromCurrentUser);
        }
    }
}
