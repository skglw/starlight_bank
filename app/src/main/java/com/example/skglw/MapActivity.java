package com.example.skglw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Branch> branches = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RequestCallable callable= new RequestCallable("http://mobile-api.fxnode.ru:18888/bankomats");
        FutureTask task = new FutureTask(callable);
        Thread request = new Thread(task);
        request.start();
        try {
            request.join();
            String jsonString = String.valueOf(task.get());
            Log.e("tt",jsonString);
            JSONArray arr = new JSONArray(jsonString);
            for (int i=0;i<arr.length();i++)
            {
                String obj = arr.get(i).toString();
                Log.e("OBJECT>> ", obj);
                JSONObject jsonO = new JSONObject(arr.get(i).toString());
                branches.add(new Branch(jsonO.get("type").toString() , jsonO.get("address").toString(),
                        Double.parseDouble(jsonO.get("lat").toString()),Double.parseDouble(jsonO.get("lon").toString())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BranchesAdapter adapter = new BranchesAdapter(this, branches);
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setDividerHeight(15);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.tv2)).getText().toString();
                for (Branch branch:branches){
                    if(selected.equals(branch.address)){
                       CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(branch.lat, branch.lon) )      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                       mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney;
        for(Branch branch: branches){

             sydney = new LatLng(branch.lat, branch.lon);
            mMap.addMarker(new MarkerOptions().position(sydney).title(branch.address));
        }
        sydney=new LatLng(branches.get(3).lat, branches.get(3).lon);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)      // Sets the center of the map to Mountain View
                .zoom(12)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public class Branch {
        double lat,lon;
        private String type, address;
        public Branch( String type, String address) {
            this.type = type;
            this.address =address;
        }
        public Branch( String type, String address,double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
            this.type = type;
            this.address =address;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }


    public class BranchesAdapter extends ArrayAdapter<Branch> {

        private Context context;
        private List<Branch> branches = new ArrayList<>();

        public BranchesAdapter(@NonNull Context context, ArrayList<Branch> list) {
            super(context, 0 , list);
            this.context = context;
            branches= list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View item = convertView;
            if(item == null)
                item = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

           Branch currentBr = branches.get(position);

            TextView name =  item.findViewById(R.id.tv1);
            name.setText(currentBr.getType());

            TextView release =  item.findViewById(R.id.tv2);
            release.setText(currentBr.getAddress());

            return item;
        }
    }
}