package ir.mhdr.bmi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment result = null;

        switch (position) {
            case 0:
                result = new GraphFragment();
                break;
            case 1:
                result = new BmiFragment();
                break;
            case 2:
                result = new TableFragment();
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
