package com.example.ndege.units;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.example.ndege.units.interfaces.MentionInterface;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.DiscountedItemsAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.Unit;
import com.example.ndege.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllDiscounted extends AppCompatActivity implements DiscountedItemsAdapter.OnDealItemClicked {
    RecyclerView discountRecycler;
    DiscountedItemsAdapter discountedItemsAdapter;
    List<MenuItems> menuItemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_discounted);

        discountRecycler = findViewById(R.id.top_deals_recycler_view);

        MentionInterface mentionInterface = ApiUtils.getMentionService();

        mentionInterface.get_discounted_items().enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                    discountRecycler.setLayoutManager(mLayoutManager);
                    discountRecycler.setItemAnimator(new DefaultItemAnimator());
                    discountedItemsAdapter = new DiscountedItemsAdapter(menuItemsList, ViewAllDiscounted.this);
                    discountRecycler.setAdapter(discountedItemsAdapter);
                    discountedItemsAdapter.setOnClick(ViewAllDiscounted.this);
                    discountedItemsAdapter.notifyDataSetChanged();
                    Toast.makeText(ViewAllDiscounted.this, String.valueOf(menuItemsList.toString()), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<MenuItems>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onDealItemClick(int position) {
        UnitInterface unitInterface = ApiUtils.getUnitService();

        unitInterface.get_unit(menuItemsList.get(position).getMenu_category().getId().intValue()).enqueue(new Callback<Unit>() {
            @Override
            public void onResponse(Call<Unit> call, Response<Unit> response) {
                if (response.code()==200){
                    Intent intent = new Intent(ViewAllDiscounted.this, ViewLargerImageActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Unit> call, Throwable throwable) {

            }
        });

    }
}
