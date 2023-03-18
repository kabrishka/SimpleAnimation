package com.kabrishka.simpleanimation;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.Locale;

public class Animation {
    private final TextView labelTextView;
    private final ConstraintLayout container;
    private CountDownTimer animStartTimer;
    private final boolean isRuLocale;

    public Animation(TextView labelTextView, ConstraintLayout container) {
        this.container = container;
        this.labelTextView = labelTextView;

        isRuLocale = Locale.getDefault().getISO3Language().equals("rus");
        initTextView();
    }

    private void initTextView() {
        labelTextView.setText(isRuLocale ? R.string.hello_ru : R.string.hello_en);
        labelTextView.setOnClickListener(view -> stopAnimation());
    }

    @SuppressLint("ResourceAsColor")
    private void setCurrentColor() {
        labelTextView.setText(R.string.hello_ru);
        labelTextView.setTextColor(isRuLocale ? R.color.ocean_boat_blue : R.color.vivid_crimson);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void changingTextViewPosition() {
        container.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                stopAnimation();

                float newPositionX = event.getX() - (float) labelTextView.getWidth() / 2;
                labelTextView.setX(newPositionX);
                float newPositionY = event.getY() - (float) labelTextView.getHeight() / 2;
                labelTextView.setY(newPositionY);

                float centreY = (float) container.getHeight() / 2;

                setCurrentColor();
                animStartTimer = new CountDownTimer(5000, 5000) {
                    @Override
                    public void onTick(long l) { }

                    @Override
                    public void onFinish() {
                        boolean isDown = centreY > newPositionY;
                        startAnimation(isDown);
                    }
                }.start();
            }
            return true;
        });
    }

    private void startAnimation(boolean isDown) {
        labelTextView.animate()
                .y(isDown ? container.getHeight() - labelTextView.getHeight() : 0)
                .setDuration(5000)
                .withEndAction(() -> startAnimation(!isDown))
                .start();
    }

    private void stopAnimation(){
        if (animStartTimer != null) {
            animStartTimer.cancel();
            labelTextView.animate().cancel();
        }
    }
}