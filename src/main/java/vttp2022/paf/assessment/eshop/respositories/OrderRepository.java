package vttp2022.paf.assessment.eshop.respositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.JsonObject;
import vttp2022.paf.assessment.eshop.models.LineItem;
import vttp2022.paf.assessment.eshop.models.Order;

import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

public class OrderRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;	
	// TODO: Task 3

	//Adding the items into lineitem table
	public void addLineItem (List<LineItem> lineItemList, String orderId) {

		List<Object[]> data = lineItemList
							.stream()
							.map(o -> {
								Object [] oa = new Object [3];
								oa[0] = o.getItem();
								oa[1] = o.getQuantity();
								oa[2] = orderId;
								return oa;
							})
							.toList();
		jdbcTemplate.batchUpdate(SQL_INSERT_LINEITEM, data);
	}

	public void addLineItem (Order order) {
        addLineItem(order.getLineItems(), order.getOrderId());
    }

	//Adding into order table
	public Boolean addOrders (Order order) {

		Integer add = jdbcTemplate.update(SQL_INSERT_ORDERS, order.getOrderId(), order.getDeliveryId(),
		order.getAddress(), order.getEmail(), order.getStatus(), order.getOrderDate(), order.getName());

		return add > 0;

	}

	@Transactional (rollbackFor = OrderException.class)
	public void addNewOrder (Order order, List<LineItem> lineItemList) throws OrderException {

		String orderId = UUID.randomUUID().toString().substring(0, 8);

		order.setOrderId(orderId);

		addOrders(order);

        addLineItem(lineItemList, order.getOrderId());
		
		throw new OrderException("Error: Fail to add Orders");

    }

	//TO be used in controller
	public static LineItem fromJson (JsonObject doc) {

        final LineItem l = new LineItem();
        l.setItem(doc.getString("item"));
        l.setQuantity(doc.getInt("quantity"));
        return l;
    }

	//Task 4: Inserting into order_status
	public Boolean addOrderStatus (String orderId, String deliveryId, String status, Date date) {

		Integer add = jdbcTemplate.update(SQL_INSERT_ORDER_STATUS_SUCCESSFUL, orderId, deliveryId, status, date);

		return add > 0;

	}


	//Task 4: Inserting if unsuccessful


	




}


