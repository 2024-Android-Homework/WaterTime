package com.wychlw.watertime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private Context context;
    private static final int EDIT_DIARY_REQUEST = 2;
    private static final int ADD_DIARY_REQUEST = 1;

    public NoteAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.tvDate.setText(note.getDate().toString());
        holder.tvWaterAmount.setText(String.format(Locale.getDefault(), "%d ml", note.getWaterAmount()));

        if (note.hasDiary()) {
            holder.layoutDiaryContent.setVisibility(View.VISIBLE);
            holder.btnAddDiary.setVisibility(View.GONE);
            holder.tvContent.setText(note.getContent());

            holder.btnEditDiary.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddEditDiaryActivity.class);
                intent.putExtra(AddEditDiaryActivity.EXTRA_DATE, note.getDate().toString());
                intent.putExtra(AddEditDiaryActivity.EXTRA_CONTENT, note.getContent());
                ((Activity) context).startActivityForResult(intent, EDIT_DIARY_REQUEST);
            });
        } else {
            holder.layoutDiaryContent.setVisibility(View.GONE);
            holder.btnAddDiary.setVisibility(View.VISIBLE);

            holder.btnAddDiary.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddEditDiaryActivity.class);
                intent.putExtra(AddEditDiaryActivity.EXTRA_DATE, note.getDate().toString());
                ((Activity) context).startActivityForResult(intent, ADD_DIARY_REQUEST);
            });
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvWaterAmount;
        TextView tvContent;
        Button btnAddDiary;
        Button btnEditDiary;
        LinearLayout layoutDiaryContent;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWaterAmount = itemView.findViewById(R.id.tv_water_amount);
            tvContent = itemView.findViewById(R.id.tv_content);
            btnAddDiary = itemView.findViewById(R.id.btn_add_diary);
            btnEditDiary = itemView.findViewById(R.id.btn_edit_diary);
            layoutDiaryContent = itemView.findViewById(R.id.layout_diary_content);
        }
    }
}
