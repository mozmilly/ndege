package com.example.ndege.units.models;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class PaginationListener  extends RecyclerView.OnScrollListener {

    public static final int PAGE_START = 1;

    @NonNull
    private StaggeredGridLayoutManager layoutManager;

    private static final int PAGE_SIZE = 10;

    public PaginationListener(@NonNull StaggeredGridLayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int mSpanCount = 2;

        int[] into = new int[mSpanCount];

        int firstVisibleItem = layoutManager.findFirstVisibleItemPositions(into)[0];

        if (!isLoading() && !isLastPage()){
            if ((visibleItemCount + firstVisibleItem) >= totalItemCount
                && firstVisibleItem >= 0
                && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}
