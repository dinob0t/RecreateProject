package io.geekgirl.recreate;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class MapActivity extends FragmentActivity
        implements OnMapClickListener, OnMarkerClickListener, OnMapLongClickListener {
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */

    private static final int WIDTH_MAX = 25;
    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 100;
    private static final double DEFAULT_RADIUS = 100;
    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    private GoogleMap mMap;
    private HashMap<LatLng,Marker> markerMap;
    private Marker markerCurrent;
    private int markerNum;
    private boolean setHomeBase;
    private Marker homeBase;
    private Circle homeBaseCircle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        markerMap = new HashMap<LatLng, Marker>();
        markerNum = 0;
        setHomeBase = false;
        //mMap.setOnMarkerClickListener((OnMarkerClickListener) this);
    }

    /**
     * Listener for quick tap on screen
     */
    @Override
    public void onMapClick(LatLng point) {

        markerCurrent = mMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude,point.longitude)));
        markerMap.put((LatLng) point,(Marker) markerCurrent);
        markerNum++;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        marker.remove();
        markerNum--;
        return true; //set to false if want default action of centering map and displaying text

    }

    @Override
    public void onMapLongClick(LatLng point) {

        if (setHomeBase == true) {
            homeBaseCircle.remove();
        }
        HomeBaseCircle homeBaseCircle = new HomeBaseCircle(point, DEFAULT_RADIUS);
        setHomeBase = true;

    }

    private class HomeBaseCircle {
        private double radius;
        public HomeBaseCircle(LatLng center, double radius) {
            this.radius = radius;
            homeBaseCircle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeColor(Color.BLACK)
                    .strokeWidth(0)
                    .fillColor(Color.HSVToColor(
                            ALPHA_MAX, new float[] {HUE_MAX, 1, 1})));

        }

    }

}
