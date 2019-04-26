package com.svi.image_processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;

public class App {
	public static void main(String args[]) throws Exception {
		final String directoryPath = "C:\\documents\\";
		final String sourcePath = "C:\\documents\\samplepdf.pdf";
		File[] files  = new File (directoryPath).listFiles();
		
		iterateFiles(files);
	}
	
	public static void iterateFiles(File[] files) {
		for (File file : files) {
	        if (file.isFile()) {
	        	if(getExtension(file).equals(".pdf")){
	        		generatePdfImages(file.getAbsolutePath());
	        	} else if(getExtension(file).equals(".doc") || getExtension(file).equals(".docx")) {
	        		generateMsDocImages(file.getAbsolutePath());
	        	}
	        }
	    }
	}
	
	public static String getExtension(File file) {
		String extension = "";
		
		try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
	}

	public static void generateMsDocImages(final String sourcePath) {
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
	        String destinationDir = "C:/documents/PDF to JPG/"; 

	        File sourceFile = new File(sourceDir);
	        File destinationFile = new File(destinationDir);
	        
//	        String sourceDir = sourcePath; // Pdf files are read from this folder
//	        File sourceFile = new File(sourceDir);
//	        String destinationDir = "C:/documents/Converted_PdfFiles_to_Image/" + sourceFile.getName().replace(".pdf", "") + "/"; 
//	        File destinationFile = new File(destinationDir);
	        
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
	                File outputfile = new File(destinationDir + fileName +"_"+ pageNumber +".jpg");
	                System.out.println("Image Created -> "+ outputfile.getName());
	                ImageIO.write(image, "jpg", outputfile);
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
