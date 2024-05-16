drop database if exists DBKinalExpress;
create database DBKinalExpress;

use DBKinalExpress;

create table Clientes(
	codigoCliente int not null,
    NITCliente varchar(10) not null,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    direccionCliente varchar(150),
    telefonoCliente varchar(8),
    correoCliente varchar(45),
    primary key PK_codigoCliente(codigoCliente)
);

create table Proveedores(
	codigoProveedor int not null,
    NITProveedor varchar(10) not null,
    nombreProveedor varchar(60),
    apellidoProveedor varchar(60),
    direccionProveedor varchar(150),
    razonSocial varchar(60),
    contactoPrincipal varchar(100),
    primary key PK_codigoProveedor(codigoProveedor)
);

-- --------- PROCEDIMIENTOS ALMACENADOS -------------
-- --------- CLIENTES -------------------------------
-- ----------Agregar Clientes -----------------------

Delimiter $$
	create procedure sp_AgregarClientes(in codigoCliente int, NITCliente varchar(10), nombreCliente varchar(50), apellidoCliente varchar(50),
    direccionCliente varchar (150), telefonoCliente varchar (8), correoCliente varchar(45))
		Begin
			Insert into Clientes(codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
            values (codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
		End $$
Delimiter ;

call sp_AgregarClientes (01, '52723542', 'Adrian', 'Benito', 'San Jose Pinula', '52723542', 'harolylunaforever@gmail.com');
call sp_AgregarClientes (02, '11400635', 'Luis', 'Pedro', 'Zona 3', '56014229', 'XxdmorentegonsalesxX@gmail.com');

-- ----------Listar Clientes -----------------------

Delimiter $$
	create procedure sp_ListarClientes()
    Begin
		select
			C.codigoCliente,
			C.NITCliente,
			C.nombreCliente,
			C.apellidoCliente,
			C.direccionCliente,
			C.telefonoCliente,
			C.correoCliente
        from Clientes C;
	End $$
Delimiter ;

call sp_ListarClientes();

-- ----------Buscar Clientes -----------------------

Delimiter $$
	create procedure sp_BuscarClientes(in codCli int)
    Begin
		select
			C.codigoCliente,
			C.NITCliente,
			C.nombreCliente,
			C.apellidoCliente,
			C.direccionCliente,
			C.telefonoCliente,
			C.correoCliente
		from Clientes C
        where codigoCliente = codCli;
	End $$
Delimiter ;

call sp_BuscarClientes(1);

-- ----------Eliminar Clientes -----------------------

Delimiter $$
	Create procedure sp_EliminarClientes(in codClie int)
		Begin
			Delete from Clientes
            where codigoCliente = codClie;
		End $$
Delimiter ;

call sp_EliminarClientes(1);

-- ----------Editar Clientes -----------------------
Delimiter $$
	create procedure sp_EditarClientes (in codCli int, nCliente varchar(10), nomClientes varchar(50), apCliente varchar(50),
    direcCliente varchar(150), telCliente varchar(8), corrCliente varchar(45))
		Begin
			Update Clientes C
				set
			C.NITCliente = nCliente,
			C.nombreCliente = nomClientes,
			C.apellidoCliente = apCliente,
			C.direccionCliente = direcCliente,
			C.telefonoCliente = telCliente,
			C.correoCliente = corrCliente
            where codigoCliente = codCli;
		End $$
Delimiter ;

call sp_EditarClientes(2, 'Mario', 'Pablo', 'Zona 3', '37405445', 'waza@gmail.com');
    