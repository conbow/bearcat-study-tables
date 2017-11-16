package edu.uc.bearcatstudytables.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.util.Date;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.ChatMessageDAO;
import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityChatBinding;
import edu.uc.bearcatstudytables.databinding.ListItemChatMessageBinding;
import edu.uc.bearcatstudytables.dto.ChatMessageDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;

public class ChatActivity extends BaseActivity {

    public final static String KEY_CHAT_ID = "CHAT_ID";
    public final static String KEY_CHAT_NAME = "CHAT_NAME";
    public final static String KEY_CHAT_DESCRIPTION = "CHAT_DESCRIPTION";

    private ActivityChatBinding mBinding;
    private ChatMessageDTO mChatMessage;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get chat information from intent
        String chatId = getIntent().getStringExtra(KEY_CHAT_ID);
        String chatName = getIntent().getStringExtra(KEY_CHAT_NAME);
        String chatDescription = getIntent().getStringExtra(KEY_CHAT_DESCRIPTION);

        // Set title and subtitle
        setTitle(chatName);
        if (getSupportActionBar() != null && !chatDescription.isEmpty())
            getSupportActionBar().setSubtitle(chatDescription);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        // Bind chat message
        mChatMessage = new ChatMessageDTO();
        mChatMessage.setChatId(chatId);
        mChatMessage.setFrom(mCurrentUser.get());
        mBinding.setChatMessage(mChatMessage);
        mBinding.executePendingBindings();

        initializeChatMessageList();
    }

    /**
     * Initialize the chat messages list such as setting up the RecyclerView
     */
    private void initializeChatMessageList() {
        // Set options for Firebase UI RecyclerView
        Query query = ChatMessageDAO.getQueryForChatId(mChatMessage.getChatId()).limitToLast(50);
        FirebaseRecyclerOptions<ChatMessageDTO> options =
                new FirebaseRecyclerOptions.Builder<ChatMessageDTO>()
                        .setQuery(query, ChatMessageDTO.class)
                        .setLifecycleOwner(this)
                        .build();

        // Create RecyclerAdapter
        FirebaseRecyclerAdapter recyclerAdapter = new FirebaseRecyclerAdapter<ChatMessageDTO,
                ChatMessageViewHolder>(options) {
            @Override
            public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                ListItemChatMessageBinding binding = ListItemChatMessageBinding
                        .inflate(layoutInflater, parent, false);
                return new ChatMessageViewHolder(binding);
            }

            @Override
            protected void onBindViewHolder(ChatMessageViewHolder viewHolder, int position,
                                            final ChatMessageDTO model) {
                viewHolder.bind(mCurrentUser.get(), model);
            }

            @Override
            public void onDataChanged() {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.noChatMessages.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
                scrollToBottom();
            }
        };

        // Setup RecyclerView and set adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setStackFromEnd(true);
        mBinding.chatMessageList.setLayoutManager(linearLayoutManager);
        mBinding.chatMessageList.setAdapter(recyclerAdapter);
    }

    /**
     * Function to smoothly scroll to the bottom of the chat messages list
     */
    private void scrollToBottom() {
        mBinding.chatMessageList.smoothScrollToPosition(mBinding.chatMessageList.getAdapter()
                .getItemCount());
    }

    /**
     * Send chat message button click handler
     * Validates input and sends data to service
     *
     * @param view View
     */
    public void onSendMessageButtonClick(final View view) {
        // Check to make sure message isn't empty and attempt to send message
        if (!mChatMessage.getMessage().isEmpty()) {
            mChatMessage.setDate(new Date());

            mChatService.sendMessage(mChatMessage, new DataAccess.TaskCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void onSuccess() {
                    mBinding.inputChatMessage.setText("");
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

    /**
     * TODO
     * Attach a file to chat button click handler
     *
     * @param view View
     */
    public void onAttachFileButtonClick(final View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // do something with the loaded picture

        }
    }

    private static class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        private ListItemChatMessageBinding mListItemBinding;

        private ChatMessageViewHolder(ListItemChatMessageBinding listItemBinding) {
            super(listItemBinding.getRoot());
            mListItemBinding = listItemBinding;
        }

        private void bind(UserDTO currentUser, ChatMessageDTO chatMessage) {
            boolean isFromCurrentUser = chatMessage.getFrom().getEmail().equals(currentUser
                    .getEmail());
            mListItemBinding.setChatMessage(chatMessage);
            mListItemBinding.setIsFromCurrentUser(isFromCurrentUser);
        }
    }
}
