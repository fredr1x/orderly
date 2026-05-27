alter table payments
alter column status TYPE VARCHAR(64)
using status::text;

alter table payments
alter column status set default 'PENDING_PAYMENT';

alter table payments
alter column currency TYPE VARCHAR(3)
using currency::text;

alter table payments
alter column currency set default 'KZT'
