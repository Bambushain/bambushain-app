package app.bambushain.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import coil.ImageLoader;
import coil.request.ImageRequest;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import lombok.val;

@Module
@InstallIn(SingletonComponent.class)
public class ProfilePictureLoader {
    final ImageLoader imageLoader;

    @ApplicationContext
    final Context context;


    public ProfilePictureLoader(ImageLoader imageLoader, Context context) {
        this.imageLoader = imageLoader;
        this.context = context;
    }

    @SuppressLint("CheckResult")
    public void loadProfilePicture(int userId, ImageView imageView) {
        val instance = context.getString(R.string.bambooInstance);
        val baseUrl = "https://" + instance + ".bambushain.app/";
        val url = baseUrl + "api/user/" + userId + "/picture";

        val request = new ImageRequest.Builder(context)
                .data(url)
                .target(imageView)
                .build();

        imageLoader.enqueue(request);
    }
}
