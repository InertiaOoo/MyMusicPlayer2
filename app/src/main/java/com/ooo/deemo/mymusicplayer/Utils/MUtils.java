package com.ooo.deemo.mymusicplayer.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ooo.deemo.mymusicplayer.Song;

import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.List;

public class MUtils {
    //定义一个集合，存放从本地读取到的内容
    public static List<Song> list;

    public static Song song;


    public static List<Song> getmusic(Context context) {

        list = new ArrayList<>();


        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                song = new Song();
               String musicfullname = (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));

               if (musicfullname.contains(".")){

                   song.song = musicfullname.substring(0,musicfullname.indexOf("."));
               }else {
                   song.song = musicfullname;
               }

                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

//                把歌曲名字和歌手切割开
                if (song.size > 1000 * 800&&song.duration>0) {
                    if (song.song.contains("-")) {
                        String[] str = song.song.split("-");
                        song.singer = str[0];
                        song.song = str[1];
                    }
                    list.add(song);
                }

            }
        }

        cursor.close();
        return list;

    }


    //    转换歌曲时间的格式
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }


}