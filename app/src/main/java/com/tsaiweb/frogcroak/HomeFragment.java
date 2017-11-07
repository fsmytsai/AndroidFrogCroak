package com.tsaiweb.frogcroak;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import MyMethod.ViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabs;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }


    private void initView(View v) {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new MapFragment());
        fragments.add(new ChatFragment());
        fragments.add(new SoundRecognitionFragment());

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments, getActivity());
        viewPagerAdapter.tabTitles = new String[]{"", "", ""};
        viewPagerAdapter.tabIcons = new int[]{
                R.drawable.map,
                R.drawable.chat,
                R.drawable.frog
        };

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(7);

        tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(viewPager);
    }

}
