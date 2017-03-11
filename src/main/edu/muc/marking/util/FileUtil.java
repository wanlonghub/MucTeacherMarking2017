package edu.muc.marking.util;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * version    date      author
 * ──────────────────────────────────
 * 1.0       17-2-22   wanlong.ma
 * Description: 文件操作工具类
 * Others:
 * Function List:
 * History:
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取文件在classpath下的物理路径
     * @param fileName
     * @return
     */
    public static String getPath(String fileName) {
        Preconditions.checkNotNull(fileName,"文件名为null");
        return ClassLoader.getSystemResource("").getPath() + fileName;
    }

    /**
     * 按行读取指定文件并形成list
     * @param filePath
     * @return
     */
    public static List<String> readFileToLines(String filePath){

        Preconditions.checkNotNull(filePath,"文件路径为null");
        List<String> content = null;

        File file = new File(filePath);
        if(!file.exists() || !file.isFile()) throw new RuntimeException("文件不存在：" + filePath);

        try{
            content = Files.readLines(file, Charsets.UTF_8);
        }catch (IOException e){
            logger.error("文件读取失败：" + filePath);
            throw new RuntimeException("文件读取失败：" + e.getStackTrace());
        }

        return content;
    }

    /**
     * 递归遍历文件，收集所有文本文件的路径
     * @param path
     * @return 文件路径
     */
    public static void traverseFolder(String path, List<String> textFilePathList) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File file2 : files) {
                    // 不是文件夹且是文本文件
                    if (!file2.isDirectory()) {
                        logger.info("遍历到文件：{}",file2.getName());
                        Preconditions.checkNotNull(textFilePathList);
                        textFilePathList.add(file2.getAbsolutePath());
                    }else {
                        traverseFolder(file2.getAbsolutePath(),textFilePathList);
                    }
                }
            }
        } else {
            logger.error("文件{}不存在",path);
            System.out.println("文件不存在!");
        }

    }

    /**
     * 由于递归在层次很深时会有问题，因而这里提供一种采用队列的非递归遍历方式
     * @param path
     * @param textFilePathList
     */
    public static void traverseFolderNotRecursion(String path, List<String> textFilePathList) {

        File file = new File(path);
        LinkedList<File> fileQueue = Lists.newLinkedList(Lists.<File>newArrayList(file));

        do{
            File mFile = fileQueue.poll();
            if(mFile.isDirectory()){
                fileQueue.addAll(Lists.newArrayList(mFile.listFiles()));
            }else{
                logger.info("遍历到文件：{}",mFile.getName());
                Preconditions.checkNotNull(textFilePathList);
                textFilePathList.add(mFile.getAbsolutePath());
            }
        }while(!fileQueue.isEmpty());

    }

    /**
     * 写入文件
     * @param filePath
     * @param content
     * @return
     */
    public static void writeFile(String filePath, String content){

        Preconditions.checkNotNull(filePath,"文件路径为空");
        Preconditions.checkNotNull(content,"写入内容为空");

        File file = new File(filePath);
        try {
            Files.append(content, file, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
