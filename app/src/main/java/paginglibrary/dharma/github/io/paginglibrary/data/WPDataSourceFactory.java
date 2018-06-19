package paginglibrary.dharma.github.io.paginglibrary.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import java.util.concurrent.Executor;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public class WPDataSourceFactory implements DataSource.Factory {
    MutableLiveData<WPDataSource> mutableLiveData;
    // private WPDataSource wpDataSource;
    private WPDataSource wpDataSource;
    private Executor executor;

    WPDataSourceFactory(Executor executor) {
        this.executor = executor;
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        wpDataSource = new WPDataSource(executor);
        mutableLiveData.postValue(wpDataSource);
        return wpDataSource;
    }

    MutableLiveData<WPDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}