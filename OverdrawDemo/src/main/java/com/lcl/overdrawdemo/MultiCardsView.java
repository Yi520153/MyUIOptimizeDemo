package com.lcl.overdrawdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * TODO
 * lcl
 * 2018/10/8
 */
public class MultiCardsView extends View {

    private ArrayList<SingleCard> cardsList = new ArrayList<SingleCard>(5);

    private boolean enableOverdrawOpt = true;

    public MultiCardsView(Context context) {
        this(context, null, 0);
    }

    public MultiCardsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiCardsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addCards(SingleCard card) {
        cardsList.add(card);
    }

    //设置是否消除过度绘制
    public void setEnableOverdrawOpt(boolean enableOrNot) {
        this.enableOverdrawOpt = enableOrNot;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (cardsList == null || canvas == null) {
            return;
        }

        Rect clip = canvas.getClipBounds();

        Log.d("draw", String.format("clip bounds %d %d %d %d", clip.left, clip.top, clip.right, clip.bottom));

        if (enableOverdrawOpt) {
            drawCardsWithoutOverDraw(canvas,cardsList.size()-1);
        } else {
            drawCardsNormal(canvas, cardsList.size() - 1);
        }
    }

    protected void drawCardsWithoutOverDraw(Canvas canvas, int index) {
        if (canvas == null || index < 0 || index >= cardsList.size()) {
            return;
        }

        SingleCard card = cardsList.get(index);

        //判断是否没和某个卡片相交，从而跳过那些非矩形区域内的绘制操作
        if (card != null && !canvas.quickReject(card.area, Canvas.EdgeType.BW)) {
            int saveCount = canvas.save();
            //只绘制可见区域
            if (canvas.clipRect(card.area, Region.Op.DIFFERENCE)) {
                drawCardsWithoutOverDraw(canvas, index - 1);
            }
            canvas.restoreToCount(saveCount);
            saveCount = canvas.save();
            //只绘制可见区域
            if (canvas.clipRect(card.area)) {
                Rect clip = canvas.getClipBounds();
                card.draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        } else {
            drawCardsWithoutOverDraw(canvas, index - 1);
        }

    }

    protected void drawCardsNormal(Canvas canvas, int index) {
        if (canvas == null || index < 0 || index >= cardsList.size()) {
            return;
        }
        SingleCard card = cardsList.get(index);
        if (card != null) {
            drawCardsNormal(canvas, index - 1);
            card.draw(canvas);
        }
    }
}
