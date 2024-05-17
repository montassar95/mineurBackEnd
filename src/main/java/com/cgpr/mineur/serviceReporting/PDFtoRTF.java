package com.cgpr.mineur.serviceReporting;
//package com.cgpr.mineur.service;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.rtf.RtfWriter2;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//public class PDFtoRTF {
//    public static ByteArrayOutputStream convertPDFtoRTF(byte[] pdfBytes) {
//        try {
//            PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//            document.close();
//
//            // Génération du fichier RTF à partir du contenu textuel
//            Document rtfDocument = new Document();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            RtfWriter2 rtfWriter = RtfWriter2.getInstance(rtfDocument, outputStream);
//            rtfDocument.open();
//            rtfDocument.add(new Paragraph(text));
//            rtfDocument.close();
//
//            return outputStream;
//        } catch (IOException | DocumentException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
////    public static void main(String[] args) {
////        try {
////            byte[] pdfBytes = {/* PDF bytes here */};
////            ByteArrayOutputStream rtfOutput = convertPDFtoRTF(pdfBytes);
////            // Utilisez le contenu de rtfOutput comme vous le souhaitez.
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//}
