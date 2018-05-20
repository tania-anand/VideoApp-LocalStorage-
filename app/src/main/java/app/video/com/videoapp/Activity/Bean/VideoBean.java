package app.video.com.videoapp.Activity.Bean;

import android.net.Uri;

/**
 * Created by tania on 20/5/18.
 */

public class VideoBean {
    private String videoName;
    private Uri videoPath;
    long videoSize;

    public VideoBean(String videoName,  Uri videoPath,long videoSize) {
        this.videoName = videoName;
        this.videoPath = videoPath;
        this.videoSize = videoSize;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public  Uri getVideoPath() {
        return videoPath;
    }

    public void setVideoPath( Uri videoPath) {
        this.videoPath = videoPath;
    }
    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }
}
