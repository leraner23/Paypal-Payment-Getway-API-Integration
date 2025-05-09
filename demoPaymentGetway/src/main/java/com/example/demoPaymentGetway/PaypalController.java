package com.example.demoPaymentGetway;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaypalController {
@Autowired
PaypalService service;


public static final String SUCCESS_URL = "pay/success";
public static final String CANCEL_URL = "pay/cancel";

@GetMapping("/")
    public String home(Model model){
    model.addAttribute("order", new Order());
    return  "home";
}

    @PostMapping("/pay")
    public String payment(@ModelAttribute("order") Order order) {
        try {
            Payment payment = service.createPayment(
                    order.getTotal(),
                    order.getCurrency(),
                    order.getMethod(),
                    order.getIntent(),
                    order.getDescription(),
                    "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL
            );

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

@GetMapping(value = CANCEL_URL)
    public String cancelPay(){
    return "cancel";
}
    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                System.out.println("Payment approved: " + payment.toJSON());
                return "success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }


}
