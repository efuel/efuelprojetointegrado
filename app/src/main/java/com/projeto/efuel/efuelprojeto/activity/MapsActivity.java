package com.projeto.efuel.efuelprojeto.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.projeto.efuel.efuelprojeto.R;
import com.projeto.efuel.efuelprojeto.config.ConfiguracaoFireBase;



public class MapsActivity extends AppCompatActivity implements  OnMapReadyCallback {

    //atributos
    private GoogleMap mMap;
    private Marker  currentLocationMaker;
    private LatLng currentLocationLatLong;
    private DatabaseReference mDatabase;
    private FirebaseAuth autenticacao;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker marker;
    private Marker Somewhere;
    private int markerclicked;
    private Context context;
    private Marker myMarker;

    //Postos
    private static final LatLng P1 = new LatLng(-19.863091, -43.957478);
    private static final LatLng P2 = new LatLng(-19.858324, -43.959418);
    private static final LatLng P3 = new LatLng(-19.853987, -43.961088);
    private static final LatLng P4 = new LatLng(-19.840299, -43.966728);
    private static final LatLng P5 = new LatLng(-19.863091, -43.957478);
    private static final LatLng P6 = new LatLng(-19.863091, -43.957478);
    private static final LatLng P7 = new LatLng(-19.863091, -43.957478);
    private static final LatLng P8 = new LatLng(-19.863091, -43.957478);
    private static final LatLng P9 = new LatLng(-19.863091, -43.957478);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //startGettingLocations();





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
        //Postos

        //recuperar localizacao do usuario

       recuperarLocalizacaoUsuario();

        // Add a marker in Sydney and move the camera

       /* LatLng recife = new LatLng(-19.872611, -43.954704);
        mMap.addMarker(new MarkerOptions().position(recife).title("Eu") .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(13).target(recife).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


    }






    //recuperar localizacao do usuario
    private void recuperarLocalizacaoUsuario(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Recuperar latitude e longitude
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng meuLocal = new LatLng(latitude, longitude);
                //Marcador localização do usuário
                mMap.clear();
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.addMarker(new MarkerOptions().position(meuLocal).title("Minha localização").icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(meuLocal, 15));
                //Postos Marcados
                marcadoresNoMapa();


            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Solicitar atulizações de localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }
    }


    public void marcadoresNoMapa(){

        mMap.addMarker(new MarkerOptions().position(P1).title("Posto Shell - Posto Tropico").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas)).snippet("Etanol:R$2,889  Gasolina:R$4,856"));
        mMap.addMarker(new MarkerOptions().position(P2).title("Posto Shell").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas)).snippet("Etanol:R$2,889  Gasolina:R$4,856"));
        mMap.addMarker(new MarkerOptions().position(P3).title("Posto Ipiranga").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas)).snippet("Etanol:R$2,889  Gasolina:R$4,856"));
        mMap.addMarker(new MarkerOptions().position(P4).title("Posto BR-Posto ViaBrasil com GNV").icon(BitmapDescriptorFactory.fromResource(R.drawable.gas)).snippet("Etanol:R$2,889  Gasolina:R$4,856"));
    }




    //Método que chama os botoes do menu na activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.menuSair:
                autenticacao.signOut();
                finish();
                break;
        }
                return super.onOptionsItemSelected(item);
        }



}

    //mÉTODO PARA ATIVAR RASTREAMENTO DE LOCALIZAÇÃO










