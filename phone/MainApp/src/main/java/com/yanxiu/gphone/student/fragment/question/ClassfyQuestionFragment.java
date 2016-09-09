package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.core.view.LineGridView;
import com.common.core.view.UnMoveGridView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ClassfyAnswerAdapter;
import com.yanxiu.gphone.student.adapter.ClassfyQuestionAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ClassfyBean;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyAnswers;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyDelPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yangjj on 2016/8/30.
 */
public class ClassfyQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;

    public int typeId;

    private YXiuAnserTextView tvYanxiu;
    private UnMoveGridView gvClassfyQuestion;
    private ClassfyAnswers vgClassfyAnswers;
    private UnMoveGridView lgClassfyAnswers;

    private ClassfyQuestionAdapter classfyQuestionAdapter;
    private ClassfyAnswerAdapter classfyAnswerAdapter;
    private ClassfyDelPopupWindow classfyPopupWindow;

    private boolean isVisibleToUser;
    private Fragment resolutionFragment;

    private String choiceTmpString;
    private int position;
    private View mRemoveView;

    private List<ClassfyBean> classfyItem = new ArrayList<ClassfyBean>();
    //private List<ArrayList<ClassfyBean>> pointItem = new ArrayList<ArrayList<ClassfyBean>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_classfy_question, null);
        }
        initView();
        initData();
        return rootView;
    }

    private void initView() {
        tvYanxiu = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        gvClassfyQuestion = (UnMoveGridView) rootView.findViewById(R.id.classfy_question_item);
        classfyQuestionAdapter = new ClassfyQuestionAdapter(getActivity());
        gvClassfyQuestion.setAdapter(classfyQuestionAdapter);
        vgClassfyAnswers = (ClassfyAnswers) rootView.findViewById(R.id.classfy_text_item);
        lgClassfyAnswers = (UnMoveGridView) rootView.findViewById(R.id.classfy_icon_item);
        lgClassfyAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choiceTmpString = String.valueOf(classfyItem.get(i).getId());
            }
        });
        classfyAnswerAdapter = new ClassfyAnswerAdapter(getActivity());
        lgClassfyAnswers.setAdapter(classfyAnswerAdapter);
    }

    private void initData() {
        if (questionsEntity.getContent() != null && questionsEntity.getContent().getChoices() != null
                && questionsEntity.getContent().getChoices().size() > 0) {
            classfyItem.clear();
            for (int i = 0; i < questionsEntity.getContent().getChoices().size(); i++) {
                ClassfyBean classfyBean = new ClassfyBean(i, questionsEntity.getContent().getChoices().get(i));
                classfyItem.add(classfyBean);
            }
        }
        //questionsEntity.getAnswerBean().getConnect_classfy_answer().clear();
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            tvYanxiu.setTextHtml(questionsEntity.getStem());
            ArrayList<ArrayList<String>> answerList = questionsEntity.getAnswerBean().getConnect_classfy_answer();
            for (int i=0; i<answerList.size(); i++) {
                ArrayList<String> answerListStr = answerList.get(i);
                for (int j=0; j<answerListStr.size(); j++) {
                    Iterator<ClassfyBean> classfyBean = classfyItem.iterator();
                    while (classfyBean.hasNext()) {
                        if (Integer.parseInt(answerListStr.get(j)) == classfyBean.next().getId()){
                            classfyBean.remove();
                        }
                    }
                    //classfyItem.remove(Integer.parseInt(answerListStr.get(j)));
                }
            }
            if (questionsEntity.getPoint() != null) {
                if (questionsEntity.getAnswerBean().getConnect_classfy_answer().size() == 0){
                    for (int j=0; j<questionsEntity.getPoint().size(); j++) {
                        ArrayList<String> list = new ArrayList<String>();
                        questionsEntity.getAnswerBean().getConnect_classfy_answer().add(list);
                    }
                }
            }
            classfyQuestionAdapter.setData(questionsEntity);
            gvClassfyQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (StringUtils.isEmpty(choiceTmpString)) {
                        classfyPopupWindow = new ClassfyDelPopupWindow(getActivity());
                        classfyPopupWindow.init(questionsEntity, questionsEntity.getPoint().get(i).getName(), i);
                        classfyPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    } else {
                        if (classfyItem.get(0).getName().contains(YanXiuConstant.IMG_SRC+"TT")) {
                            questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).add(choiceTmpString);
                            classfyItem.remove(position);
                            classfyAnswerAdapter.setData(classfyItem);
                            classfyQuestionAdapter.setData(questionsEntity);
                            choiceTmpString = null;
                        } else {
                            questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).add(choiceTmpString);
                            classfyItem.remove(position);
                            classfyQuestionAdapter.setData(questionsEntity);
                            vgClassfyAnswers.removeView(mRemoveView);
                            choiceTmpString = null;
                        }
                    }
                }
            });
        }
        if (classfyItem.size() > 0) {

            if (classfyItem.get(0).getName().contains(YanXiuConstant.IMG_SRC+"TT")) {
                classfyAnswerAdapter.setData(classfyItem);
                lgClassfyAnswers.setVisibility(View.VISIBLE);
                vgClassfyAnswers.setVisibility(View.GONE);
            } else {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                for (int i=0; i<classfyItem.size(); i++) {
                    final TextView view = (TextView) inflater.inflate(R.layout.layout_textview, null);
                    view.setText(classfyItem.get(i).getName().substring(5, 20+2*i));
                    //view.setText(classfyItem.get(i).getName());
                    view.getLayoutParams();
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(8, 8, 8, 8);
                    view.setLayoutParams(lp);
                    final int finalInt = i;
                    view.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    choiceTmpString = String.valueOf(classfyItem.get(finalInt).getId());
                                                    position = finalInt;
                                                    mRemoveView = view;
                                                }
                                            });
                    vgClassfyAnswers.addView(view);
                }
                lgClassfyAnswers.setVisibility(View.GONE);
                vgClassfyAnswers.setVisibility(View.VISIBLE);
            }

        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if (isVisibleToUser&&!ischild){
//            if (adapter!=null){
            ((QuestionsListener)getActivity()).flipNextPager(null);
//            }
        }
    }

    private void addAnalysisFragment(){
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogInfo.log("geny", "---onResume-------pageIndex----" + pageIndex);
        if(bean == null){
            bean = questionsEntity.getAnswerBean();
        }
        if (!ischild&&isVisibleToUser) {
            ((QuestionsListener) getActivity()).flipNextPager(null);
        }
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {

    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
