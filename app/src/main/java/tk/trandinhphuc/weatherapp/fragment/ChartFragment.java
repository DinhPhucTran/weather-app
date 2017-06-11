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

    private LineChart mDailyChart;
    private List<Entry> mDailyEntries;
    private TextView jsonText;
    private static ChartFragment fragment;
    private static String location;

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
        jsonText = (TextView) view.findViewById(R.id.json_text);
        if(location != null)
            jsonText.setText(location);
        mDailyEntries = new ArrayList<>();

        mDailyEntries.add(new Entry(2, 25));
        mDailyEntries.add(new Entry(3, 20));
        mDailyEntries.add(new Entry(4, 21));
        mDailyEntries.add(new Entry(5, 26));
        mDailyEntries.add(new Entry(6, 28));
        mDailyEntries.add(new Entry(7, 30));

        LineDataSet dataSet = new LineDataSet(mDailyEntries, "Temp");
        LineData lineData = new LineData(dataSet);
        mDailyChart.setData(lineData);
        mDailyChart.invalidate();
        return view;
    }

    public void setJsonText(String text){
        location = text;
    }
}
