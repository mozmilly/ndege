package com.example.ndege.units.menucategories;

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
import com.example.ndege.units.menucategories.models.MenuCategory;
import com.example.ndege.units.menucategories.models.MenuCategoryAdapter;
import com.example.ndege.units.menuitems.ViewMenuItemsActivity;
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

public class ViewMenuCategories extends AppCompatActivity implements MenuCategoryAdapter.OnItemClicked, MenuItemAdapter.OnItemClicked {
    RecyclerView recyclerView, menuItemRecycler;
    UnitInterface unitInterface;
    MenuCategoryAdapter menuCategoryAdapter;
    List<MenuCategory> menuCategoryList;

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
        setContentView(R.layout.activity_view_menu_categories);

        recyclerView = findViewById(R.id.menu_cat_recycler);
        menuItemRecycler = findViewById(R.id.menu_cat_menu_items_recycler);

        shimmerFrameLayout = findViewById(R.id.menu_cat_container);

        TextView label = findViewById(R.id.main_cat_name);

        label.setText(getIntent().getStringExtra("main_cat_name"));


        unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
        unitInterface.get_main_category_menu_category(getIntent().getIntExtra("id", 0)).enqueue(new Callback<List<MenuCategory>>() {
            @Override
            public void onResponse(Call<List<MenuCategory>> call, Response<List<MenuCategory>> response) {
                if (response.code()==200){
                    menuCategoryList = response.body();

                    menuCategoryAdapter = new MenuCategoryAdapter(menuCategoryList, ViewMenuCategories.this);
                    LinearLayoutManager glm = new LinearLayoutManager(ViewMenuCategories.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(menuCategoryAdapter);
                    menuCategoryAdapter.setOnClick(ViewMenuCategories.this);
                    menuCategoryAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<MenuCategory>> call, Throwable throwable) {

            }
        });


        unitInterface.get_main_cat_menu_items(getIntent().getIntExtra("id", 0), currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();


                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewMenuCategories.this);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    menuItemRecycler.setLayoutManager(staggeredGridLayoutManager);
                    menuItemRecycler.setItemAnimator(new DefaultItemAnimator());
                    menuItemRecycler.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewMenuCategories.this);
                    menuItemAdapter.notifyDataSetChanged();
                    menuItemRecycler.setVisibility(View.VISIBLE);
                    recyclerView.addOnScrollListener(new PaginationListener(staggeredGridLayoutManager) {
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
        Intent intent = new Intent(ViewMenuCategories.this, ViewMenuItemsActivity.class);
        intent.putExtra("id", menuCategoryList.get(position).getId());
        intent.putExtra("menu_cat_name", menuCategoryList.get(position).getCat_name());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(ViewMenuCategories.this, ViewLargerImageActivity.class);
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
                unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
                unitInterface.get_main_cat_menu_items(getIntent().getIntExtra("id", 0), currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
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
