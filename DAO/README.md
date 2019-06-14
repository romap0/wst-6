# Data access for shops.

## Entity

```sql
create table shops
(
  id           bigserial not null
    constraint shops_pkey
    primary key,
  name         varchar(200),
  isActive     boolean,
  city         varchar(200),
  address      varchar(200),
  type         varchar(200)
);
```
