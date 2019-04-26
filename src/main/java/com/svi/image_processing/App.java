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
		File[] files;  
		
		try {
			files = new File(directoryPath).listFiles();
			iterateFiles(files);
		} catch (NullPointerException e) {
			System.out.println(directoryPath +" does not exist.");
		}
	}
	
	public static void iterateFiles(File[] files) {
		if(files.length == 0) {
			System.out.println("Directory is empty.");
			return;
		}
		for (File file : files) {
	        if (file.isFile()) {
	        	if(getExtension(file).equals(".pdf")){
	        		generatePdfImages(file);
	        	} else if(getExtension(file).equals(".doc") || getExtension(file).equals(".docx")) {
	        		generateMsDocImages(file);
	        	}
	        }
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static void generatePdfImages(File file) {
		try {
	        String sourceDir = file.getAbsolutePath();
	        String destinationDir = "C:/documents/PDF to JPG/"  + file.getName() + "/"; 

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
	
	public static void generateMsDocImages(File file) {
		try {
			String sourceDir = file.getAbsolutePath();
	        String destinationDir = "C:/documents/MS DOC to JPG/" + file.getName() + "/"; 

	        Document doc = new Document(sourceDir);
	        File destinationFile = new File(destinationDir);
	        
	        ImageSaveOptions options = new ImageSaveOptions(SaveFormat.JPEG);
			options.setJpegQuality(100);
			options.setResolution(100);
			
	        if (!destinationFile.exists()) {
	            destinationFile.mkdir();
	            System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
	        }
			for (int i = 0; i < doc.getPageCount(); i++) {
				String imageFilePath = destinationDir + file.getName() + "_output_" + i + ".jpg";
				options.setPageIndex(i);
				doc.save(imageFilePath, options);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}
