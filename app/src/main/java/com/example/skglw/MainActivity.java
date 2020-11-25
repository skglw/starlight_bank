package com.example.skglw;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity {

    ViewPager pager;
    PagerAdapter pagerAdapter;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        pager = findViewById(R.id.pager);
        pagerAdapter = new fragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.item0).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.item1).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.item2).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item0:
                                pager.setCurrentItem(0);
                                break;
                            case R.id.item1:
                                pager.setCurrentItem(1);
                                break;
                            case R.id.item2:
                                pager.setCurrentItem(2);
                                break;
                        }
                        return false;
                }
        });
        bottomNavigationView.getMenu().findItem(R.id.item1).setChecked(true);
        pager.setCurrentItem(1);
    }


    public static class fragmentPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public fragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Fragment0.newInstance();
                case 1:
                    return Fragment1.newInstance();
                case 2:
                    return Fragment2.newInstance();
                default:
                    return null;
            }
        }

    }
}
//        // Returns the page title for the top indicator
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Page " + position;
//        }
//        public MyFragmentPagerAdapter(FragmentManager fm) {
//            super(fm);
//     }
//
//        @Override
//        public Fragment2 getItem(int position) {
//            switch (position) {
//                case 1:
//                    //pager.setCurrentItem(0);
//                    //tv.setText("1");
//
//                    return Fragment1.newInstance();
//
//                case  2:
//                   // pager.setCurrentItem(1);
//                    // tv.setText("2");
//
//                    return page.newInstance(position);
//                case 0:
//                   // pager.setCurrentItem(2);
//                    // tv.setText("2");
//
//                    return page.newInstance(position);
////                                imgMap.setVisibility(View.GONE);
////                                imgDial.setVisibility(View.GONE);
////                                imgMail.setVisibility(View.VISIBLE);
//            }
//            return page.newInstance(position);
//        } /////PAGE CLASS
//
//        @Override
//        public int getCount() {
//            return PAGE_COUNT;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Title " + position;
//        }






