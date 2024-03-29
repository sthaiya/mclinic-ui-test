package com.mclinic.view.sample.activities;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.mclinic.api.model.Patient;
import com.mclinic.api.service.PatientService;
import com.mclinic.view.sample.R;
import com.mclinic.view.sample.utilities.Constants;
import com.mclinic.view.sample.utilities.FileUtils;

public class ObservationChartActivity extends Activity {

    private Patient mPatient;
    private String mObservationFieldName;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private GraphicalView mChartView;
    
    @Inject
    private PatientService pService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.observation_chart);

        if (!FileUtils.storageReady()) {
            showCustomToast(getString(R.string.error_storage));
            finish();
        }

        // TODO Check for invalid patient IDs
        String patientIdStr = getIntent().getStringExtra(Constants.KEY_PATIENT_ID);
        mPatient = getPatient(patientIdStr);

        mObservationFieldName = getIntent().getStringExtra(Constants.KEY_OBSERVATION_FIELD_NAME);

        setTitle(getString(R.string.app_name) + " > "
                + getString(R.string.view_observation));

        TextView textView = (TextView) findViewById(R.id.title_text);
        if (textView != null) {
            textView.setText(mObservationFieldName);
        }

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setLineWidth(3.0f);
        r.setColor(getResources().getColor(R.color.chart_red));
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);

        mRenderer.addSeriesRenderer(r);
        mRenderer.setShowLegend(false);
        //mRenderer.setXTitle("Encounter Date");
        //mRenderer.setAxisTitleTextSize(18.0f);
        mRenderer.setLabelsTextSize(11.0f);
        //mRenderer.setXLabels(10);
        mRenderer.setShowGrid(true);
        mRenderer.setLabelsColor(getResources().getColor(android.R.color.black));
    }

    private Patient getPatient(String patientUUID) {
        return pService.getPatientByUUID(patientUUID);
    }

    private void getObservations(Patient patient, String fieldName) {

//        ClinicAdapter ca = new ClinicAdapter();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        ca.open();
//        Cursor c = ca.fetchPatientObservation(patientId, fieldName);
//
//        if (c != null && c.getCount() >= 0) {
//
//            XYSeries series;
//            if (mDataset.getSeriesCount() > 0) {
//                series = mDataset.getSeriesAt(0);
//                series.clear();
//            } else {
//                series = new XYSeries(fieldName);
//                mDataset.addSeries(series);
//            }
//
//            int valueIntIndex = c.getColumnIndex(ClinicAdapter.KEY_VALUE_INT);
//            int valueNumericIndex = c.getColumnIndex(ClinicAdapter.KEY_VALUE_NUMERIC);
//            int encounterDateIndex = c.getColumnIndex(ClinicAdapter.KEY_ENCOUNTER_DATE);
//            int dataTypeIndex = c.getColumnIndex(ClinicAdapter.KEY_DATA_TYPE);
//
//            do {
//                try {
//                    Date encounterDate = df.parse(c.getString(encounterDateIndex));
//                    int dataType = c.getInt(dataTypeIndex);
//
//                    double value;
//                    if (dataType == Constants.TYPE_INT) {
//                        value = c.getInt(valueIntIndex);
//                        series.add(encounterDate.getTime(), value);
//                    } else if (dataType == Constants.TYPE_FLOAT) {
//                        value = c.getFloat(valueNumericIndex);
//                        series.add(encounterDate.getTime(), value);
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            } while (c.moveToNext());
//        }
//
//        if (c != null) {
//            c.close();
//        }
//        ca.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPatient != null && mObservationFieldName != null) {
            getObservations(mPatient, mObservationFieldName);
        }

        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getTimeChartView(this, mDataset,
                    mRenderer, null);
            layout.addView(mChartView, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        } else {
            mChartView.repaint();
        }
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_view, null);

        // set the text in the view
        TextView tv = (TextView) view.findViewById(R.id.message);
        tv.setText(message);

        Toast t = new Toast(this);
        t.setView(view);
        t.setDuration(Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}
