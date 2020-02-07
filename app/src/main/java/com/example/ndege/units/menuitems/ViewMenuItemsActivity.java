package com.example.ndege.units.menuitems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMenuItemsActivity extends AppCompatActivity implements MenuItemAdapter.OnItemClicked {
    MenuItemAdapter menuItemAdapter;
    UnitInterface unitInterface;
    RecyclerView recyclerView;
    List<MenuItems> menuItemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_items2);

        recyclerView = findViewById(R.id.this_menuitems_recycler);


        unitInterface = ApiUtils.getUnitService();
        unitInterface.get_menu_items_api_view(getIntent().getIntExtra("id", 0)).enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();


                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewMenuItemsActivity.this);
                    GridLayoutManager glm = new GridLayoutManager(ViewMenuItemsActivity.this, 2);
                    recyclerView.setLayoutManager(glm);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewMenuItemsActivity.this);
                    menuItemAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);

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
}
