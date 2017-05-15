package com.yanxiu.gphone.studentold.view.passwordview;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;


/**
 * ●
 *
 * @author Jungly
 *         mail: jungly.ik@gmail.com
 * @date 15/3/5 21:30
 */
public class GridPasswordView extends LinearLayout implements PasswordView {
    private static final int DEFAULT_PASSWORDLENGTH = 6;
    private static final int DEFAULT_TEXTSIZE = 16;
    private static final String DEFAULT_TRANSFORMATION = "●";
    private static final int DEFAULT_LINECOLOR = 0xaa888888;
    private static final int DEFAULT_GRIDCOLOR = 0xffffffff;

    private ColorStateList textColor;
    private int textSize = DEFAULT_TEXTSIZE;
    private int tag;

    private int lineWidth;
    private int lineColor;
    private int gridColor;
    private Drawable lineDrawable;
    private Drawable outerLineDrawable;

    private int passwordLength;
    //单字符
    private String passwordTransformation;
    private int passwordType;

    private ImeDelBugFixedEditText inputView;

    private String[] passwordArr;
    private TextView[] viewArr;
    private ImeDelBugFixedEditText[] editTextsArr;

    private OnPasswordChangedListener listener;

    private PasswordTransformationMethod transformationMethod;
    private OnTextChangedListener textChangedListener;
    private Context mContext;
    DisplayMetrics displayMetrics = null;

    public interface OnTextChangedListener {
        void onTextNumberChanged(int number);
    }

    public void setTextChangedListener(OnTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    public GridPasswordView(Context context) {
        this(context, null);
    }

    public GridPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        displayMetrics = mContext.getResources().getDisplayMetrics();
        initAttrs(context, attrs, defStyleAttr);
        initViews(context);
    }

    public int px2sp(float pxValue) {
        final float fontScale = displayMetrics.scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public int dipToPx(int dipValue) {
        final float scale = displayMetrics.density;
        int pxValue = (int) (dipValue * scale + 0.5f);

        return pxValue;
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.gridPasswordView, defStyleAttr, 0);

        textColor = ta.getColorStateList(R.styleable.gridPasswordView_textColor);
        if (textColor == null)
            textColor = ColorStateList.valueOf(getResources().getColor(android.R.color.primary_text_light));
        int textSize = ta.getDimensionPixelSize(R.styleable.gridPasswordView_textSize, -1);
        if (textSize != -1) {
            this.textSize = px2sp(textSize);
        }

        lineWidth = (int) ta.getDimension(R.styleable.gridPasswordView_lineWidth, dipToPx(1));
        lineWidth = 0;
        lineColor = ta.getColor(R.styleable.gridPasswordView_lineColor, DEFAULT_LINECOLOR);
        gridColor = ta.getColor(R.styleable.gridPasswordView_gridColor, DEFAULT_GRIDCOLOR);
        lineDrawable = ta.getDrawable(R.styleable.gridPasswordView_lineColor);
        if (lineDrawable == null) {
            lineDrawable = new ColorDrawable(lineColor);
        }
        outerLineDrawable = generateBackgroundDrawable();

        passwordLength = ta.getInt(R.styleable.gridPasswordView_passwordLength, DEFAULT_PASSWORDLENGTH);
        passwordTransformation = ta.getString(R.styleable.gridPasswordView_passwordTransformation);
        if (TextUtils.isEmpty(passwordTransformation)) {
            passwordTransformation = DEFAULT_TRANSFORMATION;
        }
        passwordType = ta.getInt(R.styleable.gridPasswordView_passwordType, 0);
        ta.recycle();
        passwordArr = new String[passwordLength];
        viewArr = new TextView[passwordLength];
        editTextsArr = new ImeDelBugFixedEditText[passwordLength];
    }

    private void initViews(Context context) {
//        super.setBackgroundDrawable(outerLineDrawable);
        super.setBackgroundResource(R.drawable.group_input_bg);
        setShowDividers(SHOW_DIVIDER_NONE);
        setOrientation(HORIZONTAL);

        transformationMethod = new CustomPasswordTransformationMethod(passwordTransformation);
        inflaterViews(context);
    }

    private void inflaterViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.gridpasswordview, this);
//        inputView = (ImeDelBugFixedEditText) findViewById(R.id.inputView);
//
//        inputView.setMaxEms(passwordLength);
//        inputView.addTextChangedListener(textWatcher);
//        inputView.setDelKeyEventListener(onDelKeyEventListener);
//        setCustomAttr(inputView);

//        viewArr[0] = inputView;

        int index = 0;
        ImeDelBugFixedEditText inputViewedit = null;
        while (index < passwordLength) {

            View dividerView = inflater.inflate(R.layout.divider, null);
            LayoutParams dividerParams = new LayoutParams(lineWidth, LayoutParams.MATCH_PARENT);
            dividerView.setBackgroundDrawable(null);
//           dividerView.setBackgroundDrawable(lineDrawable);
            addView(dividerView, dividerParams);

            FrameLayout textView = (FrameLayout) inflater.inflate(R.layout.textview, null);
            TextView view = (TextView) textView.findViewById(R.id.text_view);
            setCustomAttr(view);

            ImeDelBugFixedEditText inputView = (ImeDelBugFixedEditText) textView.findViewById(R.id.inputView);
//            inputView.setMaxEms(passwordLength);
            inputView.setTouchLinstener(touchLinstener, index);
            inputView.setDelKeyEventListener(onDelKeyEventListener);
//            inputView.addTextChangedListener(textWatcher);
//            setCustomAttr(inputView);
            inputView.setTextSize(textSize);
            inputView.setOnLongClickListener(onLongClickListener);
            if (index == 0) {
//                forceInputViewGetFocus(inputView);
                inputViewedit = inputView;
            }

            LayoutParams textViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            addView(textView, textViewParams);

            viewArr[index] = view;
            editTextsArr[index] = inputView;
            index++;
        }
        if (inputViewedit != null) {
            inputView = inputViewedit;
            inputView.addTextChangedListener(textWatcher);
            forceInputViewGetFocus(inputViewedit);
        }
//        setOnClickListener(onClickListener);

//        setOnLongClickListener(onLongClickListener);
    }

    private void setCustomAttr(TextView view) {
        if (textColor != null) {
            view.setTextColor(textColor);
        }
        view.setTextSize(textSize);
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {
            case 1:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case 2:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case 3:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }
        view.setInputType(inputType);
        view.setTransformationMethod(transformationMethod);
    }

    private ImeDelBugFixedEditText.OnTouchLinstener touchLinstener = new ImeDelBugFixedEditText.OnTouchLinstener() {
        @Override
        public void onTouchListener(int tag, ImeDelBugFixedEditText view) {
            if (inputView != null) {
                inputView.setFocusable(false);
                inputView.setFocusableInTouchMode(false);
                inputView.removeTextChangedListener(textWatcher);
            }
            int lengeh = getPassWord().length();
            if (lengeh < tag) {
                inputView = editTextsArr[lengeh];
                GridPasswordView.this.tag = lengeh;
            } else {
                inputView = editTextsArr[tag];
                GridPasswordView.this.tag = tag;
            }
            inputView.setSelection(inputView.getText().toString().length());
            inputView.addTextChangedListener(textWatcher);
            forceInputViewGetFocus(inputView);
        }
    };

    private OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
//            Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
            setPopPaste();
            return false;
        }
    };

    private void setPopPaste() {
        PopupMenu menu=new PopupMenu(mContext,this);
        menu.getMenuInflater().inflate(R.menu.class_pop_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                if (cmb == null || TextUtils.isEmpty(cmb.getText()))
                    return false;
                char[] chars = cmb.getText().toString().toCharArray();//获取粘贴信息

                char[] pswArr = cmb.getText().toString().toCharArray();
                for (int i = 0; i < pswArr.length; i++) {
                    if (i < passwordArr.length) {
                        passwordArr[i] = pswArr[i] + "";
                        setHtmlTxt(viewArr[i], passwordArr[i]);
                        setEditTextMessage(editTextsArr[i],passwordArr[i]);
                    }
                }
                return true;
            }
        });
        menu.show();
    }

    private GradientDrawable generateBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(gridColor);
        drawable.setStroke(lineWidth, lineColor);
        return drawable;
    }

    private void forceInputViewGetFocus(ImeDelBugFixedEditText inputView) {
        inputView.setFocusable(true);
        inputView.setFocusableInTouchMode(true);
        inputView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputView, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setDeletePasswordArrByTag(int index) {
        index++;
        while (index < passwordArr.length) {
            passwordArr[index - 1] = passwordArr[index];
            index++;
        }
        passwordArr[passwordArr.length - 1] = null;
    }

    private ImeDelBugFixedEditText.OnDelKeyEventListener onDelKeyEventListener = new ImeDelBugFixedEditText.OnDelKeyEventListener() {

        @Override
        public void onDeleteClick() {
            int index = tag;
            setDeletePasswordArrByTag(index);

//            for (int i = passwordArr.length - 1; i >= 0; i--) {
//                if (passwordArr[i] != null) {
//                    passwordArr[i] = null;
//                    setHtmlTxt(viewArr[i], null);
//                    setEditTextMessage(editTextsArr[i], "");
//                    notifyTextChanged();
//                    break;
//                } else {
//                    setHtmlTxt(viewArr[i], null);
//                    setEditTextMessage(editTextsArr[i], "");
//                }
//            }

            if (tag != 0) {
                editTextsArr[tag].setFocusable(false);
                editTextsArr[tag].setFocusableInTouchMode(false);
                editTextsArr[tag].removeTextChangedListener(textWatcher);
                tag = tag - 1;
                for (int i = tag; i < passwordArr.length; i++) {
                    if (passwordArr[i] != null) {
                        setHtmlTxt(viewArr[i], passwordArr[i]);
                        setEditTextMessage(editTextsArr[i], passwordArr[i]);
                    } else {
                        setHtmlTxt(viewArr[i], null);
                        setEditTextMessage(editTextsArr[i], null);
                    }
                }
                editTextsArr[tag].addTextChangedListener(textWatcher);
                inputView = editTextsArr[tag];
                inputView.setSelection(inputView.getText().toString().length());
                forceInputViewGetFocus(editTextsArr[tag]);
            } else {
                inputView.removeTextChangedListener(textWatcher);
                for (int i = tag; i < passwordArr.length; i++) {
                    if (passwordArr[i] != null) {
                        setHtmlTxt(viewArr[i], passwordArr[i]);
                        setEditTextMessage(editTextsArr[i], passwordArr[i]);
                    } else {
                        setHtmlTxt(viewArr[i], null);
                        setEditTextMessage(editTextsArr[i], null);
                    }
                }
                inputView.setSelection(inputView.getText().toString().length());
                inputView.addTextChangedListener(textWatcher);
            }

            if (passwordArr != null && textChangedListener != null) {
                textChangedListener.onTextNumberChanged(getPassWord().length());
            }
        }
    };

    private void setHtmlTxt(TextView textView, String txt) {
        if (TextUtils.isEmpty(txt)) {
            textView.setText(null);
        } else {
            textView.setText(Html.fromHtml("<strong>" + txt +
                    "</strong>"));
        }
    }

    private void setEditTextMessage(EditText editText, String text) {
        if (TextUtils.isEmpty(text)) {
            editText.setText("");
        } else {
            editText.setText(text);
        }
    }

    private void setNewPasswordArr(String num) {
        String one = num;
        int index = tag;
        while (index < passwordArr.length) {
            String two = passwordArr[index];
            passwordArr[index] = one;
            one = two;
            index++;
        }

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) {
                return;
            } else {
                int passlength=getPassWord().length();
                String newStr = s.toString();
                String[] strings = new String[2];
                if (newStr.length() > 1) {
                    strings[0] = newStr.split("")[1];
                    strings[1] = newStr.split("")[2];
                    if (passlength!=8) {
                        passwordArr[tag] = strings[1];
                        setNewPasswordArr(strings[0]);
                    }else {
                        newStr=strings[0];
                        passwordArr[tag] = newStr;
                    }
                } else {
                    passwordArr[tag] = newStr;
                }

                for (int i = tag; i < passwordArr.length; i++) {
                    if (passwordArr[i] != null) {
                        setHtmlTxt(viewArr[i], passwordArr[i]);
                    }
                }
                if (tag + 1 < passwordLength) {
                    editTextsArr[tag].removeTextChangedListener(this);
                    if (passlength!=8) {
                        editTextsArr[tag].setFocusable(false);
                        editTextsArr[tag].setFocusableInTouchMode(false);
                    }
                    for (int i = tag; i < passwordArr.length; i++) {
                        if (passwordArr[i] != null) {
                            setEditTextMessage(editTextsArr[i], passwordArr[i]);
                        }
                    }
                    if (passlength!=8) {
                        tag = tag + 1;
                        inputView = editTextsArr[tag];
                        forceInputViewGetFocus(editTextsArr[tag]);
                    }
                    inputView.addTextChangedListener(this);
                    inputView.setSelection(inputView.getText().toString().length());
                }else {
                    inputView.removeTextChangedListener(this);
                    for (int i = tag; i < passwordArr.length; i++) {
                        if (passwordArr[i] != null) {
                            setEditTextMessage(editTextsArr[i], passwordArr[i]);
                        }
                    }
                    inputView.addTextChangedListener(this);
                    inputView.setSelection(inputView.getText().toString().length());
                }
            }
            if (passwordArr != null && textChangedListener != null) {
                textChangedListener.onTextNumberChanged(getPassWord().length());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Deprecated
    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                onDelKeyEventListener.onDeleteClick();
                return true;
            }
            return false;
        }
    };

    private void notifyTextChanged() {
        if (listener == null)
            return;

        String currentPsw = getPassWord();
        listener.onChanged(currentPsw);

        if (currentPsw.length() == passwordLength)
            listener.onMaxLength(currentPsw);

    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("instanceState", super.onSaveInstanceState());
//        bundle.putStringArray("passwordArr", passwordArr);
//        return bundle;
//    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if (state instanceof Bundle) {
//            Bundle bundle = (Bundle) state;
//            passwordArr = bundle.getStringArray("passwordArr");
//            state = bundle.getParcelable("instanceState");
//            inputView.removeTextChangedListener(textWatcher);
//            setPassword(getPassWord());
//            inputView.addTextChangedListener(textWatcher);
//        }
//        super.onRestoreInstanceState(state);
//    }

    //@Override
//    private void setError(String error) {
//        inputView.setError(error);
//    }

    /**
     * Return the text the PasswordView is displaying.
     */
    @Override
    public String getPassWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordArr.length; i++) {
            if (passwordArr[i] != null)
                sb.append(passwordArr[i]);
        }
        return sb.toString();
    }

    /**
     * Clear the passwrod the PasswordView is displaying.
     */
    @Override
    public void clearPassword() {
        for (int i = 0; i < passwordArr.length; i++) {
            passwordArr[i] = null;
            setHtmlTxt(viewArr[i], null);
        }
    }

    /**
     * Sets the string value of the PasswordView.
     */
    @Override
    public void setPassword(String password) {
        clearPassword();

        if (TextUtils.isEmpty(password))
            return;

        char[] pswArr = password.toCharArray();
        for (int i = 0; i < pswArr.length; i++) {
            if (i < passwordArr.length) {
                passwordArr[i] = pswArr[i] + "";
                setHtmlTxt(viewArr[i], passwordArr[i]);
            }
        }
    }

    /**
     * Set the enabled state of this view.
     */
    @Override
    public void setPasswordVisibility(boolean visible) {
        for (TextView textView : viewArr) {
            textView.setTransformationMethod(visible ? null : transformationMethod);
            if (textView instanceof EditText) {
                EditText et = (EditText) textView;
                et.setSelection(et.getText().length());
            }
        }
    }

    /**
     * Toggle the enabled state of this view.
     */
    @Override
    public void togglePasswordVisibility() {
        boolean currentVisible = getPassWordVisibility();
        setPasswordVisibility(!currentVisible);
    }

    /**
     * Get the visibility of this view.
     */
    private boolean getPassWordVisibility() {
        return viewArr[0].getTransformationMethod() == null;
    }

    /**
     * Register a callback to be invoked when password changed.
     */
    @Override
    public void setOnPasswordChangedListener(OnPasswordChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPasswordType(PasswordType passwordType) {
        boolean visible = getPassWordVisibility();
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {

            case TEXT:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case TEXTVISIBLE:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case TEXTWEB:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }

        for (TextView textView : viewArr) {
            textView.setInputType(inputType);
        }

        setPasswordVisibility(visible);
    }

    /**
     * Interface definition for a callback to be invoked when the password changed or is at the maximum length.
     */
    public interface OnPasswordChangedListener {

        /**
         * Invoked when the password changed.
         */
        void onChanged(String psw);

        /**
         * Invoked when the password is at the maximum length.
         */
        void onMaxLength(String psw);

    }
}
