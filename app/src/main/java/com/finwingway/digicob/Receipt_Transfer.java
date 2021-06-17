package com.finwingway.digicob;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by AnVin on 1/16/2017.
 */

public class Receipt_Transfer extends AppCompatActivity {
    TextView DebitAccountNumber, accountNO, Name, Mobile, oldBal, WithdrawalAmount, currentBal, withDrawalDate, withdrawalTime, transaction_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_summary);
        Intent intent = getIntent();
        final String account_number = intent.getStringExtra("account_number");
        final String debitAccountNumber = intent.getStringExtra("debit_account_number");
        final String name = intent.getStringExtra("introName");
        final String mobile = intent.getStringExtra("mobile");
        final String old_balance = intent.getStringExtra("old_balance");
        final String withdrawalAmount = intent.getStringExtra("withdrawalAmount");
        final String current_balance = intent.getStringExtra("current_balance");
        final String withdrawalDate = intent.getStringExtra("transactionDate");
        final String tran_id = intent.getStringExtra("tran_id");


        accountNO = (TextView) findViewById(R.id.accNOReceipt);
        DebitAccountNumber = (TextView) findViewById(R.id.debitAccountNumberTextView);
        Name = (TextView) findViewById(R.id.nameReceipt);
        Mobile = (TextView) findViewById(R.id.MobileReceipttxt);
        oldBal = (TextView) findViewById(R.id.oldBalanceReceipt);
        WithdrawalAmount = (TextView) findViewById(R.id.withdrawalAmountText);
        currentBal = (TextView) findViewById(R.id.CurrentBalanceReceipt);
        withDrawalDate = (TextView) findViewById(R.id.date_receipt);
        withdrawalTime = (TextView) findViewById(R.id.timeReceipt);
        transaction_id = (TextView) findViewById(R.id.receipt_transfer_transaction_no);

        accountNO.setText(account_number);
        DebitAccountNumber.setText(debitAccountNumber);
        Name.setText(name);
        Mobile.setText(mobile);
        oldBal.setText(old_balance);
        WithdrawalAmount.setText(withdrawalAmount);
        currentBal.setText(current_balance);
        withDrawalDate.setText(withdrawalDate);
        transaction_id.setText(tran_id);

        Button OKbtn = (Button) findViewById(R.id.accountStatusOKbtn);
        OKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Button CANCELbtn=(Button)findViewById(R.id.accountStatusCANCELbtn);
//        CANCELbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


    }
}
