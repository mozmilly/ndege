package com.example.ndege.units;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.ndege.R;
import com.example.ndege.units.corecategories.ViewCoreCategories;
import com.example.ndege.units.interfaces.UnitInterface;
import com.example.ndege.units.models.ExtraField;
import com.example.ndege.units.models.ExtraFieldsAdapter;
import com.example.ndege.units.models.ImagePagerAdapter;
import com.example.ndege.units.models.MenuItems;
import com.example.ndege.units.models.MyCart;
import com.example.ndege.units.models.PortfolioImage;
import com.example.ndege.utils.ApiUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewLargerImageActivity extends AppCompatActivity {
    TextView name, description, price, no_of_pieces, available;
    Button addToCart, sendInquiry, whatsapp;
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    private static int var;
    public static int getVar() {
        return var;
    }

    private static  String unit_name;

    private static List<MyCart> cart = new ArrayList<>();

    public static List<MyCart> getCart() {
        return cart;
    }



    public static List<MenuItems> fixedList = new ArrayList<>();

    ViewPager viewPager;

    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    LinearLayout parent;


    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    RecyclerView recyclerView;
    ExtraFieldsAdapter extraFieldsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_larger_image);

        recyclerView = findViewById(R.id.extra_fields_recycler);

        parent = findViewById(R.id.viewPagerParent);


        viewPager =  findViewById(R.id.viewPagerMI);
        sliderDotspanel = findViewById(R.id.SliderDotsMI);

        whatsapp = findViewById(R.id.share_whatsapp_btn);

        sendInquiry = findViewById(R.id.my_inquiry_btn);
        name = findViewById(R.id.large_image_name);
        description = findViewById(R.id.large_image_description);
        price = findViewById(R.id.large_image_price);
        no_of_pieces = findViewById(R.id.large_image_no_of_pieces);
        addToCart = findViewById(R.id.large_image_add_to_cart);
        available = findViewById(R.id.large_image_available);
        available.setVisibility(View.GONE);
        ImageView imageView = findViewById(R.id.my_large_image);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    zoomImageFromThumb(imageView, ((BitmapDrawable) imageView.getDrawable()).getBitmap());
                }
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        MenuItems menuItems = ViewCoreCategories.getMenuItems();

        UnitInterface unitInterface = ApiUtils.getUnitService();


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = imageView.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                Bitmap bitmap = bitmapDrawable .getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                String path = MediaStore.Images.Media.insertImage(ViewLargerImageActivity.this.getContentResolver(), bitmap, "Title", null);

                Uri imgUri = Uri.parse(path);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, menuItems.getItem_name()+"\n"+menuItems.getDescription());
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Log.d("TAG", ex.getMessage());
                }

            }
        });

        unitInterface.get_extra_fields(menuItems.getId()).enqueue(new Callback<List<ExtraField>>() {
            @Override
            public void onResponse(Call<List<ExtraField>> call, Response<List<ExtraField>> response) {
                if (response.code()==200){
                    extraFieldsAdapter = new ExtraFieldsAdapter(response.body(), ViewLargerImageActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(extraFieldsAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<ExtraField>> call, Throwable t) {

            }
        });

        unitInterface.get_menu_item_images(menuItems.getId()).enqueue(new Callback<List<PortfolioImage>>() {
            @Override
            public void onResponse(Call<List<PortfolioImage>> call, Response<List<PortfolioImage>> response) {
                ImagePagerAdapter viewPagerAdapter = new ImagePagerAdapter(ViewLargerImageActivity.this, response.body());

                viewPager.setAdapter(viewPagerAdapter);
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for(int i = 0; i < dotscount; i++){

                    dots[i] = new ImageView(ViewLargerImageActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }
                try{
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                } catch (Exception ex){
                    parent.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
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

            @Override
            public void onFailure(Call<List<PortfolioImage>> call, Throwable throwable) {

            }
        });


        sendInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewLargerImageActivity.this, SendInquiryActivity.class);
                intent.putExtra("name", "item");
                intent.putExtra("content", menuItems.getItem_name()+" "+menuItems.getDescription());
                startActivity(intent);

            }
        });
        Glide.with(ViewLargerImageActivity.this)
                .load("https://bombaservices.pythonanywhere.com"+getIntent().getStringExtra("image"))
                .into(imageView);

        if (Objects.requireNonNull(getIntent().getStringExtra("menu_item")).equalsIgnoreCase("true")){

            name.setText(("Product Name: "+menuItems.getItem_name()));
            description.setText(menuItems.getDescription());
            price.setText(("Price: Ksh."+menuItems.getPrice()));
            no_of_pieces.setText(("Min Order: "+menuItems.getMinimum_order()));
            available.setText(("Available: "+menuItems.getItems_in_stock()));

        }

        int quantity = 0;
        double total_price = 0;
        Gson g = new Gson();
        sharedPreferences = getSharedPreferences("Cart", 0);
        String j = sharedPreferences.getString("MY_CART", "");
        List<MyCart> arrayList = new ArrayList<>();
        try{
            Type type = new TypeToken<List<MyCart>>() {}.getType();
            arrayList = new ArrayList<>(g.fromJson(j, type));

            for (MyCart myCart: arrayList){
                quantity += myCart.getQuantity();
                total_price += myCart.getQuantity()*myCart.getMenuItems().getPrice();
            }

        } catch (Exception ex){

        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(menuItems);
            }
        });

        Button btn = findViewById(R.id.large_image_checkout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewLargerImageActivity.this, ClientDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addToCart(MenuItems menuItems){
        SharedPreferences sp = getSharedPreferences("Cart", 0);
        Gson g = new Gson();
        String j = sp.getString("MY_CART", "");
        List<MyCart> arrayList = new ArrayList<>();
        try{
            Type type = new TypeToken<List<MyCart>>() {}.getType();
            arrayList = new ArrayList<>(g.fromJson(j, type));
        } catch (Exception ex){

        }

        cart.clear();


        HashSet hs = new HashSet();

        for (MyCart mi: arrayList){
            if (mi.getMenuItems().getItem_name().equalsIgnoreCase(menuItems.getItem_name())){


            } else {
                hs.add(mi);
            }

        }
        arrayList.clear();
        arrayList.addAll(hs);
        cart.addAll(arrayList);
        if (j.equals("")){

            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            MyCart cartItem = new MyCart(menuItems, 1);

            cart.add(cartItem);
            String json = gson.toJson(cart);
            editor.putInt("id", var);
            editor.putString("unit_name", unit_name);
            editor.putString("MY_CART", json);
            editor.apply();

        } else {

            if (cart.contains(new MyCart(menuItems, 1))) {
                Toast.makeText(this, "Already in cart", Toast.LENGTH_SHORT).show();
            } else {
                if (sharedPreferences.getInt("id", 0) == 0) {
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    MyCart cartItem = new MyCart(menuItems, 1);
                    cart.add(cartItem);
                    String json = gson.toJson(cart);
                    editor.putInt("id", var);
                    editor.putString("unit_name", unit_name);
                    editor.putString("MY_CART", json);
                    editor.apply();
                } else if (var == sharedPreferences.getInt("id", 0)) {
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    MyCart cartItem = new MyCart(menuItems, 1);
                    cart.add(cartItem);
                    String json = gson.toJson(cart);
                    editor.putInt("id", var);
                    editor.putString("unit_name", unit_name);
                    editor.putString("MY_CART", json);
                    editor.apply();
                } else {
                    SharedPreferences sp2 = getSharedPreferences("Sub_name", 0);
                    if (sp2.getString("service_name", "").equalsIgnoreCase("Food")){
                        cart.clear();
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    MyCart cartItem = new MyCart(menuItems, 1);
                    cart.add(cartItem);
                    String json = gson.toJson(cart);
                    editor.putInt("id", var);
                    editor.putString("unit_name", unit_name);
                    editor.putString("MY_CART", json);
                    editor.apply();
                }
            }

            refresh();
        }

        refresh();

    }


    private void refresh(){
        int quantity = 0;
        double total_price = 0;
        Gson g2 = new Gson();
        String j2 = sharedPreferences.getString("MY_CART", "");
        List<MyCart> arrayList2 = new ArrayList<>();
        try{
            Type type = new TypeToken<List<MyCart>>() {}.getType();
            arrayList2 = new ArrayList<>(g2.fromJson(j2, type));

            for (MyCart myCart: arrayList2){
                quantity += myCart.getQuantity();
                total_price += myCart.getQuantity()*myCart.getMenuItems().getPrice();
            }

        } catch (Exception ex){

        }

    }

    private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageBitmap(bitmap);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
