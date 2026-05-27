alter table payments
add unique(id, order_id);
