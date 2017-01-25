package com.muhanbit.sycrethealth;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sycretware.auth.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinChangeActivity extends AppCompatActivity {
    @BindView(R.id.current_pin)
    EditText currentPin;
    @BindView(R.id.change_pin)
    EditText changePin;
    @BindView(R.id.change_pin_check)
    EditText changePinCheck;
    @BindView(R.id.change_btn)
    Button changeBtn;
    @BindView(R.id.activity_pin_change)
    View pinView;

    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_change);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.change_btn)
    void clickChange(){
        imm.hideSoftInputFromWindow(currentPin.getWindowToken(),0);
        imm.hideSoftInputFromWindow(changePin.getWindowToken(),0);
        imm.hideSoftInputFromWindow(changePinCheck.getWindowToken(),0);
        String currentPinString = currentPin.getText().toString();
        String changePinString = changePin.getText().toString();
        String changePinChString = changePinCheck.getText().toString();
        if(changePinString.length() >= 6){
            if(changePinString.equals(changePinChString)){
                Provider provider = SycretWare.getProvider();
                try {
                    if(provider.data.changePin(currentPinString, changePinString)){
                        Snackbar.make(pinView,"Pin 변경 완료", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(pinView,"현재 Pin number를 확인해주세요.", Snackbar.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d("TEST","pin change Exception :" + e.toString());
                }
            }else{
                Snackbar.make(pinView,"변경될 Pin번호가 다릅니다.", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            Snackbar.make(pinView,"변경될 Pin번호는 6자리 이상이여야 합니다..", Snackbar.LENGTH_SHORT).show();
        }
    }
}
