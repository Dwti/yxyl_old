package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ConnectLinesLinearLayout extends LinearLayout implements ConnectTextView.OnCheckListener, ConnectTextView.OnLayoutSuccessListener {

    public static final String LOCATION_LEFT="location_left";
    public static final String LOCATION_RIGHT="location_right";
    private static final int checked_now= R.drawable.connect_now;
    private static final int checked_no= R.drawable.connect_no;
    private static final int checked_old= R.drawable.connect_old;
    private Context context;
    private LinearLayout linear_left;
    private LinearLayout linear_right;
    private Connectlines mylines;
    private List<BaseBean> list=new ArrayList<BaseBean>();
    private List<String> mDatas=new ArrayList<String>();
    private List<String> answerData=new ArrayList<String>();
    private AnswerBean answerBean;
    private int layout_number;

    public ConnectLinesLinearLayout(Context context) {
        this(context,null);
    }

    public ConnectLinesLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConnectLinesLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.connect_lineslinear,this);
        linear_left= (LinearLayout) this.findViewById(R.id.linear_left);
        linear_right= (LinearLayout) this.findViewById(R.id.linear_right);
        mylines= (Connectlines) this.findViewById(R.id.mylines);
    }

    /**
     * 设置标准答案
     * */
    public void setAnswers(List<String> answerData){
        if (answerData!=null){
            this.answerData.addAll(answerData);
        }
    }

    public void setAnswerBean(AnswerBean answerBean){
        this.answerBean=answerBean;
    }

    public void saveAnswers(){

        if (answerBean==null){
            return;
        }

        ArrayList<ArrayList<String>> answerlist=answerBean.getConnect_classfy_answer();
        answerlist.clear();
        for (BaseBean basebean:list){
            if (basebean.getLocation().equals(LOCATION_LEFT)){
                if (basebean.isSelect()){
                    ArrayList<String> list=new ArrayList<String>();
                    list.add(basebean.getId()+"");
                    list.add(basebean.getSelect_id()+"");
                    answerlist.add(list);
                }
            }
        }
    }

    public void setDatas(final List<String> lists){
        try {
            mDatas.addAll(lists);
            int position=mDatas.size();
            setData(position/2);
        }catch (Exception e){

        }
    }

    public void setDefault(){
        ArrayList<ArrayList<String>> answerlist=answerBean.getConnect_classfy_answer();
        for (ArrayList<String> arrayList:answerlist){
            if (arrayList.size()>0){
                int id_left=Integer.parseInt(arrayList.get(0));
                int id_right=Integer.parseInt(arrayList.get(1));

                BaseBean bean=list.get(id_left);
                bean.setSelect(true);
                bean.setSelect_id(id_right);

                BaseBean bean1=list.get(id_right);
                bean1.setSelect(true);
                bean1.setSelect_id(id_left);
            }
        }
        mylines.setDatas(list);
        setMyTextviewColor();
    }

    private void setData(int position){
        for (int i=0;i<position;i++){
            BaseBean bean =new BaseBean();
            bean.setId(i);
            bean.setLocation(LOCATION_LEFT);
            ConnectTextView textView=getMyTextView(mDatas.get(i),bean);
            list.add(bean);
            linear_left.addView(textView);
        }

        for (int i=position;i<position*2;i++){
            BaseBean bean =new BaseBean();
            bean.setId(i);
            bean.setLocation(LOCATION_RIGHT);
            ConnectTextView textView=getMyTextView(mDatas.get(i),bean);
            list.add(bean);
            linear_right.addView(textView);
        }
    }

    private ConnectTextView getMyTextView(String text, BaseBean bean){
        ConnectTextView textView=new ConnectTextView(context);
        bean.setTextView(textView);
        textView.setHtmlText(text);
        textView.setOnLayoutSuccessListener(this);
        textView.setBaseBean(bean);
        textView.setCheckListener(this);
        textView.setBackgroundResource(checked_no);
        return textView;
    }

    @Override
    public void OnCheckListener(BaseBean bean) {
        cleanOnclick(bean);
        checkattachment();
        setMyTextviewColor();
        mylines.setDatas(list);
    }

    private void cleanOnclick(BaseBean bean){
        for (BaseBean basebean:list){
            if (basebean.getLocation().equals(bean.getLocation())){
                if (basebean.getId()!=bean.getId()){
                    basebean.setOnclick(false);
                }
            }
        }
    }

    private void checkattachment(){
        int id_left=getOnClickId(LOCATION_LEFT);
        int id_right=getOnClickId(LOCATION_RIGHT);
        if (id_left!=-1&&id_right!=-1){
            setSelectId(id_left,id_right);
        }
    }

    private int getOnClickId(String location){
        for (BaseBean basebean:list){
            if (basebean.getLocation().equals(location)){
                if (basebean.isOnclick()){
                    return basebean.getId();
                }
            }
        }
        return  -1;
    }

    private void setSelectId(int id_left,int id_right){
        for (BaseBean basebean:list){
            if (basebean.getLocation().equals(LOCATION_LEFT)){
                if (basebean.getId()==id_left){
                    if (basebean.isSelect()){
                        int id=basebean.getSelect_id();
                        catlines(LOCATION_RIGHT,id);
                    }
                    basebean.setOnclick(false);
                    basebean.setSelect(true);
                    basebean.setSelect_id(id_right);
                }
            }else if (basebean.getLocation().equals(LOCATION_RIGHT)){
                if (basebean.getId()==id_right){
                    if (basebean.isSelect()){
                        int id=basebean.getSelect_id();
                        catlines(LOCATION_LEFT,id);
                    }
                    basebean.setOnclick(false);
                    basebean.setSelect(true);
                    basebean.setSelect_id(id_left);
                }
            }
        }
    }

    private void catlines(String location, int id){
        for (BaseBean basebean:list){
            if (basebean.getLocation().equals(location)){
                if (basebean.getId()==id){
                    basebean.setSelect_id(-1);
                    basebean.setSelect(false);
                    basebean.setOnclick(false);
                }
            }
        }
    }

    private void setMyTextviewColor(){
        for (BaseBean bean:list){
            if (bean.isOnclick()){
                bean.getTextView().setBackgroundResource(checked_now);
                bean.getTextView().setTextColor(context.getResources().getColor(R.color.color_998e63));
            }else if (!bean.isOnclick()&&bean.isSelect()){
                bean.getTextView().setBackgroundResource(checked_old);
                bean.getTextView().setTextColor(context.getResources().getColor(R.color.color_333333));
            }else {
                bean.getTextView().setBackgroundResource(checked_no);
                bean.getTextView().setTextColor(context.getResources().getColor(R.color.color_333333));
            }
        }
    }

    @Override
    public void OnLayoutSuccessListener() {
        layout_number++;
        if (layout_number>=list.size()){
            setDefault();
        }
    }

    public class BaseBean{
        private ConnectTextView textView;
        private String location;
        private int id;
        private int y;
        private boolean select=false;
        private int select_id=-1;
        private boolean onclick=false;

        public BaseBean() {
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
