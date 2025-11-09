package es.brasatech.fastfood.web.menu;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class MenuController {

    @ResponseBody
    @GetMapping("/api/menu")
    public Map<String, Object> getMenuData() {
        return Map.of("menuData", menu(), "customizationOptions", customizationOptions());
    }

    @ResponseBody
    @PostMapping("/api/save-cart")
    public Map<String, Object> postOrder(@RequestBody List<CartItemDto> cartItems, HttpSession session) {
        var orderNumber = UUID.randomUUID().toString();
        session.setAttribute("cart", cartItems);
        session.setAttribute("orderNumber", orderNumber);

        return Map.of("status", "success", "orderNumber", orderNumber);
    }

    @GetMapping("/menu")
    public String sample2(Model model) {
        String availableCustomizations = customizationOptions().stream().map(Customization::id).collect(Collectors.joining(","));
        model.addAttribute("availableCustomizations", availableCustomizations);
        return "fastfood/menu";
    }

    @GetMapping("/select-payment")
    public String selectPayment(HttpSession session, Model model) {
        var orderNumber = (String) session.getAttribute("orderNumber");
        var cartItems = (List<CartItemDto>) session.getAttribute("cart");
        var order = new OrderDto(orderNumber,
                LocalDateTime.now(),
                10D,
                BigDecimal.valueOf(10D),
                cartItems != null? cartItems : new ArrayList<>(),
                BigDecimal.valueOf(100D),
                BigDecimal.valueOf(1000D));
        model.addAttribute("order", order);
        return "fastfood/paymentSelection";
    }

    @GetMapping("/confirmation")
    public String confirmation(HttpSession session, Model model) {
        var orderNumber = (String) session.getAttribute("orderNumber");
        model.addAttribute("orderNumber", orderNumber);
        return "fastfood/confirmation";
    }

    @ModelAttribute
    private MenuData menu() {
        return new MenuData(combos(), food(), drinks());
    }

    @ModelAttribute
    private List<Customization> customizationOptions() {
        return List.of(
            new Customization("size",  CustomizationType.SIZE,"Size", CustomizationInputType.RADIO, List.of(new CustomizationOption("Small", new BigDecimal("0.00")), new CustomizationOption("Medium", new BigDecimal("1.50")), new CustomizationOption("Large", new BigDecimal("3.00")))),
            new Customization("extras", CustomizationType.ADD_EXTRA, "Add Extras", CustomizationInputType.CHECKBOX, List.of(new CustomizationOption("Extra Cheese", new BigDecimal("1.00")), new CustomizationOption("Bacon", new BigDecimal("2.00")), new CustomizationOption("Avocado", new BigDecimal("1.50")), new CustomizationOption("Extra souce", new BigDecimal("0.50")))),
            new Customization("removals", CustomizationType.REMOVE_ITEMS, "Remove Items", CustomizationInputType.CHECKBOX, List.of(new CustomizationOption("No Onions", new BigDecimal("0.00")), new CustomizationOption("No Pickles", new BigDecimal("0.00")), new CustomizationOption("No Lettuce", new BigDecimal("0.00")), new CustomizationOption("No Tomato", new BigDecimal("0.00")))),
            new Customization("ingredients", CustomizationType.TOGGLE_ITEMS, "Ingredients", CustomizationInputType.HIDDEN, List.of(new CustomizationOption("Onions", new BigDecimal("0.00")), new CustomizationOption("Pickles", new BigDecimal("0.00")), new CustomizationOption("Lettuce", new BigDecimal("0.00")), new CustomizationOption("Tomato", new BigDecimal("0.00"))))
        );
    }


    private List<MenuProduct> combos() {
        return List.of(
            new MenuProduct("combo1", "Big Bite Combo", new BigDecimal("12.99"), "Double cheeseburger, large fries, and medium drink", "https://images.pexels.com/photos/15076692/pexels-photo-15076692.jpeg?_gl=1*bi69dj*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDE1NTkkajU5JGwwJGgw", new String[] {"size", "ingredients"}),
            new MenuProduct("combo2", "Chicken Deluxe Combo", new BigDecimal("12.00"), "Crispy chicken burger, fries, and drink", "https://images.pexels.com/photos/2983101/pexels-photo-2983101.jpeg?_gl=1*t1bllg*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDI0NzckajQzJGwwJGgw", new String[] {"ingredients"}),
            new MenuProduct("combo3", "Fish Supreme Combo", new BigDecimal("13.99"), "Fish fillet, fries, coleslaw, and drink", "https://images.pexels.com/photos/18713428/pexels-photo-18713428.jpeg?_gl=1*xw0sgd*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDI2MjgkajMyJGwwJGgw", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo4", "Veggie Paradise Combo", new BigDecimal("10.99"), "Plant-based burger, sweet potato fries, and drink", "https://images.unsplash.com/photo-1520072959219-c595dc870360?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo5", "BBQ Bacon Combo", new BigDecimal("14.99"), "BBQ bacon burger, onion rings, and drink", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400&h=300&fit=crop", new String[] {"size", "extras", "removals"}),
            new MenuProduct("combo6", "Family Feast Combo", new BigDecimal("24.99"), "4 burgers, 2 large fries, 4 drinks", "https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=400&h=300&fit=crop", new String[] {"extras", "removals"})
        );
    }

    private List<MenuProduct> food() {
        return List.of(
            new MenuProduct("food1", "Classic Cheeseburger",new BigDecimal("8.99"),"Beef patty with cheese,lettuce,tomato,and special sauce","https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food2", "Crispy ChickenSandwich",new BigDecimal("7.99"),"Breaded chicken breast with mayo and pickles","https://images.pexels.com/photos/28828553/pexels-photo-28828553.jpeg?_gl=1*fbgy3s*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDIxNzckajI1JGwwJGgw",new String[]{"extras", "removals"}),
            new MenuProduct("food3", "French Fries",new BigDecimal("3.99"),"Golden crispy seasoned fries","https://images.unsplash.com/photo-1576107232684-1279f390859f?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("food4", "Chicken Nuggets",new BigDecimal("5.99"),"6 piece crispy chicken nuggets","https://images.unsplash.com/photo-1562967916-eb82221dfb92?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("food5", "Spicy Wings",new BigDecimal("9.99"),"8 buffalo wings with ranch dip","https://images.unsplash.com/photo-1608039755401-742074f0548d?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("food6", "Fish &Chips",new BigDecimal("11.99"),"Beer-battered cod with thick-cut chips","https://images.unsplash.com/photo-1544982503-9f984c14501a?w=400&h=300&fit=crop",new String[]{}),
            new MenuProduct("food7", "Onion Rings",new BigDecimal("4.99"),"Crispy beer-battered onion rings","https://images.unsplash.com/photo-1639024471283-03518883512d?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("food8", "Caesar Salad",new BigDecimal("6.99"),"Fresh romaine lettuce with caesar dressing","https://images.unsplash.com/photo-1512852939750-1305098529bf?w=400&h=300&fit=crop",new String[]{"extras", "removals"}),
            new MenuProduct("food9", "Hot Dog",new BigDecimal("4.99"),"All-beef hot dog with mustard and ketchup","https://images.pexels.com/photos/8946522/pexels-photo-8946522.jpeg?_gl=1*v0q01*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDIzOTYkajQzJGwwJGgw",new String[]{"extras", "removals"}),
            new MenuProduct("food10", "Mozzarella Sticks",new BigDecimal("6.99"),"6 crispy mozzarella sticks with marinara","https://images.pexels.com/photos/2619967/pexels-photo-2619967.jpeg?_gl=1*1ebv3ep*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDI4NDQkajIwJGwwJGgw",new String[]{"size", "extras"})
        );
    }

    private List<MenuProduct> drinks() {
        return List.of(
            new MenuProduct("drink1","Coca Cola",new BigDecimal("2.99"),"Classic refreshing cola","https://images.unsplash.com/photo-1581006852262-e4307cf6283a?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink2","Sprite",new BigDecimal("2.99"),"Lemon-lime soda","https://images.pexels.com/photos/31332092/pexels-photo-31332092.jpeg?_gl=1*1dijsg4*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDI5MzMkajExJGwwJGgw",new String[]{"size"}),
            new MenuProduct("drink3","Orange Juice",new BigDecimal("3.49"),"Fresh squeezed orange juice","https://images.unsplash.com/photo-1613478223719-2ab802602423?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink4","Iced Coffee",new BigDecimal("4.99"),"Cold brew coffee with ice","https://images.unsplash.com/photo-1517701604599-bb29b565090c?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("drink5","Vanilla Milkshake",new BigDecimal("5.99"),"Creamy vanilla milkshake","https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("drink6","Chocolate Milkshake",new BigDecimal("5.99"),"Rich chocolate milkshake","https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("drink7","Strawberry Milkshake",new BigDecimal("5.99"),"Fresh strawberry milkshake","https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("drink8","Lemonade",new BigDecimal("3.99"),"Fresh squeezed lemonade","https://images.pexels.com/photos/109275/pexels-photo-109275.jpeg?_gl=1*1tbhz8h*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDI3MjUkajM2JGwwJGgw",new String[]{"size"}),
            new MenuProduct("drink9","Hot Coffee",new BigDecimal("2.99"),"Freshly brewed coffee","https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=300&fit=crop",new String[]{"size", "extras"}),
            new MenuProduct("drink10","Energy Drink",new BigDecimal("4.99"),"Blue energy boost drink","https://images.unsplash.com/photo-1527960471264-932f39eb5846?w=400&h=300&fit=crop",new String[]{"size"}),
            new MenuProduct("drink11","Water Bottle",new BigDecimal("1.99"),"Pure spring water","https://images.pexels.com/photos/11789722/pexels-photo-11789722.jpeg?_gl=1*o353m2*_ga*OTk0NTc2NTE0LjE3NTg5MDEzMTA.*_ga_8JE65Q40S6*czE3NTg5MDEzMDkkbzEkZzEkdDE3NTg5MDMyMTgkajYwJGwwJGgw",new String[]{}),
            new MenuProduct("drink12","Iced Tea",new BigDecimal("3.49"),"Sweet iced tea","https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400&h=300&fit=crop",new String[]{"size", "extras"})
        );
    }
}
