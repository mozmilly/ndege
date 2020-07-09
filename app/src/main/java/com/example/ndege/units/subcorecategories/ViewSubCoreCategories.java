package com.example.ndege.units.subcorecategories;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.maincategories.ViewMainCategories;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.PaginationListener;
import com.example.ndege.units.subcorecategories.models.SubCoreCategory;
import com.example.ndege.units.subcorecategories.models.SubCoreCategoryAdapter;
import com.example.ndege.utils.ApiUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ndege.units.models.PaginationListener.PAGE_START;

public class ViewSubCoreCategories extends AppCompatActivity implements SubCoreCategoryAdapter.OnItemClicked, MenuItemAdapter.OnItemClicked {

    RecyclerView recyclerView, menuItemRecycler;
    UnitInterface unitInterface;
    SubCoreCategoryAdapter subCoreCategoryAdapter;
    List<SubCoreCategory> subCoreCategoryList;

    MenuItemAdapter menuItemAdapter;
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
        setContentView(R.layout.activity_view_sub_core_categories);

        recyclerView = findViewById(R.id.sub_core_category_recycler);
        menuItemRecycler = findViewById(R.id.sub_core_category_menu_items_recycler);

        shimmerFrameLayout = findViewById(R.id.sub_core_cat_container);

        TextView label = findViewById(R.id.core_cat_name);

        label.setText(getIntent().getStringExtra("core_cat_name"));

        unitInterface = ApiUtils.getUnitService();
        unitInterface.get_sub_core_categories(getIntent().getIntExtra("id", 0)).enqueue(new Callback<List<SubCoreCategory>>() {
            @Override
            public void onResponse(Call<List<SubCoreCategory>> call, Response<List<SubCoreCategory>> response) {
                if (response.code()==200){

                    subCoreCategoryList = response.body();
                    subCoreCategoryAdapter = new SubCoreCategoryAdapter(response.body(), ViewSubCoreCategories.this);
                    LinearLayoutManager glm = new LinearLayoutManager(ViewSubCoreCategories.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(subCoreCategoryAdapter);
                    subCoreCategoryAdapter.setOnClick(ViewSubCoreCategories.this);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SubCoreCategory>> call, Throwable throwable) {

            }
        });

        unitInterface.get_core_cat_menu_items(getIntent().getIntExtra("id", 0), currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewSubCoreCategories.this);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    menuItemRecycler.setLayoutManager(staggeredGridLayoutManager);
                    menuItemRecycler.setItemAnimator(new DefaultItemAnimator());
                    menuItemRecycler.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewSubCoreCategories.this);
                    menuItemAdapter.notifyDataSetChanged();
                    menuItemRecycler.setVisibility(View.VISIBLE);
                    menuItemRecycler.addOnScrollListener(new PaginationListener(staggeredGridLayoutManager) {
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
            public void onFailure(Call<List<MenuItems>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCategoryItemClick(int position) {
        Intent intent = new Intent(ViewSubCoreCategories.this, ViewMainCategories.class);
        intent.putExtra("id", subCoreCategoryList.get(position).getId());
        intent.putExtra("sub_core_cat_name", subCoreCategoryList.get(position).getCategory_name());
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ViewSubCoreCategories.this, ViewLargerImageActivity.class);
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

    private void doApiCall() {
        final List<MenuItems> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = findViewById(R.id.this_progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                unitInterface = ApiUtils.getUnitService();
                unitInterface.get_core_cat_menu_items(getIntent().getIntExtra("id", 0), currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
                    @Override
                    public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                        if (response.code() == 200) {
                            items.addAll(response.body());

                            /**
                             * manage progress view
                             */
                            if (currentPage != PAGE_START) menuItemAdapter.removeLoading();
                            menuItemAdapter.addItems(items);

                            // check weather is last page or not
                            if (response.body().size() == 30) {
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
