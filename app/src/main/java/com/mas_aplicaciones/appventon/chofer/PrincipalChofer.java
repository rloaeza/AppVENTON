package com.mas_aplicaciones.appventon.chofer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;


public class PrincipalChofer extends Fragment {

    private final int REQUEST_ACCESS_FINE=0;
    private MapView mapView;
    public static Map<String,Object> lugar= new HashMap<>();
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(Objects.requireNonNull(getActivity()), getString(R.string.access_token));


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


    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mapView =view.findViewById(R.id.map);
        mapView.setVisibility(View.INVISIBLE);
        mapView.onCreate(savedInstanceState);
        if( ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
        mapView.getMapAsync(mapboxMap -> {


                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                // Add the SymbolLayer icon image to the map style
                                .withImage(ICON_ID, BitmapFactory.decodeResource(
                                        Objects.requireNonNull(getActivity()).getResources(), R.drawable.marker_40))

                                // Adding a GeoJson source for the SymbolLayer icons.
                                .withSource(new GeoJsonSource(SOURCE_ID,
                                        FeatureCollection.fromFeatures(FirebaseConexionFirestore.featuresChoferes)))

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
                        style -> getLocation(mapboxMap, style));
                mapboxMap.addOnMapClickListener(point -> {
                    PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                    List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID);
                    if (!features.isEmpty())
                    {
                        Feature selectedFeature = features.get(0);
                        double lat = Double.parseDouble(selectedFeature.getStringProperty("lat"));
                        double lon = Double.parseDouble(selectedFeature.getStringProperty("lon"));
                        Location location = mapboxMap.getLocationComponent().getLastKnownLocation();

                        Location location1 = new Location("punto2");
                        location1.setLongitude(lat);
                        assert location != null;
                        location.setLatitude(location.getLatitude());
                        location.setLongitude(location.getLongitude());
                        location1.setLatitude(lat);
                        location1.setLongitude(lon);
                        float distancia = location.distanceTo(location1);
                        if(distancia<100000)
                        {
                            String nombre = selectedFeature.getStringProperty("nombre");
                            String codigo = selectedFeature.getStringProperty("id");
                            String imagen = selectedFeature.getStringProperty("imagen");
                            boolean hay = Boolean.parseBoolean(selectedFeature.getStringProperty("hay"));
                            lugar.put("nombre", nombre);
                            lugar.put("id", codigo);
                            lugar.put("imagen", imagen);
                            lugar.put("hay",hay);
                            Toast.makeText(getContext(), "You selected " + distancia, Toast.LENGTH_SHORT).show();


                            findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_principalChofer_to_lugar2);
                        }
                        else{
                            Toast.makeText(getContext(),"No puede ver información está lejos del punto de acceso",Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                });

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
            StaticResources.actionSnackbar(getView(),"No podrá ver el mapa hasta que suba uma imagen de su auto", R.string.modificar,
                    R.id.action_principalChofer_to_configurar);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
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



    private void getLocation(MapboxMap mapboxMap,Style style)
    {
        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with a built LocationComponentActivationOptions object
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(Objects.requireNonNull(getActivity()),style).build());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }



}
