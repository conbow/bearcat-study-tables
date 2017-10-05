package edu.uc.bearcatstudytables.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uc.bearcatstudytables.R;

/**
 * Created by connorbowman on 10/4/17.
 */

public class CourseAdapter {

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_title;
        TextView textView_decription;
        ImageView imageView;

        public CourseViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //textView_title = (TextView) itemView.findViewById(R.id.title);
            //textView_decription = (TextView) itemView.findViewById(R.id.description);
            //imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setTitle(String title) {
            textView_title.setText(title + "");
        }

        public void setDescription(String description) {
            textView_decription.setText(description);
        }
    }
}
