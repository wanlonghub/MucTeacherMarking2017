package edu.muc.marking.util;


import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import edu.muc.marking.dao.bean.Academy;
import edu.muc.marking.dao.bean.ClassBean;
import edu.muc.marking.dao.bean.StudentCopy;
import edu.muc.marking.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.io.ByteToCharDBCS_ASCII;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-3-5   wanlong.ma
 * Description: 数据处理
 * Others:
 * Function List:
 * History:
 */
public class DataProcessHelper {

    private static Logger logger = LoggerFactory.getLogger(DataProcessHelper.class);

    public static void process() {

        Connection connection = DBUtil.openConnection();

        try{
            /*

            int index = 1;
            int size = 11677;

            for(int i=1;i<=size;i++){
                String sql = "select * from tmp_t_student_copy where id=?";
                StudentCopy studentCopy = DBUtil.queryObject(connection,sql,StudentCopy.class,i);

                if(null != studentCopy){
                    logger.info(studentCopy.toString());
                    String sql2 = "insert into t_student(student_id,student_account,student_name,class_id,status) " +
                            "values(?,?,?,?,?)";
                    int count = DBUtil.execute(connection,sql2,index++,studentCopy.getAccount(),studentCopy.getName(),studentCopy.getClass_id(),1);
                    if(count!=1) {
                        logger.info("执行失败 ： {}",studentCopy.getId());
                        System.exit(0);
                    }else{
                        logger.info("执行成功 ： {}",studentCopy.getId());
                    }
                }
            }

            */

            String sql = "select * from t_class";
            List<ClassBean> classBeanList = DBUtil.queryBeanList(connection,sql,ClassBean.class);

            Map<String,Integer> classNameToClassIdMap = Maps.newHashMap();
            for(ClassBean classBean : classBeanList){
                classNameToClassIdMap.put(classBean.getClass_name(),classBean.getClass_id());
            }

            classNameToClassIdMap.put("15生物科学类-1",453);
            classNameToClassIdMap.put("15生物科学类-2",453);
            classNameToClassIdMap.put("15生物科学类-3",453);


            int index = 1;
            int start = 11526;
            int end = 23050;
            for(int i=start;i<=end;i++){
                sql = "select * from tmp_t_student where id = " + i;
                StudentCopy studentCopy = DBUtil.queryBean(connection,sql,StudentCopy.class);
                Preconditions.checkNotNull(studentCopy,"StudentCopy 为空");
                logger.info(studentCopy.toString());

                Integer class_id = classNameToClassIdMap.get(studentCopy.getClass_name());
                if(class_id == null) class_id = 0;
                sql = "insert into t_student(student_id,student_account,student_name,class_id,status) " +
                        "values(?,?,?,?,?)";
                int count = DBUtil.execute(connection,sql,index++,studentCopy.getAccount(),studentCopy.getName(),class_id,1);
                Preconditions.checkState(count == 1,"插入失败");
            }



        }catch (Exception e){
            logger.error("",e);
        }


    }

    /**
     * 在academyList找到一个与acacemyName相似的学院名称，返回匹配到的学院id
     * @param academyList
     * @param acacemyName
     * @return
     */
    private static int matchAcademyId(List<Academy> academyList, String acacemyName){
        int id = 0;
        for(Academy academy : academyList){
            if(academy.getAcademy_name().trim().equals(acacemyName.trim()) || academy.getAcademy_name().trim().contains(acacemyName.trim())){
                id = academy.getAcademy_id();
            }
        }
        return  id;
    }

    /**
     *
     * @param
     * @param className
     * @return
     */
    private static int matchClassId(List<ClassBean> classBeanList, String className){
        int id = 0;
        for(ClassBean classBean : classBeanList){
            if(matchClassName(classBean.getClass_name().trim(),className)){
                id = classBean.getClass_id();
            }
        }
        return  id;
    }

    /**
     * 班级名字匹配
     * @param nameInTClass
     * @param nameInTStudent
     *
     * nameInTClass     2016级中韩翻译      2016级文学2班
     *
     * nameInTStudent   13中韩翻译          16文学-2
     *
     *
     * @return
     */
    private static boolean matchClassName(String nameInTClass, String nameInTStudent){

        String year = nameInTStudent.substring(0,2);
        String name = nameInTStudent.contains("-") ? nameInTStudent.substring(2,nameInTStudent.indexOf("-")) : nameInTStudent.substring(2,nameInTStudent.length());
        name = name.replace("类","");
        return nameInTClass.contains(year) && nameInTClass.contains(name);

    }

    public static void main(String[] args){

        /*String nameInTStudent = "16文学-2";
        String name = nameInTStudent.contains("-") ? nameInTStudent.substring(2,nameInTStudent.indexOf("-")) : nameInTStudent.substring(2,nameInTStudent.length());
        System.out.println(name);*/

        process();


    }



}
