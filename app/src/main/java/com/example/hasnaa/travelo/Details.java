package com.example.hasnaa.travelo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.hasnaa.travelo.R;

import java.security.PrivateKey;

public class Details extends AppCompatActivity  {
    private String id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getIntent().getStringExtra(Intent.EXTRA_TEXT);

    }

}
