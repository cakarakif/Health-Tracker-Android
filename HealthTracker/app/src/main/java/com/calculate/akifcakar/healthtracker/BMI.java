package com.calculate.bmieerpht.healthtracker;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.calculate.akifcakar.healthtracker.R;

public class BMI extends AppCompatActivity {

    private Button backButton;
    private Button calculate;
    private EditText weight;
    private EditText height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        backButton=(Button)findViewById(R.id.backbutton);
        calculate=(Button)findViewById(R.id.calculate);
        weight=(EditText) findViewById(R.id.weight);
        height=(EditText) findViewById(R.id.height);

        backHome();
        calculateBMI();

    }

    private void backHome(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void calculateBMI(){
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int wght=Integer.parseInt(weight.getText().toString());
                    int hght=Integer.parseInt(height.getText().toString());

                    double result=(double)(wght/(Math.pow(hght,2)/10000));
                    String tranform=Double.toString(result).substring(0, 4);
                    Toast.makeText(getApplicationContext(),"Your BMI:  "+tranform,Toast.LENGTH_LONG).show();//////SONUC
                    dialogueShow(result);

                } catch (NumberFormatException | NullPointerException nfe) {
                    Toast.makeText(getApplicationContext(),"Enter Valid Values!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void dialogueShow(double result){//Sonucun gösterilmesi için alert dialog kullanıldı.
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_alert_bmi,null);

         ImageView img=(ImageView)view.findViewById(R.id.img);
         ImageView article=(ImageView)view.findViewById(R.id.article);

         //Oluşacak durumlar için gerekli şartlar sağlandı.
         if(result<18.5){
             img.setImageResource(R.drawable.underweight);
             article.setImageResource(R.drawable.under);}
         else if(result>=18.5&&result<24.9){
             img.setImageResource(R.drawable.normal);
             article.setImageResource(R.drawable.nrml);}
         else if(result>=25.5&&result<29.9){
             img.setImageResource(R.drawable.overweight);
             article.setImageResource(R.drawable.over);}
         else{
             img.setImageResource(R.drawable.obese);
             article.setImageResource(R.drawable.obs);}

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog dialogueShow=alert.create();
        dialogueShow.show();
        //dialogueShow.cancel();
    }
}
