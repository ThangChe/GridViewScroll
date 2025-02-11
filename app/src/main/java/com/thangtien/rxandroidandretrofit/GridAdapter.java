package com.thangtien.rxandroidandretrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final List<GridItem> gridItems;
    private final Context context;
    private GridView gridView;
    private int startPosition = -1;
    private int lastTouchedPosition = -1;
    private boolean isSelecting = false;
    private int scrollOffset = 0;
    private int scrollSpeed = 10;

    public boolean isSelecting() {
        return isSelecting;
    }

    public GridAdapter(Context context, List<GridItem> gridItems) {
        this.context = context;
        this.gridItems = gridItems;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setGridView(GridView gridView) {
        this.gridView = gridView;
        gridView.setOnTouchListener((v, event) -> {
            if (isSelecting) {
                handleExternalTouch(event);
            }
            return false;
        });
    }

    @Override
    public int getCount() {
        return gridItems.size();
    }

    @Override
    public Object getItem(int position) {
        return gridItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.item_checkbox);
            holder.textView = convertView.findViewById(R.id.item_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridItem item = gridItems.get(position);
        holder.checkBox.setChecked(item.isChecked());
        holder.textView.setText(item.getText());

        convertView.setOnTouchListener((v, event) -> {
            int currentPosition = position;
            if (isSelecting) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPosition = currentPosition;
                        lastTouchedPosition = currentPosition;
                        selectItem(currentPosition);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (currentPosition != lastTouchedPosition) {
                            lastTouchedPosition = currentPosition;
                            selectRange(startPosition, currentPosition);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
            }
            return true;
        });

        return convertView;
    }

    private void selectRange(int start, int end) {
        if (start == -1 || end == -1) return;

        int min = Math.min(start, end);
        int max = Math.max(start, end);
        boolean select = !gridItems.get(start).isChecked();

        for (int i = min; i <= max; i++) {
            GridItem item = gridItems.get(i);
            item.setChecked(select);
            notifyDataSetChanged();
            autoScroll(i);
        }
    }

    public void selectItem(int position) {
        if (position >= 0 && position < gridItems.size()) {
            GridItem item = gridItems.get(position);
            item.setChecked(!item.isChecked());
            notifyDataSetChanged();
            autoScroll(position);
        }
    }

    private void autoScroll(int position) {
        if (gridView == null) return;
        Rect rect = new Rect();
        View view = gridView.getChildAt(position - gridView.getFirstVisiblePosition());
        if (view != null) {
            view.getGlobalVisibleRect(rect);
            int screenHeight = gridView.getHeight();
            if (rect.bottom > screenHeight) {
                gridView.smoothScrollBy(0, rect.bottom - screenHeight);
            } else if (rect.top < 0) {
                gridView.smoothScrollBy(0, rect.top);
            }
        }
    }

    private void handleExternalTouch(MotionEvent event) {
        if (gridView == null) return;
        int[] location = new int[2];
        gridView.getLocationOnScreen(location);
        int gridViewTop = location[1];
        int gridViewBottom = gridViewTop + gridView.getHeight();
        int y = (int) event.getRawY();

        if (y < gridViewTop) {
            scrollOffset = -scrollSpeed;
        } else if (y > gridViewBottom) {
            scrollOffset = scrollSpeed;
        } else {
            scrollOffset = 0;
        }
        if (scrollOffset != 0) {
            gridView.scrollBy(0, scrollOffset);
            int firstVisibleItemPosition = gridView.getFirstVisiblePosition();
            int lastVisibleItemPosition = gridView.getLastVisiblePosition();
            if (lastTouchedPosition != -1) {
                if (scrollOffset > 0) {
                    if (lastVisibleItemPosition < gridItems.size() - 1) {
                        lastTouchedPosition = lastVisibleItemPosition + 1;
                        selectRange(startPosition, lastTouchedPosition);
                    }
                } else {
                    if (firstVisibleItemPosition > 0) {
                        lastTouchedPosition = firstVisibleItemPosition - 1;
                        selectRange(startPosition, lastTouchedPosition);
                    }
                }
            }
        }
    }

    public void setIsSelecting(boolean isSelecting) {
        this.isSelecting = isSelecting;
        if (!isSelecting) {
            startPosition = -1;
            lastTouchedPosition = -1;
            scrollOffset = 0;
        }
    }

    public static class ViewHolder {
        public CheckBox checkBox;
        public TextView textView;
    }
}