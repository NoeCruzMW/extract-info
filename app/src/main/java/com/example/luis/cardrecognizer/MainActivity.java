package com.zurckz.extractor;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.widget.Toast;


import com.microblink.MicroblinkSDK;

import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.validation.CardValidator;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OperationCallBack {









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);









    }


    public void generateToken(){

        Openpay openpay = new OpenPay().getOpenpay();

        Boolean isValid = true;
        Card card = new Card();
        card.holderName("Luis Fernando Prudencio Cruz");
        card.cardNumber("5512382364233165");
        card.expirationMonth(6);
        card.expirationYear(24);
        card.cvv2("7431");


        if (!CardValidator.validateHolderName(card.getHolderName())) {
            isValid = false;
            Toast.makeText(getApplicationContext(),"Name is invalid",Toast.LENGTH_LONG).show();
        }

        if (!CardValidator.validateCVV(card.getCvv2(),card.getCardNumber())) {
            isValid = false;
            Toast.makeText(getApplicationContext(),"Cvv2 is invalid",Toast.LENGTH_LONG).show();
        }



        if (!CardValidator.validateCard(
                card.getHolderName(),
                card.getCardNumber(),
                Integer.parseInt(card.getExpirationMonth()),
                Integer.parseInt(card.getExpirationYear()),
                card.getCvv2()
        )){
            isValid = false;
            Toast.makeText(getApplicationContext(),"Card is invalid",Toast.LENGTH_LONG).show();
        }

        if(isValid){
            openpay.createToken(card, this);
        }

    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLicense(){
        MicroblinkSDK.setLicenseKey("sRwAAAAUY29tLnp1cmNrei5leHRyYWN0b3KQmN+5wafbf/I2vJ2sjrxwTnmghX93ywbfNqgGmJ1m1B5iqXYrp/uNYGFL+ik3K8jL/M7969ViIp2V6Tp3d0zPAtsolPDctjIQY90FAtlIBHHZ5ifTkPUq+Tmq0ljFGz1KSUsfBaNyyVBMpt+B4lgk6E6zKcSVwHWtsCNtZEHXcNaZL6SjHMas", this);
    }

    @Override
    public void onError(OpenpayServiceException e) {
        Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"Error Code: "+e.getErrorCode(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCommunicationError(ServiceUnavailableException e) {
        Toast.makeText(getApplicationContext(),"C Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(OperationResult operationResult) {
        Toast.makeText(getApplicationContext(),"OK: "+operationResult.getResult().toString(),Toast.LENGTH_LONG).show();
    }
}
