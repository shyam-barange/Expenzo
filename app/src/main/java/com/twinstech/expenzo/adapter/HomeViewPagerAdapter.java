package com.twinstech.expenzo.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.twinstech.expenzo.activity.ExpenseFragment;
import com.twinstech.expenzo.activity.TodaysExpenseFragment;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
  public HomeViewPagerAdapter(FragmentManager supportFragmentManager) {
    super(supportFragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    switch (position){
      case 0:
        return new ExpenseFragment();
      case 1:
        return new TodaysExpenseFragment();
    }

    return null;
  }

  @Override
  public int getCount() {
    return 2;
  }
}
