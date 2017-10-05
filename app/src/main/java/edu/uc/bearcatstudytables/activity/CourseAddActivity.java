package edu.uc.bearcatstudytables.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.databinding.ActivityCourseAddBinding;
import edu.uc.bearcatstudytables.dto.CourseDTO;
import edu.uc.bearcatstudytables.viewmodel.SingleTaskViewModel;

public class CourseAddActivity extends BaseActivity {

    public static final int REQUEST_CODE = 2;
    private static final String TAG = "CourseAddActivity";

    private ActivityCourseAddBinding mBinding;
    private CourseDTO mCourse;
    private SingleTaskViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.action_add_course));
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_course_add);
        mCourse = new CourseDTO();
        mBinding.setCourse(mCourse);
        mViewModel = ViewModelProviders.of(this).get(SingleTaskViewModel.class);
        mBinding.setViewModel(mViewModel);
    }

    public void onAddCourseButtonClick(final View view) {
        // Input validation
        View focusView = null;
        // Course name
        if (mCourse.getName().isEmpty()) {
            mBinding.courseName.setError(getString(R.string.error_field_required));
            focusView = mBinding.courseName;
        }

        // Check input validation and attempt to create course
        if (focusView != null) {
            focusView.requestFocus();
        } else {
            mViewModel.isLoading.set(true);
            mDatabase.getReference().child("course").push().setValue(mCourse)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                mViewModel.isLoading.set(false);
                                Snackbar.make(view,
                                        getString(R.string.error_course_create), Snackbar.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        }
    }
}
