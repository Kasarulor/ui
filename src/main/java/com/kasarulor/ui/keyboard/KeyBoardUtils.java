package com.kasarulor.ui.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kasarulor.ui.R;
import com.kasarulor.ui.log.LogX;

import java.util.LinkedHashSet;
import java.util.Set;


/*
 *
 *     NOTE:
 *           THIS IS A  CUSTOM VIEW'S KEYBOARD
 *
 *            JUST  SUPPORT  NUMBER OR TEXT
 *     VERSION: 1.0.0
 *
 *     CREATER:  KASARULOR
 *
 *     TIME: 2019-3-30
 *
 *
 *       NOTE2:  IF  YOU  NEED A NUMBER KEYBOARD  AND  SHOW OR  HIDDEN 'TIME   YOUR TEXTS  CAN  CLEAR DATA
 *
 *         YOU  NEED  USE  METHOD OF   CACHE   AND   YOU NEED A  GOOD CALLBACK  CAN  GOT A REAL'S DATA
 *
 *
 * */

public class KeyBoardUtils {

    private OnKeyBoardLimitCallBack onKeyBoardLimitCallBack;
    private KeyBoardType keyBoardType[];
    private StringBuilder stringBuilder = new StringBuilder();
    private StringBuilder stringBuilder2 = new StringBuilder();
    private String OPEARA = "*";

    /*点击时需要传入一个编辑框  这样我们就能够向指定的编辑框输入内容*/
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog bottomSheetDialog;/*底部弹窗 */

    private View mainView;

    private Context context;

    private TextView targetTextView[];

    private View numberView;
    private View textView;

    private Integer numberID[] = {R.id.n1,
            R.id.n2,
            R.id.n3,
            R.id.n4,
            R.id.n5,
            R.id.n6,
            R.id.n7,
            R.id.n8,
            R.id.n9,
            R.id.n0};
    /*数字键位特殊位*/
    private TextView ne, delete;

    private TextView numberText[] = new TextView[numberID.length];
    private Set<Integer> numberSet = new LinkedHashSet<>();
//    private Random random = new Random();


    private Set<Character> characterSet = new LinkedHashSet<>();

    private boolean isUpCase = false;
    /*字母键位组*/
    private Integer textId[] = {
            R.id.k1,
            R.id.k2,
            R.id.k3,
            R.id.k4,
            R.id.k5,
            R.id.k6,
            R.id.k7,
            R.id.k8,
            R.id.k9,
            R.id.k10,
            R.id.k11,
            R.id.k12,
            R.id.k13,
            R.id.k14, R.id.k15,
            R.id.k16,
            R.id.k17, R.id.k18,
            R.id.k19, R.id.k20,
            R.id.k21,
            R.id.k22,
            R.id.k23,
            R.id.k24,
            R.id.k25,
            R.id.k26


    };
    private TextView text[] = new TextView[26];
    private TextView kb, ks, number, vis;
    private TextView complete;

    public KeyBoardUtils(Context context, TextView... targetTextView) {
        this(context, targetTextView, KeyBoardType.values());
    }

    public KeyBoardUtils(Context context, TextView[] targetTextView, @NonNull KeyBoardType... keyBoardType) {
        this.context = context;
        this.keyBoardType = keyBoardType;
        this.targetTextView = targetTextView;
        if (targetTextView != null && targetTextView.length == 1) {
            if (targetTextView[0] instanceof EditText) {
                targetTextView[0].setFocusable(false);
                targetTextView[0].setInputType(InputType.TYPE_NULL);
                targetTextView[0].setFocusableInTouchMode(false);

            }
        }

        mainView = LayoutInflater.from(context).inflate(R.layout.mainkeyboard, null);


        complete = mainView.findViewById(R.id.complete);
        complete.setOnClickListener((v) -> {
            if (bottomSheetDialog != null) bottomSheetDialog.hide();
        });
        if (equals(KeyBoardType.NUMBER))
            initNumberView();
        if (equals(KeyBoardType.TEXT))
            initTextView();
        if (equals(KeyBoardType.NUMBER) && equals(KeyBoardType.TEXT)) {
            numberView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else if (equals(KeyBoardType.NUMBER)) {
            numberView.setVisibility(View.VISIBLE);
            //textView.setVisibility(View.GONE);
        } else {
            //  numberView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        createDialog();
    }

    private void initNumberView() {
        numberView = mainView.findViewById(R.id.numbers);
        ne = numberView.findViewById(R.id.ne);/*切换为字母键盘*/
        delete = numberView.findViewById(R.id.delete);/*删除键*/
        productMathRandom();/*产生随机数了*/
        int index = 0;
        for (int number : numberSet) {
            numberText[index] = numberView.findViewById(numberID[index]);
            numberText[index].setText(number + "");
            int finalIndex = index;
            numberText[index].setOnClickListener((v) -> {

                stringBuilder.append(numberText[finalIndex].getText().toString().trim());
                LogX.e("添加后的数据:", stringBuilder.toString());
                //  targetTextView.append(OPEARA);
                stringBuilder2.append(OPEARA);
                printToTargetView();
            });
            index++;
        }
        delete.setOnClickListener((v) -> {
            if (stringBuilder.toString().length() > 0) {
                stringBuilder.delete(stringBuilder.toString().length() - 1, stringBuilder.toString().length());
                stringBuilder2.delete(stringBuilder2.toString().length() - 1, stringBuilder2.toString().length());
            }
            printToTargetView();
            LogX.e("删除后的结果:", stringBuilder.toString());
        });
        ne.setOnClickListener((v) -> {
            if (equals(KeyBoardType.TEXT)) {
                numberView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(context, "当前仅支持数字键盘", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initTextView() {
        textView = mainView.findViewById(R.id.texts);
        isUpCase = false;
        productChar(isUpCase);
        int index = 0;
        for (char c : characterSet) {
            text[index] = textView.findViewById(textId[index]);
            text[index].setText(c + "");
            int finalIndex = index;
            text[index].setOnClickListener((v) -> {
                stringBuilder.append(text[finalIndex].getText().toString());
                LogX.e("添加后的数据2:", stringBuilder.toString());
                stringBuilder2.append("*");
                printToTargetView();
            });
            index++;
        }
        kb = textView.findViewById(R.id.kb);
        ks = textView.findViewById(R.id.ks);
        number = textView.findViewById(R.id.number);
        vis = textView.findViewById(R.id.vis);
        kb.setOnClickListener((v) -> {
            isUpCase = !isUpCase;
            kb.setText(isUpCase ? "小" : "大");
            productChar(isUpCase);
            int indexf = 0;
            for (char c : characterSet) {
                //text[index] = textView.findViewById(textId[index]);
                text[indexf].setText(c + "");

                indexf++;
            }
        });
        ks.setOnClickListener((v) -> {
            if (stringBuilder.toString().length() > 0) {
                stringBuilder.delete(stringBuilder.toString().length() - 1, stringBuilder.toString().length());
                stringBuilder2.delete(stringBuilder2.toString().length() - 1, stringBuilder2.toString().length());
                printToTargetView();
            }
            LogX.e("删除后的结果:", stringBuilder.toString());
        });
        number.setOnClickListener((v) -> {
            if (equals(KeyBoardType.NUMBER)) {
                numberView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "当前仅支持字母键盘", Toast.LENGTH_LONG).show();
            }
        });

        vis.setOnClickListener((v) -> {
            bottomSheetDialog.hide();
        });
    }

    /*产生随机数组*/
    private void productMathRandom() {
        numberSet.clear();
        while (numberSet.size() < 10) {
            int sjs = (int) (Math.random() * 10);
            LogX.e("随机数", sjs + "");
            numberSet.add(sjs);
            LogX.e("随机数组", numberSet + "");
        }
    }

    /*随机产生26个随机字母*/
    private void productChar(boolean isUpperCase) {
        characterSet.clear();
        while (characterSet.size() < 26) {
            char chart = (char) (Math.random() * 26 + (isUpperCase ? 'A' : 'a'));
            characterSet.add(chart);
            //    LogX.e("字符随机组:",numberSet.toArray().toString());
        }
    }

    private void createDialog() {
        bottomSheetDialog = new BottomSheetDialog(context);
//        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//
//            }
//        });
        if(bottomSheetDialog.getWindow() !=null){
            bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        }else{

        }
        bottomSheetDialog.setContentView(mainView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) mainView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_DRAGGING) {
                    /*拖动的时候*

                     */
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }

    public void show() {
        if (equals(KeyBoardType.NUMBER)) {

            /*更新数字键盘*/
            productMathRandom();/*产生随机数了*/
            int index = 0;
            for (int number : numberSet) {
                numberText[index].setText("" + number);
                index++;
            }
            LogX.e("数字:", numberSet + "");
        }
        if (equals(KeyBoardType.TEXT)) {
            productChar(isUpCase);
            int indexf = 0;
            for (char c : characterSet) {
                //text[index] = textView.findViewById(textId[index]);
                text[indexf].setText("" + c);

                indexf++;
            }
            LogX.e("数字:", characterSet + "");
        }
      //  if(bottomSheetDialog !=null  && !bottomSheetDialog.isShowing()){
         try{
             bottomSheetDialog.show();
         }catch (Exception e){}
        //}

    }


    public String getRealValue() {
        return stringBuilder.toString();
    }


    /*  if  you  user  pay  object
     *   and  you  want  got a real  result
     *   you  can set a interace  callback
     * */
    private void printToTargetView() {

        if (targetTextView != null && targetTextView.length == 1) {
            targetTextView[0].setText(stringBuilder2.toString());
        } else {
            int index = 0;
            for (TextView t : targetTextView) {
                if (t != null) {
                    if (stringBuilder2.toString().length() - 1 >= index) {
                        t.setText(stringBuilder2.charAt(index) + "");
                    } else {
                        t.setText("");
                    }

                } else {
                    LogX.e("keyBoardUitls", "空指针");
                    return;
                }
                index++;
            }
        }

        if (onKeyBoardLimitCallBack != null) {
            int limit = onKeyBoardLimitCallBack.limitLength();
            if (stringBuilder.toString().length() >= limit) {

                onKeyBoardLimitCallBack.onResult(stringBuilder.toString().substring(0, limit), limit);
                stringBuilder.delete(limit, stringBuilder.toString().length());
                stringBuilder2.delete(limit, stringBuilder2.toString().length());
                LogX.e("真实密码:",stringBuilder.toString());
                LogX.e("掩码输出:",stringBuilder2.toString());
                if (targetTextView.length == 1) {
                    targetTextView[0].setText(stringBuilder2.toString());
                }

                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.hide();
                }

            }
        }
    }


    public boolean equals(KeyBoardType keyBoardType) {
//        return super.equals(obj);
        if (this.keyBoardType != null) {
            for (KeyBoardType k : this.keyBoardType) {
                if (k == keyBoardType) return true;
            }
        }
        return false;
    }

    /*修改输入符号*/
    public void changeOpera(@NonNull String targetOpera) {
        this.OPEARA = targetOpera;

    }

    /*清除缓存  在支付时要求清空数据 每次弹起键盘都必须新的数据*/
    /*  you  may  use  it  in  your  pay's password window*/
    public void cache() {
        stringBuilder.delete(0, stringBuilder.toString().length());
        stringBuilder2.delete(0, stringBuilder2.toString().length());
        for (TextView t : targetTextView) {
            t.setText("");
        }
    }


    public void setOnKeyBoardLimitCallBack(OnKeyBoardLimitCallBack onKeyBoardLimitCallBack) {
        this.onKeyBoardLimitCallBack = onKeyBoardLimitCallBack;
    }
}
