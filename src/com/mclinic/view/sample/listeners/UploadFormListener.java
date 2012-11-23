package com.mclinic.view.sample.listeners;

import java.util.ArrayList;

public interface UploadFormListener {
    void uploadComplete(ArrayList<String> result);
    void progressUpdate(String message, int progress, int max);
}
