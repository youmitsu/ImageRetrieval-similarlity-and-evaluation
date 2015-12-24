package imageRetrieve;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Test extends Applet {
	BufferedImage imgA;
	double aveR_A, aveG_A, aveB_A;
	LinkedList<BufferedImage> imgsB = new LinkedList<BufferedImage>();
	ArrayList<Double> similarities = new ArrayList<Double>();
	HashMap<Double, BufferedImage> files = new HashMap<Double, BufferedImage>();
	BufferedImage[] resultImages = new BufferedImage[10];

	public void init() {
		readImgA(new File("image1.jpg"));
		readImgB(new File("resources"));
		RGB aveRGB_A = getRGB(imgA);
		aveR_A = aveRGB_A.r;
		aveG_A = aveRGB_A.g;
		aveB_A = aveRGB_A.b;
		while (!imgsB.isEmpty()) {
			BufferedImage imgB = imgsB.getFirst();
			RGB aveRGB_B = getRGB(imgB);
			double similarity = calcSimilarity(aveRGB_B);
			files.put(similarity, imgB);
			imgsB.remove();
		}
		calcHighSimilarities();
	}

	public RGB getRGB(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		int allPixels = width * height;
		Point p;
		int clr, red, green, blue;
		double aveR = 0;
		double aveG = 0;
		double aveB = 0;
		// 画像のRGB取得とRGBごとの平均値の算出
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				p = new Point(j, i);
				clr = img.getRGB(j, i);
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

	public double calcSimilarity(RGB c) {
		double aveR_B = c.r;
		double aveG_B = c.g;
		double aveB_B = c.b;
		double a = Math.sqrt(((aveR_A - aveR_B) * (aveR_A - aveR_B))
				+ ((aveG_A - aveG_B) * (aveG_A - aveG_B))
				+ ((aveB_A - aveB_B) * (aveB_A - aveB_B)));
		double b = Math.sqrt(3 * ((255 - 0) * (255 - 0)));
		double similarity = a / b * 100;
		// System.out.println(similarity);
		similarities.add(similarity);
		return similarity;

	}

	public void calcHighSimilarities() {
		Collections.sort(similarities);
		for (int i = 0; i < resultImages.length; i++) {
			System.out.println(similarities.get(i));
			resultImages[i] = files.get(similarities.get(i));
		}
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

	public void paint(Graphics g) {
		g.drawImage(imgA, 0, 0, 210, 140, this);
		int i = 0;
		for (BufferedImage img : resultImages) {
			if (i < 5) {
				g.drawImage(img, 0 + 210 * i, 190, 210, 140, this);
			} else {
				g.drawImage(img, 0 + 210 * (i - 5), 380, 210, 140, this);
			}
			i++;
		}

	}
}
