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

    public static BufferedImage padImage(BufferedImage src, int padLeft, int padRight, int padTop,
        int padBottom) {
        if (src == null) {
            throw new IllegalArgumentException("src cannot be null");
        }
        if (padLeft < 0 || padRight < 0 || padTop < 0 || padBottom < 0) {
            throw new IllegalArgumentException("Padding values must be non-negative");
        }

        int newW = src.getWidth() + padLeft + padRight;
        int newH = src.getHeight() + padTop + padBottom;

        BufferedImage out = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        // New image is created fully transparent by default for TYPE_INT_ARGB
        // Draw the original image offset by the left and top padding
        g.drawImage(src, padLeft, padTop, null);
        g.dispose();
        return out;
    }
}
