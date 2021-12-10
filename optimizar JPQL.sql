select 

	factura0_.id as id1_1_0_, 
	cliente1_.id as id1_0_1_, 
	items2_.id as id1_2_2_, 
	producto3_.id as id1_3_3_, 

		factura0_.cliente_id as cliente_5_1_0_, 
		factura0_.create_at as create_a2_1_0_, 
		factura0_.descripcion as descripc3_1_0_, 
		factura0_.observacion as observac4_1_0_, 

		cliente1_.apellido as apellido2_0_1_, 
		cliente1_.create_at as create_a3_0_1_, 
		cliente1_.email as email4_0_1_, 
		cliente1_.foto as foto5_0_1_, 
		cliente1_.nombre as nombre6_0_1_, 

		items2_.cantidad as cantidad2_2_2_, 
		items2_.producto_id as producto3_2_2_, 
		items2_.factura_id as factura_4_2_0__, 
		items2_.id as id1_2_0__, 

		producto3_.create_at as create_a2_3_3_, 
		producto3_.nombre as nombre3_3_3_, 
		producto3_.precio as precio4_3_3_ 

	from facturas factura0_ 

		inner join gestors cliente1_ on factura0_.cliente_id=cliente1_.id
		inner join facturas_items items2_ on factura0_.id=items2_.factura_id 
		inner join productos producto3_ on items2_.producto_id=producto3_.id 

	where factura0_.id=?
