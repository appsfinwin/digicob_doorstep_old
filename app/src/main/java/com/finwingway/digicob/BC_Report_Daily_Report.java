package com.finwingway.digicob;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Anvin on 12/20/2016.
 */
public class BC_Report_Daily_Report extends Fragment{
    TextView date,noOfcashCollected,noOfAmountPaid,noOfTransfer,withdrawalAmount,depositAmount,transferAmount,accountCreated,customerCreated,
             totalNoOFTransactions,cashInhand,agentCode,agentname,branchname,linearLayout4;
    String Date,NoOfcashCollected,NoOfAmountPaid,NoOfTransfer,WithdrawalAmount,DepositAmount,TransferAmount,AccountCreated,CustomerCreated,
            TotalNoOFTransactions,CashInhand,errorCode,errorMessage;
    LinearLayout linearLayout1,linearLayout2,linearLayout3;
    public static String  ip=login.ip_global;
    private static String api_url =ip+"/BCReport";
    String agent_code=login.agent_code,branchName=login.branch_name,agentName=login.agent_name;
    SweetAlertDialog pDialog;
    JSONParser jsonParser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View collectionview=inflater.inflate(R.layout.fragment_bc_report_daily_collection_layout,container,false);
         linearLayout1 = (LinearLayout)collectionview.findViewById(R.id.linear2);
         linearLayout2 = (LinearLayout)collectionview.findViewById(R.id.linear3);
         linearLayout3 = (LinearLayout)collectionview.findViewById(R.id.linear4);
        linearLayout4 = (TextView) collectionview.findViewById(R.id.errorTextView);
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        linearLayout3.setVisibility(View.GONE);
        linearLayout4.setVisibility(View.GONE);


        agentCode=(TextView)collectionview.findViewById(R.id.dailyagentCodeTextView);
        date=(TextView)collectionview.findViewById(R.id.dailydateDailyTextView);
        agentname=(TextView)collectionview.findViewById(R.id.dailyAgentNAmeTextView);
        branchname=(TextView)collectionview.findViewById(R.id.dailybankNAmeTextView);
        noOfcashCollected=(TextView)collectionview.findViewById(R.id.dailybcnoOFDepositCollected);
        noOfAmountPaid=(TextView)collectionview.findViewById(R.id.dailybcnoOfcashPaidTextView);
        noOfTransfer=(TextView)collectionview.findViewById(R.id.dailybcnoOftransferTransactionsTextView);
        withdrawalAmount=(TextView)collectionview.findViewById(R.id.dailybctotalAmountofWithdrawalTextView);
        depositAmount=(TextView)collectionview.findViewById(R.id.dailybcTotalAmountofDepositTextView);
        transferAmount=(TextView)collectionview.findViewById(R.id.dailybctotalAmountofTransferTextView);
        accountCreated=(TextView)collectionview.findViewById(R.id.dailybcnoOfAccountsCreatedTextView);
        customerCreated=(TextView)collectionview.findViewById(R.id.dailybcNOOFCustomersCreatedTextView);
        totalNoOFTransactions=(TextView)collectionview.findViewById(R.id.dailybcTotalNoOFTransactions);
        cashInhand=(TextView)collectionview.findViewById(R.id.dailyAmountPaidTextView);

        pDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        jsonParser=new JSONParser();
        new getBCReport().execute();

        return collectionview;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class getBCReport extends AsyncTask<String, String, String> {
        String ok="OK";
        @Override
        public void onPreExecute(){
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMapAmount=new HashMap<>();
            hashMapAmount.put("agent_id",agent_code);
            hashMapAmount.put("type","D");
            try {
                JSONObject jsonObject=jsonParser.makeHttpRequest(api_url,"POST",hashMapAmount);
                String JsonDataString=jsonObject.toString();
                JSONObject reader=new JSONObject(JsonDataString);
                JSONObject receipt=reader.getJSONObject("bc_report");
                if(receipt.has("data")){
                    JSONObject data=receipt.getJSONObject("data");
                    Date=data.getString("REPORT_GENERATED_DATE");
                    NoOfcashCollected=data.getString("NO_OF_DEPOSITS");
                    DepositAmount=data.getString("TOTAL_DEPOSITS");
                    NoOfAmountPaid=data.getString("NO_OF_WITHDRAWAL");
                    WithdrawalAmount=data.getString("TOTAL_WITHDRAWAL");
                    NoOfTransfer=data.getString("NO_OF_TRANSFER");
                    TransferAmount=data.getString("TOTAL_TRANSFER");
                    TotalNoOFTransactions=data.getString("NO_OF_TRANSACTIONS");
                    CustomerCreated=data.getString("NO_OF_CUST_CREATED");
                    AccountCreated=data.getString("NO_OF_ACC_CREATED");
                    CashInhand=data.getString("CASH_IN_HAND");
                    errorCode="ok";
                }
                if(receipt.has("error")){
                    Date=receipt.getString("TXN_DATE");
                    errorMessage=receipt.getString("error");
                    return "error";
                }
                Log.e("BC_Report",JsonDataString);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(String url){
            pDialog.dismiss();
            if(errorCode == "ok"){
                pDialog.dismiss();
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.VISIBLE);
                date.setText(Date);
                branchname.setText(branchName);
                agentname.setText(agentName);
                agentCode.setText(agent_code);
                noOfcashCollected.setText(NoOfcashCollected);
                noOfAmountPaid.setText(NoOfAmountPaid);
                noOfTransfer.setText(NoOfTransfer);
                withdrawalAmount.setText(WithdrawalAmount);
                depositAmount.setText(DepositAmount);
                transferAmount.setText(TransferAmount);
                accountCreated.setText(AccountCreated);
                customerCreated.setText(CustomerCreated);
                totalNoOFTransactions.setText(TotalNoOFTransactions);
                cashInhand.setText(CashInhand);
            }if(url =="error") {
                pDialog.dismiss();
                linearLayout4.setVisibility(View.VISIBLE);
                date.setText(Date);
                branchname.setText(branchName);
                agentname.setText(agentName);
                agentCode.setText(agent_code);
                linearLayout4.setText(errorMessage);
            }
            }
        }
    }

