package edu.uc.bearcatstudytables.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edu.uc.bearcatstudytables.R;
import edu.uc.bearcatstudytables.databinding.ActivityCoursesBinding;
import edu.uc.bearcatstudytables.databinding.NavHeaderCoursesBinding;
import edu.uc.bearcatstudytables.dto.CourseDTO;

public class CoursesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CoursesActivity";

    ActivityCoursesBinding mBinding;
    FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup Bindings
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_courses);
        mBinding.setUser(mUser);
        NavHeaderCoursesBinding _bind = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_courses, mBinding
                .navView, false);
        mBinding.navView.addHeaderView(_bind.getRoot());
        _bind.setUser(mUser);

        // Setup toolbar / nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mBinding.navView.setNavigationItemSelectedListener(this);

        // Add course fab button handler
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddCourseButtonClick();
            }
        });

        // Initialize RecyclerView
        initializeCoursesList();
    }

    private void initializeCoursesList() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("course")
                .limitToLast(50);

        FirebaseRecyclerOptions<CourseDTO> options =
                new FirebaseRecyclerOptions.Builder<CourseDTO>()
                        .setQuery(query, CourseDTO.class)
                        .build();

        mAdapter = new FirebaseRecyclerAdapter<CourseDTO, CourseViewHolder>(options) {
            @Override
            public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new CourseViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_course, parent, false));
            }

            @Override
            protected void onBindViewHolder(CourseViewHolder viewHolder, int position, final CourseDTO model) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CoursesActivity.this, CourseChatActivity.class);
                        intent.putExtra(CourseChatActivity.KEY_COURSE_ID, model.getUid());
                        intent.putExtra(CourseChatActivity.KEY_COURSE_NAME, model.getName());
                        startActivity(intent);
                    }
                });
                viewHolder.bind(model);
            }
        };
        RecyclerView recyclerView = findViewById(R.id.courses_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.courses, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_courses) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onAddCourseButtonClick() {
        Intent intent = new Intent(this, CourseAddActivity.class);
        startActivityForResult(intent, CourseAddActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CourseAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(R.id.container),
                    getString(R.string.feedback_course_created), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private static class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mDescription;

        private CourseViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mDescription = view.findViewById(R.id.description);
        }

        private void bind(CourseDTO course) {
            setName(course.getName());
            setDescription(course.getDescription());
        }

        private void setName(String name) {
            mName.setText(name);
        }

        private void setDescription(String description) {
            mDescription.setText(description);
        }
    }
}
