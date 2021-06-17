package com.finwingway.digicob.loanModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.finwingway.digicob.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.finwingway.digicob.loanModule.JLGLoanCreationAdapterTwo.removeFromListJLGcrtn;
import static com.finwingway.digicob.loanModule.JLGLoanCreationGetSet.listAdapteTwo;


public class ListviewAdapterJLGcreation extends BaseAdapter {

    private String optionSelect;
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    private ViewHolder holder;

    public ListviewAdapterJLGcreation(Activity activity, ArrayList<HashMap<String, String>> list, String _optionSelect) {
        super();
        this.activity = activity;
        this.list = list;
        this.optionSelect = _optionSelect;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tvslno;
        TextView tvcusid;
        TextView tvcusname;
        EditText edtcoapplcnt;
        EditText edtmob;
        EditText edtaddress;
        Spinner spnrConsumerGoods;
        EditText edtAmount;
        EditText edtInsuranceFee;
        EditText edtDocmntnFee;
        EditText edtCgst;
        EditText edtSgst;
        EditText edtCess;
        EditText edtDisbrsmntAmnt;
        Button btnAction;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_gropmem, null);
            holder = new ListviewAdapterJLGcreation.ViewHolder();
            holder.tvslno = convertView.findViewById(R.id.txt_list_slno);
            holder.tvcusid = convertView.findViewById(R.id.txt_list_cusid);
            holder.tvcusname = convertView.findViewById(R.id.txt_list_cusname);
            holder.edtcoapplcnt = convertView.findViewById(R.id.edt_list_nominee);
            holder.edtmob = convertView.findViewById(R.id.edt_list_mob);
            holder.edtaddress = convertView.findViewById(R.id.edt_list_address);

            holder.spnrConsumerGoods = convertView.findViewById(R.id.spnr_cnsumr_goods);
            holder.edtAmount = convertView.findViewById(R.id.edt_list_amount);
            holder.edtInsuranceFee = convertView.findViewById(R.id.edt_list_insurance_fee);
            holder.edtDocmntnFee = convertView.findViewById(R.id.edt_list_docmntnFee);
            holder.edtCgst = convertView.findViewById(R.id.edt_list_cgst);
            holder.edtSgst = convertView.findViewById(R.id.edt_list_sgst);
            holder.edtCess = convertView.findViewById(R.id.edt_list_cess);
            holder.edtDisbrsmntAmnt = convertView.findViewById(R.id.edt_list_disbrsmnt_amnt);
            holder.btnAction = convertView.findViewById(R.id.btn_list_action);

            convertView.setTag(holder);
        } else {
            holder = (ListviewAdapterJLGcreation.ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);
        holder.tvslno.setText(map.get(JLGLoanCreationGetSet.LN_SLNO));
        holder.tvcusid.setText(map.get(JLGLoanCreationGetSet.LN_CUSTID));
        holder.tvcusname.setText(map.get(JLGLoanCreationGetSet.LN_CUSTNAME));
        holder.edtcoapplcnt.setText(map.get(JLGLoanCreationGetSet.LN_COAPPLICNT));
        holder.edtmob.setText(map.get(JLGLoanCreationGetSet.LN_MOB));
        holder.edtaddress.setText(map.get(JLGLoanCreationGetSet.LN_ADDRESS));
        holder.edtAmount.setText(map.get(JLGLoanCreationGetSet.LN_AMOUNT));
        holder.edtInsuranceFee.setText(map.get(JLGLoanCreationGetSet.LN_INSURANCE_FEE));
        holder.edtDocmntnFee.setText(map.get(JLGLoanCreationGetSet.LN_DOCUMNT_FEE));
        holder.edtCgst.setText(map.get(JLGLoanCreationGetSet.LN_CGST));
        holder.edtSgst.setText(map.get(JLGLoanCreationGetSet.LN_SGST));
        holder.edtCess.setText(map.get(JLGLoanCreationGetSet.LN_CESS));
        holder.edtDisbrsmntAmnt.setText(map.get(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT));

        //getting MyItem Object for each position
        HashMap<String, String> item = list.get(position);
        //set the id to editetxt important line here as it will be helpful to set text according to position
        holder.edtcoapplcnt.setId(position);
        holder.edtmob.setId(position);
        holder.edtaddress.setId(position);
        holder.edtAmount.setId(position);
        holder.edtInsuranceFee.setId(position);
        holder.edtDocmntnFee.setId(position);
        holder.edtCgst.setId(position);
        holder.edtSgst.setId(position);
        holder.edtCess.setId(position);
        holder.edtDisbrsmntAmnt.setId(position);
        holder.edtDisbrsmntAmnt.setTag(position);
        holder.btnAction.setId(position);
        holder.spnrConsumerGoods.setId(position);
        //setting the values from object to holder views for each row

        //==========
        ArrayAdapter<String> adapterProdt;
        if (optionSelect.equals(JLGLoanCreationGetSet.OptionList.get(0))) {
            holder.spnrConsumerGoods.setEnabled(false);
            holder.spnrConsumerGoods.setClickable(false);
            holder.spnrConsumerGoods.setAdapter(null);
        } else if (optionSelect.equals(JLGLoanCreationGetSet.OptionList.get(1))) {
            holder.spnrConsumerGoods.setEnabled(true);
            holder.spnrConsumerGoods.setClickable(true);
            if (JLGLoanCreationGetSet.StrProductNameArry != null && JLGLoanCreationGetSet.StrProductNameArry.length > 0) {
                //collDayAdptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterProdt = new ArrayAdapter<String>(activity, R.layout.spinner_list_item, JLGLoanCreationGetSet.StrProductNameArry);
                holder.spnrConsumerGoods.setAdapter(adapterProdt);
            }
        }

        holder.spnrConsumerGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    HashMap<String, String> item = list.get(position);
                    item.put(JLGLoanCreationGetSet.LN_CONSUMERGOODS, JLGLoanCreationGetSet.StrProductIdArry[position]);
                    list.set(position, item);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //==========

        holder.edtcoapplcnt.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_COAPPLICNT, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtmob.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_MOB, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtaddress.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_ADDRESS, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );

////==================================

        holder.edtAmount.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_AMOUNT, field.getText().toString());
//                            item.put(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT, StrDisbrsmntAmnt);
                            list.set(id, item);
                            list.set(id, item);
                        }
                    }
                }
        );

        holder.edtInsuranceFee.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_INSURANCE_FEE, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtDocmntnFee.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_DOCUMNT_FEE, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtCgst.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_CGST, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtSgst.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_SGST, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtCess.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> item = list.get(id);
                            final EditText field = ((EditText) v);
                            item.put(JLGLoanCreationGetSet.LN_CESS, field.getText().toString());
                            list.set(id, item);
                        }
                    }
                }
        );
        holder.edtDisbrsmntAmnt.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            final int id = v.getId();
                            HashMap<String, String> Hitem = list.get(id);
                            final EditText field = ((EditText) v);
                            Hitem.put(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT, field.getText().toString());
                            list.set(id, Hitem);
                        }
                    }
                }
        );

        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromListJLGcrtn(v.getId());
            }

        });

////==================================================================

//        holder.btnCalc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final int id = v.getId();
//                double total = calcu();
//                Log.e("btnCalc: ", String.valueOf(total));
//                HashMap<String, String> Calitem = list.get(id);
//                Calitem.put(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT, String.valueOf(total));
//                list.set(id, Calitem);
//                notifyDataSetChanged();
//            }
//        });

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        return convertView;
    }

    //-=================================================================

    public Double calcu() {
        double total_amnt = 0.00;
        try {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> map = list.get(i);
                total_amnt =
                        Double.parseDouble(TextUtils.isEmpty(map.get(JLGLoanCreationGetSet.LN_AMOUNT)) ? "0.00" : map.get(JLGLoanCreationGetSet.LN_AMOUNT)) +
                                Double.parseDouble(TextUtils.isEmpty(map.get(JLGLoanCreationGetSet.LN_INSURANCE_FEE)) ? "0.00" : map.get(JLGLoanCreationGetSet.LN_INSURANCE_FEE)) +
                                Double.parseDouble(TextUtils.isEmpty(map.get(JLGLoanCreationGetSet.LN_DOCUMNT_FEE)) ? "0.00" : map.get(JLGLoanCreationGetSet.LN_DOCUMNT_FEE)) +
                                Double.parseDouble(TextUtils.isEmpty(map.get(JLGLoanCreationGetSet.LN_CGST)) ? "0.00" : map.get(JLGLoanCreationGetSet.LN_CGST)) +
                                Double.parseDouble(TextUtils.isEmpty(map.get(JLGLoanCreationGetSet.LN_SGST)) ? "0.00" : map.get(JLGLoanCreationGetSet.LN_SGST)) +
                                Double.parseDouble(TextUtils.isEmpty(map.get(JLGLoanCreationGetSet.LN_CESS)) ? "0.00" : map.get(JLGLoanCreationGetSet.LN_CESS));
                map.put(JLGLoanCreationGetSet.LN_DISBRSMNT_AMNT, String.valueOf(total_amnt));
                list.set(i, map);
            }
            notifyDataSetChanged();
        } catch (Exception ignored) {
        }
        return total_amnt;
    }

    public boolean validateFields() {
        boolean boo = false;
        try {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> map = list.get(i);
                boo = !map.get(JLGLoanCreationGetSet.LN_AMOUNT).equals("");
//                &&
//                        !map.get(JLGLoanCreationGetSet.LN_INSURANCE_FEE).equals("") &&
//                        !map.get(JLGLoanCreationGetSet.LN_DOCUMNT_FEE).equals("") &&
//                        !map.get(JLGLoanCreationGetSet.LN_CGST).equals("") &&
//                        !map.get(JLGLoanCreationGetSet.LN_SGST).equals("") &&
//                        !map.get(JLGLoanCreationGetSet.LN_CESS).equals("");
                if (!boo) {
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        return boo;
    }

    public void setListofData() {
        listAdapteTwo = list;
    }


}
