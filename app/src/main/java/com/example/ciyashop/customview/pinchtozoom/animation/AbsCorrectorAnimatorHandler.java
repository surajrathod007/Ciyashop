package com.example.ciyashop.customview.pinchtozoom.animation;

import android.animation.ValueAnimator;

import com.example.ciyashop.customview.pinchtozoom.ImageMatrixCorrector;


/**
 * Created by Martin on 13-10-2016.
 */

public abstract class AbsCorrectorAnimatorHandler implements ValueAnimator.AnimatorUpdateListener {

    private final ImageMatrixCorrector corrector;
    private final float[] values;

    public AbsCorrectorAnimatorHandler(ImageMatrixCorrector corrector) {
        this.corrector = corrector;
        this.values = new float[9];
    }

    public ImageMatrixCorrector getCorrector() {
        return corrector;
    }

    protected float[] getValues() {
        return values;
    }
}
