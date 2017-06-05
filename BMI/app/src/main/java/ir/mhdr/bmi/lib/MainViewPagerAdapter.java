package ir.mhdr.bmi.lib;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import ir.mhdr.bmi.BmiFragment;
import ir.mhdr.bmi.GraphFragment;
import ir.mhdr.bmi.TableFragment;


public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

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
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
