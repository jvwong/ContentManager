package com.example.cm.cm_repository.image.utils;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class ImageConverter {

    /**
     * Converts an image to another format
     *
     * @param inputStream source image
     * @return ByteArrayInputStream
     * @throws IOException if errors occur during writing
     */
    public static ByteArrayInputStream convertToJPEG(InputStream inputStream)
            throws IOException {

        final String FORMAT_JPG = "jpg";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);

        // writes to the output image in specified format
        ImageIO.write(inputImage, FORMAT_JPG, outputStream);
        ByteArrayInputStream newInput = new ByteArrayInputStream(outputStream.toByteArray());

        // needs to close the streams
        outputStream.close();
        inputStream.close();

        return newInput;
    }
}
