package com.bookstorepoly.nhom5_duan1.adapter;

import android.graphics.Bitmap;

public interface OnUpdateImageListener{
    void showPicker(int index);
    void OnUpdate(int index, Bitmap bitmap);
    void OnFail();
}
