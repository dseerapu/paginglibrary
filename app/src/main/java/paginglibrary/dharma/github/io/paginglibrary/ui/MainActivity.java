package paginglibrary.dharma.github.io.paginglibrary.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import paginglibrary.dharma.github.io.paginglibrary.R;
import paginglibrary.dharma.github.io.paginglibrary.data.PostViewModel;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private PostViewModel viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.postList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        viewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        final PostAdapter postAdapter = new PostAdapter();
        recyclerView.setAdapter(postAdapter);
        viewModel.pagedListLiveData.observe(this, pagedList -> {
            postAdapter.setList(pagedList);
        });
        viewModel.networkState.observe(this, networkState -> {
            postAdapter.setNetworkState(networkState);
            Log.d(TAG, "Network Changed");
        });

    }
}
