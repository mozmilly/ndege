package com.example.ndege.units.corecategories;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.ndege.R;
import com.example.ndege.adverts.MessageActivity;
import com.example.ndege.adverts.interfaces.AdvertInteface;
import com.example.ndege.adverts.models.Advert;
import com.example.ndege.adverts.models.AdvertAdapter;
import com.example.ndege.help.HelpActivity;
import com.example.ndege.login.NdegeTermsActivity;
import com.example.ndege.login.ProfileActivity;
import com.example.ndege.login.interfaces.LoginInterface;
import com.example.ndege.login.models.Login;
import com.example.ndege.tokens.interfaces.TokenInterface;
import com.example.ndege.tokens.models.TokenModel;
import com.example.ndege.units.ViewLargerImageActivity;
import com.example.ndege.units.corecategories.models.CoreCategory;
import com.example.ndege.units.corecategories.models.CoreCategoryAdapter;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.ImagePagerAdapter;
import com.example.ndege.units.models.MenuItemAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.MySearchAdapter;
import com.example.ndege.units.models.PaginationListener;
import com.example.ndege.units.models.PortfolioImage;
import com.example.ndege.units.orders.CheckOutSuccess;
import com.example.ndege.units.subcorecategories.ViewSubCoreCategories;
import com.example.ndege.utils.ApiUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ndege.units.models.PaginationListener.PAGE_START;

public class ViewCoreCategories extends AppCompatActivity implements CoreCategoryAdapter.OnItemClicked, MenuItemAdapter.OnItemClicked, AdvertAdapter.OnMenuItemClicked, MySearchAdapter.OnSearchItemClicked {

    private static final int REQUEST_CODE = 200;
    RecyclerView recyclerView, menuItemRecycler, mySearch, advertRecycler;
    UnitInterface unitInterface;
    CoreCategoryAdapter coreCategoryAdapter;
    MenuItemAdapter menuItemAdapter;
    List<MenuItems> menuItemsList;
    List<CoreCategory> coreCategoryList;
    int typeSelected = 0;


    private static MenuItems menuItems;

    public static MenuItems getMenuItems() {
        return menuItems;
    }

    public static void setMenuItems(MenuItems menuItems) {
        ViewCoreCategories.menuItems = menuItems;
    }

    ShimmerFrameLayout shimmerFrameLayout;

    AdvertInteface advertInteface;
    AdvertAdapter advertAdapter;
    private static List<Advert> advertList;

    public static List<Advert> getAdvertList() {
        return advertList;
    }

    public static void setAdvertList(List<Advert> advertList) {
        ViewCoreCategories.advertList = advertList;
    }

    private static Advert advert;

    public static Advert getAdvert() {
        return advert;
    }

    public static void setAdvert(Advert advert) {
        ViewCoreCategories.advert = advert;
    }

    EditText search;

    MySearchAdapter mySearchAdapter;
    List<MenuItems> menuItemSearchList = new ArrayList<>();


    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


    ViewPager viewPager;

    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    LinearLayout parent;

    Button help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_core_categories);

        recyclerView = findViewById(R.id.core_cat_recycler);
        menuItemRecycler = findViewById(R.id.core_cat_menu_items_recycler);

        search = findViewById(R.id.my_search1);
        mySearch = findViewById(R.id.search_item_recycler);

        advertRecycler = findViewById(R.id.advert_recycler_view);

        viewPager =  findViewById(R.id.viewPagerCC);
        sliderDotspanel = findViewById(R.id.SliderDotsCC);
        parent = findViewById(R.id.viewPagerParentCC);

        SharedPreferences sp=getSharedPreferences("pref",0);
        if (!sp.getBoolean("selected_type", false)){
            getChooseType();
        }

        Toast.makeText(this, String.valueOf(getSharedPreferences("pref", Context.MODE_PRIVATE).getBoolean("is_ndege_reseller", false)), Toast.LENGTH_SHORT).show();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
                            Toast.makeText(ViewCoreCategories.this, "Failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        TokenInterface tokenInterface = ApiUtils.getTokenService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
                        tokenInterface.store_token(token).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code()==200){
                                    Toast.makeText(ViewCoreCategories.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(DashBoardActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });



        Button orders = findViewById(R.id.view_orders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCoreCategories.this, CheckOutSuccess.class);
                startActivity(intent);
            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search.getText().toString().length()==0){
                    menuItemSearchList.clear();

                    if (menuItemAdapter!=null){
                        menuItemAdapter.notifyDataSetChanged();
                    }


                }
            }
        });

        TextView mine_search = findViewById(R.id.mine_search);

        mine_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnitInterface unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));

                unitInterface.get_all_menuitems(search.getText().toString()).enqueue(new Callback<List<MenuItems>>() {
                    @Override
                    public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                        if (response.code()==200){
                            menuItemSearchList = response.body();

                            mySearchAdapter = new MySearchAdapter(menuItemSearchList, ViewCoreCategories.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            mySearch.setLayoutManager(mLayoutManager);
                            mySearch.setItemAnimator(new DefaultItemAnimator());
                            mySearch.setAdapter(mySearchAdapter);
                            mySearchAdapter.setOnClick(ViewCoreCategories.this);
//                            close.setVisibility(View.VISIBLE);

//                            Toast.makeText(DashBoardActivity.this, "Gotten", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<MenuItems>> call, Throwable throwable) {

                    }
                });
            }
        });


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

        advertInteface = ApiUtils.get_advert_service(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
        Toast.makeText(this, getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"), Toast.LENGTH_SHORT).show();
        advertInteface.get_all_adverts().enqueue(new Callback<List<Advert>>() {
            @Override
            public void onResponse(Call<List<Advert>> call, Response<List<Advert>> response) {
                if (response.code()==200){
                    advertList = response.body();
                    advertAdapter = new AdvertAdapter(response.body(), ViewCoreCategories.this);
                    LinearLayoutManager glm = new LinearLayoutManager(ViewCoreCategories.this, RecyclerView.HORIZONTAL, false);
                    advertRecycler.setLayoutManager(glm);
                    advertRecycler.setItemAnimator(new DefaultItemAnimator());
                    advertRecycler.setAdapter(advertAdapter);
                    advertAdapter.setOnClick(ViewCoreCategories.this);

                    List<PortfolioImage> portfolioImages = new ArrayList<>();

                    for (Advert advert: response.body()){
                        portfolioImages.add(new PortfolioImage(advert.getMy_image()));
                    }


                    ImagePagerAdapter viewPagerAdapter = new ImagePagerAdapter(ViewCoreCategories.this, portfolioImages);
                    autoSlider(viewPager);
                    viewPager.setAdapter(viewPagerAdapter);
                    dotscount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotscount];

                    for(int i = 0; i < dotscount; i++){

                        dots[i] = new ImageView(ViewCoreCategories.this);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);

                        sliderDotspanel.addView(dots[i], params);

                    }
                    try{
                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    } catch (Exception ex){

                    }


                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                        }

                        @Override

                        public void onPageSelected(int position) {

                            for(int i = 0; i< dotscount; i++){
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                            }

                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Advert>> call, Throwable t)
            {

            }
        });

        unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
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
                    if (!response.body().isEmpty())
//                        Toast.makeText(ViewCoreCategories.this, "", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.VISIBLE);
                    coreCategoryAdapter.setOnClick(ViewCoreCategories.this);
                }
            }
            @Override
            public void onFailure(Call<List<CoreCategory>> call, Throwable throwable) {

            }
        });


        unitInterface.get_all_menu_items(currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
            @Override
            public void onResponse(Call<List<MenuItems>> call, Response<List<MenuItems>> response) {
                if (response.code()==200){
                    menuItemsList = response.body();

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    menuItemAdapter = new MenuItemAdapter(menuItemsList, ViewCoreCategories.this);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    menuItemRecycler.setLayoutManager(staggeredGridLayoutManager);
                    menuItemRecycler.setItemAnimator(new DefaultItemAnimator());
                    menuItemRecycler.setNestedScrollingEnabled(false);
                    menuItemRecycler.setAdapter(menuItemAdapter);
                    menuItemAdapter.setOnClick(ViewCoreCategories.this);
                    menuItemAdapter.notifyDataSetChanged();
                    menuItemRecycler.setHasFixedSize(true);

                }
            }

            @Override
            public void onFailure(Call<List<MenuItems>> call, Throwable t) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.this_home:

                    break;
                case R.id.this_help:
                    Intent intent = new Intent(ViewCoreCategories.this, HelpActivity.class);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    Intent intent1 = new Intent(ViewCoreCategories.this, ProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.orders:
                    Intent intent2 = new Intent(ViewCoreCategories.this, CheckOutSuccess.class);
                    startActivity(intent2);
                    break;
            }
            return true;
        });






    }

    @Override
    public void onCategoryItemClick(int position) {

        Intent intent = new Intent(ViewCoreCategories.this, ViewSubCoreCategories.class);
        intent.putExtra("id", coreCategoryList.get(position).getId());
        intent.putExtra("core_cat_name", coreCategoryList.get(position).getCategory_name());
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

    @Override
    public void onAdvertItemClick(int position) {
        Intent intent = new Intent(ViewCoreCategories.this, MessageActivity.class);
        setAdvert(advertList.get(position));
        startActivity(intent);

    }

    @Override
    public void onSearchItemClick(int position) {
        Intent intent = new Intent(ViewCoreCategories.this, ViewLargerImageActivity.class);
        ViewCoreCategories.setMenuItems(menuItemSearchList.get(position));
        intent.putExtra("menu_item", "true");
        intent.putExtra("image", menuItemSearchList.get(position).getImage());
        startActivity(intent);
    }

    private void doApiCall() {
        final List<MenuItems> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = findViewById(R.id.this_progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                unitInterface = ApiUtils.getUnitService(getSharedPreferences("Prefs", MODE_PRIVATE).getString("auth_token", "none"));
                unitInterface.get_all_menu_items(currentPage, "Ndege").enqueue(new Callback<List<MenuItems>>() {
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
    public void autoSlider(final ViewPager viewPager) {

        Runnable mUpdateResults = new Runnable() {
            public void run() {
                int page = viewPager.getCurrentItem();
                int numPages = viewPager.getAdapter().getCount();
                page = (page + 1) % numPages;
                viewPager.setCurrentItem(page);

            }
        };

        new Handler().postDelayed(mUpdateResults, 10);
    }

    public void getChooseType(){

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        CharSequence items[] = new CharSequence[] {"Ndege Reseller", "Retailer"};
        adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {
                typeSelected = n;
                Toast.makeText(ViewCoreCategories.this, String.valueOf(n), Toast.LENGTH_SHORT).show();
            }

        });
        adb.setTitle("Choose how you are going to use the platform..");
        adb.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (typeSelected == 0){
                    startActivity(new Intent(ViewCoreCategories.this, NdegeTermsActivity.class));
                } else if (typeSelected == 1){
                    SharedPreferences sp=getSharedPreferences("pref",0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("is_ndege_reseller", false);
                    editor.putBoolean("selected_type", true);
                    editor.apply();
                    dialog.dismiss();
                    recreate();
                }

            }
        });
        adb.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        AlertDialog dialog = adb.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
}
