package tk.trandinhphuc.weatherapp.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

import tk.trandinhphuc.weatherapp.MainActivity;
import tk.trandinhphuc.weatherapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private static MapFragment fragment;

    private MapView mMapViewTemp;
    private MapView mMapViewCloud;
    private MapView mMapViewPreci;

    private GoogleMap mMapTemp;
    private GoogleMap mMapCloud;
    private GoogleMap mMapPreci;

    private TileProvider mTileProviderTemp;
    private TileProvider mTileProviderCloud;
    private TileProvider mTileProviderPreci;

    private LatLng mCurrentLocation;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment getInstance(){
        if(fragment == null)
            fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTileProviderTemp = new UrlTileProvider(128, 128) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                /* Define the URL pattern for the tile images */
                String s = "http://tile.openweathermap.org/map/temp_new/" + zoom + "/" + x + "/" + y + ".png?appid=d3c3dd4211e267f7ebd3b9049496f813";

                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 0;
                int maxZoom = 9;

                if ((zoom < minZoom || zoom > maxZoom)) {
                    return false;
                }

                return true;
            }
        };

        mTileProviderCloud = new UrlTileProvider(128, 128) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                /* Define the URL pattern for the tile images */
                String s = "http://tile.openweathermap.org/map/clouds_new/" + zoom + "/" + x + "/" + y + ".png?appid=d3c3dd4211e267f7ebd3b9049496f813";

                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 0;
                int maxZoom = 9;

                if ((zoom < minZoom || zoom > maxZoom)) {
                    return false;
                }

                return true;
            }
        };

        mTileProviderPreci = new UrlTileProvider(128, 128) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                /* Define the URL pattern for the tile images */
                String s = "http://tile.openweathermap.org/map/precipitation_new/" + zoom + "/" + x + "/" + y + ".png?appid=d3c3dd4211e267f7ebd3b9049496f813";

                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }

            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 0;
                int maxZoom = 9;

                if ((zoom < minZoom || zoom > maxZoom)) {
                    return false;
                }

                return true;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mCurrentLocation = new LatLng(MainActivity.lat, MainActivity.lng);

        mMapViewTemp = (MapView) view.findViewById(R.id.map_temp);
        mMapViewTemp.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapViewTemp.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMapTemp = map;

                mCurrentLocation = new LatLng(MainActivity.lat, MainActivity.lng);

                TileOverlay tileOverlay = mMapTemp.addTileOverlay(new TileOverlayOptions()
                        .tileProvider(mTileProviderTemp));

                mMapTemp.addMarker(new MarkerOptions().position(mCurrentLocation).title("Your location"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mCurrentLocation).zoom(6).build();
                mMapTemp.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        mMapViewCloud = (MapView) view.findViewById(R.id.map_cloud);
        mMapViewCloud.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapViewCloud.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMapCloud = map;

                TileOverlay tileOverlay = mMapCloud.addTileOverlay(new TileOverlayOptions()
                        .tileProvider(mTileProviderCloud));

                mCurrentLocation = new LatLng(MainActivity.lat, MainActivity.lng);

                mMapCloud.addMarker(new MarkerOptions().position(mCurrentLocation).title("Your location"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mCurrentLocation).zoom(6).build();
                mMapCloud.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        mMapViewPreci = (MapView) view.findViewById(R.id.map_preci);
        mMapViewPreci.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapViewPreci.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMapPreci = map;

                TileOverlay tileOverlay = mMapPreci.addTileOverlay(new TileOverlayOptions()
                        .tileProvider(mTileProviderPreci));

                mMapPreci.addMarker(new MarkerOptions().position(mCurrentLocation).title("Your location"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mCurrentLocation).zoom(6).build();
                mMapPreci.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapViewTemp.onResume();
        mMapViewCloud.onResume();
        mMapViewPreci.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapViewTemp.onPause();
        mMapViewCloud.onPause();
        mMapViewPreci.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapViewTemp.onDestroy();
        mMapViewCloud.onDestroy();
        mMapViewPreci.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapViewTemp.onLowMemory();
        mMapViewCloud.onLowMemory();
        mMapViewPreci.onLowMemory();
    }

}
