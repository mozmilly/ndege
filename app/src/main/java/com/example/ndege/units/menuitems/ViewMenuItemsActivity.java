package com.example.ndege.units.menuitems;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.PaginationListener;
import com.example.ndege.utils.ApiUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ndege.units.models.PaginationListener.PAGE_START;

public class ViewMenuItemsActivity extends AppCompatActivity implements MenuItemAdapter.OnItemClicked, SwipeRefreshLayout.OnRefreshListener {
    MenuItemAdapter menuItemAdapter;
    UnitInterface unitInterface;
    RecyclerView recyclerView;
    List<MenuItems> menuItemsList;

    ShimmerFrameLayout shimmerFrameLayout;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_items2);

        recyclerView = findViewById(R.id.this_menuitems_recycler);

        shimmerFrameLayout = findViewById(R.id.menu_items_container);

        TextView label = findViewById(R.id.menu_cat_name);

        label.setText(getIntent().getStringExtra("menu_cat_name"));


        unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
        unitInterface.get_menu_items_api_view(getIntent().getIntExtra("id", 0), currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewMenuItemsActivity.this);
                    StaggeredGridLayoutManager glm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewMenuItemsActivity.this);
                    menuItemAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.addOnScrollListener(new PaginationListener(glm) {
                        @Override
                        protected void loadMoreItems() {
                            isLoading = true;
                            currentPage++;
                            doApiCall();

                        }

                        @Override
                        public boolean isLastPage() {
                            return isLastPage;
                        }

                        @Override
                        public boolean isLoading() {
                            return isLoading;
                        }
                    });

                }

            }

            @Override
            public void onFailure(Call<List<MenuItems>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(ViewMenuItemsActivity.this, ViewLargerImageActivity.class);
        intent.putExtra("image", menuItemsList.get(position).getImage());
        intent.putExtra("menu_item", "true");
        ViewCoreCategories.setMenuItems(menuItemsList.get(position));
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();

    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onRefresh() {

    }

    private void doApiCall() {
        final ArrayList<MenuItems> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = findViewById(R.id.this_progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
                unitInterface.get_menu_items_api_view(getIntent().getIntExtra("id", 0), currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
                    @Override
                    public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                        if (response.code()==200){
                            items.addAll(response.body());

                            /**
                             * manage progress view
                             */
                            if (currentPage != PAGE_START) menuItemAdapter.removeLoading();
                            menuItemAdapter.addItems(items);

                            // check weather is last page or not
                            if (response.body().size()==30){
                                menuItemAdapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                            progressBar.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<MenuItems>> call, Throwable t) {

                    }
                });
            }
        }, 1500);
    }

}
