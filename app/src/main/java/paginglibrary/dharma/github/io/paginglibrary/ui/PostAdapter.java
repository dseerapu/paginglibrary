package paginglibrary.dharma.github.io.paginglibrary.ui;

import android.arch.paging.PagedListAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import paginglibrary.dharma.github.io.paginglibrary.R;
import paginglibrary.dharma.github.io.paginglibrary.helper.NetworkState;
import paginglibrary.dharma.github.io.paginglibrary.helper.Status;
import paginglibrary.dharma.github.io.paginglibrary.model.Post;

/**
 * Created by Dharma Sai Seerapu on 6/19/2018
 */
public class PostAdapter extends PagedListAdapter<Post, RecyclerView.ViewHolder> {

    private NetworkState networkState;

    public PostAdapter() {
        super(Post.DIFF_CALLBACK);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view;

        if (viewType == R.layout.post_item) {
            view = layoutInflater.inflate(R.layout.post_item, parent, false);
            return new PostItemViewHolder(view);
        } else if (viewType == R.layout.network_state_item) {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false);
            return new NetworkStateItemViewHolder(view);
        } else {
            throw new IllegalArgumentException("unknown type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.post_item:
                ((PostItemViewHolder) holder).bindTo(getItem(position));
                break;
            case R.layout.network_state_item:
                ((NetworkStateItemViewHolder) holder).bindView(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.MAXPAGE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.post_item;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    static class PostItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        PostItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        void bindTo(Post post) {
            title.setText(Html.fromHtml(post.getTitle().getRendered()));
        }
    }

    static class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;
        private final TextView errorMsg;
        private Button button;

        NetworkStateItemViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            errorMsg = itemView.findViewById(R.id.error_msg);
            button = itemView.findViewById(R.id.retry_button);
            button.setOnClickListener(view -> {
            });
        }

        void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            if (networkState != null && networkState.getStatus() == Status.FAILED) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(networkState.getMsg());
            } else if (networkState != null && networkState.getStatus() == Status.MAX) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText("No More Page to Load");
            } else {
                errorMsg.setVisibility(View.GONE);
            }
        }
    }
}