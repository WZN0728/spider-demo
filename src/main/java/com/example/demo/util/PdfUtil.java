package com.example.demo.util;/**
 * @ClassName PdfUtil.java
 * @author wuting
 * @Description TODO
 * @createTime 2024年01月05日 14:33:00
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;

/**
 * @author wuting
 * @date 2024/01/05
 */
public class PdfUtil {

    public static void main(String[] args) {
        String FORMAT_CODE = "00000";
        int count = 1;
        DecimalFormat dft = new DecimalFormat(FORMAT_CODE);
        // 格式化为5位流水号 code: 00001
        System.out.println(dft.format(count));
        /*String s = "<p>TPU热熔胶粉 WCP<span style=\"font-family: 'SimSun-ExtB'\">ƏTEST测&#399; &lt;</span>测试</p>";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            s = "<html><head></head><body><h1>My First PDF File<h1/><p> This is sample pdf file</p></body></html>";
            htmlWorker.parse(new StringReader(s));
            document.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }

}
