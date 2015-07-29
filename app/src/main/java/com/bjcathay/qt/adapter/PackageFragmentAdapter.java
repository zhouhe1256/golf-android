
package com.bjcathay.qt.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.bjcathay.qt.fragment.ArrayFragment;

/**
 * Created by dengt on 15-7-28.
 */
public class PackageFragmentAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public PackageFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    // 得到每个item
    @Override
    public Fragment getItem(int position) {
        ArrayFragment arrayFragment = ArrayFragment.newInstance(position);
        arrayFragment.setContext(context);
        return arrayFragment;
    }

    // 初始化每个页卡选项
    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        // TODO Auto-generated method stub
        return super.instantiateItem(arg0, arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }
}
