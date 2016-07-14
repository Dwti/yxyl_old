package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.PaperTestEntity;
import com.yanxiu.gphone.hd.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.hd.student.bean.statistics.MistakeCountBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestDelMistakeTask;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.question.YXiuAnserTextView;

import de.greenrobot.event.EventBus;

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
        //if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wrong_answer, null);
            holder = new ViewHolder();
            holder.answerExamType = (ImageView) convertView.findViewById(R.id.answer_exam_type_text);
            holder.answerExamContent = (YXiuAnserTextView) convertView.findViewById(R.id.answer_exam_content_text);
            holder.answerExamDelete = (ImageView) convertView.findViewById(R.id.iv_answer_exam_delete);
            holder.wrongDividerLine = (ImageView) convertView.findViewById(R.id.item_divider_line);

            /*convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/
        if ((position + 1) >= getCount()) {
            holder.wrongDividerLine.setVisibility(View.GONE);
        }
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
            if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE.type){
                holder.answerExamType.setImageResource(R.drawable.subjective_title_bg);
            }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES.type) {
                holder.answerExamType.setImageResource(R.drawable.choice_single_title_bg);
            }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES.type){
                holder.answerExamType.setImageResource(R.drawable.choice_multi_title_bg);
            }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE.type){
                holder.answerExamType.setImageResource(R.drawable.judge_title_bg);
            }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS.type){
                holder.answerExamType.setImageResource(R.drawable.fill_blanks_bg);
            }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_READING.type){
                holder.answerExamType.setImageResource(R.drawable.reading_title_bg);
            }
            if(entity.getQuestions().isReadQuestion()){
                holder.answerExamType.setVisibility(View.GONE);
            }
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
