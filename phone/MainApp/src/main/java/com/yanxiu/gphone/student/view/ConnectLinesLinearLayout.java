package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ConnectLinesLinearLayout extends LinearLayout implements ConnectTextView.OnCheckListener, ConnectLinearLayout.OnLayoutSuccessListener {

    public static final String LOCATION_LEFT = "location_left";
    public static final String LOCATION_RIGHT = "location_right";
    private static final int checked_now = R.drawable.connect_now;
    private static final int checked_no = R.drawable.connect_no;
    private static final int checked_old = R.drawable.connect_old;
    private Context context;
    private LinearLayout linear;
    private Connectlines mylines;
    private List<BaseBean> list = new ArrayList<BaseBean>();
    private List<String> mDatas = new ArrayList<String>();
    private List<String> answerData = new ArrayList<String>();
    private AnswerBean answerBean;
    private boolean isResolution;
    private boolean isClick=true;
    private boolean isWrongSet;

    public ConnectLinesLinearLayout(Context context) {
        this(context, null);
    }

    public ConnectLinesLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConnectLinesLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.connect_lineslinear, this);
        linear = (LinearLayout) this.findViewById(R.id.linear);
        mylines = (Connectlines) this.findViewById(R.id.mylines);
    }

    /**
     * 设置标准答案
     */
    public void setAnswers(List<String> answerData) {
        if (answerData != null) {
            this.answerData.addAll(answerData);
        }
    }

    /**
     * 是否是解析界面
     * @param isResolution
     */
    public void setIsResolution(boolean isResolution) {
        this.isResolution = isResolution;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
        mylines.setIsClick(isClick);
    }

    public void setIsWrongSet(boolean isWrongSet) {
        this.isWrongSet = isWrongSet;
    }

    public void setAnswerBean(AnswerBean answerBean) {
        this.answerBean = answerBean;
    }

    public void saveAnswers() {

        if (answerBean == null) {
            return;
        }
        answerBean.setType(2);
        if (list.size()>0) {

        ArrayList<ArrayList<String>> answerlist = answerBean.getConnect_classfy_answer();
        answerlist.clear();
        for (BaseBean basebean : list) {
            if (basebean.getLocation().equals(LOCATION_LEFT)) {
                if (basebean.isSelect()) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(basebean.getId() + "");
                    list.add(basebean.getSelect_id() + "");
                    answerlist.add(list);
                }
            }
        }
            if (answerlist.size() < list.size() / 2) {
                answerBean.setIsFinish(false);
                answerBean.setIsRight(false);
            } else {
                answerBean.setIsFinish(true);
                getIsRight(answerlist);
            }
        }
    }

    private void getIsRight(ArrayList<ArrayList<String>> answerlist) {
        /**这边可以进行优化，将标准答案放到map里面，这样可以减少大量的逻辑处理*/
        for (String s : answerData) {
            try {
                JSONObject object = new JSONObject(s);
                String string = object.getString("answer");
                String ss[] = string.split(",");
                for (BaseBean basebean : list) {
                    if (basebean.getId() == Integer.parseInt(ss[0])) {
                        if (basebean.getSelect_id() != Integer.parseInt(ss[1])) {
                            answerBean.setIsRight(false);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        answerBean.setIsRight(true);
    }

    public void setDatas(final List<String> lists) {
        try {
            mDatas.addAll(lists);
            int position = mDatas.size();
            setData(position / 2);
        } catch (Exception e) {

        }
    }

    public void setDefault() {
        if (answerBean!=null) {
            ArrayList<ArrayList<String>> answerlist = answerBean.getConnect_classfy_answer();
            for (ArrayList<String> arrayList : answerlist) {
                if (arrayList.size() > 0) {
                    int id_left = Integer.parseInt(arrayList.get(0));
                    int id_right = Integer.parseInt(arrayList.get(1));

                    boolean flag=CheckedById(id_left,id_right);

                    BaseBean bean = getBeanById(id_left);
                    bean.setSelect(true);
                    bean.setSelect_id(id_right);
                    bean.setIsRight(flag);

                    BaseBean bean1 = getBeanById(id_right);
                    bean1.setSelect(true);
                    bean1.setSelect_id(id_left);
                    bean1.setIsRight(flag);
                }
            }
            setColorAndMyLines();
        }
    }

    private boolean CheckedById(int id_left,int id_right){

        /**这边可以进行优化，将标准答案放到map里面，这样可以减少大量的逻辑处理*/
        for (String s : answerData) {
            try {
                JSONObject object = new JSONObject(s);
                String string = object.getString("answer");
                String ss[] = string.split(",");
                if (Integer.parseInt(ss[0])==id_left){
                    if (Integer.parseInt(ss[1])==id_right){
                        return true;
                    }
                }else if (Integer.parseInt(ss[1])==id_left){
                    if (Integer.parseInt(ss[0])==id_right){
                        return true;
                    }
                }else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Nullable
    private BaseBean getBeanById(int id){
        for (BaseBean bean: list){
            if (id==bean.getId()){
                return bean;
            }
        }
        return null;
    }

    private void setData(int position) {
        for (int i = 0; i < position; i++) {
            BaseBean bean_left = new BaseBean();
            bean_left.setId(i);
            bean_left.setLocation(LOCATION_LEFT);

            BaseBean bean_right = new BaseBean();
            bean_right.setId(position+i);
            bean_right.setLocation(LOCATION_RIGHT);

            ConnectLinearLayout linearLayout=getConnectLinearLayout(mDatas.get(i),bean_left,mDatas.get(position+i), bean_right);
            list.add(bean_left);
            list.add(bean_right);
            linear.addView(linearLayout);
        }
        setDefault();
    }

    private ConnectLinearLayout getConnectLinearLayout(String text_left, BaseBean bean_left,String text_right, BaseBean bean_right) {
        ConnectLinearLayout linearLayout=new ConnectLinearLayout(context);
        linearLayout.setData_left(text_left,bean_left);
        linearLayout.setData_right(text_right,bean_right);
        linearLayout.setOnLayoutSuccessListener(this);
        linearLayout.setCheckListener(this);
        linearLayout.setBackgroud(checked_no);
        return linearLayout;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void OnCheckListener(BaseBean bean) {
        if (isClick) {
            if (bean.isOnclick()){
                bean.setOnclick(false);
            }else {
                bean.setOnclick(true);
            }
            cleanOnclick(bean);
            checkattachment();
            setColorAndMyLines();
        }
    }

    private void setColorAndMyLines(){
        setMyTextviewColor();
        mylines.setDatas(list);
    }

    private void cleanOnclick(BaseBean bean) {
        for (BaseBean basebean : list) {
            if (basebean.getLocation().equals(bean.getLocation())) {
                if (basebean.getId() != bean.getId()) {
                    basebean.setOnclick(false);
                }
            }
        }
    }

    private void checkattachment() {
        int id_left = getOnClickId(LOCATION_LEFT);
        int id_right = getOnClickId(LOCATION_RIGHT);
        if (id_left != -1 && id_right != -1) {
            setSelectId(id_left, id_right);
        }
    }

    private int getOnClickId(String location) {
        for (BaseBean basebean : list) {
            if (basebean.getLocation().equals(location)) {
                if (basebean.isOnclick()) {
                    return basebean.getId();
                }
            }
        }
        return -1;
    }

    private void setSelectId(int id_left, int id_right) {
        for (BaseBean basebean : list) {
            if (basebean.getLocation().equals(LOCATION_LEFT)) {
                if (basebean.getId() == id_left) {
                    if (basebean.isSelect()) {
                        int id = basebean.getSelect_id();
                        catlines(LOCATION_RIGHT, id);
                    }
                    basebean.setOnclick(false);
                    basebean.setSelect(true);
                    basebean.setSelect_id(id_right);
                }
            } else if (basebean.getLocation().equals(LOCATION_RIGHT)) {
                if (basebean.getId() == id_right) {
                    if (basebean.isSelect()) {
                        int id = basebean.getSelect_id();
                        catlines(LOCATION_LEFT, id);
                    }
                    basebean.setOnclick(false);
                    basebean.setSelect(true);
                    basebean.setSelect_id(id_left);
                }
            }
        }
    }

    private void catlines(String location, int id) {
        for (BaseBean basebean : list) {
            if (basebean.getLocation().equals(location)) {
                if (basebean.getId() == id) {
                    basebean.setSelect_id(-1);
                    basebean.setSelect(false);
                    basebean.setOnclick(false);
                }
            }
        }
    }

    private void setMyTextviewColor() {
        for (BaseBean bean : list) {
            if (bean.isOnclick()) {
                bean.getTextView().setBackgroundResource(checked_now);
                bean.getTextView().setTextColor(context.getResources().getColor(R.color.color_998e63));
            } else if (!bean.isOnclick() && bean.isSelect()) {
                bean.getTextView().setBackgroundResource(checked_old);
                bean.getTextView().setTextColor(context.getResources().getColor(R.color.color_333333));
            } else {
                bean.getTextView().setBackgroundResource(checked_no);
                bean.getTextView().setTextColor(context.getResources().getColor(R.color.color_333333));
            }
        }
    }

    @Override
    public void OnLayoutSuccessListener() {
        setColorAndMyLines();
    }

    public class BaseBean {
        private ConnectTextView textView;
        private String location;
        private int id;
        private int y;
        private boolean select = false;
        private int select_id = -1;
        private boolean onclick = false;
        private boolean IsRight=false;

        public BaseBean() {
        }

        public boolean getIsRight() {
            return IsRight;
        }

        public void setIsRight(boolean isRight) {
            IsRight = isRight;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getSelect_id() {
            return select_id;
        }

        public void setSelect_id(int select_id) {
            this.select_id = select_id;
        }

        public ConnectTextView getTextView() {
            return textView;
        }

        public void setTextView(ConnectTextView textView) {
            this.textView = textView;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public boolean isOnclick() {
            return onclick;
        }

        public void setOnclick(boolean onclick) {
            this.onclick = onclick;
        }
    }
}
