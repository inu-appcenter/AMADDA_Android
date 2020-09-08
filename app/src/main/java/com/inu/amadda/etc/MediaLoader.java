package com.inu.amadda.etc;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inu.amadda.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.edit_profile)
                .placeholder(R.drawable.edit_profile)
                .into(imageView);
    }
}