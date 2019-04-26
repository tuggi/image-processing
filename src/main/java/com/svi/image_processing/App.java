package com.svi.image_processing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.w3c.dom.Node;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String args[]) throws Exception {
		final String sourcePath = "C:\\documents\\samplepdf.pdf";
		generateImages(sourcePath);
	}

	public static void generateImages(final String sourcePath) {
		try {
			Document doc = new Document(sourcePath);
			ImageSaveOptions options = new ImageSaveOptions(SaveFormat.JPEG);
			options.setJpegQuality(100);
			options.setResolution(100);

			for (int i = 0; i < doc.getPageCount(); i++) {
				String imageFilePath = sourcePath + "_output_" + i + ".jpg";
				options.setPageIndex(i);
				doc.save(imageFilePath, options);
			}
		} catch (Exception e) {
			System.out.println("File is not a ms document type, trying pdf conversion.");
			generatePdfImages(sourcePath);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void generatePdfImages(String sourcePath) {
		try {
	        String sourceDir = sourcePath; // Pdf files are read from this folder
	        String destinationDir = "C:/documents/Converted_PdfFiles_to_Image/"; 

	        File sourceFile = new File(sourceDir);
	        File destinationFile = new File(destinationDir);
	        if (!destinationFile.exists()) {
	            destinationFile.mkdir();
	            System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
	        }
	        if (sourceFile.exists()) {
	            System.out.println("Images copied to Folder: "+ destinationFile.getName());             
	            PDDocument document = PDDocument.load(sourceDir);
	            List<PDPage> list = document.getDocumentCatalog().getAllPages();
	            System.out.println("Total files to be converted -> "+ list.size());

	            String fileName = sourceFile.getName().replace(".pdf", "");             
	            int pageNumber = 1;
	            for (PDPage page : list) {
	                BufferedImage image = page.convertToImage();
	                File outputfile = new File(destinationDir + fileName +"_"+ pageNumber +".png");
	                System.out.println("Image Created -> "+ outputfile.getName());
	                ImageIO.write(image, "png", outputfile);
	                pageNumber++;
	            }
	            document.close();
	            System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	        } else {
	            System.err.println(sourceFile.getName() +" File not exists");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
