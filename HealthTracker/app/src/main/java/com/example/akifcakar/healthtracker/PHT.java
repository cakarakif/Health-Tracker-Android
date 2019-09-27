package com.example.akifcakar.healthtracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PHT extends AppCompatActivity {

    private Button backButton;
    private Button check;
    private Button graph;
    private EditText tension;
    private EditText sugar;
    private EditText pulse;
    private EditText weight;

    private Button okbttn;
    private Button resetbttn;
    private LineChart lineChart;
    private LineData lineData;
    int upval[]=new int[7];//haftalık gösterim için değişkenler
    int lowval[]=new int[7];
    int sgrval[]=new int[7];
    int plsval[]=new int[7];
    int wghtval[]=new int[7];
    Boolean flag=true; //Database'de 7 değerin olduğunun kontrolü yapılır.

    BarChart chart;//verilerin grafiksel gösterimi için kullanılır.
    Realm realm; //Database tanımlanır

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pht);

        graph=(Button)findViewById(R.id.graph);
        backButton=(Button)findViewById(R.id.backbutton);
        check=(Button)findViewById(R.id.calculate);
        tension=(EditText) findViewById(R.id.tension);
        sugar=(EditText) findViewById(R.id.sugar);
        pulse=(EditText) findViewById(R.id.pulse);
        weight=(EditText) findViewById(R.id.weight);


        backHome();
        checkPHT();

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                showDatabase();  //Graph için gerekli işlemeler
                if(flag==true)
                    showChart();
                else
                    Toast.makeText(getApplicationContext(),"At least 7 values must be entered!",Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException | NullPointerException nfe) {
                    Toast.makeText(getApplicationContext(),"At least 7 values must be entered!",Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    private void checkPHT(){
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String tnsn=tension.getText().toString();//split et bunu
                    int sgr=Integer.parseInt(sugar.getText().toString());
                    int pls=Integer.parseInt(pulse.getText().toString());
                    int wght=Integer.parseInt(weight.getText().toString());

                    ////////////////Tension Control
                    String tnsnCntrl1,tnsnCntrl2;
                    String[] values = tnsn.split("/");
                    if(values.length!=2)
                        throw new NullPointerException("error");
                    int upper=Integer.parseInt(values[0]);
                    int lower=Integer.parseInt(values[1]);
                    if(upper<9){
                        tnsnCntrl1="LOW";
                    }else if(upper>=9 && upper<=12){
                        tnsnCntrl1="NORMAL";
                    }else
                        tnsnCntrl1="HIGH";

                    if(lower<6){
                        tnsnCntrl2="LOW";
                    }else if(lower>=6 && lower<=8){
                        tnsnCntrl2="NORMAL";
                    }else
                        tnsnCntrl2="HIGH";

                    ////////////////Blood Sugar Control
                    String sgrCntrl;
                    if(sgr>=70&& sgr<=110){
                        sgrCntrl="NORMAL";
                    }else if(sgr>110 && sgr<=125){
                        sgrCntrl="PRE-DIABETES";
                    }else if(sgr<70){
                        sgrCntrl="LOW";
                    }else
                        sgrCntrl="DIABETES";

                    //////////////// Pulse Control
                    String plsCntrl;
                    if(pls>=60 && pls<=100){
                        plsCntrl="NORMAL";
                    }else if(pls<60){
                        plsCntrl="LOW";
                    }else
                        plsCntrl="HIGH";

                    //////////////// Weight Control
                    String wghtCntrl;
                    if(wght>=55 && wght<=80){
                        wghtCntrl="NORMAL";
                    }else if(wght<55){
                        wghtCntrl="LOW";
                    }else
                        wghtCntrl="HIGH";

                    dialogueShow(tnsnCntrl1,tnsnCntrl2,sgrCntrl,plsCntrl,wghtCntrl);

                    realmDefine(); //Bilgiler aynı zamanda database'e eklenir.
                    insertTable(upper,lower,sgr,pls,wght);


                } catch (NumberFormatException | NullPointerException nfe) {
                    Toast.makeText(getApplicationContext(),"Enter Valid Values!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void dialogueShow(String upper,String lower,String sgrCntrl,String plsCntrl,String wghtCntrl){//Sonucun gösterilmesi için alert dialog kullanıldı.
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_alert_pht,null);

        TextView tension1=(TextView) view.findViewById(R.id.t1);
        TextView tension2=(TextView) view.findViewById(R.id.t2);
        TextView sugar=(TextView) view.findViewById(R.id.s1);
        TextView pulse=(TextView) view.findViewById(R.id.p1);
        TextView weight=(TextView) view.findViewById(R.id.w1);

        tension1.setText(upper);
        tension2.setText(lower);
        sugar.setText(sgrCntrl);
        pulse.setText(plsCntrl);
        weight.setText(wghtCntrl);

        int greenCode=Color.rgb(0,153,0);

        if(upper.equals("NORMAL"))
            tension1.setTextColor(greenCode);
        else
            tension1.setTextColor(Color.RED);
        if(lower.equals("NORMAL"))
            tension2.setTextColor(greenCode);
        else
            tension2.setTextColor(Color.RED);
        if(sgrCntrl.equals("NORMAL"))
            sugar.setTextColor(greenCode);
        else
            sugar.setTextColor(Color.RED);
        if(plsCntrl.equals("NORMAL"))
            pulse.setTextColor(greenCode);
        else
            pulse.setTextColor(Color.RED);
        if(wghtCntrl.equals("NORMAL"))
            weight.setTextColor(greenCode);
        else
            weight.setTextColor(Color.RED);


        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog dialogueShow=alert.create();
        dialogueShow.show();
        //dialogueShow.cancel();
    }


    public void realmDefine(){
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
    }

    public void insertTable(int upper,int lower, int sugar,int pulse,int weight ){
        realm.beginTransaction();//ekleme işlemi başlatıldı.
        PersonInfo info=realm.createObject(PersonInfo.class);
        info.setUpper(upper);
        info.setLower(lower);
        info.setSugar(sugar);
        info.setPulse(pulse);
        info.setWeight(weight);
        realm.commitTransaction();//begin ile bunun arasındaki kısımlar bu adımda eklenmiş olur.
    }

    public void showDatabase(){//Grafik için database'den veri alımı yapılır.
        realm.beginTransaction();
        RealmResults<PersonInfo> person=realm.where(PersonInfo.class).findAll();

        int size=person.size(),counter=0;

        if(size>=7) {
            for (PersonInfo k : person) {
                Log.i("Kontrol", k.toString());
                if(size-counter<=7) {
                    upval[7-(size-counter)] = k.getUpper();
                    lowval[7-(size-counter)] = k.getLower();
                    sgrval[7-(size-counter)] = k.getSugar();
                    plsval[7-(size-counter)] = k.getPulse();
                    wghtval[7-(size-counter)] = k.getWeight();
                    //Burada gerekli yere göndererek tüm kayıtları işleterek grafiğe aktar.
                }
                counter++;
            }
            flag=true;
        }else
            flag=false;
        realm.commitTransaction();
    }

    public void deleteDatabase(){
        RealmResults<PersonInfo> list=realm.where(PersonInfo.class).findAll();
        realm.beginTransaction();
        list.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void showChart(){//Grafiksel Gösterim

        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.activity_alert_graphics,null);


        lineChart=(LineChart)view.findViewById(R.id.chart);
        lineData=new LineData(getXvalues(),getLineDataValues());
        lineChart.setData(lineData);
        lineChart.setDescription("Personal-Health-Tracking");
        lineChart.animateY(3000);
        lineChart.invalidate();
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialogueShow=alert.create();
        dialogueShow.show();
        ////

        resetbttn=(Button)view.findViewById(R.id.reset);
        okbttn=(Button)view.findViewById(R.id.ok);

        okbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogueShow.cancel();
            }
        });

        resetbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogueShow.cancel();
                deleteDatabase();
                Toast.makeText(getApplicationContext(),"Your data deleted!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<ILineDataSet> getLineDataValues(){//Chart için gerekli ek fonksiyon
        ArrayList<ILineDataSet> lineDataSets=null;

        ArrayList<Entry> upvalList=new ArrayList<>();
        ArrayList<Entry> lowvalList=new ArrayList<>();
        ArrayList<Entry> sgrvalList=new ArrayList<>();
        ArrayList<Entry> plsvalList=new ArrayList<>();
        ArrayList<Entry> wghtvalList=new ArrayList<>();

        for (int i=0;i< upval.length;i++){
            upvalList.add(new Entry(upval[i],i));
        }

        for (int i=0;i< lowval.length;i++){
            lowvalList.add(new Entry(lowval[i],i));
        }

        for (int i=0;i< sgrval.length;i++){
            sgrvalList.add(new Entry(sgrval[i],i));
        }

        for (int i=0;i< plsval.length;i++){
            plsvalList.add(new Entry(plsval[i],i));
        }

        for (int i=0;i< wghtval.length;i++){
            wghtvalList.add(new Entry(wghtval[i],i));
        }

       LineDataSet lineDataSet1=new LineDataSet(upvalList,"Systolic");
        lineDataSet1.setColor(Color.GREEN);
        lineDataSet1.setCircleColor(Color.YELLOW);

        LineDataSet lineDataSet2=new LineDataSet(lowvalList,"Diastolic");
        lineDataSet2.setColor(Color.RED);
        lineDataSet2.setCircleColor(Color.YELLOW);

        LineDataSet lineDataSet3=new LineDataSet(sgrvalList,"Sugar");
        lineDataSet3.setColor(Color.BLUE);
        lineDataSet3.setCircleColor(Color.YELLOW);

        LineDataSet lineDataSet4=new LineDataSet(plsvalList,"Pulse");
        lineDataSet4.setColor(Color.GRAY);
        lineDataSet4.setCircleColor(Color.YELLOW);

        LineDataSet lineDataSet5=new LineDataSet(wghtvalList,"Weight");
        lineDataSet5.setColor(Color.MAGENTA);
        lineDataSet5.setCircleColor(Color.YELLOW);

        lineDataSets=new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);
        lineDataSets.add(lineDataSet4);
        lineDataSets.add(lineDataSet5);
        return lineDataSets;
    }

    private List<String> getXvalues(){//Chart için gerekli ek fonksiyon

        ArrayList<String> xvalues=new ArrayList<>();
        xvalues.add("DAY1");
        xvalues.add("DAY2");
        xvalues.add("DAY3");
        xvalues.add("DAY4");
        xvalues.add("DAY5");
        xvalues.add("DAY6");
        xvalues.add("DAY7");

        return xvalues;
    }
}

