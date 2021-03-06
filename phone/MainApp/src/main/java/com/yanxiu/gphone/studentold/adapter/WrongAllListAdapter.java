package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.PaperTestEntity;
import com.yanxiu.gphone.studentold.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.studentold.bean.statistics.MistakeCountBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestDelMistakeTask;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.question.YXiuAnserTextView;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CLASSFY;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CLOZE_COMPLEX;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CONNECT;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_LISTEN_COMPLEX;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READING;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READ_COMPLEX;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SOLVE_COMPLEX;
import static com.yanxiu.gphone.studentold.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE;

/**
 * Created by JS-00 on 2016/6/23.
 */
public class WrongAllListAdapter extends YXiuCustomerBaseAdapter<PaperTestEntity> {
    private static final long ANIMATION_DURATION = 300;
    private boolean deleteAction = false;
    public WrongAllListAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PaperTestEntity entity = getItem(position);
        final View view;
        ViewHolder holder = null;
        //if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_wrong_answer, null);
            holder = new ViewHolder();
            holder.answerExamType = (ImageView) view.findViewById(R.id.answer_exam_type_text);
            holder.answerExamContent = (YXiuAnserTextView) view.findViewById(R.id.answer_exam_content_text);
            holder.answerExamDelete = (ImageView) view.findViewById(R.id.iv_answer_exam_delete);
            holder.wrongDividerLine = (ImageView) view.findViewById(R.id.item_divider_line);
            view.setTag(holder);
        /*} else if (((ViewHolder)convertView.getTag()).needInflate) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_wrong_answer, null);
            holder = new ViewHolder();
            holder.answerExamType = (ImageView) view.findViewById(R.id.answer_exam_type_text);
            holder.answerExamContent = (YXiuAnserTextView) view.findViewById(R.id.answer_exam_content_text);
            holder.answerExamDelete = (ImageView) view.findViewById(R.id.iv_answer_exam_delete);
            holder.wrongDividerLine = (ImageView) view.findViewById(R.id.item_divider_line);
            view.setTag(holder);
        } else {
            view = convertView;
        }*/

        holder = (ViewHolder) view.getTag();
        if ((position + 1) >= getCount()) {
            holder.wrongDividerLine.setVisibility(View.GONE);
        }else {
            holder.wrongDividerLine.setVisibility(View.VISIBLE);
        }
//        holder.answerExamContent.setClasfyFlag(false);

//        holder.answerExamContent.setIsSendHeight(true);
        setData(entity, holder);
        holder.answerExamDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWrongCard(view, position, entity);
            }
        });
        return view;
    }

    private void deleteWrongCard(final View v, final int position, PaperTestEntity entity) {
        final String questionId = entity.getQid() + "";
        final int pageIndex = position;
        LogInfo.log("haitian", "questionId =" + questionId);
        if (TextUtils.isEmpty(questionId)) {
            Util.showToast(R.string.select_location_data_error);
        } else {
            if (!NetWorkTypeUtils.isNetAvailable()) {
                Util.showToast(R.string.public_loading_net_null_errtxt);
                return;
            }
//            deleteAction = true;
//            Util.showToast(R.string.mistake_question_del_done);
//            PublicErrorQuestionCollectionBean.updateDelData(questionId);
//            MistakeCountBean mistakeCountBean = new MistakeCountBean();
//            EventBus.getDefault().post(mistakeCountBean);
//            deleteCell(v, position);

            //mRootView.loading(true);
            RequestDelMistakeTask.requestTask(mContext, questionId, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    //mRootView.finish();
                    deleteAction = true;
                    Util.showToast(R.string.mistake_question_del_done);
                    PublicErrorQuestionCollectionBean.updateDelData(questionId);
                    int count=mList.size();
                    if (count<=1) {
                        MistakeCountBean mistakeCountBean = new MistakeCountBean();
                        EventBus.getDefault().post(mistakeCountBean);
                    }else {
                        NoRefreshBean refreshBean=new NoRefreshBean();
                        EventBus.getDefault().post(refreshBean);
                    }
                    deleteCell(v, position);
                }

                @Override
                public void dataError(int type, String msg) {
                    //mRootView.finish();
                    if (!TextUtils.isEmpty(msg)) {
                        Util.showToast(msg);
                    } else {
                        Util.showToast(R.string.mistake_question_del_failed);
                    }
                }
            });
        }
    }

    public class NoRefreshBean{}

    private void setData(PaperTestEntity entity,ViewHolder holder) {
        if (entity != null && entity.getQuestions() != null) {
            int typeId = entity.getQuestions().getType_id();

            if (entity.getQuestions().getStem() != null) {
                holder.answerExamContent.setHtmlFlag(false);
                if (typeId==15){
                    holder.answerExamContent.setTextHtml(entity.getQuestions().getStem().replace("(_)","___"));
                }else {
                    holder.answerExamContent.setTextHtml(entity.getQuestions().getStem());
                }
            }

            if(typeId == QUESTION_SUBJECTIVE.type){
                //    6
                holder.answerExamType.setImageResource(R.drawable.subjective_title_bg);
            }else if(typeId == QUESTION_SINGLE_CHOICES.type) {
                //    1
                holder.answerExamType.setImageResource(R.drawable.choice_single_title_bg);
                String text_heml=entity.getQuestions().getStem();
                List<String> list=entity.getQuestions().getContent().getChoices();
                for (int i=0;i<list.size();i++){
                        String ss=String.valueOf(numToLetter(String.valueOf(i+"")));
                        text_heml=text_heml+"<br/>"+ss+" : "+list.get(i);
                }
                holder.answerExamContent.setTextHtml(text_heml);
            }else if(typeId == QUESTION_MULTI_CHOICES.type){
                //    2
                holder.answerExamType.setImageResource(R.drawable.choice_multi_title_bg);
            }else if(typeId == QUESTION_JUDGE.type){
                //    4
                holder.answerExamType.setImageResource(R.drawable.judge_title_bg);
            }else if(typeId == QUESTION_FILL_BLANKS.type){
                //    3
                holder.answerExamType.setImageResource(R.drawable.fill_blanks_bg);
            }else if(typeId == QUESTION_READING.type){
                //    5
                holder.answerExamType.setImageResource(R.drawable.reading_title_bg);
            }else if(typeId == QUESTION_CLASSFY.type){
                //    13
                holder.answerExamType.setImageResource(R.drawable.classfy_title_bg);
            }else if(typeId == QUESTION_READ_COMPLEX.type){
                //    14
                holder.answerExamType.setImageResource(R.drawable.read_complex_title_bg);
            }else if(typeId == QUESTION_SOLVE_COMPLEX.type){
                //    22
                holder.answerExamType.setImageResource(R.drawable.solve_complex_title_bg);
            }else if (typeId==QUESTION_CLOZE_COMPLEX.type){
                //    15
                holder.answerExamType.setImageResource(R.drawable.gestalt_complex_title_bg);

//                String stem=entity.getQuestions().getStem().replace("(_)_","___");
//                entity.getQuestions().setStem(stem);
            }else if (typeId==QUESTION_LISTEN_COMPLEX.type){
                //    13
                holder.answerExamType.setImageResource(R.drawable.listen_complex_title_bg);
            }else if ((9<=typeId && typeId<=12) || typeId==18 || typeId==19 || typeId==21){
                holder.answerExamType.setImageResource(R.drawable.listen_complex_title_bg);
            }else if (typeId== QUESTION_CONNECT.type){
                //    7
                holder.answerExamType.setImageResource(R.drawable.attachment_title_bg);
            }else if (typeId== QUESTION_COMPUTE.type){
                //    8
                holder.answerExamType.setImageResource(R.drawable.calculate_title_bg);
            }else if (typeId==16){
                holder.answerExamType.setImageResource(R.drawable.translation_title_bg);
            }else if (typeId==17){
                holder.answerExamType.setImageResource(R.drawable.subjects_title_bg);
            }else if (typeId==20){
                holder.answerExamType.setImageResource(R.drawable.sorting_title_bg);
            }
            if(entity.getQuestions().isReadQuestion()){
                holder.answerExamType.setVisibility(View.GONE);
            }/*else{
                questionTitle = questionsEntity.getTitleName();
                if(!TextUtils.isEmpty(questionTitle)){
                    tvQuestionTitle.setText(questionTitle);
                }
            }*/

        }
    }

    // 将数字转换成字母
    public char[] numToLetter(String input) {
        char c[] = input.toCharArray();
        int i = 0;
        for (byte b : input.getBytes()) {
            c[i] = (char) (b + 49 - 26 - 6);
        }
        return c;
    }

    private class ViewHolder {
        private ImageView answerExamType;
        private YXiuAnserTextView answerExamContent;
        private ImageView answerExamDelete;
        private ImageView wrongDividerLine;
        private boolean needInflate;
    }

    private void deleteCell(final View v, final int index) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                ViewHolder vh = (ViewHolder)v.getTag();
                vh.needInflate = true;
                mList.remove(index);
                setList(mList);
            }
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        };

        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al!=null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }
}
