package com.example.ndege.units.subcorecategories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.example.ndege.units.subcorecategories.models.SubCoreCategory;
import com.example.ndege.units.subcorecategories.models.SubCoreCategoryAdapter;
import com.example.ndege.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSubCoreCategories extends AppCompatActivity implements SubCoreCategoryAdapter.OnItemClicked, MenuItemAdapter.OnItemClicked {

    RecyclerView recyclerView, menuItemRecycler;
    UnitInterface unitInterface;
    SubCoreCategoryAdapter subCoreCategoryAdapter;
    List<SubCoreCategory> subCoreCategoryList;

    MenuItemAdapter menuItemAdapter;
    List<MenuItems> menuItemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sub_core_categories);

        recyclerView = findViewById(R.id.sub_core_category_recycler);
        menuItemRecycler = findViewById(R.id.sub_core_category_menu_items_recycler);

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

        unitInterface.get_core_cat_menu_items(getIntent().getIntExtra("id", 0)).enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();


                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewSubCoreCategories.this);
                    menuItemRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    menuItemRecycler.setItemAnimator(new DefaultItemAnimator());
                    menuItemRecycler.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewSubCoreCategories.this);
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
        Intent intent = new Intent(ViewSubCoreCategories.this, ViewMainCategories.class);
        intent.putExtra("id", subCoreCategoryList.get(position).getId());
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
}
