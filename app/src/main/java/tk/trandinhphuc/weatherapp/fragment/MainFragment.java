package tk.trandinhphuc.weatherapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import tk.trandinhphuc.weatherapp.R;

public class MainFragment extends Fragment {

    private static MainFragment instance;

    private TextView mTvCity;
    private static ImageView mIconWeather;
    private static TextView mTvTemp;
    private static TextView mTvSummary;
    private static TextView mTvLow;
    private static TextView mTvHigh;
    private static TextView mTvWind;
    private static TextView mTvHumidity;
    private static TextView mTvPreci;
    private static TextView mTvCloud;

    //private static String mCity;
    public static double temp;
    public static double low;
    public static double high;
    public static double wind;
    public static double humidity;
    public static double precipitation;
    public static double cloud;
    public static String summary;
    public static int icon;
    private static Context context;



    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static MainFragment getInstance(){
        if(instance == null)
            instance = new MainFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mIconWeather = (ImageView) view.findViewById(R.id.icon_weather);
        mTvTemp = (TextView) view.findViewById(R.id.tv_temp);
        mTvSummary = (TextView) view.findViewById(R.id.tv_summary);
        mTvLow = (TextView) view.findViewById(R.id.tv_low);
        mTvHigh = (TextView) view.findViewById(R.id.tv_high);
        mTvWind = (TextView) view.findViewById(R.id.tv_wind);
        mTvHumidity = (TextView) view.findViewById(R.id.tv_humidity);
        mTvPreci = (TextView) view.findViewById(R.id.tv_preci);
        mTvCloud = (TextView) view.findViewById(R.id.tv_cloud);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        mTvTemp.setText( (int)temp + "");
        mTvLow.setText((int)low + "");
        mTvHigh.setText((int)high + "");
        mTvWind.setText(wind + " km/h");
        mTvHumidity.setText((int)(humidity * 100) + "%");
        mTvPreci.setText((int)(precipitation * 100) + "%");
        mTvCloud.setText((int)(cloud * 100)+ "%");

        if(summary != null)
            mTvSummary.setText(summary);

        if(icon != 0){
            Glide.with(getContext()).load(icon).into(mIconWeather);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static void setTemp(double t){
        temp = t;
        if(mTvTemp != null)
            mTvTemp.setText( (int)temp + "");
    }

    public static void setLow(double l){
        low = l;
        if(mTvLow != null) {
            mTvLow.setText((int)low + "");
        }
    }

    public static void setHigh(double h){
        high = h;
        if(mTvHigh != null) {
            mTvHigh.setText((int)high + "");
        }
    }

    public static void setWind(double w){
        wind = w;
        if(mTvWind != null) {
            mTvWind.setText(wind + " km/h");
        }
    }

    public static void setHumidity(double h){
        humidity = h;
        if(mTvHumidity != null) {
            mTvHumidity.setText((int)(humidity * 100) + "%");
        }
    }

    public static void setPrecipitation(double p){
        precipitation = p;
        if(mTvPreci != null) {
            mTvPreci.setText((int)(precipitation * 100) + "%");
        }
    }

    public static void setCloud(double c){
        cloud = c;
        if(mTvCloud != null) {
            mTvCloud.setText((int)(cloud * 100) + "%");
        }
    }

    public static void setSummary(String s){
        summary = s;
        if(mTvSummary != null) {
            mTvSummary.setText(summary);
        }
    }

    public static void setIcon(int i){
        icon = i;
        if(mIconWeather != null) {
            Glide.with(context).load(icon).into(mIconWeather);
        }
    }
}
