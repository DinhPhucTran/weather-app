package tk.trandinhphuc.weatherapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

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
    private static LineChart mHourlyChart;
    private static String[] mHourlyLabels;
    private static int mColorWhite;
    private static int mColorAccent;
    private static int mColorBlue;
    private static IValueFormatter mValueFormatter;

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
        mValueFormatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + ((int) value);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        mDailyChart = (LineChart) view.findViewById(R.id.daily_chart);
        mHourlyChart = (LineChart) view.findViewById(R.id.hourly_chart);

        mHourlyChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.mViewPager.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Context context = view.getContext();
        mColorAccent = ContextCompat.getColor(context, R.color.colorAccent);
        mColorBlue = ContextCompat.getColor(context, R.color.colorLightBlue);
        mColorWhite = ContextCompat.getColor(context, R.color.colorTextWhite);
        return view;
    }

    public static void invalidateDailyChart(){
        LineDataSet dataSetLow = new LineDataSet(MainActivity.dailyLowEntries, "Low");
        dataSetLow.setLineWidth(2f);
        dataSetLow.setCircleRadius(4f);
        dataSetLow.setValueTextColor(mColorBlue);
        dataSetLow.setValueTextSize(12f);
        dataSetLow.setColor(mColorBlue);
        dataSetLow.setCircleColor(mColorBlue);
        dataSetLow.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSetLow.setValueFormatter(mValueFormatter);

        LineDataSet dataSetHigh = new LineDataSet(MainActivity.dailyHighEntries, "High");
        dataSetHigh.setLineWidth(2f);
        dataSetHigh.setCircleRadius(4f);
        dataSetHigh.setValueTextColor(mColorAccent);
        dataSetHigh.setValueTextSize(12f);
        dataSetHigh.setColor(mColorAccent);
        dataSetHigh.setCircleColor(mColorAccent);
        dataSetHigh.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSetHigh.setValueFormatter(mValueFormatter);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetLow);
        dataSets.add(dataSetHigh);

        LineData lineData = new LineData(dataSets);
        mDailyChart.setData(lineData);
        mDailyChart.setDescription(null);
        mDailyChart.animateXY(500, 500);
        mDailyChart.getLegend().setTextColor(mColorWhite);

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

        YAxis yAxisLeft = mDailyChart.getAxisLeft();
        yAxisLeft.setTextColor(mColorWhite);
        yAxisLeft.setTextSize(15f);

        YAxis yAxisRight = mDailyChart.getAxisRight();
        yAxisRight.setTextColor(mColorWhite);
        yAxisRight.setTextSize(15f);

        mDailyChart.invalidate();
    }

    public static void invalidateHourlyChart() {

        LineDataSet dataSet = new LineDataSet(MainActivity.hourlyEntries, "Hourly Temp.");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setValueTextSize(12f);
        dataSet.setColor(Color.GREEN);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setValueFormatter(mValueFormatter);

        LineData lineData = new LineData(dataSet);
        mHourlyChart.setData(lineData);
        mHourlyChart.setDescription(null);
        mHourlyChart.animateXY(500, 500);
        mHourlyChart.getLegend().setTextColor(mColorWhite);
        mHourlyChart.zoom(5, 1, 0, 0);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mHourlyLabels[(int) value];
            }

            public int getDecimalDigits() {  return 0; }
        };

        XAxis xAxis = mHourlyChart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setTextColor(mColorWhite);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15f);
        xAxis.setGranularity(1f);

        YAxis yAxisLeft = mHourlyChart.getAxisLeft();
        yAxisLeft.setTextColor(mColorWhite);
        yAxisLeft.setTextSize(15f);

        YAxis yAxisRight = mHourlyChart.getAxisRight();
        yAxisRight.setTextColor(mColorWhite);
        yAxisRight.setTextSize(15f);

        mHourlyChart.invalidate();
    }

    public static void setHourlyLabels(String[] labels) {
        mHourlyLabels = labels;
    }

    public static void setDailyLabels(String[] labels) {
        mDailyLabels = labels;
    }
}
