//package adapters;
//
//import android.support.v7.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.experia.experia.R;
//import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
//import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
//import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
//import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
//import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
//import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
//import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
//
//
//import models.AbstractDataProvider;
//import util.ViewUtils;
//
//
///**
// * Created by doc_dungeon on 8/20/16.
// */
//public class SwipeableWithButtonExampleAdapter
//        extends RecyclerView.Adapter<SwipeableWithButtonExampleAdapter.MyViewHolder>
//        implements SwipeableItemAdapter<SwipeableWithButtonExampleAdapter.MyViewHolder> {
//    private static final String TAG = "MySwipeableItemAdapter";
//
//    // NOTE: Make accessible with short name
//    private interface Swipeable extends SwipeableItemConstants {
//    }
//
//    private AbstractDataProvider mProvider;
//    private EventListener mEventListener;
//    private View.OnClickListener mSwipeableViewContainerOnClickListener;
//    private View.OnClickListener mUnderSwipeableViewButtonOnClickListener;
//
//    public interface EventListener {
//        void onItemPinned(int position);
//
//        void onItemViewClicked(View v);
//
//        void onUnderSwipeableViewButtonClicked(View v);
//    }
//
//    public static class MyViewHolder extends AbstractSwipeableItemViewHolder {
//        public RelativeLayout mContainer;
//        public TextView mTextView;
//        public Button mButton;
//
//        public MyViewHolder(View v) {
//            super(v);
//            mContainer = (RelativeLayout) v.findViewById(R.id.container);
//            mTextView = (TextView) v.findViewById(android.R.id.text1);
//            mButton = (Button) v.findViewById(android.R.id.button1);
//        }
//
//        @Override
//        public View getSwipeableContainerView() {
//            return mContainer;
//        }
//
//    }
//
//    public SwipeableWithButtonExampleAdapter(AbstractDataProvider dataProvider) {
//        mProvider = dataProvider;
//        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSwipeableViewContainerClick(v);
//            }
//        };
//        mUnderSwipeableViewButtonOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onUnderSwipeableViewButtonClick(v);
//            }
//        };
//
//        // SwipeableItemAdapter requires stable ID, and also
//        // have to implement the getItemId() method appropriately.
//        setHasStableIds(true);
//    }
//
//    private void onSwipeableViewContainerClick(View v) {
//        if (mEventListener != null) {
//            mEventListener.onItemViewClicked(
//                    RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
//        }
//    }
//
//    private void onUnderSwipeableViewButtonClick(View v) {
//        if (mEventListener != null) {
//            mEventListener.onUnderSwipeableViewButtonClicked(
//                    RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
//        }
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return mProvider.getItem(position).getId();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mProvider.getItem(position).getViewType();
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        final View v = inflater.inflate(R.layout.list_item_with_leave_behind_button, parent, false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        final AbstractDataProvider.Data item = mProvider.getItem(position);
//
//        // set listeners
//        // (if the item is *pinned*, click event comes to the mContainer)
//        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);
//        holder.mButton.setOnClickListener(mUnderSwipeableViewButtonOnClickListener);
//
//        // set text
//        holder.mTextView.setText(item.getText());
//
//        // set background resource (target view ID: container)
//        final int swipeState = holder.getSwipeStateFlags();
//
//        if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
//            int bgResId;
//
//            if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
//                bgResId = R.drawable.bg_item_swiping_active_state;
//            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
//                bgResId = R.drawable.bg_item_swiping_state;
//            } else {
//                bgResId = R.drawable.bg_item_normal_state;
//            }
//
//            holder.mContainer.setBackgroundResource(bgResId);
//        }
//
//        // set swiping properties
//        holder.setMaxLeftSwipeAmount(-0.5f);
//        holder.setMaxRightSwipeAmount(0);
//        holder.setSwipeItemHorizontalSlideAmount(item.isPinned() ? -0.5f : 0);
//
//        // Or, it can be specified in pixels instead of proportional value.
//        // float density = holder.itemView.getResources().getDisplayMetrics().density;
//        // float pinnedDistance = (density * 100); // 100 dp
//
//        // holder.setProportionalSwipeAmountModeEnabled(false);
//        // holder.setMaxLeftSwipeAmount(-pinnedDistance);
//        // holder.setMaxRightSwipeAmount(0);
//        // holder.setSwipeItemHorizontalSlideAmount(item.isPinned() ? -pinnedDistance: 0);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mProvider.getCount();
//    }
//
//    @Override
//    public int onGetSwipeReactionType(MyViewHolder holder, int position, int x, int y) {
//        if (ViewUtils.hitTest(holder.getSwipeableContainerView(), x, y)) {
//            return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
//        } else {
//            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
//        }
//    }
//
//    @Override
//    public void onSetSwipeBackground(MyViewHolder holder, int position, int type) {
//        int bgRes = 0;
//        switch (type) {
//            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
//                bgRes = R.drawable.bg_swipe_item_neutral;
//                break;
//            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
//                bgRes = R.drawable.bg_swipe_item_left;
//                break;
//            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
//                bgRes = R.drawable.bg_swipe_item_right;
//                break;
//        }
//
//        holder.itemView.setBackgroundResource(bgRes);
//    }
//
//    @Override
//    public SwipeResultAction onSwipeItem(MyViewHolder holder, int position, int result) {
//        Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");
//
//        switch (result) {
//            // swipe left --- pin
//            case Swipeable.RESULT_SWIPED_LEFT:
//                return new SwipeLeftResultAction(this, position);
//            // other --- do nothing
//            case Swipeable.RESULT_SWIPED_RIGHT:
//            case Swipeable.RESULT_CANCELED:
//            default:
//                if (position != RecyclerView.NO_POSITION) {
//                    return new UnpinResultAction(this, position);
//                } else {
//                    return null;
//                }
//        }
//    }
//
//    public EventListener getEventListener() {
//        return mEventListener;
//    }
//
//    public void setEventListener(EventListener eventListener) {
//        mEventListener = eventListener;
//    }
//
//    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
//        private SwipeableWithButtonExampleAdapter mAdapter;
//        private final int mPosition;
//        private boolean mSetPinned;
//
//        SwipeLeftResultAction(SwipeableWithButtonExampleAdapter adapter, int position) {
//            mAdapter = adapter;
//            mPosition = position;
//        }
//
//        @Override
//        protected void onPerformAction() {
//            super.onPerformAction();
//
//            AbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);
//
//            if (!item.isPinned()) {
//                item.setPinned(true);
//                mAdapter.notifyItemChanged(mPosition);
//                mSetPinned = true;
//            }
//        }
//
//        @Override
//        protected void onSlideAnimationEnd() {
//            super.onSlideAnimationEnd();
//
//            if (mSetPinned && mAdapter.mEventListener != null) {
//                mAdapter.mEventListener.onItemPinned(mPosition);
//            }
//        }
//
//        @Override
//        protected void onCleanUp() {
//            super.onCleanUp();
//            // clear the references
//            mAdapter = null;
//        }
//    }
//
//    private static class UnpinResultAction extends SwipeResultActionDefault {
//        private SwipeableWithButtonExampleAdapter mAdapter;
//        private final int mPosition;
//
//        UnpinResultAction(SwipeableWithButtonExampleAdapter adapter, int position) {
//            mAdapter = adapter;
//            mPosition = position;
//        }
//
//        @Override
//        protected void onPerformAction() {
//            super.onPerformAction();
//
//            AbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);
//            if (item.isPinned()) {
//                item.setPinned(false);
//                mAdapter.notifyItemChanged(mPosition);
//            }
//        }
//
//        @Override
//        protected void onCleanUp() {
//            super.onCleanUp();
//            // clear the references
//            mAdapter = null;
//        }
//    }
//}
