package com.twinstech.expenzo.view;


import androidx.fragment.app.Fragment;

public interface NavigationDrawerItemView {
    void render(Fragment fragment);

    void goToHome();
}
