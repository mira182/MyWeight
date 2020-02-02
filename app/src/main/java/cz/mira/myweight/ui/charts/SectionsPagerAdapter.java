package cz.mira.myweight.ui.charts;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.mira.myweight.R;
import cz.mira.myweight.rest.dto.WeightReportDTO;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.weight_tab_text,
            R.string.bmi_tab_text,
            R.string.bodyFat_tab_text,
            R.string.visceralFat_tab_text,
            R.string.muscleMass_tab_text,
            R.string.muscleQuality_tab_text,
            R.string.boneMass_tab_text,
            R.string.bmr_tab_text,
            R.string.metabolicAge_tab_text,
            R.string.bodyWatter_tab_text,
            R.string.physiqueRating_tab_text,
            R.string.all_tab_text
    };

    private final Context mContext;

    private final List<Fragment> mFragmentList = new ArrayList<>();

    private ArrayList<WeightReportDTO> weightReport;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    public void addFragment(Fragment fragment, int position) {
        mFragmentList.add(position, fragment);
    }

    public void setWeightReport(ArrayList<WeightReportDTO> weightReport) {
        this.weightReport = weightReport;
    }

    public void removeAll() {
        mFragmentList.clear();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}