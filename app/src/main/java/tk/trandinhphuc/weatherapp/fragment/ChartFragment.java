package tk.trandinhphuc.weatherapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tk.trandinhphuc.weatherapp.AsyncResponse;
import tk.trandinhphuc.weatherapp.JsonTask;
import tk.trandinhphuc.weatherapp.MainActivity;
import tk.trandinhphuc.weatherapp.R;

public class ChartFragment extends Fragment {

    public static final String TAG = "---> ChartFragment";

    private static LineChart mDailyChart;
    private static ChartFragment fragment;

    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance() {
        fragment = new ChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChartFragment getInstance(){
        if(fragment == null)
            fragment = new ChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        mDailyChart = (LineChart) view.findViewById(R.id.daily_chart);
        return view;
    }

    public static void invalidateChart(){
        LineDataSet dataSet = new LineDataSet(MainActivity.dailyLowEntries, "Temp");
        LineData lineData = new LineData(dataSet);
        mDailyChart.setData(lineData);
        mDailyChart.invalidate();
    }
}
