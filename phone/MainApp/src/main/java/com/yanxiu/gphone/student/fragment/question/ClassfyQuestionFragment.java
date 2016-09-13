package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyAnswers;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyDelPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yangjj on 2016/8/30.
 */
public class ClassfyQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex, BasePopupWindow.OnDissmissListener, View.OnClickListener {
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
    private ClassfyBean mRemoveBean;

    private List<ClassfyBean> classfyItem = new ArrayList<ClassfyBean>();
    //private List<ArrayList<ClassfyBean>> pointItem = new ArrayList<ArrayList<ClassfyBean>>();

    private List<String> answerData = new ArrayList<String>();
    private AnswerBean answerBean;
    private boolean isResolution;
    private boolean isClick=true;
    private boolean isWrongSet;
    private Button addBtn;

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
                classfyAnswerAdapter.setSeclection(i);
                classfyAnswerAdapter.notifyDataSetChanged();
            }
        });
        classfyAnswerAdapter = new ClassfyAnswerAdapter(getActivity());
        lgClassfyAnswers.setAdapter(classfyAnswerAdapter);
    }

    private void initData() {
        createClassItem();
        questionsEntity.getAnswerBean().getConnect_classfy_answer().clear();
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            tvYanxiu.setTextHtml(questionsEntity.getStem());
            filterClassItem();
            if (questionsEntity.getAnswer() != null) {
                if (questionsEntity.getAnswerBean().getConnect_classfy_answer().size() == 0){
                    for (int j=0; j<questionsEntity.getAnswer().size(); j++) {
                        ArrayList<String> list = new ArrayList<String>();
                        questionsEntity.getAnswerBean().getConnect_classfy_answer().add(list);
                    }
                }
            }
            if (answerData != null) {
                answerData.addAll(questionsEntity.getAnswer());
            }
            answerBean = questionsEntity.getAnswerBean();
            classfyQuestionAdapter.setData(questionsEntity);
            gvClassfyQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (StringUtils.isEmpty(choiceTmpString)) {
                        classfyPopupWindow = new ClassfyDelPopupWindow(getActivity());
                        JSONObject object = null;
                        String string = null;
                        try {
                            object = new JSONObject(questionsEntity.getAnswer().get(position));
                            string = object.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        classfyPopupWindow.init(questionsEntity, string, questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).size(), i);
                        classfyPopupWindow.setOnDissmissListener(ClassfyQuestionFragment.this);
                        classfyPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    } else {
                        if (classfyItem.get(0).getName().contains(YanXiuConstant.IMG_SRC)) {
                            questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).add(choiceTmpString);
                            classfyItem.remove(position);
                            classfyAnswerAdapter.setData(classfyItem);
                            classfyQuestionAdapter.setData(questionsEntity);
                            choiceTmpString = null;
                        } else {
                            questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).add(choiceTmpString);
                            classfyItem.remove(mRemoveBean);
                            classfyQuestionAdapter.setData(questionsEntity);
                            vgClassfyAnswers.removeView(mRemoveView);
                            choiceTmpString = null;
                        }
                    }
                }
            });
        }
        if (questionsEntity.getContent().getChoices().get(0).contains(YanXiuConstant.IMG_SRC)) {
            classfyAnswerAdapter.setData(classfyItem);
            lgClassfyAnswers.setVisibility(View.VISIBLE);
            vgClassfyAnswers.setVisibility(View.GONE);
        } else {
            vgClassfyAnswers.setData(classfyItem, ClassfyQuestionFragment.this);
            lgClassfyAnswers.setVisibility(View.GONE);
            vgClassfyAnswers.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if (!isVisibleToUser) {
            saveAnswers();
        }
        if (isVisibleToUser&&!ischild){
//            if (adapter!=null){
            ((QuestionsListener)getActivity()).flipNextPager(null);
//            }
        }
    }

    private void selectTypeView(){
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
                setIsResolution(true);
                setIsClick(false);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                setIsWrongSet(true);
                setIsClick(false);
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        addAnalysisFragment();
                    }
                });
                break;
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

    /**
     * 是否是解析界面
     * @param isResolution
     */
    public void setIsResolution(boolean isResolution) {
        this.isResolution = isResolution;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    public void setIsWrongSet(boolean isWrongSet) {
        this.isWrongSet = isWrongSet;
    }

    public void saveAnswers() {

        if (answerBean == null) {
            return;
        }

        ArrayList<ArrayList<String>> answerlist = answerBean.getConnect_classfy_answer();
        int size = 0;
        for (ArrayList<String> arrayList:answerlist) {
            size = size + arrayList.size();
        }
        if (size < questionsEntity.getContent().getChoices().size()) {
            answerBean.setIsFinish(false);
            answerBean.setIsRight(false);
        } else {
            answerBean.setIsFinish(true);
            getIsRight(answerlist);
        }
    }

    private void getIsRight(ArrayList<ArrayList<String>> answerlist) {
        for (int i = 0; i<answerData.size(); i++) {
            JSONObject object = null;
            String string = null;
            try {
                object = new JSONObject(answerData.get(i));
                string = object.getString("answer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String ss[] = string.split(",");
            if (ss.length != answerlist.get(i).size()) {
                answerBean.setIsRight(false);
            } else {
                for (int j = 0; j < ss.length; j++) {
                    if (!answerlist.get(i).get(j).equals(ss[j])) {
                        answerBean.setIsRight(false);
                    }
                }
            }
        }

        answerBean.setIsRight(true);
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
        if(bean != null) {
            saveAnswers();
        }
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void onDismiss() {
        classfyQuestionAdapter.notifyDataSetChanged();
        if (questionsEntity.getContent().getChoices().get(0).contains(YanXiuConstant.IMG_SRC)) {
            createClassItem();
            filterClassItem();
            classfyAnswerAdapter.notifyDataSetChanged();
        } else {
            createClassItem();
            filterClassItem();
            vgClassfyAnswers.removeAllViews();
            vgClassfyAnswers.setData(classfyItem, ClassfyQuestionFragment.this);
        }
    }

    @Override
    public void onClick(View view) {
        choiceTmpString = String.valueOf(((ClassfyBean)view.getTag()).getId());
        mRemoveBean = (ClassfyBean)view.getTag();

        mRemoveView = view;
    }

    private void createClassItem() {
        if (questionsEntity.getContent() != null && questionsEntity.getContent().getChoices() != null
                && questionsEntity.getContent().getChoices().size() > 0) {
            classfyItem.clear();
            for (int i = 0; i < questionsEntity.getContent().getChoices().size(); i++) {
                ClassfyBean classfyBean = new ClassfyBean(i, questionsEntity.getContent().getChoices().get(i));
                classfyItem.add(classfyBean);
            }
        }
    }

    private void filterClassItem() {
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
    }
}
