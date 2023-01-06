package vttp2022.paf.assessment.eshop.controllers;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import vttp2022.paf.assessment.eshop.models.Customer;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.CustomerRepository;
import vttp2022.paf.assessment.eshop.respositories.OrderException;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;
import vttp2022.paf.assessment.eshop.services.WarehouseService;

@RestController
public class OrderController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	WarehouseService warehouseService;

	//TODO: Task 3

    //Task 3a
    @GetMapping (path= "/findcustomer/{name}")
    public ResponseEntity <String> findCustomerName (@PathVariable String name) {


        Optional<Customer> customer = customerRepository.findCustomerByName(name);
        Customer cus = customer.get();

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("name", cus.getName());
        objectBuilder.add("email", cus.getEmail());
        objectBuilder.add("address", cus.getAddress());

        JsonObject result = objectBuilder.build();

    
        if (cus.getName() == null) {

            return ResponseEntity 
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body("error: Customer " + name + "not found!");
        } else {

            return ResponseEntity 
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
        }
    }

    //Task 3d
    @PostMapping (path="/add/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <String> addOrder (@RequestBody String json) {

		JsonReader jsonReader = Json.createReader(new StringReader(json));

		JsonObject jsonObject = jsonReader.readObject();
		String name = jsonObject.getString("name");
		String address = jsonObject.getString("address");
		String email = jsonObject.getString("email");
		String status = jsonObject.getString("status");
		String orderId = jsonObject.getString("orderId");
		String deliveryId = jsonObject.getString("deliveryId");
		Date orderDate = new Date();

		
		JsonArray jsonArray = jsonObject.getJsonArray("lineitem");

		List<LineItem> lineItems = jsonArray.stream()
		.map(v -> (JsonObject)v)
		.map(v -> OrderRepository.fromJson(v))
		.toList();

		Order order = new Order();
		order.setName(name);
		order.setAddress(address);
		order.setEmail(email);
		order.setStatus(status);
		order.setOrderId(orderId);
		order.setDeliveryId(deliveryId);
		order.setOrderDate(orderDate);
		order.setLineItems(lineItems);

		
		
		try {
			orderRepository.addNewOrder(order, lineItems);
		} catch (OrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity 
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body("Error: Cannot add orders");
		}

		return ResponseEntity 
		.status(HttpStatus.OK)
		.contentType(MediaType.APPLICATION_JSON)
		.body("successfully added");

		
        
    }

	//Task 4
	@PostMapping (path = "http://paf.chuklee.com/dispatch/{orderId}") 
	public ResponseEntity <String> addOrderStatus (@RequestParam String json) {

		JsonReader jsonReader = Json.createReader(new StringReader(json));

		JsonObject jsonObject = jsonReader.readObject();
		String name = jsonObject.getString("name");
		String address = jsonObject.getString("address");
		String email = jsonObject.getString("email");
		String orderId = jsonObject.getString("orderId");
		String deliveryId = jsonObject.getString("deliveryId");
		Date orderDate = new Date();

		
		JsonArray jsonArray = jsonObject.getJsonArray("lineitem");

		List<LineItem> lineItems = jsonArray.stream()
		.map(v -> (JsonObject)v)
		.map(v -> OrderRepository.fromJson(v))
		.toList();

		Order order = new Order();
		order.setName(name);
		order.setAddress(address);
		order.setEmail(email);
		order.setOrderId(orderId);
		order.setDeliveryId(deliveryId);
		order.setOrderDate(orderDate);
		order.setLineItems(lineItems);

		OrderStatus orderStatus = warehouseService.dispatch(order);
		Date date = new Date();


		if (orderId.length() > 13) {

			String status = "dispatched";
			orderRepository.addOrderStatus(orderId, deliveryId, status, date);
			return ResponseEntity 
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body("Status: dispatched");

		} else {
			String status = "pending";
			orderRepository.addOrderStatus(orderId, deliveryId, status, date);
			return ResponseEntity 
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body("Status: pending");
		}

		
		


    }






	


}
