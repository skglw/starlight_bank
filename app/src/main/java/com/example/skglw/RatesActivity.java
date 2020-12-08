package com.example.skglw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

public class RatesActivity extends AppCompatActivity {

    ArrayList<Сurrency> valutes = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rates);

        LocalDate today = LocalDate.now();
        today.getMonth();
        String month = String.valueOf(today.getMonthValue());
        String day = String.valueOf(today.getDayOfMonth());
        if(today.getMonthValue()<10) month = "0"+today.getMonthValue();
        if(today.getDayOfMonth()<10) day = "0"+today.getDayOfMonth();
        RequestCallable callable =
                new RequestCallable("http://www.cbr.ru/scripts/XML_daily.asp?date_req="+day+"/"+month+"/"+today.getYear());
        FutureTask task = new FutureTask(callable);
        Thread request = new Thread(task);
        request.start();
        try {
            request.join();
            String ratesXML = String.valueOf(task.get());

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
          //  factory.setNamespaceAware(true);
            XmlPullParser  parser = factory.newPullParser();
            Сurrency сurrency = null; String text="";
            parser.setInput(new StringReader( ratesXML));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equals("Valute")) {
                            сurrency = new Сurrency();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equals("Valute"))
                            valutes.add(сurrency);

                        else if (tagname.equals("NumCode"))
                            сurrency.setNumCode(text);

                        else if (tagname.equals("CharCode"))
                            сurrency.setCharCode(text);

                        else if (tagname.equals("Nominal"))
                            сurrency.setNominal(Integer.parseInt(text));

                        else if (tagname.equals("Value"))
                            сurrency.setValue((text));

                        else if (tagname.equals("Name"))
                            сurrency.setName(text);
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }
        }
        catch (Exception e) {e.printStackTrace();}

        RatesAdapter adapter = new RatesAdapter(this, valutes);
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


    public class RatesAdapter extends ArrayAdapter<Сurrency> {

        private Context context;
        private List<Сurrency> rates;

        public RatesAdapter(@NonNull Context context, ArrayList<Сurrency> list) {
            super(context, 0 , list);
            this.context = context;
            rates = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View item = convertView;
            if(item == null)
                item = LayoutInflater.from(context).inflate(R.layout.item_rates,parent,false);

            Сurrency curVal = rates.get(position);

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
