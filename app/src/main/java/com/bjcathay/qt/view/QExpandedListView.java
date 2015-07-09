/**
 *
 */

package com.bjcathay.qt.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.CityAdapter;

/**
 * @author dengt
 */
public class QExpandedListView extends ExpandableListView implements
        View.OnClickListener, AbsListView.OnScrollListener {
    private FrameLayout parentLayout = null; // listview所在的父层对象
    private boolean isFixGroup = true; // 是否要固定显示组名在上边
    private View FixGroupView = null; // 始终固定显示的组view
    private ExpandableListAdapter mAdapter;
    private OnScrollListener mOnScrollListener = null;
    private int indicatorGroupId = -1; // 当前固定显示的组索引
    private int indicatorGroupHeight = 0;
    private LinearLayout indicatorGroup;
    private Context mContext;

    // private View header;
    // private LayoutInflater inflater;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public QExpandedListView(Context context) {
        super(context);
        mContext = context;
        // initView(context);
        this.isFixGroup = true;
        super.setOnScrollListener(this);
    }

    public QExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // initView(context);

        this.isFixGroup = true;
        super.setOnScrollListener(this);
    }

    public QExpandedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        // initView(context);

        this.isFixGroup = true;
        super.setOnScrollListener(this);
    }

    /*
     * private void initView(Context context) { inflater =
     * LayoutInflater.from(context); header =
     * inflater.inflate(R.layout.expanded_head_view, null);
     * this.addHeaderView(header); }
     */

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        if (isFixGroup) {// 是否显示固定组头
            boolean isFrameLayoutParent = getParent() instanceof FrameLayout;
            mAdapter = this.getExpandableListAdapter();
            if (isFrameLayoutParent) {// 当前listview是放到FrameLayout的容器中
                parentLayout = (FrameLayout) getParent();
                QExpandedListView listView = (QExpandedListView) view; // 得到当前listview
                if (null == indicatorGroup) {
                    indicatorGroup = new LinearLayout(mContext);
                    indicatorGroup.setLayoutParams(new MarginLayoutParams(ViewGroup.
                            LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }

                int npos = view.pointToPosition(0, 0);
                if (npos != AdapterView.INVALID_POSITION) {
                    long pos = listView.getExpandableListPosition(npos);
                    int childPos = ExpandableListView.getPackedPositionChild(pos);
                    final int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                    if (childPos == AdapterView.INVALID_POSITION) {
                        View groupView = listView.getChildAt(npos
                                - listView.getFirstVisiblePosition());
                        indicatorGroupHeight = groupView.getHeight();
                    }
                    if (indicatorGroupHeight == 0) {
                        return;
                    }
                    if (groupPos != indicatorGroupId) {
                        if (mAdapter != null) {
                            FixGroupView = mAdapter.getGroupView(groupPos, false, null,
                                    indicatorGroup);
                            FixGroupView.setLayoutParams(new LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            final ImageView iv = (ImageView) FixGroupView
                                    .findViewById(R.id.expand_right_img);
                            if (isGroupExpanded(groupPos)) {
                                iv.setImageResource(R.drawable.ic_city_select);
                            } else {
                                iv.setImageResource(R.drawable.ic_into_list);
                            }
                            FixGroupView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isGroupExpanded(groupPos)) {
                                        collapseGroup(groupPos);
                                        iv.setImageResource(R.drawable.ic_city_select);
                                    } else {
                                        expandGroup(groupPos);
                                        iv.setImageResource(R.drawable.ic_into_list);
                                    }
                                }
                            });
                            /*
                             * FixGroupView.setOnLongClickListener(new
                             * OnLongClickListener() {
                             * @Override public boolean onLongClick(View v) { //
                             * TODO Auto-generated method stub return false; }
                             * });
                             */
                            indicatorGroupId = groupPos;
                            // ((CityAdapter)mAdapter).hideGroup(indicatorGroupId);
                            // ((CityAdapter)mAdapter).notifyDataSetChanged();
                        }
                    }
                }
                if (indicatorGroupId == -1) {
                    return;
                }
                int showHeight = indicatorGroupHeight;
                int nEndPos = listView.pointToPosition(0, indicatorGroupHeight);
                if (nEndPos != AdapterView.INVALID_POSITION) {
                    long pos = listView.getExpandableListPosition(nEndPos);
                    int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                    if (groupPos != indicatorGroupId) {
                        View viewNext = listView.getChildAt(nEndPos - listView.
                                getFirstVisiblePosition());
                        showHeight = viewNext.getTop();
                    }
                }
                if (FixGroupView.getParent() == null) {
                    indicatorGroup.removeAllViews();
                    indicatorGroup.addView(FixGroupView);
                }
                ViewGroup.LayoutParams vlparams = indicatorGroup.getLayoutParams();
                if ((null != vlparams) && (vlparams instanceof MarginLayoutParams)) {
                    MarginLayoutParams layoutParams = (MarginLayoutParams) vlparams;
                    layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
                    indicatorGroup.setLayoutParams(layoutParams);
                    indicatorGroup.requestLayout();
                }

                if (indicatorGroup.getParent() == null) {
                    parentLayout.addView(indicatorGroup);
                }
            }
        }
        if (this.mOnScrollListener != null)
            this.mOnScrollListener.onScroll(view,
                    firstVisibleItem, visibleItemCount, totalItemCount);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mOnScrollListener != null)
            this.mOnScrollListener.onScrollStateChanged(view, scrollState);
    }

    @Override
    public void setOnScrollListener(OnScrollListener paramOnScrollListener) {
        this.mOnScrollListener = paramOnScrollListener;
    }

    @Override
    public void setAdapter(ExpandableListAdapter paramExpandableListAdapter) {
        super.setAdapter(paramExpandableListAdapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
