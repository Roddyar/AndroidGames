package ec.foxkey.game.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import ec.foxkey.game.R;
import ec.foxkey.game.ui.home.util.FingerLine;
import ec.foxkey.game.ui.home.util.MazeView;

public class HomeFragment extends Fragment {

    public enum Color {
        WHITE, GRAY, BLACK;
    }

    private static MazeView mMazeView;
    private static FingerLine mFingerLine;
    static ImageView house;
    private static ImageView arrow;
    private static int mazeSize;
    private static DisplayMetrics displaymetrics = new DisplayMetrics();
    private static FrameLayout mFrameLayout;
    private static final int PADDING = 64;
    private static final int FAT_FINGERS_MARGIN = 25;
    private static int level = 6;
    Switch sw1, sw2;
    private static Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        HomeFragment.context = getContext();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        arrow = (ImageView) root.findViewById(R.id.arrow);
        house = (ImageView) root.findViewById(R.id.house);

        mFrameLayout = (FrameLayout) root.findViewById(R.id.mazeWrapper);
        ViewGroup.LayoutParams params = mFrameLayout.getLayoutParams();
        params.height = (int) Math.floor(displaymetrics.heightPixels * 0.7);
        mFrameLayout.setLayoutParams(params);

        FloatingActionButton newMazeButton = (FloatingActionButton) root.findViewById(R.id.newMazeButton);
        newMazeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createMaze();
            }
        });
        newMazeButton.performClick();

        sw1 = (Switch) root.findViewById(R.id.sw_fclab);
        sw2 = (Switch) root.findViewById(R.id.sw_dflab);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw2.setChecked(false);
                    level = 6;
                    createMaze();
                } else {
                    sw2.setChecked(true);
                    level = 10;
                    createMaze();
                }
            }
        });
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw1.setChecked(false);
                    level = 10;
                    createMaze();
                } else {
                    sw1.setChecked(true);
                    level = 6;
                    createMaze();
                }
            }
        });

        return root;
    }

    public static void createMaze() {
        // First remove any existing MazeView and FingerLine
        if (mMazeView != null) {
            ((ViewGroup) mMazeView.getParent()).removeView(mMazeView);
        }
        if (mFingerLine != null) {
            ((ViewGroup) mFingerLine.getParent()).removeView(mFingerLine);
        }
        mazeSize = level;
        mMazeView = new MazeView(getAppContext(), mazeSize);

        // Trace the path from start to farthestVertex using the line of predecessors,
        // apply this information to form an array of rectangles
        // which will be passed on to fingerLine view
        // where the line has to pass.
        // The array be checked against the drawn line in FingerLine.

        int[][] solutionAreas = new int[mMazeView.lengthOfSolutionPath][4];

        int currentVertexKey;
        int totalMazeWidth = displaymetrics.widthPixels - PADDING;
        // int totalMazeHeight = totalMazeWidth;
        int cellSide = totalMazeWidth / mazeSize;
        int row, column;
        int topLeftX, topLeftY, bottomRightX, bottomRightY;

        for (int i = 0; i < mMazeView.lengthOfSolutionPath; i++) {

            currentVertexKey = mMazeView.listOfSolutionVertecesKeys[i];

            // Translate vertex key to location on screen
            row = (currentVertexKey) / mazeSize;
            column = (currentVertexKey) % mazeSize;
            topLeftX = (PADDING / 2) + (column * cellSide) - FAT_FINGERS_MARGIN;
            topLeftY = (PADDING / 2) + (row * cellSide) - FAT_FINGERS_MARGIN;

            bottomRightX = (PADDING / 2) + ((column + 1) * cellSide) + FAT_FINGERS_MARGIN;
            bottomRightY = (PADDING / 2) + ((row + 1) * cellSide) + FAT_FINGERS_MARGIN;
            solutionAreas[i] = new int[]{topLeftX, topLeftY, bottomRightX, bottomRightY};
        }

        mFrameLayout.addView(mMazeView);
        mFingerLine = new FingerLine(getAppContext(), null, solutionAreas);
        mFrameLayout.addView(mFingerLine);

        // Add start arrow pic
        int startCellArrowX = solutionAreas[mMazeView.lengthOfSolutionPath - 1][0] + 12 + FAT_FINGERS_MARGIN;
        int startCellArrowY = solutionAreas[mMazeView.lengthOfSolutionPath - 1][1] + 100 + FAT_FINGERS_MARGIN;

        arrow.setX(startCellArrowX);
        arrow.setY(startCellArrowY);
        arrow.setVisibility(View.VISIBLE);

        // Add house pic
        int endCellHouseX = solutionAreas[0][0] + 8 + FAT_FINGERS_MARGIN;
        int endCellHouseY = solutionAreas[0][1] + 10 + FAT_FINGERS_MARGIN;

        house.setX(endCellHouseX);
        house.setY(endCellHouseY);
        house.setVisibility(View.VISIBLE);
    }


    // Function to remove a specific Integer from Integer array
    // and return a new Integer array without the specified value
    public static Integer[] removeValueFromArray(Integer[] array, Integer value) {

        ArrayList<Integer> arrayList = new ArrayList<Integer>(Arrays.asList(array));
        arrayList.remove(arrayList.indexOf(value));

        Integer[] newArray = new Integer[arrayList.size()];
        arrayList.toArray(newArray);

        return newArray;
    }

    public static void startGameSolvedAnimation() {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(700);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        house.startAnimation(animation);
    }

    public static Context getAppContext() {
        return HomeFragment.context;
    }

}
