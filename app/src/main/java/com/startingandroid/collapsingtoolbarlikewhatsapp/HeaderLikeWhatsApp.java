package com.startingandroid.collapsingtoolbarlikewhatsapp;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import static com.startingandroid.collapsingtoolbarlikewhatsapp.Utils.getToolbarHeight;

/**
 * Created by Zeeshan on 13/04/2016.
 */
public class HeaderLikeWhatsApp extends CoordinatorLayout.Behavior<HeaderView> {

    private final int mHeaderViewStartMarginLeft;
    private final int mHeaderViewEndMarginLeft;
    private final int mHeaderViewStartMarginBottom;
    private final int mHeaderViewEndMarginRight;
    private final int mHeaderViewStartTextSize;
    private final int mHeaderViewEndTextSize;
    private final int mToolbarHeight;

    private int mStartMarginLeft;
    private int mEndMarginLeft;
    private int mMarginRight;
    private int mStartMarginBottom;
    private float mTitleStartSize;
    private float mTitleEndSize;
    private boolean isHide;

    public HeaderLikeWhatsApp(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = context.getResources();
        this.mHeaderViewStartMarginLeft = resources.getDimensionPixelOffset(R.dimen.header_view_start_margin_left);
        this.mHeaderViewEndMarginLeft = resources.getDimensionPixelOffset(R.dimen.header_view_end_margin_left);
        this.mHeaderViewStartMarginBottom = resources.getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom);
        this.mHeaderViewEndMarginRight = resources.getDimensionPixelOffset(R.dimen.header_view_end_margin_right);
        this.mHeaderViewStartTextSize = resources.getDimensionPixelOffset(R.dimen.header_view_start_text_size);
        this.mHeaderViewEndTextSize = resources.getDimensionPixelOffset(R.dimen.header_view_end_text_size);

        this.mToolbarHeight = getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, HeaderView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, HeaderView child, View dependency) {
        shouldInitProperties();

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;
        float childPosition = dependency.getHeight()
                + dependency.getY()
                - child.getHeight()
                - (mToolbarHeight - child.getHeight()) * percentage / 2;

        childPosition = childPosition - mStartMarginBottom * (1f - percentage);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (Math.abs(dependency.getY()) >= maxScroll / 2) {
            float layoutPercentage = (Math.abs(dependency.getY()) - (maxScroll / 2)) / Math.abs(maxScroll / 2);
            lp.leftMargin = (int) (layoutPercentage * mEndMarginLeft) + mStartMarginLeft;
            child.setTextSize(getTranslationOffset(mTitleStartSize, mTitleEndSize, layoutPercentage));
        } else {
            lp.leftMargin = mStartMarginLeft;
        }
        lp.rightMargin = mMarginRight;
        child.setLayoutParams(lp);
        child.setY(childPosition);

        if (isHide && percentage < 1) {
            child.setVisibility(View.VISIBLE);
            isHide = false;
        } else if (!isHide && percentage == 1) {
            child.setVisibility(View.GONE);
            isHide = true;
        }
        return true;
    }

    protected float getTranslationOffset(float expandedOffset, float collapsedOffset, float ratio) {
        return expandedOffset + ratio * (collapsedOffset - expandedOffset);
    }

    private void shouldInitProperties() {
        if (mStartMarginLeft == 0) {
            mStartMarginLeft = mHeaderViewStartMarginLeft;
        }

        if (mEndMarginLeft == 0) {
            mEndMarginLeft = mHeaderViewEndMarginLeft;
        }

        if (mStartMarginBottom == 0) {
            mStartMarginBottom = mHeaderViewStartMarginBottom;
        }

        if (mMarginRight == 0) {
            mMarginRight = mHeaderViewEndMarginRight;
        }

        if (mTitleStartSize == 0) {
            mTitleStartSize = mHeaderViewStartTextSize;
        }

        if (mTitleEndSize == 0) {
            mTitleEndSize = mHeaderViewEndTextSize;
        }
    }

}