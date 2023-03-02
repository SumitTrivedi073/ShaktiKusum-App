package com.shaktipumplimited.retrofit.interfaces;

public interface OnPDFCreatedInterface {
    void onPDFCreationStarted();

    void onPDFCreated(boolean success, String path);
}
