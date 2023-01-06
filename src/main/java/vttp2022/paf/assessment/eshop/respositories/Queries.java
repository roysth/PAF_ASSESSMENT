package vttp2022.paf.assessment.eshop.respositories;

public class Queries {

    //Find customer by name
    //select * from customers where name = "fred"
    public static final String SQL_FIND_CUSTOMER_BY_NAME = "select * from customers where name = ?";


    //Insert into Customers table
    //insert into customers values ('fred', '201 Cobblestone Lane', 'fredflintstone@bedrock.com');
    public static final String SQL_INSERT_CUSTOMERS = "insert into customers values (?,?,?)";

    //Insert into Lineitem table
    public static final String SQL_INSERT_LINEITEM = "insert into lineitem (item, quantity, orderId) values (?,?,?)";


    //Insert into order table
    public static final String SQL_INSERT_ORDERS = "insert into orders (orderId, deliveryId, address, email, status, orderDate, name) values (?, ?, ?, ?, ?, ?, ?)";
    
    //Insert into order_status table (For successful dispatch)
    public static final String SQL_INSERT_ORDER_STATUS_SUCCESSFUL = "insert into order_status (order_id, delivery_id, status, status_update) values (?, ?, ?, ?)";


    //Insert into order_status table 
    public static final String SQL_INSERT_ORDER_STATUS_UNSUCCESSFUL = "insert into order_status (order_id, delivery_id, status, status_update) values (?, ?, ?, ?)";

    




}


