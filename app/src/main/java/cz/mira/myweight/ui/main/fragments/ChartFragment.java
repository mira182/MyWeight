package cz.mira.myweight.ui.main.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import cz.mira.myweight.R;
import cz.mira.myweight.charts.AbstractChart;
import cz.mira.myweight.charts.ChartType;
import cz.mira.myweight.rest.dto.WeightReportDTO;
import lombok.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment {

    private static final String TAG = "ChartFragment";

    private static final String ARG_PARAM1 = "weightReport";

    private static final String ARG_PARAM2 = "chartType";

    private ArrayList<WeightReportDTO> weightReport;

    private ChartType chartType;

    private OnFragmentInteractionListener mListener;

    public ChartFragment() {}

    public static ChartFragment newInstance(@NonNull ArrayList<WeightReportDTO> weightReport, ChartType chartType) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, weightReport);
        args.putString(ARG_PARAM2, chartType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weightReport = (ArrayList<WeightReportDTO>) getArguments().getSerializable(ARG_PARAM1);
            chartType = ChartType.valueOf(getArguments().getString(ARG_PARAM2));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_weight_chart, container, false);
        AbstractChart chart = chartType.createChart(getContext(), view, weightReport);
        chart.createChart();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
