package app.video.com.videoapp.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import app.video.com.videoapp.Activity.Activity.MainActivity;
import app.video.com.videoapp.Activity.Bean.VideoBean;
import app.video.com.videoapp.Activity.Helper.GlideApp;
import app.video.com.videoapp.R;

import static app.video.com.videoapp.Activity.Helper.GlideOptions.fitCenterTransform;

/**
 * Created by tania on 20/5/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private  ArrayList<VideoBean> arrayList;
    private Context context;


    public VideoAdapter(Context context ,ArrayList<VideoBean> arrayList){
        this.arrayList = arrayList;
        this.context = context;

    }
    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_listitem,parent,false);
        VideoAdapter.ViewHolder viewHolder = new VideoAdapter.ViewHolder(view);
        return viewHolder;
    }
    int pos =0;

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        pos=position;

        holder.videoName.setText(arrayList.get(position).getVideoName());
        holder.videoSize.setText(arrayList.get(position).getVideoSize()+" mb");

        holder.videoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, app.video.com.videoapp.Activity.Activity.VideoView.class);
                i.putExtra("videoPath",String.valueOf(arrayList.get(pos).getVideoPath()));
                context.startActivity(i);
            }
        });

        GlideApp.with(context)
                .load("file://"+arrayList.get(position).getVideoPath())
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(false)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("show", "Load failed", e);
                        // Logs the individual causes:
                        for (Throwable t : e.getRootCauses()) {
                            Log.e("show", "Caused by", t);
                        }
                        // Logs the root causes
                        e.logRootCauses("show");
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //loaded do something
                        return false;
                    }
                })
                .into(holder.videoView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoPlay;
        ImageView videoView;
        TextView videoName ,videoSize;
        public ViewHolder(View itemView) {
            super(itemView);
            videoPlay = itemView.findViewById(R.id.videoplay);
            videoView = itemView.findViewById(R.id.videoView);
            videoName = itemView.findViewById(R.id.videoName);
            videoSize = itemView.findViewById(R.id.videoSize);

        }
    }
}
