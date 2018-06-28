package com.space.sodapop.androguard.fragments;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.space.sodapop.androguard.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener,View.OnClickListener{
private static final String PHAT_START="start";
    private static final String PHAT_message="message";
public String valor;
   MarkerOptions marker1= new MarkerOptions();
    private View rootview;
    private GoogleMap gmap;
    private MapView mapView;
    private List<Address> addresses;
    private Geocoder geocoder;
    private MarkerOptions marker2,marker3;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

private FloatingActionButton fab;

    public MapFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootview =inflater.inflate(R.layout.fragment_map,container,false);
fab=(FloatingActionButton) rootview.findViewById(R.id.fab);






//recupera firebase y muestra

        final TextView  texto=(TextView) rootview.findViewById(R.id.textito);
        texto.setText("orjas");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference referencia=database.getReference(PHAT_START).child(PHAT_message);
referencia.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
texto.setText(dataSnapshot.getValue(String.class));
        valor=dataSnapshot.getValue(String.class);
       gmap.clear();
        MarkerOptions markerr= new MarkerOptions();

        LatLng huaral2 = new LatLng( -12.120 ,  -77.033);




         gmap.addMarker(new MarkerOptions().
                position(huaral2).
                title("Cesar Edward")
                .snippet("valor "+"\n"+valor)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
Toast.makeText(getContext(),"error al consuktar en firebase",Toast.LENGTH_LONG).show();

    }
});


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
        this.cheka();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap=googleMap;


        gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });



        CameraUpdate zoom =CameraUpdateFactory.zoomTo((18));
        LatLng huaral = new LatLng( -12.120144 ,  -77.033581);

        gmap.moveCamera(CameraUpdateFactory.newLatLng(huaral));
gmap.animateCamera(zoom);
gmap.setOnMarkerDragListener(this);
geocoder=new Geocoder(getContext(), Locale.getDefault());


    }
private  void  cheka(){
    try {
        int gpssignal=Settings.Secure.getInt(getActivity().getContentResolver(),Settings.Secure.LOCATION_MODE);
        if(gpssignal==0){

            //el intent gps no esta activo
            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    } catch (Settings.SettingNotFoundException e) {
        e.printStackTrace();
    }


    }
    @Override
    public void onMarkerDragStart(Marker marker) {
marker.hideInfoWindow();
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

        if (marker1.getTitle().equals("huaral2"))

            marker.setSnippet(valor);
        marker.showInfoWindow();


        if (marker.getTitle().equals("oreja")){
            marker.setSnippet("adrress: "+address+"\n"+
                    "cyti: "+cty+"\n"+
                    "state: "+state+"\n"+
                    "country: "+country+"\n"+
                    "postalcode: "+postalcode+"\n"
            );


        }
        marker.showInfoWindow();
        /*Toast.makeText(getContext(),"adrress: "+address+"\n"+
                        "cyti: "+cty+"\n"+
                "state: "+state+"\n"+
                        "country: "+country+"\n"+
                        "postalcode: "+postalcode+"\n"


                ,Toast.LENGTH_LONG).show();*/    }

    @Override
    public void onClick(View v) {

    }

    public static void animateMarker(final Location destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }
    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }
}
