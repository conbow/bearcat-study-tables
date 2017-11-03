package edu.uc.bearcatstudytables.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.dao.IDataAccess;
import edu.uc.bearcatstudytables.databinding.ActivityUserBinding;
import edu.uc.bearcatstudytables.databinding.NavHeaderUserBinding;
import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.fragment.ChatsFragment;
import edu.uc.bearcatstudytables.ui.util.ValidationUtil;
import edu.uc.bearcatstudytables.ui.viewmodel.AuthViewModel;

public class UserActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "UserActivity";

    public static final int REQUEST_IMAGE_CAPTURE = 5;
    private static final String KEY_NAV_ID = "NAV_ID";
    private static final int NAV_CHATS = 0;
    private static final int NAV_PROFILE = 1;

    private ActivityUserBinding mBinding;
    private AuthViewModel mProfileViewModel;
    private int mCurrentNav;
    private ChatDTO.types mTabChatType = ChatDTO.types.COURSE;
    private int mTabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentNav = savedInstanceState != null ? savedInstanceState.getInt(KEY_NAV_ID, 0) : 0;

        // Setup Bindings
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        mProfileViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        if (savedInstanceState == null) {
            mProfileViewModel.setUser(mCurrentUser.get());
        }
        mProfileViewModel.currentUser = mCurrentUser;
        /*
        if (mProfileViewModel.getCurrentUser() == null) {
            mProfileViewModel.setCurrentUserObservable(mCurrentUser);
        }*/
        mBinding.pageProfile.setViewModel(mProfileViewModel);

        NavHeaderUserBinding navHeaderUserBinding =
                DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_user, mBinding
                        .navView, false);
        mBinding.navView.addHeaderView(navHeaderUserBinding.getRoot());
        navHeaderUserBinding.setViewModel(mProfileViewModel);

        // Setup toolbar / nav drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mBinding.navView.getMenu().getItem(mCurrentNav).setChecked(true);
        mBinding.navView.setNavigationItemSelectedListener(this);

        // Setup tabs (courses / groups)
        mBinding.contentViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        mBinding.contentViewPager.addOnPageChangeListener(new TabPagerListener());
        mBinding.appBarChats.contentTabLayout.setupWithViewPager(mBinding.contentViewPager);

        showScreen(mCurrentNav);
    }

    @Override
    public void onStart() {
        super.onStart();
        toggleFabButton(mTabPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleFabButton(mTabPosition);
    }

    private void toggleFabButton(int position) {
        if ((position == 0 && mCurrentUser.get().getType().equals(UserDTO.types.INSTRUCTOR.name()))
                || (position != 0 && mCurrentNav == 0)) {
            mBinding.fab.show();
        } else {
            mBinding.fab.hide();
        }
    }

    private ChatDTO.types getTabChatType(int position) {
        return position == 0 ? ChatDTO.types.COURSE : ChatDTO.types.GROUP;
    }

    private void showScreen(int navId) {
        boolean isMain = (navId == NAV_CHATS);

        if (isMain) {
            setTitle(R.string.chats);
            toggleFabButton(mTabPosition);
        } else if (navId == NAV_PROFILE) {
            setTitle(R.string.action_profile);
            mBinding.fab.hide();
        }

        mBinding.appBarChats.contentTabLayout.setVisibility(isMain ? View.VISIBLE : View.GONE);
        mBinding.contentViewPager.setVisibility(isMain ? View.VISIBLE : View.GONE);

        mBinding.pageProfile.getRoot().setVisibility(navId == NAV_PROFILE ? View.VISIBLE : View.GONE);

        mCurrentNav = navId;
    }

    public void onAddCourseButtonClick(View view) {
        Intent intent = new Intent(this, ChatAddActivity.class);
        intent.putExtra(ChatAddActivity.KEY_CHAT_TYPE, mTabChatType.name());
        startActivityForResult(intent, ChatAddActivity.REQUEST_CODE);
    }

    public void onUpdateProfileButtonClick(final View view) {
        final UserDTO inputUser = mProfileViewModel.getUser();

        // Input validation
        View focusView = null;
        // Password Repeat
        String passwordRepeat = mBinding.pageProfile.passwordRepeat.getText().toString();
        if (!passwordRepeat.isEmpty() && !ValidationUtil.isValidPassword(passwordRepeat)) {
            mBinding.pageProfile.passwordRepeat.setError(getString(R.string.error_invalid_password));
            focusView = mBinding.pageProfile.passwordRepeat;
        } else if (!passwordRepeat.equals(inputUser.getPassword())) {
            mBinding.pageProfile.passwordRepeat.setError(getString(R.string.error_passwords_must_match));
            focusView = mBinding.pageProfile.passwordRepeat;
        }
        // Password
        if (!inputUser.getPassword().isEmpty() && !ValidationUtil.isValidPassword(inputUser
                .getPassword())) {
            mBinding.pageProfile.password.setError(getString(R.string.error_invalid_password));
            focusView = mBinding.pageProfile.password;
        } else if (!inputUser.getPassword().isEmpty()
                && !inputUser.getPassword().equals(passwordRepeat)) {
            mBinding.pageProfile.password.setError(getString(R.string.error_passwords_must_match));
            focusView = mBinding.pageProfile.password;
        }
        // Email
        if (inputUser.getEmail().isEmpty()) {
            mBinding.pageProfile.email.setError(getString(R.string.error_field_required));
            focusView = mBinding.pageProfile.email;
        } else if (!ValidationUtil.isValidEmail(inputUser.getEmail())) {
            mBinding.pageProfile.email.setError(getString(R.string.error_invalid_email));
            focusView = mBinding.pageProfile.email;
        }
        // Name
        if (inputUser.getName() != null && inputUser.getName().isEmpty()) {
            mBinding.pageProfile.name.setError(getString(R.string.error_field_required));
            focusView = mBinding.pageProfile.name;
        }

        // Check input validation and attempt profile update
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            mUserService.updateProfile(mProfileViewModel.getUser(), new IDataAccess.TaskCallback() {
                @Override
                public void onStart() {
                    mProfileViewModel.setIsLoading(true);
                }

                @Override
                public void onComplete() {
                    mBinding.pageProfile.passwordRepeat.setText("");
                    mProfileViewModel.setIsLoading(false);

                    // Clear glide image cache and reload current user
                    Glide.get(UserActivity.this).clearMemory();
                    new AsyncTask<Void, Void, Void>() {
                        protected Void doInBackground(Void... unused) {
                            Glide.get(UserActivity.this).clearDiskCache();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            mUserService.reload();
                        }
                    }.execute();
                }

                @Override
                public void onSuccess() {
                    // Show success message
                    Snackbar.make(view, R.string.success_profile_update, Snackbar.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onFailure(Exception e) {
                    // Show error message
                    Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    public void onProfilePhotoChangeButtonClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chats) {
            showScreen(NAV_CHATS);
        } else if (id == R.id.nav_profile) {
            showScreen(NAV_PROFILE);
        } else if (id == R.id.nav_logout) {
            // We just need to call logout, auth listener will then kick them out of the Activity
            mUserService.logout();
        }

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle activity result for either creating a chat group or adding a profile image
        if (requestCode == ChatAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            int feedback = mTabPosition == 0 ? R.string.feedback_course_created
                    : R.string.feedback_group_created;
            Snackbar.make(findViewById(R.id.container), getString(feedback), Snackbar.LENGTH_LONG)
                    .show();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the image from intent extras bundle
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                // Compress the image and put it into user DTO
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                mProfileViewModel.getUser().setPhoto(byteArrayOutputStream.toByteArray());
                mProfileViewModel.setUser(mProfileViewModel.getUser());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current navigation state
        outState.putInt(KEY_NAV_ID, mCurrentNav);
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabs = {getString(R.string.courses), getString(R.string.groups)};

        TabPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int id) {
            return ChatsFragment.newInstance(getTabChatType(id));
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    private class TabPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mTabPosition = position;
            mTabChatType = getTabChatType(position);
            toggleFabButton(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
