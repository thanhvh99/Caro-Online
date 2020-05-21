package com.mobile.caro.TwoPlayersOnlineActivity.Activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.mobile.caro.R;
import com.mobile.caro.TwoPlayersOnlineActivity.Network.SocketHandler;

import org.json.JSONObject;

public class ImageSelectorActivity extends FragmentActivity {

    private String[] images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_image_selector);

        images = getResources().getStringArray(R.array.images);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                try {
                    JSONObject object = new JSONObject();
                    object.put("imageUrl", images[index]);
                    SocketHandler.emit("image", object);
                    SocketHandler.emit("information");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        };

        int row = 6;
        int col = 3;

        Resources resources = getResources();
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, resources.getDisplayMetrics());

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(size, size);

        for (int i = 0; i < row; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < col; j++) {
                int index = i * col + j;
                ImageView imageView = new ImageView(this);
                imageView.setTag(index);
                imageView.setLayoutParams(layoutParams);
                imageView.setOnClickListener(listener);
                int id = resources.getIdentifier(images[index], "drawable", getPackageName());
                Glide.with(this).load(id).into(imageView);
                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);
        }
    }
}
