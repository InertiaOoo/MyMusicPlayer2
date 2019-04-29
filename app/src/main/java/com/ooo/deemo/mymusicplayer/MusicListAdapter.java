package com.ooo.deemo.mymusicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ooo.deemo.mymusicplayer.Utils.MusicUtils;

import java.util.List;

/**
 * Author by Deemo, Date on 2019/4/22.
 * Have a good day
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
public static MediaPlayer  mPlayer = new MediaPlayer();
    private Context context;
    private static List<Song> list;
    private int position_flag = 0;
    private  static int currentposition = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        currentposition = position;
            final Song song = list.get(position);

            holder.songView.setText(song.getSong());

            holder.singerView.setText(song.getSinger());


        int duration = list.get(position).getDuration();
        String time = MusicUtils.formatTime(duration);
        holder.durationView.setText(time);

            holder.positionView.setText(position+1+"");

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


mPlayer.stop();
        musicPlay(position);

    }
});

mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {
        musicNext();
        Log.e("onCompletion","musicNext");
    }
});

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
TextView songView;
TextView singerView;
TextView durationView;
TextView positionView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

songView = itemView.findViewById(R.id.item_mymusic_song);
            singerView = itemView.findViewById(R.id.item_mymusic_singer);
            durationView = itemView.findViewById(R.id.item_mymusic_duration);

            positionView = itemView.findViewById(R.id.item_mymusic_postion);


        }
    }


    public MusicListAdapter(List<Song> list) {
        this.list = list;
    }


    public static void musicPlay(int position) {
        try {


mPlayer.stop();
FirstActivity.play_bt.setText("PAUSE");

            mPlayer.reset();
            mPlayer.setDataSource(list.get(position).getPath());
            mPlayer.prepare();
            mPlayer.start();
FirstActivity.getCurrentMusic(position);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void musicNext() {
        try {
            currentposition++;
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.setDataSource(list.get(currentposition).getPath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void musicPre() {
        try {
            currentposition--;
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.setDataSource(list.get(currentposition).getPath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void musicSearch(){



    }

}
