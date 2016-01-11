package com.mikhaellopez.dualshot.service.event;

/**
 * Created by mlopez on 08/01/16.
 */
public class TakeImageEvent {

    private boolean isFront;
    private String pathFile;

    public TakeImageEvent(boolean isFront, String pathFile) {
        this.isFront = isFront;
        this.pathFile = pathFile;
    }

    public boolean isFront() {
        return isFront;
    }

    public String getPathFile() {
        return pathFile;
    }
}
