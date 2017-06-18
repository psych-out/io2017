package io.psych.io2017.dynamicanimation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.psych.io2017.R;
import io.psych.io2017.psych.PuckImageIterator;

/**
 * Activity that demonstrates chained icons moving around the screen chasing a "destination"
 * position.  Icons can be added at runtime by using the associated button on the screen.  The
 * speed at which the destination location changes can also be sped up or slowed down.
 *
 * This particular implementation uses Facebook's Rebound library.
 */
public class ReboundActivity extends Activity {

    private static final String TAG = "ReboundActivity";

    private SpringSystem mSpringSystem;
    private ViewGroup mAnimationContainer;
    private View mDestinationView;
    private final List<SpringIcon> mChainedSpringIcons = new ArrayList<>();
    private Handler mMoveDestinationHandler;
    private int mTimeUntilNextMoveInMillis = 500;
    private int mPuckSize;
    private PuckImageIterator mPuckImageIterator = new PuckImageIterator();

    private final Runnable mMoveDestination = new Runnable() {
        @Override
        public void run() {
            moveDestination();
            mMoveDestinationHandler.postDelayed(this, mTimeUntilNextMoveInMillis);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_animation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mPuckSize = getResources().getDimensionPixelSize(R.dimen.puck_size);

        mAnimationContainer = (ViewGroup) findViewById(R.id.animation_container);

        createDestinationView();

        mSpringSystem = SpringSystem.create();

        findViewById(R.id.button_add_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIcon();
            }
        });

        findViewById(R.id.button_faster).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveFaster();
            }
        });

        findViewById(R.id.button_slower).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveSlower();
            }
        });

        mMoveDestinationHandler = new Handler();
        mMoveDestinationHandler.post(mMoveDestination);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void createDestinationView() {
        ImageView destinationView = new ImageView(this);
        destinationView.setImageResource(R.drawable.puck_pineapple);
        mAnimationContainer.addView(destinationView, new ViewGroup.LayoutParams(mPuckSize, mPuckSize));
        mDestinationView = destinationView;
    }

    private void addIcon() {
        final SpringIcon springIcon = createSpringIcon();
        mChainedSpringIcons.add(springIcon);

        boolean isFirstIcon = mChainedSpringIcons.size() == 1;
        if (null != mDestinationView && isFirstIcon) {
            connectSpringIconToDestination();
        } else if (!isFirstIcon) {
            SpringIcon predecessor = mChainedSpringIcons.get(mChainedSpringIcons.size() - 2);
            predecessor.setChainedSpringIcon(springIcon);
        }
    }

    private SpringIcon createSpringIcon() {
        View view = createIcon();
        Spring springX = mSpringSystem.createSpring().setCurrentValue(view.getX());
        Spring springY = mSpringSystem.createSpring().setCurrentValue(view.getY());
        return new SpringIcon(view, springX, springY);
    }

    private View createIcon() {
        ImageView view = new ImageView(this);
        mAnimationContainer.addView(view, new ViewGroup.LayoutParams(mPuckSize, mPuckSize));
        view.setX(mAnimationContainer.getWidth() / 2);
        view.setY(mAnimationContainer.getHeight() / 2);
        view.setImageResource(mPuckImageIterator.next());
        return view;
    }

    private void moveFaster() {
        mTimeUntilNextMoveInMillis -= 500;
        if (mTimeUntilNextMoveInMillis < 500) {
            mTimeUntilNextMoveInMillis = 500;
        }
    }

    private void moveSlower() {
        mTimeUntilNextMoveInMillis += 500;
    }

    private void moveDestination() {
        float x = (float) (Math.random() * mAnimationContainer.getWidth());
        float y = (float) (Math.random() * mAnimationContainer.getHeight());
        mDestinationView.setX(x);
        mDestinationView.setY(y);

        if (mChainedSpringIcons.size() > 0) {
            connectSpringIconToDestination();
        }
    }

    private void connectSpringIconToDestination() {
        float x = mDestinationView.getX();
        float y = mDestinationView.getY();

        SpringIcon rootSpringIcon = mChainedSpringIcons.get(0);
        rootSpringIcon.updateAnchor(x, y);
    }

    private static class SpringIcon {
        private  View mSpringIconView;
        private Spring mSpringX;
        private Spring mSpringY;
        private SpringIcon mChainedSpringIcon;

        public SpringIcon(final View springIconView, Spring springX, Spring springY) {
            this.mSpringIconView = springIconView;
            this.mSpringX = springX;
            this.mSpringX.addListener(new SpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    springIconView.setX((float) spring.getCurrentValue());

                    if (null != mChainedSpringIcon) {
                        mChainedSpringIcon.updateAnchor(springIconView.getX(), springIconView.getY());
                    }
                }

                @Override
                public void onSpringAtRest(Spring spring) { }

                @Override
                public void onSpringActivate(Spring spring) { }

                @Override
                public void onSpringEndStateChange(Spring spring) { }
            });

            this.mSpringY = springY;
            this.mSpringY.addListener(new SpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    springIconView.setY((float) spring.getCurrentValue());

                    if (null != mChainedSpringIcon) {
                        mChainedSpringIcon.updateAnchor(springIconView.getX(), springIconView.getY());
                    }
                }

                @Override
                public void onSpringAtRest(Spring spring) { }

                @Override
                public void onSpringActivate(Spring spring) { }

                @Override
                public void onSpringEndStateChange(Spring spring) { }
            });
        }

        public void updateAnchor(float x, float y) {
            mSpringX.setEndValue(x);
            mSpringY.setEndValue(y);
        }

        public void setChainedSpringIcon(@Nullable SpringIcon chainedSpringIcon) {
            this.mChainedSpringIcon = chainedSpringIcon;
        }
    }
}
