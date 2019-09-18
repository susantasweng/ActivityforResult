package quiz.android.bits.com.movieassignment.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.IOException;
import java.util.HashMap;

import quiz.android.bits.com.movieassignment.R;
import quiz.android.bits.com.movieassignment.adapters.MovieListAdapter;

import static quiz.android.bits.com.movieassignment.activities.MainActivity.LIST_ACTIVITY_REQUEST_CODE_CANCEL;
import static quiz.android.bits.com.movieassignment.activities.MainActivity.LIST_ACTIVITY_REQUEST_CODE_OK;

public class MovieListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String TAG = MovieListActivity.class.getSimpleName();
    private String[] movieNamesList;
    private String[] moviePlotsList;
    private String[] movieCastsList;
    private String[] movieDirectorsList;
    private ListView movieList;
    private MovieListAdapter listAdapter;

    public static String LIST_POSITION = "quiz.android.bits.com.movieassignment.list_position";
    public static String MOVIE_PLOT_URI = "quiz.android.bits.com.movieassignment.movie_plot";
    public static String MOVIE_TITLE_URI = "quiz.android.bits.com.movieassignment.movie_title";
    public static String MOVIE_IMAGE_URI = "quiz.android.bits.com.movieassignment.movie_poster_uri";
    public static String MOVIE_CAST_URI = "quiz.android.bits.com.movieassignment.movie_cast_uri";
    public static String MOVIE_DIRECTOR_URI = "quiz.android.bits.com.movieassignment.movie_director_uri";

    private int screenOrientation;
    private boolean mBind;

    private AppBarLayout appBarLayout;
    private TextView titleView;
    private TextView plotView;
    private Bundle listItemBundle;

    private HashMap<String, String> moviePlotHashMap = new HashMap<>();
    private HashMap<String, String> movieCastsHashMap = new HashMap<>();
    private HashMap<String, String> movieDirectorsHashMap = new HashMap<>();


    private final String LAST_SELECTED_LIST_INDEX = "LAST_SELECTED_LIST_INDEX";
    private int mSelectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);

        movieNamesList = getResources().getStringArray(R.array.movie_list_data_feed);
        moviePlotsList = getResources().getStringArray(R.array.movie_plots_data_feed);
        movieCastsList = getResources().getStringArray(R.array.movie_cast_list_data_feed);
        movieDirectorsList = getResources().getStringArray(R.array.movie_director_list_data_feed);

        moviePlotHashMap = getMoviePlotMap(moviePlotsList);
        movieCastsHashMap = getMovieCastsMap(movieCastsList);
        movieDirectorsHashMap = getMovieDirectorsMap(movieDirectorsList);


        movieList = findViewById(R.id.list_view);
        listAdapter = new MovieListAdapter(this, movieNamesList);
        movieList.setAdapter(listAdapter);
        movieList.setOnItemClickListener(this);

        screenOrientation = this.getResources().getConfiguration().orientation;

        if (savedInstanceState != null) {
            mSelectedIndex = savedInstanceState.getInt(LAST_SELECTED_LIST_INDEX);
        }
        updateOrientionViews(mSelectedIndex);

        findViewById(R.id.ok_button).setOnClickListener(mButtonClickListener);
        findViewById(R.id.cancel_button).setOnClickListener(mButtonClickListener);
    }

    private void updateOrientionViews(int lastSelectedIndex) {
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            appBarLayout = findViewById(R.id.info_screen_appbar);
            titleView = findViewById(R.id.movie_info_title_textview);
            plotView = findViewById(R.id.movie_plot_textview);

            updateMovieDetails(lastSelectedIndex);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedIndex = position;
        Log.d(TAG, "onItemClick in t" + position);
    }

    View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.ok_button) {
                if(mSelectedIndex > 0) {
                    Bundle bundle = getMovieItemDetails(mSelectedIndex);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(LIST_ACTIVITY_REQUEST_CODE_OK, intent);
                    finish();
                }

            } else if(v.getId() == R.id.cancel_button) {
                setResult(LIST_ACTIVITY_REQUEST_CODE_CANCEL, null);
                finish();
            }
        }
    };

    private Bundle getMovieItemDetails(int position) {
        String movie = movieNamesList[position];
        String movie_id = getMovieIdFromName(movie);
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_POSITION, position);
        bundle.putString(MOVIE_TITLE_URI, movie);
        bundle.putString(MOVIE_PLOT_URI, moviePlotHashMap.get(movie_id));
        bundle.putString(MOVIE_CAST_URI, movieCastsHashMap.get(movie_id));
        bundle.putString(MOVIE_DIRECTOR_URI, movieDirectorsHashMap.get(movie_id));
        bundle.putString(MOVIE_IMAGE_URI, getMovieIdFromName(movieNamesList[position])+".jpeg");
        return bundle;
    }

    private String getMovieIdFromName(String movieName) {
        movieName = movieName.replaceAll("[$&+,:;=?@#!<>.^*()%]", "");
        movieName = (movieName.replaceAll(" ", "_"));
        return movieName.toLowerCase();
    }

    private HashMap<String, String> getMoviePlotMap(String[] moviePlotsList) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String moviePlot:moviePlotsList) {
            String[] fields = moviePlot.split("\\|");
            hashMap.put(fields[0], fields[1]);
        }
        return hashMap;
    }

    private HashMap<String, String> getMovieCastsMap(String[] movieCastsList) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String moviePlot:movieCastsList) {
            String[] fields = moviePlot.split("\\|");
            hashMap.put(fields[0], fields[1]);
        }
        return hashMap;
    }

    private HashMap<String, String> getMovieDirectorsMap(String[] movieDirectorsList) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String moviePlot:movieDirectorsList) {
            String[] fields = moviePlot.split("\\|");
            hashMap.put(fields[0], fields[1]);
        }
        return hashMap;
    }

    private void updateMovieDetails(int position) {
        try {
            appBarLayout.setBackground(Drawable.createFromStream(getAssets().open(getMovieIdFromName(movieNamesList[position])+".jpeg"),
                    null));
        } catch (IOException e) {
            Log.e("MovieInfo", "IOException: Failed to load the movie image from asset");
            e.printStackTrace();
        }
        titleView.setText(movieNamesList[position]);
        plotView.setText(moviePlotsList[position]);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LAST_SELECTED_LIST_INDEX, mSelectedIndex);
    }
}
