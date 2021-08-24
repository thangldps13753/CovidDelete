package com.example.coviddelete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.coviddelete.adapter.CountryAdapter;
import com.example.coviddelete.api.ApiUtilities;
import com.example.coviddelete.api.CountryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {
    RecyclerView rcvCountry;
    List<CountryData> list;
    ProgressDialog dialog;
    EditText edtSearch;
    CountryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        list = new ArrayList<>();
        rcvCountry = findViewById(R.id.rcvCountry);
        edtSearch = findViewById(R.id.searchBar);

        adapter = new CountryAdapter(this,list);
        rcvCountry.setLayoutManager(new LinearLayoutManager(this));
        rcvCountry.setAdapter(adapter);
        rcvCountry.setHasFixedSize(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        dialog.dismiss();
                    }
                });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }
    private void filter(String text){
        List<CountryData> filter = new ArrayList<>();
        for(CountryData items: list){
            if(items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filter.add(items);
            }
        }
        adapter.filterList(filter);
    }

}