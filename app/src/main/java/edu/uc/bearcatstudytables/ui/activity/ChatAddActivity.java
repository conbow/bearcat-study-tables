package edu.uc.bearcatstudytables.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.util.HashMap;
import java.util.List;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.DataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityChatAddBinding;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.viewmodel.ChatViewModel;

public class ChatAddActivity extends BaseActivity {

    public static final int REQUEST_CODE = 4;
    public final static String KEY_CHAT_TYPE = "CHAT_TYPE";

    private ActivityChatAddBinding mBinding;
    private ChatViewModel mViewModel;
    private HashMap<String, String> mEmailToUserId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get selected chat type from intent
        Intent intent = getIntent();
        ChatDTO.Type chatType = ChatDTO.Type.valueOf(intent.getStringExtra(KEY_CHAT_TYPE));

        // Set activity title
        setTitle(chatType.equals(ChatDTO.Type.COURSE) ? R.string.add_course : R.string.add_group);

        // Setup ViewModel for retaining data on configuration changes
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);

        // Create empty chat model and add it to the ViewModel
        ChatDTO chat = new ChatDTO();
        chat.setType(chatType);
        mViewModel.setData(chat);

        // Setup data binding and set ViewModel
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_add);
        mBinding.setViewModel(mViewModel);

        // Setup simple autocomplete array adapter
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        // Fetch all users and add them to the autocomplete
        mUserService.fetchAll(new DataAccess.TaskDataCallback<List<UserDTO>>() {
            @Override
            public void onStart() {
                mViewModel.setIsLoadingStudents(true);
            }

            @Override
            public void onComplete() {
                mViewModel.setIsLoadingStudents(false);
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onSuccess(List<UserDTO> data) {
                for (UserDTO user : data) {
                    // Add everyone except current user
                    // The current user will always be added manually
                    if (!mCurrentUser.get().getId().equals(user.getId())) {
                        autoComplete.add(user.getEmail());
                        mEmailToUserId.put(user.getEmail(), user.getId());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Show error message
                Snackbar.make(mBinding.getRoot(), e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        // Bind autocomplete adapter and set threshold/tokenizer
        mBinding.studentsAutoComplete.setAdapter(autoComplete);
        mBinding.studentsAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    /**
     * Generate HashMap of members from ID to true if they're in the chat
     *
     * @return HashMap
     */
    private HashMap<String, Boolean> generateMembers() {
        HashMap<String, Boolean> members = new HashMap<>();
        String[] studentEmails = mBinding.studentsAutoComplete.getText().toString().split(",");
        for (String studentEmail : studentEmails) {
            // Get the student by email, with all white space removed
            String studentId = mEmailToUserId.get(studentEmail.replaceAll("\\s+", ""));

            // Put the student id in member list
            if (studentId != null && !studentId.isEmpty()) {
                members.put(studentId, true);
            }
        }

        // Also add the current user
        members.put(mCurrentUser.get().getId(), true);

        return members;
    }

    /**
     * Chat Add button click handler
     * Validates input and sends data to service
     *
     * @param view View
     */
    public void onAddChatButtonClick(final View view) {
        if (mViewModel.getValidation().isValid()) {
            // Manually add members to chat object
            mViewModel.getData().setMembers(generateMembers());

            mChatService.create(mViewModel.getData(), new DataAccess.TaskCallback() {
                @Override
                public void onStart() {
                    mViewModel.setIsLoading(true);
                }

                @Override
                public void onComplete() {
                    // We don't hide progress unless the task fails, because we don't want the user
                    // to be able to click again in the split second when switching activities
                }

                @Override
                public void onSuccess() {
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    mViewModel.setIsLoading(false);

                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
