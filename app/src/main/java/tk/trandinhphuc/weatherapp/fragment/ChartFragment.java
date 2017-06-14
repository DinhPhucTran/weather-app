package tk.trandinhphuc.weatherapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
    private static String[] mDailyLabels;
    private static int mColorWhite;
    private static int mColorAccent;
    private static int mColorBlue;

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
        Context context = view.getContext();
        mColorAccent = ContextCompat.getColor(context, R.color.colorAccent);
        mColorBlue = ContextCompat.getColor(context, R.color.colorLightBlue);
        mColorWhite = ContextCompat.getColor(context, R.color.colorTextWhite);
        return view;
    }

    public static void invalidateChart(){
        LineDataSet dataSetLow = new LineDataSet(MainActivity.dailyLowEntries, "Low");
        //dataSetLow.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetLow.setColor(mColorBlue);
        dataSetLow.setCircleColor(mColorBlue);
        LineDataSet dataSetHigh = new LineDataSet(MainActivity.dailyHighEntries, "High");
        //dataSetHigh.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetHigh.setColor(mColorAccent);
        dataSetHigh.setCircleColor(mColorAccent);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetLow);
        dataSets.add(dataSetHigh);

        LineData lineData = new LineData(dataSets);
        mDailyChart.setData(lineData);
        mDailyChart.setDescription(null);
        mDailyChart.animateXY(500, 500);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mDailyLabels[(int) value];
            }

            public int getDecimalDigits() {  return 0; }
        };

        XAxis xAxis = mDailyChart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setTextColor(mColorWhite);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);

        YAxis yAxis = mDailyChart.getAxisLeft()

        mDailyChart.invalidate();
    }

    public static void setDailyLabels(String[] labels) {
        mDailyLabels = labels;
    }
}
