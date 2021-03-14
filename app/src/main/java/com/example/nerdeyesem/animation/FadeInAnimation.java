package com.example.nerdeyesem.animation;


import android.view.View;

public final class FadeInAnimation {

    private FadeInAnimation() {}

    public static void startAnimation(View view, int duration) {
        // Set the login field to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        view.animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null);
    }
}
