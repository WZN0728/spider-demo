package com.example.demo.controller;

import com.ironsoftware.ironpdf.PdfDocument;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;


@RestController
@RequestMapping("/pdf")
public class PdfController {

    @PostMapping(value = "/pdfUpload")
    public void pdfUpload(String htmlPdfValue) {
        htmlPdfValue = "<html><head></head><body><h1>My First PDF File<h1/><p> This is sample pdf file</p></body></html>";
        PdfDocument pdfDocument = PdfDocument.renderHtmlAsPdf(htmlPdfValue);
        try {
            File resource = ResourceUtils.getFile("classpath:test.pdf");
            pdfDocument.saveAs(Paths.get("test.pdf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/pdfUpload1")
    public void pdfUpload1(String htmlPdfValue) throws Exception {
        htmlPdfValue = "<p>TPU热熔胶粉 WCP ƏTEST测&#399; &lt; 测试</p>";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
        document.open();
        HTMLWorker htmlWorker = new HTMLWorker(document);
        //htmlPdfValue = "<html><head></head><body><h1>My First PDF File<h1/><p> This is sample pdf file</p></body></html>";
        htmlWorker.parse(new StringReader(htmlPdfValue));
        document.close();
    }

}
