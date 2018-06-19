package paginglibrary.dharma.github.io.paginglibrary.api;

import java.util.List;

import paginglibrary.dharma.github.io.paginglibrary.model.Post;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public final class WPRestAPI {
    private final WPRESTService wordPressService;
    static String base_url = "https://www.nplix.com/wp-json/";

    private WPRestAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wordPressService = retrofit.create(WPRESTService.class);
    }

    public static WPRestAPI getInstance() {
        return InstanceHolder.INSTASNCE;
    }

    public final Call<List<Post>> getAllPost(int page) {
        return wordPressService.getAllPostByPage(page);
    }

    private static final class InstanceHolder {
        private static final WPRestAPI INSTASNCE = new WPRestAPI();
    }
}