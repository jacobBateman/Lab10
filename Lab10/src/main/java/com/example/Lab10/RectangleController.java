package com.example.Lab10;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rectangle")
public class RectangleController {

    //Prywatna lista prostokątów
    private List<Rectangle> rectangles = new ArrayList<>();

    //Metoda dodawania prostokątów
    @PostMapping("/add")
    public void addRectangle(@RequestBody Rectangle rectangle){ //RequestBody odpowiada na żądania HTTP
        rectangles.add(rectangle);
    }

    //Metoda do zwracania listy prostokątów w formacie JSON
    @GetMapping("/list")
    public List<Rectangle> getRectangles(){
        return rectangles;
    }

    @GetMapping
    public Rectangle getRectangle(){
        Rectangle rectangle  = new Rectangle(10, 20, 30, 40, "red");
        return rectangle;
    }

    // Metoda do generowania kodu SVG z prostokątami na liście
    @GetMapping("/svg")
    public String generateSvg() {
        StringBuilder svgBuilder = new StringBuilder();
        svgBuilder.append("<svg width=\"800\" height=\"600\" xmlns=\"http://www.w3.org/2000/svg\">");

        for (Rectangle rectangle : rectangles) {
            svgBuilder.append(String.format(
                    "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" style=\"fill:%s;stroke:black;stroke-width:1\" />",
                    rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), rectangle.getColor()
            ));
        }

        svgBuilder.append("</svg>");
        return svgBuilder.toString();
    }

    // Metoda GET z argumentem typu int, zwracająca prostokąt w liście o podanym indeksie
    @GetMapping("/{index}")
    public ResponseEntity<Rectangle> getRectangle(@PathVariable int index) {
        if (index >= 0 && index < rectangles.size()) {
            return new ResponseEntity<>(rectangles.get(index), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Metoda PUT z argumentem typu int i argumentem typu Rectangle, modyfikująca istniejący prostokąt
    @PutMapping("/{index}")
    public ResponseEntity<Rectangle> updateRectangle(@PathVariable int index, @RequestBody Rectangle rectangle) {
        if (index >= 0 && index < rectangles.size()) {
            rectangles.set(index, rectangle);
            return new ResponseEntity<>(rectangle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Metoda DELETE z argumentem typu int, usuwająca prostokąt z listy z miejsca o podanym indeksie
    @DeleteMapping("/{index}")
    public ResponseEntity<Void> deleteRectangle(@PathVariable int index) {
        if (index >= 0 && index < rectangles.size()) {
            rectangles.remove(index);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
