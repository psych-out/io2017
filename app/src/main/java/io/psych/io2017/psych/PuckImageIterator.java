package io.psych.io2017.psych;

import java.util.Iterator;

import io.psych.io2017.R;

public class PuckImageIterator implements Iterator<Integer> {

    private static final int[] PUCKS = new int[] {
            R.drawable.puck_shawn,
            R.drawable.puck_gus,
            R.drawable.puck_juliet,
            R.drawable.puck_lassiter,
            R.drawable.puck_henry,
            R.drawable.puck_vick
    };

    private int mIndex = 0;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        int rid = PUCKS[mIndex];
        mIndex = mIndex < (PUCKS.length - 1) ? mIndex + 1 : 0;
        return rid;
    }
}
