package imageRetrieve;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Test {
	BufferedImage imgA;
	ArrayList<BufferedImage> imgsB = new ArrayList<BufferedImage>();

	public static void main(String[] args) {
		new Test().start();
	}

	public void start() {
		readFolder(new File("3flower"));
	}

	public void readFolder(File dir) {
		File[] files = dir.listFiles();
		BufferedImage img;
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (!file.exists()) {
				continue;
			} else if (file.isDirectory()) {
				readFolder(file);
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
	/*
	 * public void execute(File file){ System.out.println(file.getPath()); }
	 */

}
