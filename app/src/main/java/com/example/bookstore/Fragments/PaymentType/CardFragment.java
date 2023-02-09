package com.example.bookstore.Fragments.PaymentType;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.Classes.CardData;
import com.example.bookstore.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;


public class CardFragment extends Fragment {

    private View rootView;

    private TextInputEditText etCardNumber,etDate,etCVV,etCardHolderName;
    boolean checkCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_card, container, false);

        etCardNumber = rootView.findViewById(R.id.etCardNumber);
        etDate = rootView.findViewById(R.id.etDate);
        etCVV = rootView.findViewById(R.id.etCVV);
        etCardHolderName = rootView.findViewById(R.id.etCardHolderName);

        etCardNumber.setText("1111111111111111");
        etDate.setText("01/28");
        etCVV.setText("111");
        etCardHolderName.setText("Nikola Tuček");


        etDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 3) {
                    checkCount = true;
                } else {
                    checkCount = false;
                }

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(count != 0)
            {
                if(s.length() == 2)
                {
                    etDate.append("/");
                }
            }
            else
            {
                if(checkCount)
                    {
                        etDate.getText().delete(s.length() - 1, s.length());
                    }
            }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return rootView;
    }

    public boolean CheckInput()
    {
        if(etCardNumber.getText().length() != 16)
        {
            etCardNumber.setError("Unesite ispravan broj kartice");
            return false;
        }


       if(etDate.getText().length() != 5)
       {
           etDate.setError("Unesite ispravan datum");
           return false;
       }
       else
       {
           int mm = Integer.parseInt(String.valueOf(etDate.getText().charAt(0)) + String.valueOf(etDate.getText().charAt(1)));
           int yy = Integer.parseInt(String.valueOf(etDate.getText().charAt(3)) + String.valueOf(etDate.getText().charAt(4)));
           if(mm>12)
           {
               etDate.setError("Neispravan mjesec!");
               return false;
           }
           Calendar calendar = Calendar.getInstance();
           int year = calendar.get(Calendar.YEAR);
           int month = calendar.get(Calendar.MONTH)+1;
           year = Integer.parseInt(Integer.toString(year).substring(2));


           if((year+5) < yy || year > yy)
           {
               etDate.setError("Neispravna godina!");
               return false;
           }

           if(yy == year && month > mm)
           {
               etDate.setError("Kartica je istekla!");
               return false;
           }
       }

       if(etCVV.getText().length() != 3)
       {
           etCVV.setError("CVV se sastoji od 3 broja!");
           return false;
       }

        boolean isWhitespace = etCardHolderName.getText().toString().matches("^\\s*$");

       if(etCardHolderName.getText().length() == 0 || isWhitespace)
       {
           etCardHolderName.setError("Unesite vlasnika kartice");
           return false;
       }

        return true;
    }


    public CardData GetData()
    {
        CardData cardData = new CardData();
        cardData.setCardNumber(etCardNumber.getText().toString());
        cardData.setExpireDate(etDate.getText().toString());
        cardData.setCvv(etCVV.getText().toString());
        cardData.setCardHolderName(etCardHolderName.getText().toString());

        return cardData;
    }

}