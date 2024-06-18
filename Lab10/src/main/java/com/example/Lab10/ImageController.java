package com.example.Lab10;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/image")

public class ImageController {
    @GetMapping("/adjustBrightness")
    public String adjustBrightness(@RequestParam("image") String base64Image, @RequestParam("brightness") int brightness) {
        try {
            // Dekodowanie obrazu base64
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            // Modyfikacja jasności obrazu
            BufferedImage brightenedImage = changeBrightness(image, brightness);

            // Kodowanie obrazu do formatu base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(brightenedImage, "png", baos);
            byte[] brightenedImageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(brightenedImageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/adjustBrightnessUnencoded")
    public ResponseEntity<byte[]> adjustBrightnessUnencoded(@RequestParam("image") String base64Image, @RequestParam("brightness") int brightness) {
        try {
            // Dekodowanie obrazu base64
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            // Modyfikacja jasności obrazu
            BufferedImage brightenedImage = changeBrightness(image, brightness);

            // Konwersja zmodyfikowanego obrazu do bajtów
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(brightenedImage, "png", baos);
            byte[] brightenedImageBytes = baos.toByteArray();

            // Tworzenie nagłówków odpowiedzi
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png");

            return new ResponseEntity<>(brightenedImageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private BufferedImage changeBrightness(BufferedImage original, int brightness) {
        BufferedImage result = new BufferedImage(
                original.getWidth(), original.getHeight(), original.getType());
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int rgb = original.getRGB(x, y);
                Color color = new Color(rgb);

                int r = color.getRed() + brightness;
                int g = color.getGreen() + brightness;
                int b = color.getBlue() + brightness;

                // Ograniczanie wartości kolorów do zakresu 0-255
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                Color newColor = new Color(r, g, b);
                result.setRGB(x, y, newColor.getRGB());
            }
        }
        return result;
    }

}
