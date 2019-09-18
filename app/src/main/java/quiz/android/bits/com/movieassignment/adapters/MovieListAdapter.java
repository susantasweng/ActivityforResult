package quiz.android.bits.com.movieassignment.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import quiz.android.bits.com.movieassignment.R;

public class MovieListAdapter extends ArrayAdapter {

    private Activity context;
    private String[] names;
    //private int mSelectedItem;

    public MovieListAdapter (Activity context, String[] names ) {
        super(context, R.layout.list_row_layout, names);
        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(convertView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.list_row_layout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.labelView = (TextView) view.findViewById(R.id.list_item_textview);
            //viewHolder.imageView = (ImageView) view.findViewById(R.id.list_item_imageview);
            /*if (mSelectedItem == position) {
                view.setBackgroundColor(Color.DKGRAY);
            }*/
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }



        ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.labelView.setText(names[position]);
        //viewHolder.imageView.setImageResource(R.drawable.arrow);

        return view;
    }

    /*public void updateSelectedView(int position) {
        mSelectedItem = position;
        notifyDataSetChanged();
    }*/

    private class ViewHolder {
        public TextView labelView;
        //public ImageView imageView;
    }
}
