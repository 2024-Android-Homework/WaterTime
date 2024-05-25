package com.wychlw.watertime;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder>{

    private List<Drink> mDrinkList;

    private static OnButtonClickListener mListener;

    public interface OnButtonClickListener {
        void onButtonClick(Record record);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View drinkView;
        ImageView drinkImage;
        TextView drinkName;
        EditText drinkEdit;
        Button drinkButton;
        int imageId;

        public ViewHolder(View view) {
            super(view);
            drinkView = view;
            drinkImage = (ImageView) view.findViewById(R.id.drink_image);
            drinkName = (TextView) view.findViewById(R.id.drink_name);
            drinkEdit = (EditText) view.findViewById(R.id.drink_edit);
            drinkButton = (Button) view.findViewById(R.id.drink_button);
            drinkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the value from EditText and pass it to the listener
                    if (drinkEdit.getText().toString().equals("")) {
                        Toast.makeText(v.getContext(),
                                "请输入喝水量",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int add = Integer.parseInt(drinkEdit.getText().toString());
                    if(20 > add || add > 2000){
                        Toast.makeText(v.getContext(),
                                "喝水量 n范围：20ml-2000ml",Toast.LENGTH_SHORT).show();
                    }
                    else if (mListener != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mListener.onButtonClick(new Record(
                                    drinkName.getText().toString(), imageId,
                                    add, LocalDateTime.now()));
                        }
                        drinkEdit.setText("");
                    }
                }
            });
        }
    }

    public DrinkAdapter(List<Drink> drinkList) {
        mDrinkList = drinkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drink fruit = mDrinkList.get(position);
        holder.imageId = fruit.getImageId();
        holder.drinkImage.setImageResource(holder.imageId);
        holder.drinkName.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return mDrinkList.size();
    }

}