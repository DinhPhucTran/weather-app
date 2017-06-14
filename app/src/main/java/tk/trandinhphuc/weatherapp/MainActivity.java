package tk.trandinhphuc.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;
import tk.trandinhphuc.weatherapp.fragment.ChartFragment;
import tk.trandinhphuc.weatherapp.fragment.MainFragment;
import tk.trandinhphuc.weatherapp.fragment.MapFragment;

public class MainActivity extends AppCompatActivity implements LocationListener, AsyncResponse {

    static final String TAG = "---> MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static ViewPager mViewPager;
    private CoordinatorLayout mMainLayout;
    private AppBarLayout mAppBarLayout;
    private View mBlurView;
    private ImageView mMainBg;
    private ImageView mBlurBg;

    private LocationManager mLocationManager;
    private String provider;

    public static double lat;
    public static double lng;

    private JsonTask mJsonTask;
    private JSONObject mJsonObjet;
    private JSONObject mCurrentJsonObject;
    private JSONObject mDailyJsonObject;
    private JSONArray mDailyDataArray;
    private String mIconCode;

    private Geocoder mGeoCoder;
    private List<Address> mAddresses;

    public static List<Entry> dailyLowEntries;
    public static List<Entry> dailyHighEntries;
    public static List<Entry> hourlyEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mMainBg = (ImageView) findViewById(R.id.mainBg);
        mBlurBg = (ImageView) findViewById(R.id.blurBg);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setPadding(0, 0, 0, getNavigationBarHeight());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        dailyLowEntries = new ArrayList<>();
        dailyHighEntries = new ArrayList<>();
        hourlyEntries = new ArrayList<>();

        mJsonTask = new JsonTask();
        mJsonTask.delegate = this;

        mGeoCoder = new Geocoder(this, Locale.getDefault());
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = mLocationManager.getBestProvider(criteria, false);
        if(provider == null)
            provider = "";


//        Glide.with(this).load(R.drawable.bg_cloudy).into(mMainBg);
//        Glide.with(this).load(R.drawable.bg_cloudy)
//                .bitmapTransform(new BlurTransformation(this, 25))
//                .into(mBlurBg);
//        setBackground(R.drawable.bg_sunny14);

        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mBlurBg.animate().alpha(0.0f);
                } else {
                    mBlurBg.animate().alpha(1.0f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mViewPager.addOnPageChangeListener(onPageChangeListener);
        //to call onPageSelected on first page
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageChangeListener.onPageSelected(mViewPager.getCurrentItem());
            }
        });

        Location location = getLastKnownLocation(); //mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(this, "Location null", Toast.LENGTH_SHORT).show();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setBackground(int backgroundId) {
        Glide.with(this).load(backgroundId).into(mMainBg);
        Glide.with(this).load(backgroundId)
                .bitmapTransform(new BlurTransformation(this, 25))
                .into(mBlurBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        Toast.makeText(this, "Location changed to " + location.toString(), Toast.LENGTH_SHORT).show();

        try {
            mAddresses = mGeoCoder.getFromLocation(lat, lng, 1);
            int max = mAddresses.get(0).getMaxAddressLineIndex();
            StringBuilder address = new StringBuilder();
            for(int i = 0; i < max; i++){
                address.append(mAddresses.get(0).getAddressLine(i));
            }
            String city = mAddresses.get(0).getLocality();
            MainFragment.getInstance().setCity(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mJsonTask.execute("http://www.weathersite.somee.com/api/Weather?lat=" + lat + "&lng=" + lng);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onJsonLoaded(String json) {
        try {
            mJsonObjet = new JSONObject(json);
            mCurrentJsonObject = mJsonObjet.getJSONObject("currently");
            mDailyJsonObject = mJsonObjet.getJSONObject("daily");
            mDailyDataArray = mDailyJsonObject.getJSONArray("data");
            int l1 = mDailyDataArray.length();
            String[] dailyLabels = new String[l1];
            SimpleDateFormat df = new SimpleDateFormat("EEE");
            Calendar calendar = Calendar.getInstance();

            dailyLowEntries.clear();
            dailyHighEntries.clear();
            JSONObject dailyObject;
            for(int i = 0; i < l1; i++) {
                dailyObject = mDailyDataArray.getJSONObject(i);
                dailyLowEntries.add(new Entry(i, (int)dailyObject.getDouble("temperatureMin")));
                dailyHighEntries.add(new Entry(i, (int)dailyObject.getDouble("temperatureMax")));
                calendar.setTimeInMillis(dailyObject.getLong("time") * 1000);
                dailyLabels[i] = df.format(calendar.getTime());
            }

            ChartFragment.setDailyLabels(dailyLabels);
            ChartFragment.invalidateDailyChart();

            JSONArray hourlyArray = mJsonObjet.getJSONObject("hourly").getJSONArray("data");
            hourlyEntries.clear();
            JSONObject hourlyObject;
            int l2 = hourlyArray.length();
            String[] hourlyLabels = new String[l2];
            SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm");
            for(int i = 0; i < l2; i++) {
                hourlyObject = hourlyArray.getJSONObject(i);
                hourlyEntries.add(new Entry(i, (int)hourlyObject.getDouble("temperature")));
                hourlyLabels[i] = dfHour.format(hourlyObject.getLong("time") * 1000);
            }

            ChartFragment.setHourlyLabels(hourlyLabels);
            ChartFragment.invalidateHourlyChart();

            //MainFragment.temp = mCurrentJsonObject.getDouble("temperature");
            MainFragment.setTemp(mCurrentJsonObject.getDouble("temperature"));
            MainFragment.setSummary(mCurrentJsonObject.getString("summary"));
            MainFragment.setLow(mDailyDataArray.getJSONObject(0).getDouble("temperatureMin"));
            MainFragment.setHigh(mDailyDataArray.getJSONObject(0).getDouble("temperatureMax"));
            MainFragment.setWind(mCurrentJsonObject.getDouble("windSpeed"));
            MainFragment.setHumidity(mCurrentJsonObject.getDouble("humidity"));
            MainFragment.setPrecipitation(mCurrentJsonObject.getDouble("precipProbability"));
            MainFragment.setCloud(mCurrentJsonObject.getDouble("cloudCover"));

            mIconCode = mCurrentJsonObject.getString("icon");
            if("wind".equals(mIconCode)) {
                setBackground(R.drawable.bg_hail);
                MainFragment.setIcon(R.drawable.wind);
            } else if("clear-day".equals(mIconCode)) {
                setBackground(R.drawable.bg_clear_day_edited);
                MainFragment.setIcon(R.drawable.clear_day);
            } else if("clear-night".equals(mIconCode)) {
                setBackground(R.drawable.bg_clear_day_edited);
                MainFragment.setIcon(R.drawable.clear_night);
            } else if("partly-cloudy-day".equals(mIconCode)) {
                setBackground(R.drawable.bg_cloudy3);
                MainFragment.setIcon(R.drawable.partly_cloudy_day);
            } else if("partly-cloudy-night".equals(mIconCode)) {
                setBackground(R.drawable.bg_cloudy15);
                MainFragment.setIcon(R.drawable.partly_cloudy_night);
            } else if("cloudy".equals(mIconCode)) {
                setBackground(R.drawable.bg_cloudy);
                MainFragment.setIcon(R.drawable.cloudy);
            } else if("fog".equals(mIconCode)) {
                setBackground(R.drawable.bg_fog_mist);
                MainFragment.setIcon(R.drawable.fog);
            } else if("sleet".equals(mIconCode)) {
                setBackground(R.drawable.bg_so_cold);
                MainFragment.setIcon(R.drawable.sleet);
            }  else if("snow".equals(mIconCode)) {
                setBackground(R.drawable.bg_so_cold);
                MainFragment.setIcon(R.drawable.snow);
            } else if("rain".equals(mIconCode)) {
                setBackground(R.drawable.bg_rain);
                MainFragment.setIcon(R.drawable.rain);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MainFragment.newInstance();
                case 1:
                    return ChartFragment.newInstance();
                case 2:
                    return MapFragment.getInstance();

            }
            return MainFragment.newInstance();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Weather";
                case 1:
                    return "Charts";
                case 2:
                    return "Maps";
            }
            return null;
        }
    }
}
