package com.thangtien.rxandroidandretrofit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG = Constant.PERFIX + "MainActivity";
    private GridAdapter girdAdapter;
    private RecyclerViewAdapter recyclerViewAdapter;
    GridView gridView ;
    RecyclerView recyclerView;
    CustomLinearLayoutManager linearLayoutManager;
    boolean isMultiSelectionForRecyclerView = false;
    int startPositionForRecyclerView = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.grid_view);
        recyclerView = findViewById(R.id.recycler_view);

        // Tạo dữ liệu mẫu
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            items.add(new Item("Item " + i, false));
        }

        girdAdapter = new GridAdapter(this, items);
        gridView.setAdapter(girdAdapter);
        girdAdapter.setGridView(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("thang.tien","onItemClick selecting = " + girdAdapter.isSelecting() + "  - position: " + position);
                Toast.makeText(getApplicationContext(), "onItemClick selecting = " + girdAdapter.isSelecting() + "  - position: " + position, Toast.LENGTH_SHORT).show();
                if (!girdAdapter.isSelecting()) {
                    girdAdapter.selectItem(position);
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "onItemLongClick - position: " + position, Toast.LENGTH_SHORT).show();
                Log.i("thang.tien","onItemLongClick - position: " + position);
                Log.i("thang.tien","ahfjkhsd");
                girdAdapter.setIsSelecting(true);
                girdAdapter.selectItem(position);
                return true;
            }
        });


        linearLayoutManager = new CustomLinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(this, items, eventListener);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG,"onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isMultiSelectionForRecyclerView){
                            View viewChild = recyclerView.findChildViewUnder(event.getX(), event.getY());
                            int position = -1;
                            if(viewChild != null){
                                position = recyclerView.getChildAdapterPosition(viewChild);
                                if (position != -1 && position != startPositionForRecyclerView){
                                    recyclerViewAdapter.selectItem(position);
                                }
                                if(position == -1 || position == linearLayoutManager.findLastVisibleItemPosition()){
                                    recyclerView.smoothScrollBy(0, 200, null, 40);
                                }
                            }
                            Log.i(TAG,"onTouch ACTION_MOVE " + position + " - last: " + linearLayoutManager.findLastVisibleItemPosition());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG,"onTouch ACTION_UP");
                        isMultiSelectionForRecyclerView = false;
                        startPositionForRecyclerView = -1;
                        if (linearLayoutManager != null) {
                            linearLayoutManager.setScrollEnabled(true);
                        }
                        break;
                }
                return false;
            }
        });
    }
    public RecyclerViewAdapter.EventListener eventListener = new RecyclerViewAdapter.EventListener() {
        @Override
        public void onClick(int position) {
            isMultiSelectionForRecyclerView = false;
            startPositionForRecyclerView = -1;
            recyclerViewAdapter.selectItem(position);

        }

        @Override
        public void onLongClick(int position) {
            recyclerViewAdapter.selectItem(position);
            isMultiSelectionForRecyclerView = true;
            startPositionForRecyclerView = position;
            if (linearLayoutManager != null) {
                linearLayoutManager.setScrollEnabled(false);
            }
        }
    };
}