package com.muhanbit.sycrethealth.presenter;

import at.grabner.circleprogress.CircleProgressView;

/**
 * Created by hwjoo on 2017-01-16.
 */

public interface MainFragPresenter {
    void clickProgress();
    void clickOverflow();
    void initCircleProgress(CircleProgressView circleProgressView);
    void setTimeForProgress(CircleProgressView circleProgressView, int hour, int minute);
    void clickResetMenu(CircleProgressView circleProgressView);
    void clickSetMenu();
}
