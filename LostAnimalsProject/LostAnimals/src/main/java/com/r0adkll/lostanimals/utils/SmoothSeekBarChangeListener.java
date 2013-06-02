package com.r0adkll.lostanimals.utils;

import android.widget.SeekBar;


/**
 * This is an implementation of the seekbar that smooths out progress sliding
 * on seekbar tracking changes
 * 
 * @author drew.heavner
 *
 */
public class SmoothSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
	private int smoothnessFactor = 10;

    public SmoothSeekBarChangeListener(){}
    public SmoothSeekBarChangeListener(int smoothFactor){
        smoothnessFactor = smoothFactor;
    }
	
	/**
	 * Unused
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		seekBar.setProgress(Math.round((seekBar.getProgress() + (smoothnessFactor / 2)) / smoothnessFactor) * smoothnessFactor);
	}
}
