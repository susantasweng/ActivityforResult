package quiz.android.bits.com.movieassignment.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import quiz.android.bits.com.movieassignment.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    private View mControlsView;
    public static final int LIST_ACTIVITY_REQUEST_CODE_OK = 1;
    public static final int LIST_ACTIVITY_REQUEST_CODE_CANCEL = 2;

    private NestedScrollView movieInfoContainer;
    private ImageView movieBackground;
    private TextView titleView;
    private TextView plotView;
    private TextView castView;
    private TextView directorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.push_button).setOnClickListener(mPushButtonClickListener);

        movieInfoContainer = findViewById(R.id.movie_info_container);
        movieBackground = findViewById(R.id.movie_image);
        titleView = findViewById(R.id.movie_info_title_textview);
        plotView = findViewById(R.id.movie_plot_textview);
        castView = findViewById(R.id.movie_cast_textview);
        directorView = findViewById(R.id.movie_director_textview);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private final View.OnClickListener mPushButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            movieInfoContainer.setVisibility(View.INVISIBLE);
            startActivityForResult(new Intent(MainActivity.this, MovieListActivity.class), LIST_ACTIVITY_REQUEST_CODE_OK);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == LIST_ACTIVITY_REQUEST_CODE_OK) {
            Bundle listItemBundle = data.getExtras();
            movieInfoContainer.setVisibility(View.VISIBLE);
            titleView.setText(listItemBundle.getString(MovieListActivity.MOVIE_TITLE_URI));
            plotView.setText(listItemBundle.getString(MovieListActivity.MOVIE_PLOT_URI));
            castView.setText(listItemBundle.getString(MovieListActivity.MOVIE_CAST_URI));
            directorView.setText(listItemBundle.getString(MovieListActivity.MOVIE_DIRECTOR_URI));
            try {
                movieBackground.setBackground(Drawable.createFromStream(getAssets().open(listItemBundle.getString(
                        MovieListActivity.MOVIE_IMAGE_URI)), null));
            } catch (IOException e) {
                Log.e("MovieInfo", "IOException: Failed to load the movie image from asset");
                e.printStackTrace();
            }
        }
    }
}
