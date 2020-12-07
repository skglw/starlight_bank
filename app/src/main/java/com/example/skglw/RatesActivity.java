package com.example.skglw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

public class RatesActivity extends AppCompatActivity {

    ArrayList<Сurrency> valutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rates);

        RequestCallable callable = new RequestCallable("http://www.cbr.ru/scripts/XML_daily.asp?date_req=18/11/2020");
        FutureTask task = new FutureTask(callable);
        Thread request = new Thread(task);
        request.start();
        try {
            request.join();
            String ratesXML = String.valueOf(task.get());
           // TextView tv = findViewById(R.id.tv);
           // tv.setText(ratesXML);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser  parser = factory.newPullParser();
            Сurrency сurrency = null; String text="hhvg";
            parser.setInput(new StringReader( ratesXML));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("Valute")) {
                            // create a new instance of employee
                            сurrency = new Сurrency();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("Valute")) {
                            valutes.add(сurrency);
                        }else if (tagname.equalsIgnoreCase("NumCode")) {

                            // Log.e("##########", "numcode "+text);
                            сurrency.setNumCode(text);
                        }else if (tagname.equalsIgnoreCase("CharCode")) {

                           // Log.e("##########", "charcode "+text);
                            сurrency.setCharCode(text);
                        }  else if (tagname.equalsIgnoreCase("Nominal")) {
                            сurrency.setNominal(Integer.parseInt(text));
                            //Log.e("##########", "nominal "+text);
                        } else if (tagname.equalsIgnoreCase("Value")) {
                            сurrency.setValue((text));
                            //Log.e("##########", "value "+text);
                        } else if (tagname.equalsIgnoreCase("Name")) {
                            сurrency.setName(text);
                            //Log.e("##########", "name "+text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        }
        catch (Exception e) {e.printStackTrace();}

        ValutesAdapter adapter = new ValutesAdapter(this, valutes);
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setDividerHeight(15);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String selected = ((TextView) view.findViewById(R.id.tv2)).getText().toString();
            }
        });
    }

    public class Сurrency{
       String NumCode, CharCode, Name;
       int Nominal;
       double Value;
        public Сurrency( ) {
        }

        public String getNumCode() {
            return NumCode;
        }

        public void setNumCode(String numCode) {
            NumCode = numCode;
        }

        public String getCharCode() {
            return CharCode;
        }

        public void setCharCode(String charCode) {
            CharCode = charCode;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getNominal() {
            return Nominal;
        }

        public void setNominal(int nominal) {
            Nominal = nominal;
        }

        public String getValue() {
            return String.valueOf(Value);
        }

        public void setValue(String value) {
            Value = Double.parseDouble(value.replaceAll(",","."));
        }
    }


    public class ValutesAdapter extends ArrayAdapter<Сurrency> {

        private Context context;
        private List<Сurrency> valutes;

        public ValutesAdapter(@NonNull Context context, ArrayList<Сurrency> list) {
            super(context, 0 , list);
            this.context = context;
            valutes = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View item = convertView;
            if(item == null)
                item = LayoutInflater.from(context).inflate(R.layout.item_rates,parent,false);

            Сurrency curVal = valutes.get(position);

            TextView numCode =  item.findViewById(R.id.tv1);
            numCode.setText(curVal.getNumCode());

            TextView charCode=  item.findViewById(R.id.tv2);
            charCode.setText(curVal.getCharCode());

            TextView value =  item.findViewById(R.id.tv3);
            value.setText(curVal.getValue());

            return item;
        }
    }
}
