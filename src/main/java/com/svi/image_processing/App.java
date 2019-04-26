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
import org.apache.pdfbox.pdmodel.PDPageTree;
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
			e.printStackTrace();
			///////////////////////////////////////////
			try {
				String storagePath = sourcePath;

				// Image Save Directory
				String realPathtopdfImageSaveDir = "C://uploads/";

				RandomAccessFile raf = new RandomAccessFile(storagePath, "r");
				FileChannel channel = raf.getChannel();
				ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
				PDFFile pdffile = new PDFFile(buf);

				int numPgs = pdffile.getNumPages();

				for (int i = 0; i < numPgs; i++) {
					PDFPage page = pdffile.getPage(i);

					Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
							(int) page.getBBox().getHeight());

					Image img = page.getImage(rect.width, rect.height, rect, null, true, true);

					// save it as a file
					BufferedImage bImg = toBufferedImage(img);
					File yourImageFile = new File(realPathtopdfImageSaveDir + File.separator + "page_" + i + ".png");

					ImageIO.write(bImg, "png", yourImageFile);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			///////////////////////////////////////////

		}
	}

//	public static List<String> generatePdfImages(File pdfFile) throws IOException {
//
//		String imagePath = "/Users/$user/pdfimages/";
//		List<String> fileNames = new ArrayList<String>();
//		PDDocument document = PDDocument.load(pdfFile); //// load pdf
//		PDPageTree node = document.getDocumentCatalog().getPages(); ///// get pages
//		List<PDPage> kids = node.getKids();
//		int count = 0;
//		for (PDPage page : kids) { ///// iterate
//			BufferedImage img = page.convertToImage(BufferedImage.TYPE_INT_RGB, 128);
//			File imageFile = new File(imagePath + count++ + ".jpg");
//			ImageIO.write(img, "jpg", imageFile);
//			fileNames.add(imageFile.getName());
//		}
//		return fileNames;
//	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		image = new ImageIcon(image).getImage();

		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			int transparency = Transparency.OPAQUE;

			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			System.out.println("The system does not have a screen");
		}

		if (bimage == null) {
			int type = BufferedImage.TYPE_INT_RGB;
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		Graphics g = bimage.createGraphics();

		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

}
