package paginglibrary.dharma.github.io.paginglibrary.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import paginglibrary.dharma.github.io.paginglibrary.api.WPRestAPI;
import paginglibrary.dharma.github.io.paginglibrary.helper.NetworkState;
import paginglibrary.dharma.github.io.paginglibrary.helper.Status;
import paginglibrary.dharma.github.io.paginglibrary.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public class WPDataSource extends PageKeyedDataSource {
    private static String TAG = "WP DATA SOURCE";
    int page = 1;
    int max_page;
    WPRestAPI wordPressService;
    LoadInitialParams initialParams;
    LoadParams afterParams;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private Executor retryExecutor;

    public WPDataSource(Executor retryExecutor) {
        wordPressService = WPRestAPI.getInstance();
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.retryExecutor = retryExecutor;
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback callback) {
        Log.i(TAG, "Loading Page " + 1 + " Load Size " + params.requestedLoadSize);
        final List postList = new ArrayList<>();
        initialParams = params;
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);


        Call<List<Post>> request = wordPressService.getAllPost(page);
        Response<List<Post>> response;
        try {
            response = request.execute();
            if (response.isSuccessful() && response.code() == 200) {
                postList.addAll(response.body());
                String totalPage = response.headers().get("X-WP-TotalPages");
                max_page = Integer.parseInt(totalPage);
                Log.d(TAG, "Total page is " + totalPage);
                callback.onResult(postList, null, page + 1);
                initialLoading.postValue(NetworkState.LOADED);
                networkState.postValue(NetworkState.LOADED);
                initialParams = null;
            } else {
                Log.e("WP API CALL", response.message());
                initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                networkState.postValue(new NetworkState(Status.FAILED, response.message()));
            }
        } catch (IOException ex) {

            String errorMessage;
            errorMessage = ex.getMessage();
            if (ex == null) {
                errorMessage = " error";
            }
            networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams params, @NonNull LoadCallback callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams params, @NonNull LoadCallback callback) {
        Log.i(TAG, "Loading Page " + params.key + " Size " + params.requestedLoadSize);
        page = page + 1;
        final List postList = new ArrayList<>();
        afterParams = params;
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);


        Call<List<Post>> request = wordPressService.getAllPost((int) params.key);
        Response<List<Post>> response;
        try {
            response = request.execute();
            if (response.isSuccessful() && response.code() == 200) {
                postList.addAll(response.body());
                callback.onResult(postList, page + 1);
                initialLoading.postValue(NetworkState.LOADED);
                networkState.postValue(NetworkState.LOADED);
                initialParams = null;
            } else if ((int) params.key > max_page) {
                Log.e("WP API CALL", response.message() + (int) params.key);
                initialLoading.postValue(new NetworkState(Status.MAX, response.message()));
                networkState.postValue(new NetworkState(Status.MAX, response.message()));
            } else {
                Log.e("WP API CALL", response.message());
                initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                networkState.postValue(new NetworkState(Status.FAILED, response.message()));
            }
        } catch (IOException ex) {

            String errorMessage;
            errorMessage = ex.getMessage();
            if (ex == null) {
                errorMessage = " error";
            }
            networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
        }
    }
}