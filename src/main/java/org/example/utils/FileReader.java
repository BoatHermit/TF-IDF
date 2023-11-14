package org.example.utils;


import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import org.apache.xmlbeans.XmlException;
import org.example.ExternalFile;
import org.example.InternalFile;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    /**
     * 读取外规文件加载到文件列表
     * @param filePath 文件路径
     * @return 文件列表
     */
    public List<ExternalFile> readExternalFile(String filePath) {
        List<ExternalFile> fileList = new ArrayList<>();
        File dir = new File(filePath);
        if (!dir.exists()){
            System.out.println("目录不存在");
        }else {
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {
                    ExternalFile tmp = new ExternalFile();
                    tmp.setName(file.getName());
                    tmp.setContent(txtToString(file.getPath()));
                    fileList.add(tmp);
                }
            }
        }
        return fileList;
    }

    /**
     * 递归获取所有文件
     * @param fileInput 输入文件或文件夹
     */
    private void getAllFile(File fileInput, List<InternalFile> fileList){
        File[] files = fileInput.listFiles();
        assert files != null;
        for (File file : files){
            if (file.isFile()){
                InternalFile tmp = new InternalFile();
                tmp.setName(file.getName());
                tmp.setContent(docToString(file));
                tmp.setDepartment(file.getParentFile().getName());
                fileList.add(tmp);
            }else {
                getAllFile(file, fileList);
            }
        }
    }

    /**
     * 读取内规文件加载到文件列表
     * @param filePath 文件路径
     * @return 文件列表
     */
    public List<InternalFile> readInternalFile(String filePath){
        List<InternalFile> fileList = new ArrayList<>();
        File dir = new File(filePath);
        if (!dir.exists()){
            System.out.println("目录不存在");
        }else {
            getAllFile(dir, fileList);
        }
        return fileList;
    }

    /**
     * 读取外规txt文件内容转化为字符串
     * @param filePath 文件
     * @return 文件内容的字符串形式
     */
    public String txtToString(String filePath){
        String content = "";
        BufferedReader reader;
        try {
            //读入文件
            reader = new BufferedReader(new java.io.FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                content += line + "\n";
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 读取内规doc、docx文件内容转化为字符串
     * @param file 文件
     * @return 文件内容的字符串形式
     */
    public String docToString(File file){
        String content = "";
        if (file.getName().endsWith(".doc")){
            InputStream is = null;
            try {
                is = Files.newInputStream(file.toPath());
                WordExtractor ex = new WordExtractor(is);
                content = ex.getText();
                ex.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (file.getName().endsWith(".docx")) {
            OPCPackage opcPackage = null;
            try {
                opcPackage = POIXMLDocument.openPackage(file.getPath());
                POIXMLTextExtractor ex = new XWPFWordExtractor(opcPackage);
                content = ex.getText();
                ex.close();
            } catch (IOException | OpenXML4JException | XmlException e) {
                throw new RuntimeException(e);
            }
        }
        return content;
    }
}
