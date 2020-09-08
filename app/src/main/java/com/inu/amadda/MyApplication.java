package com.inu.amadda;

import android.app.Application;

import com.inu.amadda.etc.MediaLoader;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.Locale;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        setAlbum();
    }

    private void setAlbum() {
        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.getDefault())
                .build());
    }
}
