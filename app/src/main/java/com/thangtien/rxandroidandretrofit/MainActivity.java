package com.thangtien.rxandroidandretrofit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridAdapter adapter;
    GridView gridView ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.grid_view);

        // Tạo dữ liệu mẫu
        List<GridItem> gridItems = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            gridItems.add(new GridItem("Item " + i, false));
        }

        adapter = new GridAdapter(this, gridItems);
        gridView.setAdapter(adapter);
        adapter.setGridView(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("thang.tien","onItemClick selecting = " + adapter.isSelecting() + "  - position: " + position);
                Toast.makeText(getApplicationContext(), "onItemClick selecting = " + adapter.isSelecting() + "  - position: " + position, Toast.LENGTH_SHORT).show();
                if (!adapter.isSelecting()) {
                    adapter.selectItem(position);
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "onItemLongClick - position: " + position, Toast.LENGTH_SHORT).show();
                Log.i("thang.tien","onItemLongClick - position: " + position);
                adapter.setIsSelecting(true);
                adapter.selectItem(position);
                return true;
            }
        });
    }
}