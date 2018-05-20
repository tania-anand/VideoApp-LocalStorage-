package app.video.com.videoapp.Activity.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.video.com.videoapp.Activity.Adapter.VideoAdapter;
import app.video.com.videoapp.Activity.Bean.VideoBean;
import app.video.com.videoapp.R;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    RecyclerView rvVideo;
    FloatingActionButton fab;
    ArrayList<VideoBean> videoFileName;

    LinearLayout emptyView;

    int messageCode=0;

    static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"MyVideoApp";

    VideoAdapter videoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    void initViews(){

        emptyView = findViewById(R.id.emView);

        rvVideo = (RecyclerView)findViewById(R.id.rvVideo);
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager( new LinearLayoutManager(this));
        rvVideo.setItemAnimator(new DefaultItemAnimator());

        videoFileName = new ArrayList<>();

        videoAdapter = new VideoAdapter(getApplicationContext(),videoFileName);
        rvVideo.setAdapter(videoAdapter);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // on click of floating bar activity is displayed
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        ) {
                    messageCode=102;
                    handler.sendEmptyMessage(messageCode);
                } else {
                    messageCode=102;
                    grantPermissions();
                }

            }
        });


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                ) {
            messageCode=101;
            handler.sendEmptyMessage(messageCode);
        } else {
            messageCode=101;
            grantPermissions();
        }
    }

    void getVideoList(){

        File file = new File(path);
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (!mkdir) {
                Log.d("show", "Failed to create directory MyCameraVideo.");

            }
        }

        String[] fileName = file.list();
        File[] files = file.listFiles();

        if (fileName.length > 0) {
            videoFileName.clear();
            rvVideo.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            for (File name : files) {
                long fileSizeMb = name.length() / (1024 * 1024);
                videoFileName.add(new VideoBean(name.getName(), Uri.parse(file.getAbsolutePath() + File.separator + name.getName()), fileSizeMb));
            }
            videoAdapter.notifyDataSetChanged();
        } else {
            // show empty view
            rvVideo.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    Uri videoUri;
    private void dispatchTakeVideoIntent() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            videoUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            Toast.makeText(MainActivity.this,"Video Saved ",Toast.LENGTH_LONG).show();
            // as video is saved so add to recycler list
            getVideoList();
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_CANCELED) {
            // display toast has cancellerd
            Toast.makeText(MainActivity.this,"Video Not Saved ",Toast.LENGTH_LONG).show();

        }
    }

    private Uri getOutputMediaFileUri(int type){
        return  FileProvider.getUriForFile(MainActivity.this,
                "helper" + ".provider",
                getOutputMediaFile());
    }

    private static File getOutputMediaFile(){
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(path);
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){
            boolean mkdir = mediaStorageDir.mkdir();
            if (!mkdir){
                Log.d("show", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        // Create a media file name
        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());

        File mediaFile;

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");


        return mediaFile;
    }

    void grantPermissions() {
        ActivityCompat.requestPermissions(this, new
                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String
            permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    handler.sendEmptyMessage(messageCode);

                } else {
                    grantPermissions();
                }
                return;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101) {
                getVideoList();
            }
            else if(msg.what == 102) {
                dispatchTakeVideoIntent();
            }
        }
    };
}
