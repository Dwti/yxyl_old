package com.yanxiu.gphone.student.view.question.classfy;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.view.UnMoveGridView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ClassfyAnswerAdapter;
import com.yanxiu.gphone.student.adapter.ClassfyAnswerPopupAdapter;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ClassfyDelPopupWindow extends BasePopupWindow {
    private QuestionEntity mQuestionsEntity;
    private TextView classfyDelPopText;
    private String classfyDelPopString;
    private ClassfyAnswers vgClassfyAnswers;
    private UnMoveGridView lgClassfyAnswers;

    private ClassfyAnswerPopupAdapter classfyAnswerPopupAdapter;
    public ClassfyDelPopupWindow(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        View view=View.inflate(mContext, R.layout.classfy_del_pop_view,null);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);

        classfyDelPopText = (TextView)view.findViewById(R.id.classfyDelPopText);
        vgClassfyAnswers = (ClassfyAnswers) view.findViewById(R.id.classfy_text_item);
        lgClassfyAnswers = (UnMoveGridView) view.findViewById(R.id.classfy_icon_item);
        classfyAnswerPopupAdapter = new ClassfyAnswerPopupAdapter(mContext);
        lgClassfyAnswers.setAdapter(classfyAnswerPopupAdapter);
        loadingData();
    }

    public void init(QuestionEntity questionsEntity, String str){
        mQuestionsEntity = questionsEntity;
        classfyDelPopString = str;
        this.initView(mContext);
    }

    @Override
    public void loadingData() {
        classfyDelPopText.setText(classfyDelPopString);
        if (mQuestionsEntity != null && mQuestionsEntity.getStem() != null) {
            if (mQuestionsEntity.getContent() != null && mQuestionsEntity.getContent().getChoices() != null
                    && mQuestionsEntity.getContent().getChoices().size() > 0) {
                if (mQuestionsEntity.getContent().getChoices().get(0).contains(YanXiuConstant.IMG_SRC+"UU")) {
                    classfyAnswerPopupAdapter.setData(mQuestionsEntity.getContent().getChoices());
                    lgClassfyAnswers.setVisibility(View.VISIBLE);
                    vgClassfyAnswers.setVisibility(View.GONE);
                } else {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    for (int i=0; i<mQuestionsEntity.getContent().getChoices().size(); i++) {
                        TextView view = (TextView) inflater.inflate(R.layout.layout_textview, null);
                        view.setText(mQuestionsEntity.getContent().getChoices().get(i).substring(5, 20+2*i));
                        //view.setText(mQuestionsEntity.getContent().getChoices().get(i));
                        view.getLayoutParams();
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(8, 8, 8, 8);
                        view.setLayoutParams(lp);
                        final ImageView removeButton = new ImageView(mContext);
                        RelativeLayout.LayoutParams exitParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        removeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.classfy_del_icon));
                        exitParams.addRule(RelativeLayout.ALIGN_RIGHT,R.drawable.classfy_del_icon);
                        exitParams.addRule(RelativeLayout.ALIGN_TOP,R.drawable.classfy_del_icon);
                        vgClassfyAnswers.addView(removeButton, exitParams);
                        vgClassfyAnswers.addView(view);
                    }
                    lgClassfyAnswers.setVisibility(View.GONE);
                    vgClassfyAnswers.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View view) {

    }
}
