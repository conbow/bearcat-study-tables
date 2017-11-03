package edu.uc.bearcatstudytables.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.ChatDAO;
import edu.uc.bearcatstudytables.databinding.FragmentChatsBinding;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.ui.activity.ChatActivity;

public class ChatsFragment extends BaseFragment {

    private static final String TAG = "ChatsFragment";

    private static final String KEY_TYPE = "TYPE";

    private String mChatType;
    private FragmentChatsBinding mBinding;
    private FirebaseRecyclerAdapter mRecyclerAdapter;

    public ChatsFragment() {
    }

    public static ChatsFragment newInstance(ChatDTO.types type) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type.name());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        mChatType = bundle.getString(KEY_TYPE, ChatDTO.types.COURSE.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatsBinding.inflate(inflater, container, false);

        if (mChatType.equals(ChatDTO.types.GROUP.toString())) {
            mBinding.noChatsText.setText(R.string.you_dont_have_any_groups);
            mBinding.noChatsImage.setImageResource(R.drawable.ic_menu_people);
        }

        initializeList();

        return mBinding.getRoot();
    }

    private void initializeList() {
        mBinding.chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        mBinding.chatsList.addItemDecoration(dividerItemDecoration);

        Query query = ChatDAO.getReferenceForType(ChatDTO.types.valueOf(mChatType))
                .orderByChild("members/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .equalTo(true);

        FirebaseRecyclerOptions<ChatDTO> options =
                new FirebaseRecyclerOptions.Builder<ChatDTO>()
                        .setQuery(query, ChatDTO.class)
                        .setLifecycleOwner(this)
                        .build();

        mRecyclerAdapter = new FirebaseRecyclerAdapter<ChatDTO, ChatGroupViewHolder>(options) {
            @Override
            public ChatGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ChatGroupViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_chat, parent, false));
            }

            @Override
            protected void onBindViewHolder(final ChatGroupViewHolder viewHolder, int position,
                                            final ChatDTO model) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(ChatActivity.KEY_CHAT_ID, mRecyclerAdapter
                                .getRef(viewHolder.getAdapterPosition()).getKey());
                        intent.putExtra(ChatActivity.KEY_CHAT_NAME, model.getName());
                        startActivity(intent);
                    }
                });
                viewHolder.bind(model);
            }

            @Override
            public void onDataChanged() {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.noChats.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };

        mBinding.chatsList.setAdapter(mRecyclerAdapter);
    }

    private static class ChatGroupViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;

        private ChatGroupViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mDescription = view.findViewById(R.id.description);
        }

        private void bind(ChatDTO chat) {
            setName(chat.getName());
            setDescription(chat.getDescription());
        }

        private void setName(String name) {
            mName.setText(name);
        }

        private void setDescription(String description) {
            mDescription.setText(description);
        }
    }
}
