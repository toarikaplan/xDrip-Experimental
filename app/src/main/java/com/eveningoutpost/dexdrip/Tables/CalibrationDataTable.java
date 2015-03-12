package com.eveningoutpost.dexdrip.Tables;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eveningoutpost.dexdrip.Models.Calibration;
import com.eveningoutpost.dexdrip.NavigationDrawerFragment;
import com.eveningoutpost.dexdrip.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CalibrationDataTable extends ListActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private String menu_name = "Calibration Data Table";
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_data_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), menu_name, this);
        getData();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mNavigationDrawerFragment.swapContext(position);
    }

    private void getData() {
        final List<Calibration> latest = Calibration.latest(50);

        CalibrationDataCursorAdapter adapter = new CalibrationDataCursorAdapter(this, latest);

        this.setListAdapter(adapter);
    }


    public static class CalibrationDataCursorAdapterViewHolder {
        private final TextView timestamp;
        private final TextView bg;
        private final TextView estimate_raw;
        private final TextView slope;
        private final TextView intercept;

        public CalibrationDataCursorAdapterViewHolder(View root) {
            bg = (TextView) root.findViewById(R.id.list_data_bg);
            estimate_raw = (TextView) root.findViewById(R.id.estimate_raw);
            slope = (TextView) root.findViewById(R.id.list_data_slope);
            intercept = (TextView) root.findViewById(R.id.list_data_intercept);
            timestamp = (TextView) root.findViewById(R.id.list_data_timestamp);
        }
    }

    public static class CalibrationDataCursorAdapter extends BaseAdapter {
        private final Context           context;
        private final List<Calibration> calibrations;

        public CalibrationDataCursorAdapter(Context context, List<Calibration> calibrations) {
            this.context = context;
            if (calibrations == null)
                calibrations = new ArrayList<>();

            this.calibrations = calibrations;
        }

        public View newView(Context context, ViewGroup parent) {
            final View view = LayoutInflater.from(context).inflate(R.layout.list_item_calibration_data, parent, false);

            final CalibrationDataCursorAdapterViewHolder holder = new CalibrationDataCursorAdapterViewHolder(view);
            view.setTag(holder);

            return view;
        }

        public void bindView(View view, Context context, Calibration calibration) {
            final CalibrationDataCursorAdapterViewHolder tag = (CalibrationDataCursorAdapterViewHolder) view.getTag();
            tag.bg.setText(String.format("BG: %.2f", calibration.bg));
            tag.estimate_raw.setText(String.format("Est.RAW: %.2f", calibration.estimate_raw_at_time_of_calibration));
            tag.slope.setText(String.format("Slope: %.2f", calibration.slope));
            tag.intercept.setText(String.format("Intercept: %.2f", calibration.intercept));
            tag.timestamp.setText(new Date(calibration.getTimestampInMillis()).toString());
        }

        @Override
        public int getCount() {
            return calibrations.size();
        }

        @Override
        public Calibration getItem(int position) {
            return calibrations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = newView(context, parent);

            bindView(convertView, context, getItem(position));
            return convertView;
        }
    }
}
