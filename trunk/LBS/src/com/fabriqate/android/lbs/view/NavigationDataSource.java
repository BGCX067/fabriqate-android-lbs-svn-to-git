package com.fabriqate.android.lbs.view;

import android.graphics.Bitmap;

public interface NavigationDataSource {
    public int navigationCount();
    public String navigationTitle(int index);
    public Bitmap navigationIcon(int index);
}
