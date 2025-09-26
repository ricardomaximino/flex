package es.brasatech.fastfood.web.controller;

import es.brasatech.fastfood.web.dto.Customization;
import es.brasatech.fastfood.web.dto.CustomizationOption;
import es.brasatech.fastfood.web.dto.MenuData;
import es.brasatech.fastfood.web.dto.MenuProduct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @GetMapping
    public String sample2(Model model) {
        return "fastfood/menu";
    }

    @ModelAttribute
    private MenuData menu() {
        return new MenuData(combos(), food(), drinks());
    }

    @ModelAttribute
    private List<Customization> customizationOptions() {
        return List.of(
            new Customization("size", "Size", "radio", List.of(new CustomizationOption("Small", new BigDecimal("0.00")), new CustomizationOption("Medium", new BigDecimal("1.50")), new CustomizationOption("Large", new BigDecimal("3.00")))),
            new Customization("extras", "Add Extras", "checkbox", List.of(new CustomizationOption("Extra Cheese", new BigDecimal("1.00")), new CustomizationOption("Bacon", new BigDecimal("2.00")), new CustomizationOption("Avocado", new BigDecimal("1.50")), new CustomizationOption("Extra souce", new BigDecimal("0.50")))),
            new Customization("removals", "Remove Items", "checkbox", List.of(new CustomizationOption("No Onions", new BigDecimal("0.00")), new CustomizationOption("No Pickles", new BigDecimal("0.00")), new CustomizationOption("No Lettuce", new BigDecimal("0.00")), new CustomizationOption("No Tomato", new BigDecimal("0.00"))))
        );
    }


    private List<MenuProduct> combos() {
        return List.of(
            new MenuProduct("combo1", "Big Bite Combo", new BigDecimal("12.99"), "Double cheeseburger, large fries, and medium drink", "https://images.unsplash.com/photo-1553979459-d2229ba7433a?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo2", "Chicken Deluxe Combo", new BigDecimal("11.99"), "Crispy chicken burger, fries, and drink", "https://images.unsplash.com/photo-1606755962773-d324e9a13086?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo3", "Fish Supreme Combo", new BigDecimal("13.99"), "Fish fillet, fries, coleslaw, and drink", "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo4", "Veggie Paradise Combo", new BigDecimal("10.99"), "Plant-based burger, sweet potato fries, and drink", "https://images.unsplash.com/photo-1520072959219-c595dc870360?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo5", "BBQ Bacon Combo", new BigDecimal("14.99"), "BBQ bacon burger, onion rings, and drink", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo6", "Family Feast Combo", new BigDecimal("24.99"), "4 burgers, 2 large fries, 4 drinks", "https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=400&h=300&fit=crop", new String[] {"extras", "removals"})
        );
    }

    private List<MenuProduct> food() {
        return List.of(
            new MenuProduct("food1", "Classic Cheeseburger",new BigDecimal("8.99"),"Beef patty with cheese,lettuce,tomato,and special sauce","https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food2", "Crispy ChickenSandwich",new BigDecimal("7.99"),"Breaded chicken breast with mayo and pickles","https://images.unsplash.com/photo-1606755456206-b25206be902b?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food3", "French Fries",new BigDecimal("3.99"),"Golden crispy seasoned fries","https://images.unsplash.com/photo-1576107232684-1279f390859f?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("food4", "Chicken Nuggets",new BigDecimal("5.99"),"6 piece crispy chicken nuggets","https://images.unsplash.com/photo-1562967916-eb82221dfb92?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("food5", "Spicy Wings",new BigDecimal("9.99"),"8 buffalo wings with ranch dip","https://images.unsplash.com/photo-1608039755401-742074f0548d?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("food6", "Fish &Chips",new BigDecimal("11.99"),"Beer-battered cod with thick-cut chips","https://images.unsplash.com/photo-1544982503-9f984c14501a?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food7", "Onion Rings",new BigDecimal("4.99"),"Crispy beer-battered onion rings","https://images.unsplash.com/photo-1639024471283-03518883512d?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("food8", "Caesar Salad",new BigDecimal("6.99"),"Fresh romaine lettuce with caesar dressing","https://images.unsplash.com/photo-1512852939750-1305098529bf?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food9", "Hot Dog",new BigDecimal("4.99"),"All-beef hot dog with mustard and ketchup","https://images.unsplash.com/photo-1612392062798-2fa8ec40df87?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food10", "Mozzarella Sticks",new BigDecimal("6.99"),"6 crispy mozzarella sticks with marinara","https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop",new String[]{"size", "extras"})
        );
    }

    private List<MenuProduct> drinks() {
        return List.of(
            new MenuProduct("drink1","Coca Cola",new BigDecimal("2.99"),"Classic refreshing cola","https://images.unsplash.com/photo-1581006852262-e4307cf6283a?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink2","Sprite",new BigDecimal("2.99"),"Lemon-lime soda","https://images.unsplash.com/photo-1625772452859-1c03d5bf1137?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink3","Orange Juice",new BigDecimal("3.49"),"Fresh squeezed orange juice","https://images.unsplash.com/photo-1613478223719-2ab802602423?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink4","Iced Coffee",new BigDecimal("4.99"),"Cold brew coffee with ice","https://images.unsplash.com/photo-1517701604599-bb29b565090c?w=400&h=300&fit=crop",new String[]{"size','extras"}),
            new MenuProduct("drink5","Vanilla Milkshake",new BigDecimal("5.99"),"Creamy vanilla milkshake","https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400&h=300&fit=crop",new String[]{"size','extras"}),
            new MenuProduct("drink6","Chocolate Milkshake",new BigDecimal("5.99"),"Rich chocolate milkshake","https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop",new String[]{"size','extras"}),
            new MenuProduct("drink7","Strawberry Milkshake",new BigDecimal("5.99"),"Fresh strawberry milkshake","https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=400&h=300&fit=crop",new String[]{"size','extras"}),
            new MenuProduct("drink8","Lemonade",new BigDecimal("3.99"),"Fresh squeezed lemonade","https://images.unsplash.com/photo-1523371683702-bf38ae2cad34?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink9","Hot Coffee",new BigDecimal("2.99"),"Freshly brewed coffee","https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=300&fit=crop",new String[]{"size','extras"}),
            new MenuProduct("drink10","Energy Drink",new BigDecimal("4.99"),"Blue energy boost drink","https://images.unsplash.com/photo-1527960471264-932f39eb5846?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink11","Water Bottle",new BigDecimal("1.99"),"Pure spring water","https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=400&h=300&fit=crop",new String[]{}),
            new MenuProduct("drink12","Iced Tea",new BigDecimal("3.49"),"Sweet iced tea","https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400&h=300&fit=crop",new String[]{"size','extras"})
        );
    }
}
