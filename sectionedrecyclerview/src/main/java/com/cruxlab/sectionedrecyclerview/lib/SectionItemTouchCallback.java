/*
 * MIT License
 *
 * Copyright (c) 2017 Cruxlab, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.cruxlab.sectionedrecyclerview.lib;

import android.graphics.Canvas;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

/**
 * This class lets you control swipe/drag and drop behavior of each item view within a section
 * receiving callbacks when user performs touch actions.
 * <p>
 * To control which actions user can take on each section's item view, you should override
 * {@link #getSwipeDirFlags(RecyclerView, ItemViewHolder)}
 * and {@link #getDragDirFlags(RecyclerView, ItemViewHolder)}
 * returning an appropriate set of direction flags combining ({@link ItemTouchHelper.Callback#LEFT},
 * {@link ItemTouchHelper.Callback#RIGHT}, {@link ItemTouchHelper.Callback#START}
 * and {@link ItemTouchHelper.Callback#END} or 0 if no movement is allowed.
 * <p>
 * When a View is swiped, ItemTouchHelper animates it until it goes out of bounds, then calls
 * {@link #onSwiped(ItemViewHolder, int)}. At this point, you should update your
 * adapter (e.g. remove the item) and call related SectionAdapter#notify event.
 * <p>
 * If user drags an item, ItemTouchHelper will call
 * {@link #onMove(RecyclerView, ItemViewHolder, ItemViewHolder)}}. Upon receiving this callback, you
 * should move the item from the old position in section to new position in your SectionAdapter and
 * also call {@link BaseSectionAdapter#notifyItemMoved(int, int)}.
 * To control where a View can be dropped, you can override
 * {@link #canDropOver(RecyclerView, ViewHolder, ViewHolder)}.
 * <p>
 * When a dragging View overlaps multiple other views, default implementation chooses the closest
 * View with which dragged View might have changed positions. You can customize this behavior
 * implementing {@link GeneralTouchCallback#chooseDropTarget(ViewHolder, List, int, int)} and
 * setting {@link GeneralTouchCallback} instance via
 * {@link SectionDataManager#setGeneralTouchCallback(GeneralTouchCallback)}.
 * Note, that new behavior will be applied to all sections.
 * <p>
 * Similar to {@link ItemTouchHelper.Callback} and uses its default implementations using
 * {@link #defaultCallback}.
 */
public abstract class SectionItemTouchCallback extends PartialTouchCallback {

    /**
     * Returns set of swipe direction flags for each item view combining
     * ({@link ItemTouchHelper.Callback#LEFT}, {@link ItemTouchHelper.Callback#RIGHT},
     * {@link ItemTouchHelper.Callback#START} and {@link ItemTouchHelper.Callback#END}
     * or 0 if no movement is allowed.
     *
     * @param recyclerView The RecyclerView to which the ItemTouchHelper is attached to.
     * @param viewHolder   The ViewHolder for which the swipe direction is required.
     * @return A binary OR of swipe direction flags.
     */
    public abstract int getSwipeDirFlags(RecyclerView recyclerView,
                                         ItemViewHolder viewHolder);

    /**
     * Returns set of drag direction flags for each item view combining
     * ({@link ItemTouchHelper.Callback#LEFT}, {@link ItemTouchHelper.Callback#RIGHT},
     * {@link ItemTouchHelper.Callback#START} and {@link ItemTouchHelper.Callback#END}
     * or 0 if no movement is allowed.
     *
     * @param recyclerView The RecyclerView to which the ItemTouchHelper is attached to.
     * @param viewHolder   The ViewHolder for which the drag direction is required.
     * @return A binary OR of drag direction flags.
     */
    public abstract int getDragDirFlags(RecyclerView recyclerView,
                                        ItemViewHolder viewHolder);

    /**
     * Called when a ViewHolder is swiped by the user.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#onSwiped(RecyclerView.ViewHolder, int)}.
     *
     * @param viewHolder The ViewHolder which has been swiped by the user.
     * @param direction  The direction to which the ViewHolder is swiped.
     */
    public abstract void onSwiped(ItemViewHolder viewHolder, int direction);

    /**
     * Called by ItemTouchHelper on RecyclerView's onDraw callback.
     * <p>
     * Default implementation translates the child by the given <code>dX</code>,
     * <code>dY</code>, but you can customize how your View's respond to user interactions.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#onChildDraw(Canvas, RecyclerView,
     * RecyclerView.ViewHolder, float, float, int, boolean)}.
     *
     * @param c                 The canvas which RecyclerView is drawing its children.
     * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder        The ViewHolder which is being interacted by the User or it was
     *                          interacted and simply animating to its original position.
     * @param dX                The amount of horizontal displacement caused by user's action.
     * @param dY                The amount of vertical displacement caused by user's action.
     * @param actionState       The type of interaction on the View.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or
     *                          false it is simply animating back to its original state.
     */
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            ItemViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView,
                dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * Called by ItemTouchHelper on RecyclerView's onDraw callback.
     * <p>
     * Default implementation translates the child by the given <code>dX</code>,
     * <code>dY</code>, but you can customize how your View's respond to user interactions.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#onChildDrawOver(Canvas, RecyclerView,
     * RecyclerView.ViewHolder, float, float, int, boolean)}.
     *
     * @param c                 The canvas which RecyclerView is drawing its children.
     * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder        The ViewHolder which is being interacted by the User or it was
     *                          interacted and simply animating to its original position.
     * @param dX                The amount of horizontal displacement caused by user's action.
     * @param dY                The amount of vertical displacement caused by user's action.
     * @param actionState       The type of interaction on the View.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or
     *                          false it is simply animating back to its original state.
     */
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                ItemViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, viewHolder.itemView,
                dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * Called when the ViewHolder swiped or dragged by the ItemTouchHelper is changed.
     * <p>
     * If you override this method, you should call super.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#onSelectedChanged(RecyclerView.ViewHolder, int)}.
     *
     * @param viewHolder  The new ViewHolder that is being swiped. Might be null if it is cleared.
     * @param actionState One of {@link ItemTouchHelper#ACTION_STATE_IDLE},
     *                    {@link ItemTouchHelper#ACTION_STATE_SWIPE} or
     *                    {@link ItemTouchHelper#ACTION_STATE_DRAG}.
     */
    @CallSuper
    public void onSelectedChanged(ItemViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(viewHolder.itemView);
        }
    }

    /**
     * Called by the ItemTouchHelper when the user interaction with an element is over and it
     * also completed its animation.
     * <p>
     * Here you should clear all changes previously done on the View.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#clearView(RecyclerView, RecyclerView.ViewHolder)}.
     *
     * @param recyclerView The RecyclerView which is controlled by the ItemTouchHelper.
     * @param viewHolder   The View that was interacted by the user.
     */
    public void clearView(RecyclerView recyclerView, ItemViewHolder viewHolder) {
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(viewHolder.itemView);
    }

    /**
     * Returns whether ItemTouchHelper should start a swipe operation if a pointer is swiped
     * over the View.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#isItemViewSwipeEnabled()}.
     *
     * @return True if ItemTouchHelper should start swiping an item when user swipes a pointer
     * over the View, false otherwise. Default value is <code>true</code>.
     */
    public boolean isSwipeEnabled() {
        checkDefaultCallback();
        return defaultCallback.isSwipeEnabledByDefault();
    }

    /**
     * Returns the fraction that the user should move the View to be considered as swiped.
     * The fraction is calculated with respect to RecyclerView's bounds.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#getSwipeThreshold(RecyclerView.ViewHolder)}.
     *
     * @param viewHolder The ViewHolder that is being dragged.
     * @return A float value that denotes the fraction of the View size. Default value is .5f .
     */
    public float getSwipeThreshold(ItemViewHolder viewHolder) {
        checkDefaultCallback();
        return defaultCallback.getDefaultSwipeThreshold(viewHolder);
    }

    /**
     * Returns whether ItemTouchHelper should start a drag and drop operation if an item is
     * long pressed.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#isLongPressDragEnabled()}.
     *
     * @return True if ItemTouchHelper should start dragging an item when it is long pressed,
     * false otherwise. Default value is <code>true</code>.
     */
    public boolean isLongPressDragEnabled() {
        checkDefaultCallback();
        return defaultCallback.isLongPressDragEnabledByDefault();
    }

    /**
     * Returns the fraction that the user should move the View to be considered as it is
     * dragged. After a view is moved this amount, ItemTouchHelper starts checking for Views
     * below it for a possible drop.
     * <p>
     * Similar {@link ItemTouchHelper.Callback#getMoveThreshold(RecyclerView.ViewHolder)}.
     *
     * @param viewHolder The ViewHolder that is being dragged.
     * @return A float value that denotes the fraction of the View size. Default value is .5f .
     */
    public float getMoveThreshold(ItemViewHolder viewHolder) {
        checkDefaultCallback();
        return defaultCallback.getDefaultMoveThreshold(viewHolder);
    }

    /**
     * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#canDropOver(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)}.
     *
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param current      The ViewHolder that user is dragging.
     * @param target       The ViewHolder which is below the dragged ViewHolder.
     * @return True if the dragged ViewHolder can be replaced with the target ViewHolder, false
     * otherwise. Default value is <code>true</code>.
     */
    public boolean canDropOver(RecyclerView recyclerView, ViewHolder current, ViewHolder target) {
        checkDefaultCallback();
        return defaultCallback.canDropOverByDefault(recyclerView, current, target);
    }

    /**
     * Called when ItemTouchHelper wants to move the dragged item from its old position to
     * the new position.
     * <p>
     * Similar to {@link ItemTouchHelper.Callback#onMove(RecyclerView, RecyclerView.ViewHolder, RecyclerView.ViewHolder)}.
     *
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder   The ViewHolder which is being dragged by the user.
     * @param target       The ViewHolder over which the currently active item is being
     *                     dragged.
     * @return True if the {@code viewHolder} has been moved to the adapter position of {@code target}.
     */
    public abstract boolean onMove(RecyclerView recyclerView,
                                   ItemViewHolder viewHolder, ItemViewHolder target);

    /**
     * Called when {@link #onMove(RecyclerView, ItemViewHolder, ItemViewHolder)} returns true.
     * <p>
     * Default implementation is {@link ItemTouchHelper.Callback#onMoved(RecyclerView, RecyclerView.ViewHolder, int, RecyclerView.ViewHolder, int, int, int)}.
     *
     * @param recyclerView The RecyclerView controlled by the ItemTouchHelper.
     * @param viewHolder   The ViewHolder under user's control.
     * @param fromPos      The previous position in section of the dragged item (before it was
     *                     moved).
     * @param target       The ViewHolder on which the currently active item has been dropped.
     * @param toPos        The new position in section of the dragged item.
     * @param x            The updated left value of the dragged View after drag translations
     *                     are applied. This value does not include margins added by
     *                     {@link RecyclerView.ItemDecoration}s.
     * @param y            The updated top value of the dragged View after drag translations
     *                     are applied. This value does not include margins added by
     *                     {@link RecyclerView.ItemDecoration}s.
     */
    public void onMoved(RecyclerView recyclerView, ItemViewHolder viewHolder, int fromPos, ItemViewHolder target, int toPos, int x, int y) {
        checkDefaultCallback();
        defaultCallback.onMovedByDefault(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

}
