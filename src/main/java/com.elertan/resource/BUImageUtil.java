package com.elertan.resource;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class BUImageUtil {

    public static BufferedImage resizeNearest(BufferedImage src, int w, int h, int offsetX,
        int offsetY) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );
        g.drawImage(
            src,
            offsetX,
            offsetY,
            offsetX + w,
            offsetY + h,
            0,
            0,
            src.getWidth(),
            src.getHeight(),
            null
        );
        g.dispose();
        return out;
    }
}
