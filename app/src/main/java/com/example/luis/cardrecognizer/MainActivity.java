package com.example.luis.cardrecognizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, OperationCallBack {

    private static final int REQUEST_SCAN = 100;
    private static final int REQUEST_AUTOTEST = 200;

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

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_OVERLAY_LAYOUT_ID, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_INSTRUCTIONS, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true); // default: false
        //scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, true); // default: false


        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, REQUEST_SCAN);

        /*Intent intent = new ScanCardIntent.Builder(this).build();
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "Antes de iniciar: " + requestCode, Toast.LENGTH_LONG).show();
        if (requestCode == REQUEST_SCAN) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                onCreateDialog(scanResult.cardNumber, scanResult.expiryYear + "/"+scanResult.expiryMonth, "032");
                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                /*resultDisplayStr = "Card Number: " + scanResult.cardNumber + "\n";
                resultDisplayStr += "Card Number: " + scanResult.expiryYear + "\n";
                resultDisplayStr += "Card Number: " + scanResult.expiryMonth + "\n";
                resultDisplayStr += "Card Number: " + scanResult.cardholderName + "\n";
                resultDisplayStr += "Card Number: " + scanResult.describeContents() + "\n";
                resultDisplayStr += "Card Number: " + scanResult.getCardType() + "\n";
                resultDisplayStr += "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );
                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.toString() + "/" + scanResult.expiryYear + "\n";
                }
                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }
                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
                Toast.makeText(getApplicationContext(), resultDisplayStr, Toast.LENGTH_LONG).show();*/
            }
            else {
                resultDisplayStr = "Scan was canceled.";
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }


    public void onCreateDialog(String cardNumber, String expired, String cvv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.confirm, null);

        //builder.setView(v);

        EditText number = (EditText) v.findViewById(R.id.number);
        number.setText(cardNumber);

        EditText exp = (EditText) v.findViewById(R.id.expired);
        exp.setText(expired);

        EditText cvvtxt = (EditText) v.findViewById(R.id.cvv);
        cvvtxt.setText(cvv);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
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
