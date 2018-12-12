package ws.slink.test.convert.processor;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

@Service
public class ImageConverter {

	public BufferedImage resize(BufferedImage image) {
		return resize(image, 100, 100);
	}

	public BufferedImage resize(BufferedImage image, int width, int height) {
		return resizeWithHint(image, width, height);
	}

	
	@SuppressWarnings("unused")
	private BufferedImage resizeNormal(BufferedImage image, int width, int height) {
		
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
	
	private BufferedImage resizeWithHint(BufferedImage image, int width, int height) {

		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();

		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		                   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		                   RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                   RenderingHints.VALUE_ANTIALIAS_ON);
		
		return resizedImage;		
	}
	
}
