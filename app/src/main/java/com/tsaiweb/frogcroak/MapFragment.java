package com.tsaiweb.frogcroak;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import MyMethod.CustomInfoWindowAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity());
                googleMap.setInfoWindowAdapter(adapter);
                // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(25.032205, 121.509884);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("台北植物園")
                        .snippet("推薦賞蛙季節：春、夏\n" +
                                "賞蛙重點：貢德氏赤蛙、金線蛙、牛蛙、黑眶蟾蜍")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(25.207213, 121.518016);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("三板橋")
                        .snippet("推薦賞蛙季節：四季\n" +
                                "賞蛙重點：台北樹蛙、斯文豪氏赤蛙、中國樹蟾、面天樹蛙、白頷樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(24.825223, 121.528436);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("內洞森林遊樂區")
                        .snippet("推薦賞蛙季節：四季\n" +
                                "賞蛙重點：古氏赤蛙、拉都希氏赤蛙、面天樹蛙、日本樹蛙、褐樹蛙、斯文豪氏赤蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(24.827958, 121.526329);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("信賢娃娃谷")
                        .snippet("推薦賞蛙季節：四季\n" +
                                "賞蛙重點：斯文豪氏赤蛙、古氏赤蛙、梭德氏赤蛙、褐樹蛙、日本樹蛙、翡翠樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(24.750222, 121.640365);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("仁山植物園步道")
                        .snippet("推薦賞蛙季節： 四季\n" + "賞蛙重點：莫氏樹蛙、日本樹蛙、面天樹蛙、貢德氏赤蛙、黑眶蟾蜍 ")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(23.658142, 121.408840);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("馬太鞍濕地")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 莫氏樹蛙、艾氏樹蛙、白頷樹蛙、日本樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );
//                );

                sydney = new LatLng(23.883861, 120.615958);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("十八彎古道")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 白頷樹蛙、小雨蛙、貢德氏赤蛙、褐樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(23.703469, 120.594099);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("斗六梅林里")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 諸羅樹蛙、中國樹蟾、黑蒙西氏小雨蛙、小雨蛙、白頷樹蛙、貢德氏赤蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(22.644444, 120.609764);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("屏科大后山")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 花狹口蛙、台北赤蛙、虎皮蛙、牛蛙、金線蛙、白頷樹蛙、小雨蛙、黑蒙西氏小雨蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(23.267731, 120.501190);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("青山仙公廟(175縣道)")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 日本樹蛙、褐樹蛙、拉都希氏赤蛙、澤蛙、黑眶蟾蜍、黑蒙西氏小雨蛙、面天樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(24.778289, 120.988108);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("輔英科大旁農園")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 中國樹蟾、花狹口蛙、虎皮蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(23.184464, 120.313115);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("官田水雉復育區")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 台北赤蛙、金線蛙、虎皮蛙、澤蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(23.942511, 120.933694);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("桃米坑")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 金線蛙、日本樹蛙、虎皮蛙、腹斑蛙、黑眶蟾蜍、古氏赤蛙、斯文豪氏赤蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(22.407324, 120.756307);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("大漢山林道")
                        .snippet("推薦賞蛙季節： 夏\n" + "賞蛙重點： 莫氏樹蛙、艾氏樹蛙、橙腹樹蛙、白頷樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );

                sydney = new LatLng(23.674764, 120.796819);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("溪頭森林遊樂區")
                        .snippet("推薦賞蛙季節： 四季\n" + "賞蛙重點： 莫氏樹蛙、艾氏樹蛙")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon))
                );


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(7.6f).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return view;
    }

}
