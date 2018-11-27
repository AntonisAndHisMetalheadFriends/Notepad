package aahmf.notepad;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenActivity extends AppCompatActivity {
    private  Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);


        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            mImageUri = callingActivityIntent.getData();
            if(mImageUri != null) {
                Glide.with(this)
                        .load(mImageUri)
                        .into(fullScreenImageView);
            }
        }
    }
}
