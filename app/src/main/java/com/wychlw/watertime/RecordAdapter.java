package com.wychlw.watertime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    private List<Record> mRecordList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View historyView;
        ImageView historyItemImage;
        TextView historyItemName;
        TextView volume_time;

        public ViewHolder(View view) {
            super(view);
            historyView = view;
            historyItemImage = (ImageView) view.findViewById(R.id.historyItem_image);
            historyItemName = (TextView) view.findViewById(R.id.historyItem_name);
            volume_time = (TextView) view.findViewById(R.id.historyItem_text);
        }
    }

    public RecordAdapter(List<Record> recordList) {
        mRecordList = recordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = mRecordList.get(position);
        holder.historyItemImage.setImageResource(record.getImageId());
        holder.historyItemName.setText(record.getDrinkName());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = record.getCurrent().format(formatter);
            holder.volume_time.setText(record.getVolume() + "ml" + "\r\n" +
                    "Recorded at " + formattedTime);
        }
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }
}
