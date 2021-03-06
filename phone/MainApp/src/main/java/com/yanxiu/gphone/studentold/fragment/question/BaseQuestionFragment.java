package com.yanxiu.gphone.studentold.fragment.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.AnswerViewActivity;
import com.yanxiu.gphone.studentold.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.studentold.bean.AnswerBean;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.inter.MistakeRedoCallback;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.question.QuestionsListener;

import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.*;

/**
 * Created by Administrator on 2015/12/17.
 */
public class BaseQuestionFragment extends Fragment implements QuestionsListener ,MistakeRedoCallback{
    protected QuestionEntity questionsEntity;
    protected QuestionsListener listener;


    protected int answerViewTypyBean;
    protected int pageIndex;
    private int mPageCount;

    protected View rootView;

    private TextView tvQuestionTitleLeft, tvQuestionTitle, tvQuestionTitleRight;
    private String questionTitle;

    private ImageView ivTopIcon;

    private RelativeLayout rlTopView;

    protected boolean ischild = false;
    /**
     * 是否小题需要显示最后一个
     */
    protected boolean is_reduction = false;
    protected int childPagerIndex;
    protected int selectPagerIndex = -1;
    public InputMethodManager imm;
    private int number;
    private String parent_template;
    private int parent_type;
    private int totalCount;
    private int pageNumber;
    public int wrongId;
    private int wrongCount;
    private Object message;
    protected MistakeRedoCallback redoCallback;
    private boolean isVisible;
    protected long startTime,endTime;
    private int costTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("当前", this.getClass().getName());
        if (questionsEntity == null && getArguments() == null && savedInstanceState != null && savedInstanceState.getSerializable("questionsEntity") != null) {
            this.questionsEntity = (QuestionEntity) savedInstanceState.getSerializable("questionsEntity");
            this.answerViewTypyBean = savedInstanceState.getInt("answerViewTypyBean", 0);
            this.pageIndex = savedInstanceState.getInt("pageIndex", 0);
            this.wrongId = savedInstanceState.getInt("wrongId", 0);
            this.wrongCount = savedInstanceState.getInt("wrongCount", 0);
        } else {
            this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
            this.answerViewTypyBean = (getArguments() != null) ? getArguments().getInt("answerViewTypyBean") : null;
            this.pageIndex = (getArguments() != null) ? getArguments().getInt("pageIndex") : 0;
            this.wrongId = (getArguments() != null) ? getArguments().getInt("wrong") : 0;
            this.wrongCount = (getArguments() != null) ? getArguments().getInt("wrongCount") : 0;
        }
        if (savedInstanceState!=null){
            ischild=savedInstanceState.getBoolean("isChild");
        }
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("questionsEntity", questionsEntity);
        outState.putInt("answerViewTypyBean", answerViewTypyBean);
        outState.putInt("pageIndex", pageIndex);
        outState.putInt("wrongId", wrongId);
        outState.putInt("wrongCount", wrongCount);
        outState.putBoolean("isChild",ischild);
        super.onSaveInstanceState(outState);
    }

    public void setTagMessage(Object message) {
        this.message = message;
    }

    public Object getTagMessage() {
        return message;
    }

    public void setMistakeRedoCallback(MistakeRedoCallback redoCallback){
        this.redoCallback=redoCallback;
    }

    public ViewPager getViewPager(){
        return null;
    };

    public void setMistakeSubmit(){};

    public void setMistakeDelete(){};

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuestionTitle = (TextView) view.findViewById(R.id.tv_title_name);
        tvQuestionTitleLeft = (TextView) view.findViewById(R.id.tv_title_name_left);
        tvQuestionTitleRight = (TextView) view.findViewById(R.id.tv_title_name_right);
        rlTopView = (RelativeLayout) view.findViewById(R.id.rl_top);
        ivTopIcon = (ImageView) view.findViewById(R.id.iv_answer_top_icon);
        if (questionsEntity != null) {
            BaseQuestionFragment fragment = this;
            costTime = questionsEntity.getAnswerBean().getConsumeTime();
            int typeId = questionsEntity.getType_id();
            if (!ischild) {
                if (typeId == QUESTION_SUBJECTIVE.type) {
                    //    6
                    ivTopIcon.setImageResource(R.drawable.subjective_title_bg);
                } else if (typeId == QUESTION_SINGLE_CHOICES.type) {
                    //    1
                    ivTopIcon.setImageResource(R.drawable.choice_single_title_bg);
                } else if (typeId == QUESTION_MULTI_CHOICES.type) {
                    //    2
                    ivTopIcon.setImageResource(R.drawable.choice_multi_title_bg);
                } else if (typeId == QUESTION_JUDGE.type) {
                    //    4
                    ivTopIcon.setImageResource(R.drawable.judge_title_bg);
                } else if (typeId == QUESTION_FILL_BLANKS.type) {
                    //    3
                    ivTopIcon.setImageResource(R.drawable.fill_blanks_bg);
                } else if (typeId == QUESTION_READING.type) {
                    //    5
                    ivTopIcon.setImageResource(R.drawable.reading_title_bg);
                } else if (typeId == QUESTION_READ_COMPLEX.type) {
                    //    14
                    ivTopIcon.setImageResource(R.drawable.read_complex_title_bg);
                } else if (typeId == QUESTION_SOLVE_COMPLEX.type) {
                    //    22
                    ivTopIcon.setImageResource(R.drawable.solve_complex_title_bg);
                } else if (typeId == QUESTION_CLOZE_COMPLEX.type) {
                    //    15
                    ivTopIcon.setImageResource(R.drawable.gestalt_complex_title_bg);
                } else if (typeId == QUESTION_CLASSFY.type) {
                    //    13
                    ivTopIcon.setImageResource(R.drawable.classfy_title_bg);
                } else if ((9 <= typeId && typeId <= 12) || typeId == 18 || typeId == 19 || typeId == 21) {
                    ivTopIcon.setImageResource(R.drawable.listen_complex_title_bg);
                } else if (typeId == QUESTION_CONNECT.type) {
                    //    7
                    ivTopIcon.setImageResource(R.drawable.attachment_title_bg);
                } else if (typeId == QUESTION_COMPUTE.type) {
                    //    8
                    ivTopIcon.setImageResource(R.drawable.calculate_title_bg);
                } else if (typeId == 16) {
                    ivTopIcon.setImageResource(R.drawable.translation_title_bg);
                } else if (typeId == 17) {
                    ivTopIcon.setImageResource(R.drawable.subjects_title_bg);
                } else if (typeId == 20) {
                    ivTopIcon.setImageResource(R.drawable.sorting_title_bg);
                }
            }
            /*if (questionsEntity.isReadQuestion()) {
                rlTopView.setVisibility(View.GONE);
            } else {
                questionTitle = questionsEntity.getTitleName();
                if (!TextUtils.isEmpty(questionTitle)) {
                    tvQuestionTitle.setText(questionTitle);
                }
            }*/
            if (answerViewTypyBean==SubjectExercisesItemBean.WRONG_SET||answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO){
                setWrongQuestionTitle(typeId);
                if (!IsReady) {
                    setWrongQuestionTitle(wrongId + "", wrongCount + "");
                }
            } else {
                setQuestionTitle(typeId);
            }
        }
    }

    protected void setCurrent(ViewPager pager) {
        try {
            boolean isShowing = ((BaseAnswerViewActivity) getActivity()).getCurrent(this.hashCode());
            if (!isShowing) {
                pager.setCurrentItem(0);
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    private void setQuestionTitle(int typeId) {
        if (ischild) {
            if (parent_template.equals(YanXiuConstant.MULTI_QUESTION) && (parent_type == QUESTION_SOLVE_COMPLEX.type || parent_type == QUESTION_COMPUTE.type)) {
                tvQuestionTitleLeft.setText("" + (pageIndex + 1));
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + number);
            } else {
                tvQuestionTitleLeft.setText("" + (questionsEntity.getChildPageNumber() + pageIndex));
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + totalCount);
            }
        } else {
            if (questionsEntity.getTemplate().equals(YanXiuConstant.MULTI_QUESTION) && (typeId == QUESTION_SOLVE_COMPLEX.type || typeId == QUESTION_COMPUTE.type)) {
                tvQuestionTitleLeft.setText("" + pageIndex);
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + count);
            } else if (questionsEntity.getTemplate().equals(YanXiuConstant.SINGLE_CHOICES) || questionsEntity.getTemplate().equals(YanXiuConstant.MULTI_CHOICES)
                    || questionsEntity.getTemplate().equals(YanXiuConstant.FILL_BLANK) || questionsEntity.getTemplate().equals(YanXiuConstant.ANSWER_QUESTION)
                    || questionsEntity.getTemplate().equals(YanXiuConstant.CLASSIFY_QUESTION) || questionsEntity.getTemplate().equals(YanXiuConstant.JUDGE_QUESTION)
                    || questionsEntity.getTemplate().equals(YanXiuConstant.CONNECT_QUESTION)) {
                tvQuestionTitleLeft.setText("" + pageIndex);
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + count);
            }
        }
    }

    private static boolean IsFirst=true;

    private void setWrongQuestionTitle(int typeId) {
        if (!ischild) {
            if (IsFirst) {
                tvQuestionTitleLeft.setText("" + 1);
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + wrongCount);
                IsFirst = false;
            } else {
                tvQuestionTitleLeft.setText("" + wrongId);
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + wrongCount);

            }
        }
    }

    private boolean IsReady = true;

    public void setWrongQuestionTitle(String wrongId, String wrongCount) {
        if (!ischild) {
            this.wrongId = Integer.parseInt(wrongId);
            this.wrongCount = Integer.parseInt(wrongCount);
            if (tvQuestionTitle != null && tvQuestionTitleLeft != null && tvQuestionTitleRight != null) {
                tvQuestionTitleLeft.setText("" + wrongId);
                tvQuestionTitle.setVisibility(View.VISIBLE);
                tvQuestionTitleRight.setText("" + wrongCount);
                IsReady = true;
            } else {
                IsReady = false;
            }
        }
    }

    public void setChildPagerIndex(int childPagerIndex) {
        this.childPagerIndex = childPagerIndex;
        this.selectPagerIndex = childPagerIndex;
    }

    public void setRefresh() {
        String ss = "";
        ss = "";
    }

    int count;

    public void setTotalCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return count;
    }

    public Fragment getChildFragment() {
        return null;
    }

    public int getChildCount() {
        return 1;
    }

    public void setIsChild(boolean ischild, int number, String parent_template, int parent_type, int totalCount) {
        this.ischild = ischild;
        this.number = number;
        this.parent_template = parent_template;
        this.parent_type = parent_type;
        this.totalCount = totalCount;
    }


    public void saveAnwser() {

    }

    public void calculateAndSetCostTime(){
        if(questionsEntity !=null && answerViewTypyBean == SubjectExercisesItemBean.ANSWER_QUESTION){
            endTime = System.currentTimeMillis();
            long millSeconds = endTime - startTime;
            int seconds = (int) Math.ceil(millSeconds / 1000.00);
            costTime += seconds;
            questionsEntity.getAnswerBean().setConsumeTime(costTime);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            startTime = System.currentTimeMillis();
            if (YanXiuConstant.OnClick_TYPE == 0) {
                is_reduction = false;
            } else if (YanXiuConstant.OnClick_TYPE == 1) {
                YanXiuConstant.OnClick_TYPE = 0;
                is_reduction = true;
            }
        }else  {
            if(isResumed()){
                calculateAndSetCostTime();
            }
        }
        hideSoftInput();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible){
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isVisible){
            calculateAndSetCostTime();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void hideSoftInput() {
        if (imm != null && getActivity() instanceof AnswerViewActivity) {
            ((AnswerViewActivity) getActivity()).getTvToptext().setFocusable(true);
            ((AnswerViewActivity) getActivity()).getTvToptext().setFocusableInTouchMode(true);
            imm.hideSoftInputFromWindow(((AnswerViewActivity) getActivity()).getTvToptext().getWindowToken(), 0);
            ((AnswerViewActivity) getActivity()).getTvToptext().requestFocus();
        }
    }


    @Override
    public void flipNextPager(QuestionsListener listener) {

    }

    @Override
    public void setDataSources(AnswerBean bean) {

    }

    @Override
    public void initViewWithData(AnswerBean bean) {

    }

    @Override
    public void answerViewClick() {

    }

    @Override
    public void redoCallback() {

    }
}
