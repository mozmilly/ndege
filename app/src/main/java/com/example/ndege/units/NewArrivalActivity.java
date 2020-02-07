package com.example.ndege.units;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndege.R;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.NewArrivalsAdapter;
import com.example.ndege.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewArrivalActivity extends AppCompatActivity implements NewArrivalsAdapter.OnNewArrivalItemClicked {

    RecyclerView newArrivalRecycler;
    NewArrivalsAdapter newArrivalsAdapter;
    List<MenuItems> newArrivalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arrival);

        newArrivalRecycler = findViewById(R.id.view_all_new_arrivals);

        getNewArrivals();


    }

    public void getNewArrivals(){
        UnitInterface unitInterface = ApiUtils.getUnitService();
        unitInterface.get_new_arrivals().enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    newArrivalList = response.body();

                    newArrivalsAdapter = new NewArrivalsAdapter(newArrivalList, NewArrivalActivity.this);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                    newArrivalRecycler.setLayoutManager(mLayoutManager);
                    newArrivalRecycler.setItemAnimator(new DefaultItemAnimator());
                    newArrivalRecycler.setAdapter(newArrivalsAdapter);
                    newArrivalsAdapter.setOnClick(NewArrivalActivity.this);
                    newArrivalsAdapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onFailure(Call<List<MenuItems>> call, Throwable throwable) {

            }
        });
    }


    @Override
    public void onNewArrivalClicked(int position) {

        Intent intent = new Intent(NewArrivalActivity.this, ViewLargerImageActivity.class);
        ViewCoreCategories.setMenuItems(newArrivalList.get(position));
        intent.putExtra("menu_item", "true");
        intent.putExtra("image", newArrivalList.get(position).getImage());
        startActivity(intent);

    }
}
