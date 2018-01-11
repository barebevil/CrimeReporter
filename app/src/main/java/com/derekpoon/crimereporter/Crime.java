package com.derekpoon.crimereporter;

import java.util.UUID;

/**
 * Created by derekpoon on 11/01/2018.
 */

//plain java Crime class that has
    //ID and title as member variables

public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime() {
        // Generate unique identifier
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
