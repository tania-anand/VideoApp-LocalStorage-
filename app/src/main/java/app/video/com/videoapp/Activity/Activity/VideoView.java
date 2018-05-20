package app.video.com.videoapp.Activity.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.MediaController;

import app.video.com.videoapp.R;

public class VideoView extends AppCompatActivity {

    android.widget.VideoView videoView ;
    MediaController mediaController;
    Uri videoUri;

    int videoSeekPostion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i("show","on create");

        videoUri = Uri.parse(getIntent().getStringExtra("videoPath"));
        // create an object of media controller
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        // initiate a video view
        videoView = findViewById(R.id.videoView2);
        // set media controller object for a video view
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.seekTo(videoSeekPostion);
        videoView.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        videoView.seekTo(videoSeekPostion);
        Log.i("show","video view current position "+videoSeekPostion);
        Log.i("show","on start");
        super.onStart();
    }


    @Override
    protected void onPause() {
        Log.i("show","on pause");
        videoSeekPostion =videoView.getCurrentPosition();
        Log.i("show","video view current position "+videoSeekPostion);
        videoView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("show","on resume");
        videoView.seekTo(videoSeekPostion);
        Log.i("show","video view current position "+videoSeekPostion);
        super.onResume();
    }
}
