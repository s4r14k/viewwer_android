package com.priscilla.viewwer.api;

import com.priscilla.viewwer.model.responseSearch;

import java.util.ArrayList;

public interface ApiCallBackVisite {
    void onResponse(ArrayList<responseSearch> result);
}
