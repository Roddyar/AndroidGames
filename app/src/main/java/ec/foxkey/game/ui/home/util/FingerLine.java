package ec.foxkey.game.ui.home.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import ec.foxkey.game.R;
import ec.foxkey.game.ui.home.HomeFragment;

import java.util.ArrayList;

/**
 * View of the line the user draws with their finger.
 */
public class FingerLine extends View {
    private Paint mPaint;
    private Path mPath;
    private int[][] solutionPath;
    int solutionCellsVisited;
    private ArrayList<Boolean> solved;
    private boolean solvedMaze;

    public FingerLine(Context context) {
        this(context, null);
        init();
    }

    public FingerLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FingerLine(Context context, AttributeSet attrs, int[][] solutionPath) {
        super(context, attrs);
        this.solutionPath = solutionPath;
        init();
    }

    private void init() {
        // Create the paint brush
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10f);
        mPath = new Path();

        solutionCellsVisited = 0;

        solvedMaze = false;

        solved = new ArrayList<Boolean>();

        for (int i = 0; i < solutionPath.length; i++) {
            solved.add(false);
        }
        // Log.d("SOLVED_LENGTH", Integer.toString(solved.size()));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        // Schedule a repaint
        invalidate();
        // Check if user solved the maze
        boolean xInCell;
        boolean yInCell;

        for (int i = 0; i < solutionPath.length; i++) {

            xInCell = (event.getX() >= solutionPath[i][0] && event.getX() <= solutionPath[i][2]);
            yInCell = (event.getY() >= solutionPath[i][1] && event.getY() <= solutionPath[i][3]);

            if (xInCell && yInCell) {
                solved.set(i, true);

                if (!solved.contains(false) && !solvedMaze) {
                    HomeFragment.startGameSolvedAnimation();
                    Toast.makeText(this.getContext(), R.string.maze_solved, Toast.LENGTH_SHORT).show();
                    solvedMaze = true;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Bien Hecho, Juego terminado");
                    builder.setMessage("¿Deseas continuar jugando?");

                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HomeFragment.createMaze();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Juego terminado!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true; // not sure it this line is necessary
                }
            }
        }
        return true;
    }
}