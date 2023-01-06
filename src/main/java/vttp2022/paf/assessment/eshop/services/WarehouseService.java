package vttp2022.paf.assessment.eshop.services;

import java.io.StringReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JSpinner.DateEditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;
import vttp2022.paf.assessment.eshop.models.OrderStatus;
import vttp2022.paf.assessment.eshop.respositories.OrderRepository;

public class WarehouseService {

	@Autowired
	OrderRepository orderRepo;

	// You cannot change the method's signature
	// You may add one or more checked exceptions
	public OrderStatus dispatch(Order order) {

		// TODO: Task 4

		String url = "http://paf.chuklee.com/dispatch";

		RequestEntity<Void> req = RequestEntity
									.get(url)
									.accept(MediaType.APPLICATION_JSON)
									.build();
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> resp = template.exchange(req, String.class);
		String payload = resp.getBody();

		//Parse the String to JsonObject
		JsonReader reader = Json.createReader((new StringReader(payload)));

		JsonObject result = reader.readObject();
		String deliveryId = result.getString("deliveryId");
		String orderId = result.getString("orderId");
		
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setDeliveryId(deliveryId);
		orderStatus.setOrderId(orderId);


		return orderStatus;
		





		

	}
	


}
