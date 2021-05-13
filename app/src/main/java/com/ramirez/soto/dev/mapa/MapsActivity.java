package com.ramirez.soto.dev.mapa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {//, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private List<Address> address;
    private double[][] Puntos_array = {{19.031593719530207, -98.20960414711624},{19.033627889375683, -98.20800018621611},{19.03287736030919, -98.20560765575259}};
    private String[][] info_u ={{"primera ubicacion", "9:00"},{"segunda ubicacion", "10:00"},{"Prueba","10:30"}};
    //*****************************  PERMISO PARA UBICACIONES    ***********************************

    private static final String TAG = "**************Mapa";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    AutocompleteSupportFragment autocompleteFragment = null;

    double Lat = 0.0;
    double Longi = 0.0;
    String Direccion = "Ubicacion Actual del Usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


       // Obtener_Ubicacion_Dispositivo();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }

        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);

      /*  mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle()
            {


                double latt =  mMap.getCameraPosition().target.latitude;
                double lngg =  mMap.getCameraPosition().target.longitude;

                Lat = latt;
                Longi = lngg;
                try { address = geocoder.getFromLocation(Lat,Longi,1); } catch (IOException e) { e.printStackTrace(); }

                Address direccion =  address.get(0);
                Direccion = direccion.getAddressLine(0);
                System.out.println(Direccion);
                Agregar_Nueva_Ubicacion_Mapa( mMap.getCameraPosition().target, DEFAULT_ZOOM,Direccion);

            }
        });*/

        mMap.clear();
        for (int i =0; i<Puntos_array.length; i++){
            Agregar_Viajes_Encontrados(new LatLng(Puntos_array[i][0],Puntos_array[i][1]),DEFAULT_ZOOM, info_u[i][0], info_u[i][1],i);
        }
        //Inicializar_Autocompletador();
        /*CameraPosition camera =new CameraPosition.Builder()
                .target(sydney)
                .zoom(18)
                .bearing(0)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));*/

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void Agregar_Viajes_Encontrados(LatLng latLng, float zoom, String title, String hora, int index)
    {
        MarkerOptions mark = new MarkerOptions().position(latLng).draggable(true);

        Marker Nuevo_Marcador = mMap.addMarker(mark);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


        Nuevo_Marcador.setTag((Object)index);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                final int temp = (int)marker.getTag();
                System.err.println("AQui"+ temp);
                AlertDialog.Builder Ventada_info = new AlertDialog.Builder(MapsActivity.this);
                View vista_alerta = getLayoutInflater().inflate(R.layout.elemento_dialog_mapa,null); // VINCULAMOS VISTA **************
                Ventada_info.setView(vista_alerta);
                final AlertDialog vista_Viaje = Ventada_info.create();

                TextView Campo_titulo = (TextView) vista_alerta.findViewById(R.id.titulo_id);
                TextView Campo_hora = (TextView) vista_alerta.findViewById(R.id.hora_id);
                ImageView Campo_foto = vista_alerta.findViewById(R.id.foto_id); ;
                Campo_titulo.setTextColor(Color.WHITE);
                Campo_hora.setTextColor(Color.WHITE);

                Campo_titulo.setText(title);
                System.out.println(title);
                Campo_hora.setText(hora);
                if( temp == 0)
                { Campo_foto.setImageResource(R.drawable.im1);}
                else if (temp == 1)
                { Campo_foto.setImageResource(R.drawable.img2);}
                else if (temp == 2)
                { Campo_foto.setImageResource(R.drawable.img3);}
                vista_Viaje.show();


                return false;
            }
        });
        /*
            mark.icon(BitmapDescriptorFactory.fromResource(R.drawable.box4));
            Marker Nuevo_Marcador = mMap.addMarker(mark);
            Nuevo_Marcador.setTag(index);
        System.out.println("*******************************"+index);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            //System.out.println(title);

            //Nuevo_Marcador.showInfoWindow();

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {
                @Override
                public boolean onMarkerClick(Marker marker)
                {
                    int n = (int) Nuevo_Marcador.getTag();

                    AlertDialog.Builder Ventada_info = new AlertDialog.Builder(MapsActivity.this);
                    View vista_alerta = getLayoutInflater().inflate(R.layout.elemento_dialog_mapa,null); // VINCULAMOS VISTA **************
                    Ventada_info.setView(vista_alerta);
                    final AlertDialog vista_Viaje = Ventada_info.create();

                    TextView Campo_titulo = (TextView) vista_alerta.findViewById(R.id.titulo_id);
                    TextView Campo_hora = (TextView) vista_alerta.findViewById(R.id.hora_id);
                    ImageView Campo_foto = vista_alerta.findViewById(R.id.foto_id); ;

                    Campo_titulo.setText(title);
                    System.out.println(title);
                    Campo_hora.setText(hora);
                    if (title.equals("primera ubicacion"))
                    { Campo_foto.setImageResource(R.drawable.im1);}
                    else if (title.equals("segunda ubicacion"))
                    { Campo_foto.setImageResource(R.drawable.img2);}
                    else if (title.equals("Prueba"))
                    { Campo_foto.setImageResource(R.drawable.img3);}
                    vista_Viaje.show();

                    //***************************************************************************************************************************************************************
                 return false;
                }
            });*/
       // }

    }

    /*private void Obtener_Ubicacion_Dispositivo()
    {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try
        {

            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();

                        Lat = currentLocation.getLatitude();
                        Longi = currentLocation.getLongitude();

                        try { address = geocoder.getFromLocation(Lat,Longi,1); } catch (IOException e) { e.printStackTrace(); }

                        Address direccion =  address.get(0);
                        Direccion = direccion.getAddressLine(0);

                        Agregar_Nueva_Ubicacion_Mapa(new LatLng(Lat,Longi),DEFAULT_ZOOM,Direccion);

                    }
                    else
                    {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (SecurityException e){ Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() ); }
    }*/

   /* private void Agregar_Nueva_Ubicacion_Mapa(LatLng latLng, float zoom, String title)
    {
        //Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.addMarker(new MarkerOptions().position(latLng).title(title).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.setOnMarkerDragListener(this);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }*/

  /*  @Override
    public void onMarkerDragStart(Marker marker) {
       // autocompleteFragment.a.setText("Buscando...");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
       // autocompleteFragment.a.setText("Buscando...");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        /* double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;

        try
        {

            address = geocoder.getFromLocation(lat,lng,1);

        } catch (IOException e) { e.printStackTrace(); }
        Address direccion =  address.get(0);

        mMap.clear();
        Agregar_Nueva_Ubicacion_Mapa(new LatLng(lat,lng),DEFAULT_ZOOM,direccion.getAddressLine(0));
        Lat = lat;
        Longi = lng;
        Direccion = direccion.getAddressLine(0);
        System.out.print("["+ Lat+ ","+ Longi+"],"+"Dir: "+Direccion);
        //autocompleteFragment.a.setText(Direccion);

         */
        /*

        String mensaje ="{Latitud: ["+direccion.getLatitude()+"]\n"+
                        "Longitud: ["+direccion.getLongitude()+"]\n"+
                        "Direccion: ["+direccion.getAddressLine(0)+"]\n"+
                        "Nombre elemento: ["+direccion.getFeatureName()+"]}";

        Mensaje_CuadroDialogo("Encontrado", mensaje, "ok");*/

        //Log.d(TAG, "Movi el marker: "+mensaje+"");
   // }*/

    /*private void Inicializar_Autocompletador()
    {
        // Initialize Places.

        Places.initialize(getApplicationContext(), API_Google);

        // Create a new Places client instance.

        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.

        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.AutoCompletador_Origen);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+ ", " + place.getLatLng().longitude);
                Agregar_Nueva_Ubicacion_Mapa(place.getLatLng(),DEFAULT_ZOOM,place.getAddress());
                Lat = place.getLatLng().latitude;
                Longi = place.getLatLng().longitude;
                Direccion = place.getAddress();
                System.out.print("["+ Lat+ ","+ Longi+"],"+"Dir: "+Direccion);

                autocompleteFragment.a.setText(Direccion);

            }

            @Override
            public void onError(Status status)
            {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }*/
}