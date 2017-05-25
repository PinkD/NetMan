package moe.pinkd.netman.ui.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import moe.pinkd.netman.R;
import moe.pinkd.netman.config.Config;
import moe.pinkd.netman.util.SharedPreferenceUtil;
import moe.pinkd.netman.util.ShellUtil;

public class ReadMeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ReadMeActivity";

    private FrameLayout[] readmePages = new FrameLayout[3];
    private int[] layoutIds = {R.id.readme_1, R.id.readme_2, R.id.readme_3};
    private TextView[] tips = new TextView[3];
    private int[] tipsText = {R.string.tip_1, R.string.tip_2, R.string.tip_3};
    private Button[] buttons = new Button[3];
    private int[] buttonsText = {R.string.get_it, R.string.test_root, R.string.get_cellular};
    private ViewPropertyAnimator[] viewPropertyAnimators = new ViewPropertyAnimator[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferenceUtil.loadCellularInterfaceName(this)) {
            Log.d(TAG, "onAnimationEnd: startActivity");
            startActivity(new Intent(ReadMeActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_readme);
        for (int i = 0; i < readmePages.length; i++) {
            readmePages[i] = (FrameLayout) findViewById(layoutIds[i]);
            tips[i] = (TextView) readmePages[i].findViewById(R.id.read_me_tip);
            tips[i].setText(tipsText[i]);
            buttons[i] = (Button) readmePages[i].findViewById(R.id.readme_button);
            buttons[i].setText(buttonsText[i]);
            buttons[i].setOnClickListener(this);
            viewPropertyAnimators[i] = readmePages[i].animate();
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < readmePages.length; i++) {
            if (readmePages[i].indexOfChild(v) > -1) {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        ShellUtil.initIptables();
                        break;
                    case 2:
                        ShellUtil.initCellularInterfaces(this);
                        if (Config.CELLULAR_INTERFACE == null) {
                            Toast.makeText(this, "404", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(this, Config.CELLULAR_INTERFACE, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                viewPropertyAnimators[i].setListener(new TargetAnimatorListener(readmePages, i)).alpha(0).translationX(-readmePages[i].getMeasuredWidth()).setDuration(1000).start();
            }
        }
    }

    private class TargetAnimatorListener implements Animator.AnimatorListener {

        private View[] targets;
        private int index;

        public TargetAnimatorListener(View[] targets, int index) {
            this.targets = targets;
            this.index = index;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (index < targets.length - 1) {
                Log.d(TAG, "onAnimationEnd: Next");
                targets[index].setVisibility(View.GONE);
                readmePages[index + 1].setVisibility(View.VISIBLE);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(500);
                readmePages[index + 1].startAnimation(alphaAnimation);
            } else {
                Log.d(TAG, "onAnimationEnd: startActivity");
                startActivity(new Intent(ReadMeActivity.this, MainActivity.class));
                finish();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
