package paginglibrary.dharma.github.io.paginglibrary.api;

import java.util.List;

import paginglibrary.dharma.github.io.paginglibrary.model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public interface WPRESTService {
    @GET("wp/v2/posts")
    Call<List<Post>> getAllPostByPage(@Query("page") int id);
}