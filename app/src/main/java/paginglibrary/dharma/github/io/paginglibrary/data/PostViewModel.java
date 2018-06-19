package paginglibrary.dharma.github.io.paginglibrary.data;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import paginglibrary.dharma.github.io.paginglibrary.helper.NetworkState;
import paginglibrary.dharma.github.io.paginglibrary.model.Post;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public class PostViewModel extends ViewModel {
    public LiveData<PagedList<Post>> pagedListLiveData;
    public LiveData<NetworkState> networkState;
    Executor executor;
    LiveData<WPDataSource> myDataSource;

    public PostViewModel() {
        executor = Executors.newFixedThreadPool(5);
        WPDataSourceFactory wpDataSourceFactory = new WPDataSourceFactory(executor);
        myDataSource = wpDataSourceFactory.getMutableLiveData();
        networkState = Transformations.switchMap(myDataSource,
                (Function<WPDataSource, LiveData<NetworkState>>) WPDataSource::getNetworkState);
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(2)
                        .setPageSize(4).build();
        pagedListLiveData = (new LivePagedListBuilder(wpDataSourceFactory, pagedListConfig))
                .setBackgroundThreadExecutor(executor)
                .build();
    }
}