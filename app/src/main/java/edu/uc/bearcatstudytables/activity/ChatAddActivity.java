package edu.uc.bearcatstudytables.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.dao.UserDAO;
import edu.uc.bearcatstudytables.databinding.ActivityChatAddBinding;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.viewmodel.ChatViewModel;

public class ChatAddActivity extends BaseActivity {

    private static final String TAG = "ChatAddActivity";

    public static final int REQUEST_CODE = 4;
    public final static String KEY_CHAT_TYPE = "CHAT_TYPE";

    private ActivityChatAddBinding mBinding;
    private ChatViewModel mViewModel;
    private String mChatType;
    private HashMap<String, String> mEmailToUserId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mChatType = intent.getStringExtra(KEY_CHAT_TYPE);
        if (mChatType != null) {
            setTitle(mChatType.equals(ChatDTO.types.COURSE.name())
                    ? R.string.action_add_course : R.string.action_add_group);
        }

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);

        // Create empty chat model and add it to the ViewModel
        ChatDTO chat = new ChatDTO();
        chat.setType(mChatType); // TODO
        mViewModel.setData(chat);

        // Setup data binding and set ViewModel
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_add);
        mBinding.setViewModel(mViewModel);


        /**
         *
         *
         *
         *
         *
         */

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        UserDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    UserDTO user = dataSnapshotChild.getValue(UserDTO.class);
                    if (user != null) {
                        autoComplete.add(user.getEmail());
                        mEmailToUserId.put(user.getEmail(), user.getId());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBinding.studentsAutoComplete.setThreshold(2);
        mBinding.studentsAutoComplete.setAdapter(autoComplete);
        mBinding.studentsAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        //int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        //String[] dogArr = getResources().getStringArray(R.array.dogs_list);
        //List<String> dogList = Arrays.asList(dogsArr);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layoutItemId, dogList);

        //mBinding.membersAutoComplete.setAdapter(new AutoCompleteAdapter(this, autoComplete));
        //mBinding.membersAutoComplete.setThreshold(2);
        //mBinding.membersAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private HashMap<String, Boolean> generateMembers() {
        HashMap<String, Boolean> members = new HashMap<>();
        for (Map.Entry<String, String> entry : mEmailToUserId.entrySet()) {
            UserDTO user = new UserDTO();
            user.setEmail(entry.getKey());
            members.put(entry.getValue(), true);
        }
        return members;
    }

    private List<UserDTO> generateMemberList() {
        List<UserDTO> members = new ArrayList<>();
        for (Map.Entry<String, String> entry : mEmailToUserId.entrySet()) {
            UserDTO user = new UserDTO();
            user.setEmail(entry.getKey());
            members.add(user);
        }
        return members;
    }

    public void onAddCourseButtonClick(final View view) {
        ChatDTO inputChat = mViewModel.getData();
        inputChat.setMembers(generateMembers());
        //inputChat.setMembers(generateMemberList());

        // Input validation
        View focusView = null;
        // Students list
        if (mBinding.studentsAutoComplete.getText().toString().isEmpty()) {
            mBinding.studentsAutoComplete.setError(getString(R.string.error_field_required));
            focusView = mBinding.studentsAutoComplete;
        }
        // Course name
        if (inputChat.getName().isEmpty()) {
            mBinding.name.setError(getString(R.string.error_field_required));
            focusView = mBinding.name;
        }

        // Check input validation and attempt to create chat
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mChatService.create(inputChat, new IDataAccess.TaskCallback() {
                @Override
                public void onStart() {
                    mViewModel.setIsLoading(true);
                }

                @Override
                public void onComplete() {
                    mViewModel.setIsLoading(false);
                }

                @Override
                public void onSuccess() {
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    /*
    private class AutoCompleteAdapter extends ArrayAdapter<UserDTO> {

        private final List<UserDTO> users;
        private List<UserDTO> filteredUsers = new ArrayList<>();

        AutoCompleteAdapter(Context context, List<UserDTO> users) {
            super(context, 0, users);
            this.users = users;
        }

        @Override
        public int getCount() {
            return filteredUsers.size();
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return new UsersFilter(this, users);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item from filtered list.
            UserDTO user = filteredUsers.get(position);

            // Inflate your custom row layout as usual.
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            //TextView tvName = (TextView) convertView.findViewById(R.id.row_breed);
            //ImageView ivIcon = (ImageView) convertView.findViewById(R.id.row_icon);
            //tvName.setText(user.getName());
            //ivIcon.setImageResource(dog.drawable);
            TextView txt1 = (TextView) convertView.findViewById(android.R.id.text1);
            txt1.setText(user.getEmail());

            return convertView;
        }

        @Nullable
        @Override
        public UserDTO getItem(int position) {
            return filteredUsers.get(position);
        }
    }

    private class UsersFilter extends Filter {

        AutoCompleteAdapter adapter;
        List<UserDTO> originalList;
        List<UserDTO> filteredList;

        UsersFilter(AutoCompleteAdapter adapter, List<UserDTO> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                // Your filtering logic goes in here
                for (final UserDTO user : originalList) {
                    if (user.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredUsers.clear();
            adapter.filteredUsers.addAll((List) results.values);
            adapter.notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            UserDTO user = (UserDTO) resultValue;
            return user.getEmail();
        }
    }
    */
}
