package com.mas_aplicaciones.appventon.chofer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;


public class PrincipalChofer extends Fragment {

    private final int REQUEST_ACCESS_FINE=0;
    private MapView mapView;
   // private LatLng currentPosition;
  //  private FusedLocationProviderClient fusedLocationClient;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private AlertDialog alertDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));


        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_ACCESS_FINE);
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    getActivity().finishAffinity();
                }
            }
        }
        /*fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.d("Lon",String.valueOf(location.getLatitude()));
                            Log.d("Lon",String.valueOf(location.getLongitude()));
                            currentPosition = new LatLng(location.getLatitude(),location.getLongitude());


                        }
                    }
                });*/





    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mapView =view.findViewById(R.id.map);
        mapView.setVisibility(View.INVISIBLE);
        mapView.onCreate(savedInstanceState);
        if( ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {









                    mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/streets-v11")

                                    // Add the SymbolLayer icon image to the map style
                                    .withImage(ICON_ID, BitmapFactory.decodeResource(
                                            getActivity().getResources(), R.drawable.marker_40))

                                    // Adding a GeoJson source for the SymbolLayer icons.
                                    .withSource(new GeoJsonSource(SOURCE_ID,
                                            FeatureCollection.fromFeatures(FirebaseConexionFirestore.features)))

                                    // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                                    // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                                    // the coordinate point. This is offset is not always needed and is dependent on the image
                                    // that you use for the SymbolLayer icon.
                                    .withLayer(
                                            new SymbolLayer(LAYER_ID, SOURCE_ID)
                                                    .withProperties(PropertyFactory.iconImage(ICON_ID),
                                                            iconAllowOverlap(true),
                                                            iconIgnorePlacement(true),
                                                            iconOffset(new Float[]{0f, -9f}))
                                    ),
                            new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {

                                    getLocation(mapboxMap, style);


                                }

                            });
                    mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                        @Override
                        public boolean onMapClick(@NonNull LatLng point) {
                            PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                            List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID);
                            if (!features.isEmpty()) {
                                Feature selectedFeature = features.get(0);
                                String title = selectedFeature.getStringProperty("title");
                                Toast.makeText(getContext(), "You selected " + title, Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).navigate(R.id.action_principalChofer_to_menu2);
                            }
                            return true;
                        }
                    });

                }




        });
       }




    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(1);
        }

        final View view = inflater.inflate(R.layout.fragment_principal_chofer, container, false);
        ImageButton btnMenu = view.findViewById(R.id.image_button_menu);

        btnMenu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_principalChofer_to_menu2));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mapView.onStart();
        if(FirebaseConexionFirestore.getValue("URI_Coche").equals(""))
        {
            mapView.setVisibility(View.INVISIBLE);
            StaticResources.actionSnackbar(getView());
        }
        else {
            mapView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onStart();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_ACCESS_FINE)
        {
            if(grantResults.length >0  && grantResults[0] == PackageManager.PERMISSION_GRANTED &&  grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(),"concedida la geolocalización",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getActivity(),"Debe de permitir la geolocalización",Toast.LENGTH_SHORT).show();
            }

        }
    }








    private boolean enableLocationComponent() {

        // Check if permissions are enabled and if not request
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setMessage("Localizando").build();
        alertDialog.show();
       if( ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
       {

            Toast.makeText(getActivity(),"paso",Toast.LENGTH_LONG).show();
            alertDialog.cancel();
            return true;

        }
        else {
            alertDialog.cancel();

           return false;


        }
    }

    private void getLocation(MapboxMap mapboxMap,Style style)
    {
        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with a built LocationComponentActivationOptions object
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(getActivity(),style).build());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }



}
