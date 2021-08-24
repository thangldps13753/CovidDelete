package com.example.coviddelete.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coviddelete.MainActivity;
import com.example.coviddelete.R;
import com.example.coviddelete.api.CountryData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    Context context;
    List<CountryData> list;

    public CountryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_countries, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.countryCases.setText(NumberFormat.getInstance().
                format(Integer.parseInt(list.get(position).getCases())));
        holder.countryName.setText(list.get(position).getCountry());
        holder.sno.setText(String.valueOf(position+1));

        Map<String, String> img = list.get(position).getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.imgCountry);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("country",list.get(position).getCountry());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView sno, countryName, countryCases;
        ImageView imgCountry;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            countryName = itemView.findViewById(R.id.countryName);
            countryCases = itemView.findViewById(R.id.countryCases);
            imgCountry = itemView.findViewById(R.id.countryImage);

        }
    }

    public void filterList(List<CountryData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }
}
