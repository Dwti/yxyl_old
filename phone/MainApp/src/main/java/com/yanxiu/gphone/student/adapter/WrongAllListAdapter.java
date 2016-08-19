package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.student.bean.statistics.MistakeCountBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestDelMistakeTask;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CLOZE_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CONNECT;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_LISTEN_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READING;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READ_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SOLVE_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE;

/**
 * Created by JS-00 on 2016/6/23.
 */
public class WrongAllListAdapter extends YXiuCustomerBaseAdapter<PaperTestEntity> {
    private boolean deleteAction = false;
    public WrongAllListAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PaperTestEntity entity = getItem(position);
        ViewHolder holder = null;
//        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wrong_answer, null);
            holder = new ViewHolder();
            holder.answerExamType = (ImageView) convertView.findViewById(R.id.answer_exam_type_text);
            holder.answerExamContent = (YXiuAnserTextView) convertView.findViewById(R.id.answer_exam_content_text);
            holder.answerExamDelete = (ImageView) convertView.findViewById(R.id.iv_answer_exam_delete);
            holder.wrongDividerLine = (ImageView) convertView.findViewById(R.id.item_divider_line);

//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if ((position + 1) >= getCount()) {
//            holder.wrongDividerLine.setVisibility(View.GONE);
//        }

        holder.answerExamContent.setIsSendHeight(true);
        setData(entity, holder);
        holder.answerExamDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWrongCard(position, entity);
            }
        });
        return convertView;
    }

    private void deleteWrongCard(final int position, PaperTestEntity entity) {
        final String questionId = entity.getQid() + "";
        final int pageIndex = position;
        LogInfo.log("haitian", "questionId =" + questionId);
        if (TextUtils.isEmpty(questionId)) {
            Util.showToast(R.string.select_location_data_error);
        } else {
            if (NetWorkTypeUtils.isNetAvailable()) {
                Util.showToast(R.string.public_loading_net_null_errtxt);
                return;
            }
            //mRootView.loading(true);
            RequestDelMistakeTask.requestTask(mContext, questionId, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    //mRootView.finish();
                    deleteAction = true;
                    Util.showToast(R.string.mistake_question_del_done);
                    PublicErrorQuestionCollectionBean.updateDelData(questionId);
                    mList.remove(position);
                    setList(mList);
                    MistakeCountBean mistakeCountBean = new MistakeCountBean();
                    EventBus.getDefault().post(mistakeCountBean);
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


    private void setData(PaperTestEntity entity,ViewHolder holder) {
        if (entity != null && entity.getQuestions() != null) {
            int typeId = entity.getQuestions().getType_id();
            if(typeId == QUESTION_SUBJECTIVE.type){
                //    6
                holder.answerExamType.setImageResource(R.drawable.subjective_title_bg);
            }else if(typeId == QUESTION_SINGLE_CHOICES.type) {
                //    1
                holder.answerExamType.setImageResource(R.drawable.choice_single_title_bg);
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
            }else if(typeId == QUESTION_READ_COMPLEX.type){
                //    14
                holder.answerExamType.setImageResource(R.drawable.read_complex_title_bg);
            }else if(typeId == QUESTION_SOLVE_COMPLEX.type){
                //    22
                holder.answerExamType.setImageResource(R.drawable.solve_complex_title_bg);
            }else if (typeId==QUESTION_CLOZE_COMPLEX.type){
                //    15
                holder.answerExamType.setImageResource(R.drawable.gestalt_complex_title_bg);
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
            if (entity.getQuestions().getStem() != null) {
                holder.answerExamContent.setTextHtml(entity.getQuestions().getStem());
            }
        }
    }



    private class ViewHolder {
        private ImageView answerExamType;
        private YXiuAnserTextView answerExamContent;
        private ImageView answerExamDelete;
        private ImageView wrongDividerLine;
    }
}
