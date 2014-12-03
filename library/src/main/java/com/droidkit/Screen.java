package com.droidkit;

import android.app.Application;

/**
 * Created by Jesus Christ. Amen.
 */
public class Screen {
        private static float density;
        private static float scaledDensity;

        public static int dp(float dp) {
            if (density == 0f)
                density = AppContext.getContext().getResources().getDisplayMetrics().density;

            return (int) (dp * density + .5f);
        }

        public static int sp(float sp) {
            if (scaledDensity == 0f)
                scaledDensity = AppContext.getContext().getResources().getDisplayMetrics().scaledDensity;

            return (int) (sp * scaledDensity + .5f);
        }

        public static int getWidth() {
            return AppContext.getContext().getResources().getDisplayMetrics().widthPixels;
        }

        public static int getHeight() {
            return AppContext.getContext().getResources().getDisplayMetrics().heightPixels;
        }

}
