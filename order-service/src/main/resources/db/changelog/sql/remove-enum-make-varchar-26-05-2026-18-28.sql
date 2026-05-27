alter table orders
alter column status TYPE VARCHAR(64)
using status::text;

alter table orders
alter column status SET DEFAULT 'PENDING_PAYMENT'
