package com.space.sodapop.androguard.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.space.sodapop.androguard.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener{

    private View rootview;
    private GoogleMap gmap;
    private MapView mapView;
    private List<Address> addresses;
    private Geocoder geocoder;




    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootview =inflater.inflate(R.layout.fragment_map,container,false);


        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

mapView=(MapView) rootview.findViewById(R.id.map);
if (mapView!=null){
    mapView.onCreate(null);
    mapView.onResume();
    mapView.getMapAsync(this);
}


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap=googleMap;



        LatLng huaral = new LatLng( -12.120144 ,  -77.033581);
        CameraUpdate zoom =CameraUpdateFactory.zoomTo((18));

        gmap.addMarker(new MarkerOptions()
        .position(huaral)
        .title("oreja huaral")
        .draggable(true))
        ;
        gmap.moveCamera(CameraUpdateFactory.newLatLng(huaral));
gmap.animateCamera(zoom);
gmap.setOnMarkerDragListener(this);
geocoder=new Geocoder(getContext(), Locale.getDefault());



    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude=marker.getPosition().latitude;
        double longitud=marker.getPosition().longitude;

        try {
            addresses=  geocoder.getFromLocation(latitude,longitud,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

         String address=addresses.get(0).getAddressLine(0);
        String cty=addresses.get(0).getLocality();
        String state=addresses.get(0).getAdminArea();
        String country=addresses.get(0).getCountryName();
        String postalcode=addresses.get(0).getPostalCode();

        Toast.makeText(getContext(),"adrress: "+address+"\n"+
                        "cyti: "+cty+"\n"+
                "state: "+state+"\n"+
                        "country: "+country+"\n"+
                        "postalcode: "+postalcode+"\n"


                ,Toast.LENGTH_LONG).show();
    }
}
