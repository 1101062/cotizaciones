select 

	cliente0_.id as id1_0_0_, 
	facturas1_.id as id1_1_1_, 

		cliente0_.apellido as apellido2_0_0_, 
		cliente0_.create_at as create_a3_0_0_, 
		cliente0_.email as email4_0_0_, 
		cliente0_.foto as foto5_0_0_, 
		cliente0_.nombre as nombre6_0_0_, 
		
		facturas1_.cliente_id as cliente_5_1_1_, 
		facturas1_.create_at as create_a2_1_1_, 
		facturas1_.descripcion as descripc3_1_1_, 
		facturas1_.observacion as observac4_1_1_, 
		facturas1_.cliente_id as cliente_5_1_0__, 
		facturas1_.id as id1_1_0__ 

	from gestors cliente0_

		inner join facturas facturas1_ on cliente0_.id=facturas1_.cliente_id 

	where cliente0_.id=?
