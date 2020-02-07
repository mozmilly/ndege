package com.example.ndege.units.corecategories;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.models.CoreCategory;
import com.example.ndege.units.corecategories.models.CoreCategoryAdapter;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.subcorecategories.ViewSubCoreCategories;
import com.example.ndege.utils.ApiUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCoreCategories extends AppCompatActivity implements CoreCategoryAdapter.OnItemClicked, MenuItemAdapter.OnItemClicked {

    private static final int REQUEST_CODE = 200;
    RecyclerView recyclerView, menuItemRecycler;
    UnitInterface unitInterface;
    CoreCategoryAdapter coreCategoryAdapter;
    MenuItemAdapter menuItemAdapter;
    List<MenuItems> menuItemsList;
    List<CoreCategory> coreCategoryList;


    private static MenuItems menuItems;

    public static MenuItems getMenuItems() {
        return menuItems;
    }

    public static void setMenuItems(MenuItems menuItems) {
        ViewCoreCategories.menuItems = menuItems;
    }

    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_core_categories);

        recyclerView = findViewById(R.id.core_cat_recycler);
        menuItemRecycler = findViewById(R.id.core_cat_menu_items_recycler);

        shimmerFrameLayout = findViewById(R.id.core_cat_container);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                //File write logic here

            } else {
                Toast.makeText(this, "Allow permission to be able to share images", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }

        unitInterface = ApiUtils.getUnitService();
        unitInterface.get_all_core_categories().enqueue(new Callback<List<CoreCategory>>() {
            @Override
            public void onResponse(Call<List<CoreCategory>> call, Response<List<CoreCategory>> response) {
                if (response.code()==200){
                    coreCategoryList = response.body();
                    coreCategoryAdapter = new CoreCategoryAdapter(response.body(), ViewCoreCategories.this);
                    LinearLayoutManager glm = new LinearLayoutManager(ViewCoreCategories.this, RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(coreCategoryAdapter);
                    coreCategoryAdapter.setOnClick(ViewCoreCategories.this);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<CoreCategory>> call, Throwable throwable) {

            }
        });


        unitInterface.get_all_menu_items().enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewCoreCategories.this);
                    menuItemRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    menuItemRecycler.setNestedScrollingEnabled(false);
                    menuItemRecycler.setItemAnimator(new DefaultItemAnimator());
                    menuItemRecycler.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewCoreCategories.this);
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

        Intent intent = new Intent(ViewCoreCategories.this, ViewSubCoreCategories.class);
        intent.putExtra("id", coreCategoryList.get(position).getId());
        startActivity(intent);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ViewCoreCategories.this, ViewLargerImageActivity.class);
        intent.putExtra("image", menuItemsList.get(position).getImage());
        intent.putExtra("menu_item", "true");
        ViewCoreCategories.setMenuItems(menuItemsList.get(position));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
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
