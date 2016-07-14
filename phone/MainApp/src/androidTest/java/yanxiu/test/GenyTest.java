package yanxiu.test;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yanxiu.gphone.student.parser.SubjectExerisesItemParser;

import org.json.JSONObject;

/**
 * Created by lidm on 2015/9/24.
 */
public class GenyTest extends ApplicationTestCase<Application> {
    final String TAG = "ApplicationTest";

    String data = "{\"data\":[\n" +
            "{\"authorid\":10387467,\"bedition\":1431,\"begintime\":1445436000000,\"buildtime\":1445435723000,\"chapterid\":45425,\"editionName\":\"人教新版\",\"endtime\":1445529600000,\"id\":10450,\"name\":\"123&456\",\"paperStatus\":{\"id\":8190,\"ppid\":10450,\"status\":1,\"uid\":100},\"paperTest\":[{\"id\":81127,\"isfavorite\":0,\"pid\":10450,\"qid\":886799,\"qtype\":0,\"questions\":{\"analysis\":\"<em>【解答】</em>解:参照物可任意选择，既可以是运动的物体，也可以是静止的物体，具体选哪一种物体为参照物，是以研究问题的方便而定的;<br>因为运动和静止是相对的，所以，不事先选定参照物，就无法对某个物体的运动状态作出肯定的回答，说这个物体是运动或静止是毫无意义的.<br>故选项B正确，选项A、C、D错误.<br>故选B.\",\"answer\":[\"1\"],\"content\":{\"choices\":[\"只有地面上静止的物体才能选作参照物\",\"任何物体都可以选作参照物，但在具体选择时，要根据实际情况而定\",\"只有地球上的物体才能选作参照物\",\"研究物体的运动，选择太阳为参照物最合适，因为太阳是真正不动的物体\"]},\"id\":\"886799\",\"point\":[{\"id\":42325,\"name\":\"参照物及其选择\"}],\"stem\":\"以下说法正确的是（　　）\",\"template\":\"choice\",\"type_id\":\"1\"},\"sectionid\":0},{\"id\":81128,\"isfavorite\":0,\"pid\":10450,\"qid\":1889161,\"qtype\":0,\"questions\":{\"analysis\":\"<em>【解答】</em>解：A、中学生正常步行上楼功率约为150W；故A错误；\\n<br>B、中学生正常步行速度约为1.2m/s，接近生活实际；故B正确；\\n<br>C、一盏家用白炽灯泡正常工作电流约为0.2A，故C错误；\\n<br>D、置于水平桌面的物理书对桌面的压强可达50Pa，故D错误．\\n<br>故选B．\",\"answer\":[\"1\"],\"content\":{\"choices\":[\"中学生上楼的功率可达1000W\",\"中学生正常步行速度约为1.2m/s\",\"一盏家用白炽灯泡正常工作电流约为2A\",\"置于水平桌面的物理书对桌面的压强可达500Pa\"]},\"id\":\"1889161\",\"point\":[{\"id\":42339,\"name\":\"速度与物体运动\"},{\"id\":42406,\"name\":\"压强大小比较\"},{\"id\":42583,\"name\":\"功率大小的比较\"},{\"id\":42659,\"name\":\"电流的大小\"}],\"stem\":\"下列估测中，最接近实际的是（　　）\",\"template\":\"choice\",\"type_id\":\"1\"},\"sectionid\":0},{\"id\":81129,\"isfavorite\":0,\"pid\":10450,\"qid\":872636,\"qtype\":0,\"questions\":{\"analysis\":\"<em>【解答】</em>解:A、全程为100m，跑完全程时，兔子用的时间短，所以兔子赢，故A不正确.<br>B、乌龟在0秒时出发，兔子在t<sub>1</sub>秒出发，所以乌龟先出发，故B正确.<br>C、路程相同，时间相同时，二者相遇，表现在图象上是两图象的交点.两图象共有三个交点，所以途中相遇三次，故C正确.<br>D、图象中，乌龟的运动图象为一条直线，所以为匀速直线运动，故D正确.<br>故此题选A.\",\"answer\":[\"0\"],\"content\":{\"choices\":[\"比赛结果是乌龟获胜\",\"比赛时，乌龟先出发\",\"比赛途中，兔子和乌龟共相遇过三次\",\"乌龟在比赛过程中在做匀速直线运动\"]},\"id\":\"872636\",\"point\":[{\"id\":42343,\"name\":\"匀速直线运动\"},{\"id\":42344,\"name\":\"变速运动与平均速度\"}],\"stem\":\"<img src=\\\"http://scc.jsyxw.cn/tizi/qf1/images/c/c/5/cc58457a32fc7bc7d1e1b3e1ef709f118c1d194d.jpg\\\" style=\\\"vertical-align:middle;FLOAT:right\\\">龟兔赛跑是一个十分有趣且富有哲理的寓言故事．请仔细阅读图这幅反映新的龟兔百米赛跑的S一t图象，则根据这幅图象下列判断错误的是（　　）\",\"template\":\"choice\",\"type_id\":\"1\"},\"sectionid\":0},{\"id\":81130,\"isfavorite\":0,\"pid\":10450,\"qid\":2615864,\"qtype\":1,\"questions\":{\"analysis\":\"<p>这是解析</p><p><img src=\\\"http://scc.jsyxw.cn/answer/images/2015/1021/file_56278dbb116f5.png\\\" title=\\\"http://scc.jsyxw.cn/answer/images/2015/1021/file_56278dbb116f5.png\\\" alt=\\\"http://scc.jsyxw.cn/answer/images/2015/1021/file_56278dbb116f5.png\\\"/></p>\",\"answer\":[null],\"content\":\"\",\"id\":\"2615864\",\"point\":[{\"id\":42243,\"name\":\"物质\"},{\"id\":42244,\"name\":\"物态变化\"},{\"id\":42245,\"name\":\"物质的物理特征\"}],\"stem\":\"<p>这是题干<img class=\\\"kfformula\\\" src=\\\"data:image/png;base64,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\\\" data-latex=\\\"=div &lt;sqrt[{2}] {placeholder }\\\"/></p>\",\"template\":\"answer\",\"type_id\":\"6\"},\"sectionid\":0}],\"ptype\":1,\"quesnum\":3,\"sectionid\":0,\"showana\":1,\"stageName\":\"初中\",\"stageid\":1203,\"status\":1,\"subjectName\":\"物理\",\"subjectid\":1105,\"subquesnum\":1,\"volume\":210331,\"volumeName\":\"八年级上\"}],\"status\":{\"code\":0,\"desc\":\"get question list success\"}}";

    public GenyTest() {
        super(Application.class);
    }

    public void test() throws Exception {

        SubjectExerisesItemParser parser = new SubjectExerisesItemParser();
        JSONObject json = new JSONObject(data);
        parser.parse(json);
//        final int expected = 1;
//        final int reality = 1;
//        assertEquals(expected, reality);
    }


//    public void testUpload(){
//        LogInfo.log("geny", "testUpload------------------");
//        Map<String, File> fileMap = new HashMap<String, File>();
//        fileMap.put("filename", new File("/storage/emulated/ss.jpg"));
//        fileMap.put("filename2", new File("/storage/emulated/test.jpg"));
//
////        fileMap.put("filename", new File("/storage/emulated/0/DCIM/Camera/1.jpg"));
////        fileMap.put("filename2", new File("/storage/emulated/0/DCIM/Camera/2.jpg"));
////        fileMap.put("filename3", new File("/storage/emulated/0/DCIM/Camera/3.jpg"));
//        YanxiuHttpApi.requestUploadImage(fileMap, new YanxiuHttpApi.UploadFileListener() {
//
//            @Override
//            public void onFail(YanxiuBaseBean bean) {
//                LogInfo.log("geny", "requestUploadImage s =onFail");
//            }
//
//            @Override
//            public void onSuccess(YanxiuBaseBean bean) {
//                LogInfo.log("geny", "requestUploadImage s =onSuccess");
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                LogInfo.log("geny", "requestUploadImage s =onProgress-----------------" + progress);
//            }
//        });
//    }

}