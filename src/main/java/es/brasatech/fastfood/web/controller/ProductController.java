package es.brasatech.fastfood.web.controller;

import es.brasatech.fastfood.web.dto.ProductDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {


    @GetMapping
    public String productForm(Model model) {
        model.addAttribute("product", ProductDto.empty());
        return "fastfood/product";
    }

    @PostMapping
    public String submitProduct(Model model, @ModelAttribute ProductDto productDto, BindingResult errors) {
        if(errors.hasErrors()) {
            throw new RuntimeException("Errors on biding the message attribute");
        }
        model.addAttribute("products", List.of(productDto));
        return "fastfood/products";
    }

    @GetMapping("/list")
    public String productList(Model model) {
        record MenuItem(String id, String name, BigDecimal price, String image){}
        var list = List.of(
                new MenuItem("01", "Kebab grande", new BigDecimal("7.50"), "imageL.jpg"),
                new MenuItem("02", "Kebab mediano", new BigDecimal("5.50"), "imageM.jpg"),
                new MenuItem("03", "Kebab pequeño", new BigDecimal("4.50"), "imageS.jpg")
                );
        model.addAttribute("products", list);
        return "fastfood/products";
    }


}
