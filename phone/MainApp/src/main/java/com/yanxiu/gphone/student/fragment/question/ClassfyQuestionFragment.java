package com.yanxiu.gphone.student.fragment.question;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.core.view.AllGridView;
import com.common.core.view.UnMoveGridView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.adapter.ClassfyAnswerAdapter;
import com.yanxiu.gphone.student.adapter.ClassfyQuestionAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ClassfyBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyAnswers;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyDelPopupWindow;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yangjj on 2016/8/30.
 */
public class ClassfyQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex, BasePopupWindow.OnDissmissListener, View.OnClickListener, ClassfyQuestionAdapter.updatesuccesslistener {
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;

    public int typeId;

    private YXiuAnserTextView tvYanxiu;
    private AllGridView gvClassfyQuestion;
    private ClassfyAnswers vgClassfyAnswers;
    private View view_line_ccc4a3_2;
    private UnMoveGridView lgClassfyAnswers;

    private ClassfyQuestionAdapter classfyQuestionAdapter;
    private ClassfyAnswerAdapter classfyAnswerAdapter;

    private boolean isVisibleToUser;
    private Fragment resolutionFragment;

    private String choiceTmpString;
    private int position;
    private View mRemoveView;
    private ClassfyBean mRemoveBean;

    private ClassfyPopupWindow classfyPopupWindow;
    private ClassfyDelPopupWindow classfyDelPopupWindow;

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
//        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_classfy_question, null);
//        }
        initView();
        initData();

        return rootView;
    }

    private void initView() {
        tvYanxiu = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        gvClassfyQuestion = (AllGridView) rootView.findViewById(R.id.classfy_question_item);
        gvClassfyQuestion.setSelector(new ColorDrawable(Color.TRANSPARENT));
        classfyQuestionAdapter = new ClassfyQuestionAdapter(gvClassfyQuestion, getActivity());
        classfyQuestionAdapter.setlistener(this);
        gvClassfyQuestion.setAdapter(classfyQuestionAdapter);
        view_line_ccc4a3_2 = (View) rootView.findViewById(R.id.view_line_ccc4a3_2);
        vgClassfyAnswers = (ClassfyAnswers) rootView.findViewById(R.id.classfy_text_item);
        lgClassfyAnswers = (UnMoveGridView) rootView.findViewById(R.id.classfy_icon_item);
        lgClassfyAnswers.setSelector(new ColorDrawable(Color.TRANSPARENT));
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if(ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (answerViewTypyBean != SubjectExercisesItemBean.RESOLUTION && answerViewTypyBean != SubjectExercisesItemBean.WRONG_SET) {
            if (answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO){
                if ("0".equals(questionsEntity.getType())){
                    lgClassfyAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            if (mRemoveBean == classfyItem.get(i)) {
                                classfyAnswerAdapter.setSeclection(-1);
                                mRemoveBean = null;
                                choiceTmpString = "";
                            } else {
                                classfyAnswerAdapter.setSeclection(i);
                                mRemoveBean = classfyItem.get(i);
                                choiceTmpString = String.valueOf(classfyItem.get(i).getId());
                            }
                            classfyAnswerAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }else {
                lgClassfyAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (mRemoveBean == classfyItem.get(i)) {
                            classfyAnswerAdapter.setSeclection(-1);
                            mRemoveBean = null;
                            choiceTmpString = "";
                        } else {
                            classfyAnswerAdapter.setSeclection(i);
                            mRemoveBean = classfyItem.get(i);
                            choiceTmpString = String.valueOf(classfyItem.get(i).getId());
                        }
                        classfyAnswerAdapter.notifyDataSetChanged();
                    }
                });
            }

        }
        classfyAnswerAdapter = new ClassfyAnswerAdapter(getActivity());
        lgClassfyAnswers.setAdapter(classfyAnswerAdapter);
        classfyPopupWindow = new ClassfyPopupWindow(getActivity());
        classfyPopupWindow.setOnDissmissListener(ClassfyQuestionFragment.this);
        classfyDelPopupWindow = new ClassfyDelPopupWindow(getActivity());
        if (answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO) {
            classfyDelPopupWindow.setCallBack(callBack);
        }
        classfyDelPopupWindow.setOnDissmissListener(ClassfyQuestionFragment.this);
    }

    private void initData() {
        createClassItem();

        //questionsEntity.getAnswerBean().getConnect_classfy_answer().clear();
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            tvYanxiu.setTextHtml(questionsEntity.getStem());
            filterClassItem();
            if (answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION || answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET) {
                if (classfyItem.size() == 0) {
                    view_line_ccc4a3_2.setVisibility(View.GONE);
                }
            }
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
            //if (answerViewTypyBean != SubjectExercisesItemBean.RESOLUTION && answerViewTypyBean != SubjectExercisesItemBean.WRONG_SET) {
                gvClassfyQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (StringUtils.isEmpty(choiceTmpString)) {
                            JSONObject object = null;
                            String string = null;
                            try {
                                object = new JSONObject(questionsEntity.getAnswer().get(i));
                                string = object.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).size() > 0) {
                                backgroundAlpha(0.5f);
                                if (answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION || answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET||(answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO&&!"0".equals(questionsEntity.getType()))) {
                                    classfyPopupWindow.init(questionsEntity, string, questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).size(), i);
                                    classfyPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                                } else {
                                    classfyDelPopupWindow.init(questionsEntity, string, questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).size(), i);
                                    classfyDelPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                                }
                            }

                        } else {
                            if (classfyItem.get(0).getName().contains(YanXiuConstant.IMG_SRC)) {
                                questionsEntity.getAnswerBean().getConnect_classfy_answer().get(i).add(choiceTmpString);
                                classfyItem.remove(mRemoveBean);
                                classfyAnswerAdapter.setSeclection(-1);
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

                            if (callBack!=null){
                                callBack.callback();
                            }
                        }
                    }
                });
            //}
        }

//        gvClassfyQuestion.post(new Runnable() {
//            @Override
//            public void run() {
//                if (questionsEntity.getContent().getChoices().get(0).contains(YanXiuConstant.IMG_SRC)) {
//                    classfyAnswerAdapter.setData(classfyItem);
//                    lgClassfyAnswers.setVisibility(View.VISIBLE);
//                    vgClassfyAnswers.setVisibility(View.GONE);
//                } else {
//                    if (answerViewTypyBean != SubjectExercisesItemBean.RESOLUTION && answerViewTypyBean != SubjectExercisesItemBean.WRONG_SET) {
//                        vgClassfyAnswers.setData(classfyItem, ClassfyQuestionFragment.this);
//                    } else {
//                        vgClassfyAnswers.setData(classfyItem, null);
//                    }
//                    lgClassfyAnswers.setVisibility(View.GONE);
//                    vgClassfyAnswers.setVisibility(View.VISIBLE);
//                }
//            }
//        });






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
            try {
                ((QuestionsListener)getActivity()).flipNextPager(null);
            }catch (Exception e){}
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
            case SubjectExercisesItemBean.MISTAKEREDO:
                if (ischild){
                    if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())||QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
                        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                        setIsResolution(true);
                        setIsClick(false);
                        vgClassfyAnswers.clearFocuse();
                        lgClassfyAnswers.setOnItemClickListener(null);
                        FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                    }
                    return;
                }
                FrameLayout layout= (FrameLayout) rootView.findViewById(R.id.fra_sub_or_del);
                layout.setVisibility(View.VISIBLE);
                final SubmitOrDeleteFragment fragment = new SubmitOrDeleteFragment();
                initSubOrDel(fragment);
                fragment.setEntity(questionsEntity);
                fragment.setListener(new SubmitOrDeleteFragment.OnButtonClickListener() {
                    @Override
                    public void onClick(String type) {
                        switch (type) {
                            case SubmitOrDeleteFragment.TYPE_SUBMIT:
                                questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                                setIsResolution(true);
                                setIsClick(false);
                                vgClassfyAnswers.clearFocuse();
                                lgClassfyAnswers.setOnItemClickListener(null);
                                checkTheAnswer();
                                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
                                FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                                break;
                            case SubmitOrDeleteFragment.TYPE_DELETE:
                                questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
                                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
                                break;
                        }
                    }
                });
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fra_sub_or_del, fragment,"sub_or_del");
                transaction.show(fragment);
                transaction.commit();
                break;
        }
    }

    @Override
    public void setMistakeSubmit() {
        super.setMistakeSubmit();
        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
        setIsResolution(true);
        setIsClick(false);
        vgClassfyAnswers.clearFocuse();
        lgClassfyAnswers.setOnItemClickListener(null);
        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
        FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
    }

    @Override
    public void setMistakeDelete() {
        super.setMistakeDelete();
        questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
    }

    private SetAnswerCallBack callBack=new SetAnswerCallBack() {
        @Override
        public void callback() {
            if (ischild){
                boolean f1=getIsHavaAnswer();
                boolean f2=getTheAnswerIsRight();
                if (redoCallback!=null){
                    redoCallback.redoCallback();
                }
                return;
            }
            Fragment fragment=getChildFragmentManager().findFragmentByTag("sub_or_del");
            if (fragment==null){
                return;
            }
            SubmitOrDeleteFragment submitOrDeleteFragment= (SubmitOrDeleteFragment) fragment;
            initSubOrDel(submitOrDeleteFragment);
        }
    };

    private boolean getIsHavaAnswer(){
        ArrayList<ArrayList<String>> list=questionsEntity.getAnswerBean().getConnect_classfy_answer();
        boolean flag = false;
        if (list!=null&&list.size()==answerData.size()) {
            int answer_count=0;
            int data_count=0;
            for (int i = 0; i < list.size(); i++) {
                answer_count+=list.get(i).size();
                JSONObject object = null;
                String string = null;
                try {
                    object = new JSONObject(answerData.get(i));
                    string = object.getString("answer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String ss[] = string.split(",");
                data_count+=ss.length;
//                ArrayList<String> arrayList = list.get(i);
//                if (arrayList != null && arrayList.size() > 0&&arrayList.size()==ss.length) {
//                    flag = true;
//                }else {
//                    flag=false;
//                    questionsEntity.setHaveAnser(flag);
//                    return flag;
//                }
            }
            if (answer_count==data_count){
                flag = true;
            }else {
                flag=false;
            }
        }
        questionsEntity.setIsAllBlanksFilled(flag);
        return flag;
    }

    private void initSubOrDel(SubmitOrDeleteFragment fragment) {
        if (QuestionEntity.TYPE_SUBMIT.equals(questionsEntity.getType())) {
            if (getIsHavaAnswer()){
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_HASANSWER);
            }else {
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_NOANSWER);
            }
        }else if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())){
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            setIsResolution(true);
            setIsClick(false);
            vgClassfyAnswers.clearFocuse();
            lgClassfyAnswers.setOnItemClickListener(null);
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            setIsResolution(true);
            setIsClick(false);
            vgClassfyAnswers.clearFocuse();
            lgClassfyAnswers.setOnItemClickListener(null);
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }
    }

    private boolean getTheAnswerIsRight(){
        ArrayList<ArrayList<String>> answer_list = questionsEntity.getAnswerBean().getConnect_classfy_answer();
        if (answer_list!=null) {
            for (int i = 0; i < answerData.size(); i++) {
                JSONObject object = null;
                String string = null;
                try {
                    object = new JSONObject(answerData.get(i));
                    string = object.getString("answer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String ss[] = string.split(",");
                if (answerData.size() > answer_list.size() || answer_list.get(i) == null) {
                    questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                    return false;
                } else {
                    if (ss.length != answer_list.get(i).size()) {
                        questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                        return false;
                    } else {
                        for (int j = 0; j < ss.length; j++) {
                            if (!answer_list.get(i).contains(ss[j])) {
                                questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                                return false;
                            }
                        }
                    }
                }
            }
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
            return true;
        }else {
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_DEFULT);
            return false;
        }
    }

    private void checkTheAnswer() {
        if (getTheAnswerIsRight()){
            //回答正确
            ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.RIGHT);
        }else {
            //回答错误
            ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.FAIL);
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
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            }catch (Exception e){}
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
        answerBean.setType(3);
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
            if (answerData.size() > answerlist.size() || answerlist.get(i) == null) {
                answerBean.setIsRight(false);
                return;
            } else {
                if (ss.length != answerlist.get(i).size()) {
                    answerBean.setIsRight(false);
                    return;
                } else {
                    for (int j = 0; j < ss.length; j++) {
                        if (!answerlist.get(i).contains(ss[j])) {
                            answerBean.setIsRight(false);
                            return;
                        }
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
            if (answerViewTypyBean != SubjectExercisesItemBean.RESOLUTION && answerViewTypyBean != SubjectExercisesItemBean.WRONG_SET&&"0".equals(questionsEntity.getType())) {
                vgClassfyAnswers.setData(classfyItem, ClassfyQuestionFragment.this);
            } else {
                vgClassfyAnswers.setData(classfyItem, null);
            }
        }
        backgroundAlpha(1f);
    }

    @Override
    public void onClick(View view) {
        if (mRemoveBean == (ClassfyBean)view.getTag()) {
            vgClassfyAnswers.setViewBackground(-1);
            mRemoveBean = null;
            choiceTmpString = "";
        } else {
            mRemoveBean = (ClassfyBean)view.getTag();
            vgClassfyAnswers.setViewBackground(mRemoveBean.getId());
            choiceTmpString = String.valueOf(((ClassfyBean)view.getTag()).getId());
        }
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
                    try {
                        if (Integer.parseInt(answerListStr.get(j)) == classfyBean.next().getId()) {
                            classfyBean.remove();
                        }
                    } catch (NumberFormatException e) {

                    }
                }
                //classfyItem.remove(Integer.parseInt(answerListStr.get(j)));
            }
        }
    }


      /** 
      * 设置添加屏幕的背景透明度 
      * @param bgAlpha 
      */
    public void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;//0.0-1.0  
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void updatesuccess() {
        if (questionsEntity.getContent() != null && questionsEntity.getContent().getChoices() != null && questionsEntity.getContent().getChoices().get(0).contains(YanXiuConstant.IMG_SRC)) {
            classfyAnswerAdapter.setData(classfyItem);
            lgClassfyAnswers.setVisibility(View.VISIBLE);
            vgClassfyAnswers.setVisibility(View.GONE);
        } else {
            if (answerViewTypyBean != SubjectExercisesItemBean.RESOLUTION && answerViewTypyBean != SubjectExercisesItemBean.WRONG_SET) {
                vgClassfyAnswers.setData(classfyItem, ClassfyQuestionFragment.this);
            } else {
                vgClassfyAnswers.setData(classfyItem, null);
            }
            lgClassfyAnswers.setVisibility(View.GONE);
            vgClassfyAnswers.setVisibility(View.VISIBLE);
        }
        FragmentTransaction ft = ClassfyQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();

        selectTypeView();

    }
}
