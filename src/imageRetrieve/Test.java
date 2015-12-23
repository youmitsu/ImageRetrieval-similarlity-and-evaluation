package imageRetrieve;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Test {
	BufferedImage imgA;
	double aveR_A, aveG_A, aveB_A;
	LinkedList<BufferedImage> imgsB = new LinkedList<BufferedImage>();
    ArrayList<Double> similarities = new ArrayList<Double>();
	
	public static void main(String[] args) {
		new Test().start();
	}

	public void start() {
		readImgA(new File("image1.jpg"));
		readImgB(new File("3flower"));
		RGB aveRGB_A = getRGB(imgA);
		aveR_A = aveRGB_A.r;
		aveG_A = aveRGB_A.g;
		aveB_A = aveRGB_A.b;
		while(!imgsB.isEmpty()){
			BufferedImage imgB = imgsB.getFirst();
			RGB aveRGB_B = getRGB(imgB);
			calcSimilarity(aveRGB_B);
		    imgsB.remove();
		}
	}
	
	public RGB getRGB(BufferedImage img){
		int width = img.getWidth();
		int height = img.getHeight();
		int allPixels = width*height;
		Point p;
		int clr, red, green, blue;
		double aveR = 0; 
		double aveG = 0; 
		double aveB = 0;
		//画像AのRGB取得とRGBごとの平均値の算出
		for(int i = 0; i < height;i++){
			for(int j = 0; j < width; j++){
				p = new Point(j, i);
				clr = img.getRGB(j,i);
				red = (clr & 0x00ff0000) >> 16;
			    green = (clr & 0x0000ff00) >> 8;
			    blue = clr & 0x000000ff;
			    aveR += red;
			    aveG += green;
			    aveB += blue;
			}
		}
		aveR /= allPixels;
		aveG /= allPixels;
		aveB /= allPixels;
		
		return new RGB(aveR, aveG, aveB);
	}
	
	public void calcSimilarity(RGB c){
		double aveR_B = c.r;
		double aveG_B = c.g;
		double aveB_B = c.b;
		double a = Math.sqrt((aveR_A-aveR_B)*(aveR_A-aveR_B)
				+(aveG_A-aveG_B)*(aveG_A-aveG_B)+(aveB_A-aveB_B)*(aveB_A-aveB_B));
		double b = Math.sqrt(3*255-0);
		double similarity = a/b*100;
		System.out.println(similarity);
		similarities.add(similarity);
	}

	public void readImgA(File file) {
		try {
			imgA = ImageIO.read(file);
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void readImgB(File dir) {
		File[] files = dir.listFiles();
		BufferedImage img;
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (!file.exists()) {
				continue;
			} else if (file.isDirectory()) {
				readImgB(file);
			} else if (file.isFile()) {
				try {
					img = ImageIO.read(file);
					imgsB.add(img);
				} catch (IOException e) {
					e.getMessage();
				}
			}
		}
	}
}
