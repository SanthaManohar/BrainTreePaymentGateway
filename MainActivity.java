package com.app.braintreepaygateway;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;

public class MainActivity extends AppCompatActivity {

    TextView buynow,payment_nonce,clienttoken;
    EditText amount;
    static final int DROP_IN_REQUEST = 100;
    String mClientToken = "sandbox_9qryjssj_nryd76jy7qkvsv78";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buynow = findViewById(R.id.buynow);
        amount = findViewById(R.id.amount);
        payment_nonce = findViewById(R.id.paymentnonce);
        clienttoken = findViewById(R.id.clienttoken);

        clienttoken.setText("ClientToken: "+mClientToken);

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amountstr = amount.getText().toString();

                if(amountstr != null && !amountstr.equals("") && !amountstr.equals("0")) {
                    DropInRequest dropInRequest = new DropInRequest()
                            .clientToken(mClientToken)
                            .amount(amountstr);
                    startActivityForResult(dropInRequest.getIntent(MainActivity.this), DROP_IN_REQUEST);
                }else {
                    Toast.makeText(MainActivity.this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
                }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DROP_IN_REQUEST) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();

                Log.d("paymentMethodNonce:", "paymentMethodNonce:" + paymentMethodNonce);

                payment_nonce.setText("PaymentNonce: "+paymentMethodNonce);

                // send paymentMethodNonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // canceled
            } else {
                // an error occurred, checked the returned exception
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }

    }
}
