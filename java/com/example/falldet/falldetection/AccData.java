package com.example.damera.falldetection;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by SJADHAV on 04/14/2017.
 */

public class AccData implements Serializable {


        private static final long serialVersionUID = 1L;
        private double smallest, largest;
        private long Difference;
        private Context context;


        private AccData(double min, double max, long timeDifference) {
            this.smallest = min;
            this.largest = max;
            this.Difference = timeDifference;
        }

        public AccData() {
            this.smallest = 3;
            this.largest = 35;
            this.Difference = 300;
        }

        public double getSmallest()
        {
            return smallest;
        }

        public double getLargest()
        {
            return largest;
        }

        public long getTimeDifference()
        {
            return Difference;
        }

    }


