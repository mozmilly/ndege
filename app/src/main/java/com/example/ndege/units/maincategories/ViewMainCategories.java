package com.example.ndege.units.maincategories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.maincategories.models.MainCategory;
import com.example.ndege.units.maincategories.models.MainCategoryAdapter;
import com.example.ndege.units.menucategories.ViewMenuCategories;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.utils.ApiUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMainCategories extends AppCompatActivity implements MainCategoryAdapter.OnItemClicked, MenuItemAdapter.OnItemClicked {

    RecyclerView recyclerView, menuItemRecycler;
    UnitInterface unitInterface;
    MainCategoryAdapter mainCategoryAdapter;
    List<MainCategory> mainCategoryList;
    Button add_category;

    MenuItemAdapter menuItemAdapter;
    List<MenuItems> menuItemsList;

    ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_main_categories);


        recyclerView = findViewById(R.id.main_cat_recycler);
        menuItemRecycler = findViewById(R.id.main_cat_menu_items_recycler);

        shimmerFrameLayout = findViewById(R.id.main_cat_container);

        SharedPreferences sharedPreferences = getSharedPreferences("Scope", 0);
        int unit_id = sharedPreferences.getInt("unit_id", 0);
        unitInterface = ApiUtils.getUnitService();
        unitInterface.get_this_main_categories(getIntent().getIntExtra("id", 0)).enqueue(new Callback<List<MainCategory>>() {
            @Override
            public void onResponse(Call<List<MainCategory>> call, Response<List<MainCategory>> response) {
                if (response.code()==200){

                    mainCategoryList = response.body();
                    mainCategoryAdapter = new MainCategoryAdapter(response.body(), ViewMainCategories.this);
                    LinearLayoutManager glm = new LinearLayoutManager(ViewMainCategories.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mainCategoryAdapter);
                    mainCategoryAdapter.setOnClick(ViewMainCategories.this);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<MainCategory>> call, Throwable throwable) {

            }
        });

        unitInterface.get_sub_core_cat_menu_items(getIntent().getIntExtra("id", 0)).enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewMainCategories.this);
                    menuItemRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    menuItemRecycler.setItemAnimator(new DefaultItemAnimator());
                    menuItemRecycler.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewMainCategories.this);
                    menuItemAdapter.notifyDataSetChanged();
                    menuItemRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<MenuItems>> call, Throwable t) {

            }
        });


    }

    @Override
    public void onCategoryItemClick(int position) {
        Intent intent = new Intent(ViewMainCategories.this, ViewMenuCategories.class);
        intent.putExtra("id", mainCategoryList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ViewMainCategories.this, ViewLargerImageActivity.class);
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
}
