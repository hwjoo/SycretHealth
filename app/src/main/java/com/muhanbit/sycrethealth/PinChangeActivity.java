package com.muhanbit.sycrethealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinChangeActivity extends AppCompatActivity {
    @BindView(R.id.pin_text)
    EditText pinText;
    @BindView(R.id.newpin_text)
    EditText newPinText;
    @BindView(R.id.change_btn)
    Button changeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_change);
        ButterKnife.bind(this);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPin = pinText.getText().toString();
                String newPin = newPinText.getText().toString();

                SycretWare.init(getBaseContext());
                if(SycretWare.getProvider().init(oldPin)){
                       if(SycretWare.getProvider().data.changePin(oldPin, newPin)){
                           Toast.makeText(PinChangeActivity.this,"변경완료, new pin : "+newPin,Toast.LENGTH_SHORT).show();
                       }
                }else{
                    Toast.makeText(PinChangeActivity.this,"init fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
