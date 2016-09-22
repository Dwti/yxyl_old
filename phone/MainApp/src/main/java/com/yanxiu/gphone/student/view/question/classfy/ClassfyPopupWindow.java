package com.yanxiu.gphone.student.view.question.classfy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.view.UnMoveGridView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ClassfyAnswerAdapter;
import com.yanxiu.gphone.student.bean.ClassfyBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import java.util.ArrayList;
import java.util.List;

import static com.yanxiu.gphone.student.view.question.classfy.ClassfyAnswers.fromHtml;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ClassfyPopupWindow extends BasePopupWindow  {
    private QuestionEntity mQuestionsEntity;
    private TextView classfyDelPopText;
    private String classfyDelPopString;
    private int position;
    private int mNum;
    private ClassfyAnswers vgClassfyAnswers;
    private UnMoveGridView lgClassfyAnswers;
    private LinearLayout ll_screen;

    private List<ClassfyBean> classfyPopItem = new ArrayList<ClassfyBean>();
    private int mViewType;

    private ClassfyAnswerAdapter classfyAnswerPopupAdapter;
    public ClassfyPopupWindow(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        View view=View.inflate(mContext, R.layout.classfy_del_pop_view,null);
        //this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);

        ll_screen = (LinearLayout)view.findViewById(R.id.ll_screen);
        ll_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        classfyDelPopText = (TextView)view.findViewById(R.id.classfyDelPopText);
        vgClassfyAnswers = (ClassfyAnswers) view.findViewById(R.id.classfy_text_item);
        lgClassfyAnswers = (UnMoveGridView) view.findViewById(R.id.classfy_icon_item);
        lgClassfyAnswers.setSelector(new ColorDrawable(Color.TRANSPARENT));
        classfyAnswerPopupAdapter = new ClassfyAnswerAdapter((Activity)mContext);
        lgClassfyAnswers.setAdapter(classfyAnswerPopupAdapter);
        loadingData();
    }

    public void init(QuestionEntity questionsEntity, String str, int num, int i){
        mQuestionsEntity = questionsEntity;
        classfyDelPopString = str;
        mNum = num;
        position = i;
        classfyPopItem.clear();
        for (String string: mQuestionsEntity.getAnswerBean().getConnect_classfy_answer().get(position)) {
            ClassfyBean classfyBean = new ClassfyBean(Integer.parseInt(string), mQuestionsEntity.getContent().getChoices().get(Integer.parseInt(string)));
            classfyPopItem.add(classfyBean);
        }
        this.initView(mContext);
    }

    @Override
    public void loadingData() {
        classfyDelPopText.setText(classfyDelPopString+" ("+mNum+")");
        if (mQuestionsEntity != null && mQuestionsEntity.getStem() != null) {
            if (mQuestionsEntity.getContent() != null && mQuestionsEntity.getContent().getChoices() != null
                    && mQuestionsEntity.getContent().getChoices().size() > 0) {
                if (mQuestionsEntity.getContent().getChoices().get(0).contains(YanXiuConstant.IMG_SRC)) {
                    classfyAnswerPopupAdapter.setData(classfyPopItem);
                    lgClassfyAnswers.setVisibility(View.VISIBLE);
                    vgClassfyAnswers.setVisibility(View.GONE);
                } else {
                    for (int i=0; i<classfyPopItem.size(); i++) {
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        final View containerView = inflater.inflate(R.layout.classfy_layout_textview, null);
                        TextView classfy_answer_popup_text = (TextView) containerView.findViewById(R.id.classfy_answer_popup_text);
                        Spanned spanned=ClassfyAnswers.fromHtml(mContext,mQuestionsEntity.getContent().getChoices().get(i));
                        classfy_answer_popup_text.setText(spanned);
                        //view.setText(mQuestionsEntity.getContent().getChoices().get(i));
                        containerView.getLayoutParams();
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 24, 24);
                        containerView.setLayoutParams(lp);
                        containerView.setTag(classfyPopItem.get(i));
                        vgClassfyAnswers.addView(containerView);
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
