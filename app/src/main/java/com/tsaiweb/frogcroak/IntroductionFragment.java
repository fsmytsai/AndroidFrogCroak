package com.tsaiweb.frogcroak;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import MyMethod.IntroPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionFragment extends Fragment {
    private ViewPager vp_Introduction;
    private RadioGroup rg_Introduction;

    public IntroductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduction, container, false);
        vp_Introduction = (ViewPager) view.findViewById(R.id.vp_Introduction);
        rg_Introduction = (RadioGroup) view.findViewById(R.id.rg_Introduction);

        final LayoutInflater mInflater = getActivity().getLayoutInflater().from(getActivity());

        View v1 = mInflater.inflate(R.layout.intro1, null);
        View v2 = mInflater.inflate(R.layout.intro2, null);
        View v3 = mInflater.inflate(R.layout.intro3, null);
        View v4 = mInflater.inflate(R.layout.intro4, null);

        List<View> viewList = new ArrayList();
        viewList.add(v1);
        viewList.add(v2);
        viewList.add(v3);
        viewList.add(v4);

        vp_Introduction.setAdapter(new IntroPagerAdapter(viewList,getActivity()));
        vp_Introduction.setCurrentItem(0);
        vp_Introduction.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rg_Introduction.check(R.id.rb_page1);
                        break;
                    case 1:
                        rg_Introduction.check(R.id.rb_page2);
                        break;
                    case 2:
                        rg_Introduction.check(R.id.rb_page3);
                        break;
                    case 3:
                        rg_Introduction.check(R.id.rb_page4);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

}
