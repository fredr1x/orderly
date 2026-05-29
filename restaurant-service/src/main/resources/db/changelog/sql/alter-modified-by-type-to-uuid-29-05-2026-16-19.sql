alter table restaurant_orders drop column modified_by;

alter table restaurant_orders add column modified_by uuid;
