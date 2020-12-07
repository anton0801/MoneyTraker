package priler.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DiagramView extends View {

    private int income;
    private int expense;

    private Paint incomePaint = new Paint();
    private Paint expensePaint = new Paint();

    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        incomePaint.setColor(getResources().getColor(R.color.balance_income_color));
        expensePaint.setColor(getResources().getColor(R.color.balance_expense_color));

        if (isInEditMode()) {
            // проверка что мы смотрим нашу view из редактора layout
            income = 19000;
            expense = 4500;
        }
    }

    public void update(int income, int expense) {
        this.income = income;
        this.expense = expense;
        // requestLayout(); // вызовет все итераций
        invalidate(); // перерисовка нашей view
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        // обработка размеров view
//        // параметры нам говорят киким хочет видеть нашу view родительский layout
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthValue = MeasureSpec.getSize(widthMeasureSpec);
//
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightValue = MeasureSpec.getSize(heightMeasureSpec);
//
//        setMeasuredDimension(300, 300);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawPieDiagram(canvas);
        } else {
            drawRectDiagram(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawPieDiagram(Canvas canvas) {
        if (income + expense == 0)
            return;

        long max = Math.max(expense, income);
        float expenseAngle = 360.0f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);

        int w = getWidth() / 4;

        int space = 10;
        int size = Math.min(getWidth(), getHeight() - space * 2);
        final int xMargin = (getWidth() - size) / 2,
                yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenseAngle / 2, expenseAngle,
                true, expensePaint);

        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomeAngle / 2, incomeAngle,
                true, incomePaint);
    }

    private void drawRectDiagram(Canvas canvas) {
        if (income + expense == 0)
            return;

        long max = Math.max(expense, income); // выщитываем чего больше, в зависимости от этого мы будем выстраивать высоту дохода, или траты
        long expensesHeight = getHeight() * expense / max;
        long incomeHeight = getHeight() * income / max;

        int w = getWidth() / 4;

        canvas.drawRect(w / 2, getHeight() - expensesHeight, w * 3 / 2, getHeight(), expensePaint);
        canvas.drawRect(5 * w / 2, getHeight() - incomeHeight, w * 7 / 2, getHeight(), incomePaint);
    }

}
