package com.calculate.bmieerpht.healthtracker;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.calculate.akifcakar.healthtracker.R;

public class EER extends AppCompatActivity {

    private RadioGroup  valPA;
    private RadioGroup  gender;
    private EditText age;
    private EditText weight;
    private EditText height;
    private Button calculate;
    private Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eer);

        calculate=(Button)findViewById(R.id.calculate);
        backbutton=(Button)findViewById(R.id.backbutton);
        valPA=(RadioGroup)findViewById(R.id.valPA);
        gender=(RadioGroup)findViewById(R.id.gender);
        age=(EditText)findViewById(R.id.age);
        weight=(EditText) findViewById(R.id.weight);
        height=(EditText) findViewById(R.id.height);

        backHome();
        calculate();
    }

    private void backHome(){
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculate(){
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                int getPA=valPA.getCheckedRadioButtonId();
                int getGender=gender.getCheckedRadioButtonId();

                int ageNum=Integer.parseInt(age.getText().toString());
                int wghtNum=Integer.parseInt(weight.getText().toString());
                int hghtNum=Integer.parseInt(height.getText().toString());

                double PA=0;
                if(getPA==R.id.sedentary)
                    PA=1;
                else if(getPA==R.id.lowactive && getGender==R.id.man)
                    PA=1.11;
                else if(getPA==R.id.active && getGender==R.id.man)
                    PA=1.25;
                else if(getPA==R.id.veryactive && getGender==R.id.man)
                    PA=1.48;
                else if(getPA==R.id.lowactive && getGender==R.id.woman)
                    PA=1.12;
                else if(getPA==R.id.active && getGender==R.id.woman)
                    PA=1.27;
                else if(getPA==R.id.veryactive && getGender==R.id.woman)
                    PA=1.45;
                else
                    throw new NullPointerException("nfe");
                ///////////
                    int ERR;
                if(getGender==R.id.man)
                    ERR=(int)(662-(9.53*ageNum)+PA*((15.91*wghtNum)+(539.6*hghtNum/100)));
                else
                    ERR=(int)(354-(6.91*ageNum)+PA*((9.36*wghtNum)+(726*hghtNum/100)));

                    dialogueShow(ERR);
                    //Toast.makeText(getApplicationContext(),"Result "+ERR,Toast.LENGTH_SHORT).show();

                } catch (NumberFormatException | NullPointerException nfe) {
                    Toast.makeText(getApplicationContext(),"Enter Valid Values!",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void dialogueShow(int result){//Sonucun gösterilmesi için alert dialog kullanıldı.
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_alert_eer,null);

        TextView txt=(TextView) view.findViewById(R.id.valeer);

        txt.setText(""+result);

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog dialogueShow=alert.create();
        dialogueShow.show();
        //dialogueShow.cancel();
    }


}
