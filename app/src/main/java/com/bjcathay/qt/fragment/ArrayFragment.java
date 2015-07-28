
package com.bjcathay.qt.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bjcathay.qt.R;

/**
 * Created by dengt on 15-7-28.
 */
public class ArrayFragment extends Fragment {
    int mNum;

    public static ArrayFragment newInstance(int num) {
        ArrayFragment array = new ArrayFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        array.setArguments(args);
        return array;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        System.out.println("mNum Fragment create =" + mNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        System.out.println("onCreateView = ");
        // 在这里加载每个 fragment的显示的 View
        View v = null;
        if (mNum == 0) {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            ((TextView) v.findViewById(R.id.textView1)).setText(mNum + "= mNum");
        } else if (mNum == 1) {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            ((TextView) v.findViewById(R.id.textView1)).setText(mNum + "= mNum");
        } else if (mNum == 2) {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            ((TextView) v.findViewById(R.id.textView1)).setText(mNum + "= mNum");
        } else {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            ((TextView) v.findViewById(R.id.textView1)).setText(mNum + "= mNum");
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        System.out.println("onActivityCreated = ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        System.out.println(mNum + "mNumDestory");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
