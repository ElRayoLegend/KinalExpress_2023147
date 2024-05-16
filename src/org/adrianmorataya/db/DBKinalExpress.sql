drop database if exists DBKinalExpress;
create database DBKinalExpress;

use DBKinalExpress;

create table Clientes(
	codigoCliente int not null,
    NITCliente varchar(10) not null,
    nombreCliente varchar(50) not null,
    apellidoCliente varchar(50) not null,
    direccionCliente varchar(150),
    telefonoCliente varchar(15),
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
    contactoProveedor varchar(100),
    primary key PK_codigoProveedor(codigoProveedor)
);

create table CargoEmpleado(
	codigoCargoEmpleado int not null auto_increment,
    nombreCargo varchar(45) not null,
    descripcionCargo varchar(60) not null,
    primary key PK_codigoCargoEmpleado(codigoCargoEmpleado)
);
 
create table Empleados(
	codigoEmpleado int not null auto_increment,
    nombreEmpleado varchar(50) not null,
    apellidoEmpleado varchar(50) not null,
    sueldo decimal(10,2) not null,
    direccionEmpleado varchar(150) not null,
    turno varchar(15) not null,
    CargoEmpleado_codigoCargoEmpleado int not null,
    primary key PK_codigoEmpleado(codigoEmpleado),
    constraint FK_Empleados_CargoEmpleado foreign key Empleados(CargoEmpleado_codigoCargoEmpleado)
		references CargoEmpleado(codigoCargoEmpleado)
);
create table TelefonoProveedor(
	codigoTelefonoProveedor int not null auto_increment,
    numeroPrincipal varchar(8) not null,
    numeroSecundario varchar(8),
    observaciones varchar(45),
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoTelefonoProveedor(codigoTelefonoProveedor),
    constraint FK_telefonoProveedor_Proveedores foreign key telefonoProveedor(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor)
);
 
create table EmailProveedor(
	codigoEmailProveedor int not null auto_increment,
    emailProveedor varchar(50) not null,
    descripcion varchar(100) not null,
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoEmailProveedor(codigoEmailProveedor),
    constraint FK_EmailProveedor_Proveedores foreign key EmailProveedor(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor)
);
 
 
create table Compras(
	numeroDocumento int not null auto_increment,
    fechaDocumento date not null,
    descripcion varchar(60) not null,
    totalDocumento decimal(10,2),
    primary key PK_numeroDocumento(numeroDocumento)
);
 
create table TipoProducto(
	codigoTipoProducto int not null auto_increment,
    descripcion varchar(45) not null,
    primary key PK_codigoTipoProducto(codigoTipoProducto)
);
 
create table Productos(
	codigoProducto varchar(15) not null,
    descripcionProducto varchar(45) not null,
    precioUnitario decimal(10,2),
    precioDocena decimal(10,2),
    precioMayor decimal(10,2),
    imagenProducto varchar(45),
    existencia int,
    TipoProducto_codigoTipoProducto int not null,
    Proveedores_codigoProveedor int not null,
    primary key PK_codigoProducto(codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(TipoProducto_codigoTipoProducto)
		references TipoProducto(codigoTipoProducto),
    constraint FK_Productos_Proveedores foreign key Productos(Proveedores_codigoProveedor)
		references Proveedores(codigoProveedor)
);
 
create table DetalleCompra(
	codigoDetalleCompra int not null auto_increment,
    costoUnitario decimal(10,2) not null,
    cantidad int not null,
    Productos_codigoProducto varchar(15) not null,
    Compras_numeroDocumento int not null,
    primary key PK_codigoDetalleCompra(codigoDetalleCompra),
    constraint FK_DetalleCompra_Compras foreign key DetalleCompra(Compras_numeroDocumento)
		references Compras(numeroDocumento),
	constraint FK_DetalleCompra_Productos foreign key DetalleCompra(Productos_codigoProducto)
		references Productos(codigoProducto)
);
 
create table Factura(
	numeroFactura int not null auto_increment,
    estado varchar(50) not null,
    totalFactura decimal(10,2),
    fechaFactura varchar(45),
    Clientes_codigoCliente int not null,
    Empleados_codigoEmpleado int not null,
    primary key PK_numeroFactura(numeroFactura),
    constraint FK_Factura_Clientes foreign key Factura(Clientes_codigoCliente)
		references Clientes(codigoCliente),
    constraint FK_Factura_Empleados foreign key Factura(Empleados_codigoEmpleado)
		references Empleados(codigoEmpleado)
);
 
create table DetalleFactura(
	codigoDetalleFactura int not null auto_increment,
    precioUnitario decimal(10,2),
    cantidad int not null,
    Factura_numeroFactura int not null,
    Productos_codigoProducto varchar(15) not null,
    primary key PK_codigoDetalleFactura(codigoDetalleFactura),
    constraint FK_DetalleFactura_Compras foreign key DetalleFactura(Factura_numeroFactura)
		references Factura(numeroFactura),
	constraint FK_DetalleFactura_Productos foreign key DetalleFactura(Productos_codigoProducto)
		references Productos(codigoProducto)
);

-- --------- PROCEDIMIENTOS ALMACENADOS -------------
-- --------- CLIENTES -------------------------------
-- ----------Agregar Clientes -----------------------

Delimiter $$
	create procedure sp_AgregarClientes(in codigoCliente int, NITCliente varchar(10), nombreCliente varchar(50), apellidoCliente varchar(50),
    direccionCliente varchar (150), telefonoCliente varchar (15), correoCliente varchar(45))
		Begin
			Insert into Clientes(codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
            values (codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
		End $$
Delimiter ;

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

-- ----------Eliminar Clientes -----------------------

Delimiter $$
	Create procedure sp_EliminarClientes(in codClie int)
		Begin
			Delete from Clientes
            where codigoCliente = codClie;
		End $$
Delimiter ;

-- ----------Editar Clientes -----------------------
Delimiter $$
	create procedure sp_EditarClientes (in codCli int, nCliente varchar(10), nomClientes varchar(50), apCliente varchar(50),
    direcCliente varchar(150), telCliente varchar(15), corrCliente varchar(45))
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

-- ----------Agregar Proveedores -----------------------

Delimiter $$
	create procedure sp_AgregarProveedores(in codigoProveedor int, NITProveedor varchar(10), nombreProveedor varchar(50), apellidoProveedor varchar(50),
    direccionProveedor varchar (150), razonSocial varchar (60), contactoProveedor varchar(100))
		Begin
			Insert into Proveedores(codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoProveedor)
            values (codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoProveedor);
		End $$
Delimiter ;

-- ----------Listar Proveedores -----------------------

Delimiter $$
	create procedure sp_ListarProveedores()
    Begin
		select
			P.codigoProveedor,
			P.NITProveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial,
			P.contactoProveedor
        from Proveedores P;
	End $$
Delimiter ;

-- ----------Buscar Proveedores -----------------------

Delimiter $$
	create procedure sp_BuscarProveedores(in codPro int)
    Begin
		select
			P.codigoProveedor,
			P.NITProveedor,
			P.nombreProveedor,
			P.apellidoProveedor,
			P.direccionProveedor,
			P.razonSocial,
			P.contactoProveedor
		from Proveedores P
        where codigoProveedor = codPro;
	End $$
Delimiter ;

-- ----------Eliminar Proveedores -----------------------

Delimiter $$
	Create procedure sp_EliminarProveedores(in codPro int)
		Begin
			Delete from Proveedores
            where codigoProveedor = codPro;
		End $$
Delimiter ;

-- ----------Editar Proveedores -----------------------
Delimiter $$
	create procedure sp_EditarProveedores (in codPro int, nPro varchar(10), nomPro varchar(50), apPro varchar(50),
    direcPro varchar(150), razSocial varchar(8), contP varchar(45))
		Begin
			Update Proveedores P
				set
			P.NITProveedor = nPro,
			P.nombreProveedor = nomPro,
			P.apellidoProveedor = apPro,
			P.direccionProveedor = direcPro,
			P.razonSocial = razSocial,
			P.contactoProveedor = contP
            where codigoProveedor = codPro;
		End $$
Delimiter ;


	codigoEmpleado int not null auto_increment,
    nombreEmpleado varchar(50) not null,
    apellidoCliente varchar(50) not null,
    sueldo decimal(10,2) not null,
    direccionEmpleado varchar(150) not null,
    turno varchar(15) not null,
    CargoEmpleado_codigoCargoEmpleado int not null,
    
    -- ----------Agregar Empleados -----------------------

Delimiter $$
	create procedure sp_AgregarEmpleados(in codigoEmpleado int, nombreEmpleado varchar(50), apellidoEmpleado varchar(50), sueldo decimal(10, 2),
    direccionEmpleado varchar (150), turno varchar (15), CargoEmpleado_codigoCargoEmpleado int)
		Begin
			Insert into Empleados(codigoEmpleado, nombreEmpleado, apellidoEmpleado, sueldo, direccionEmpleado, turno, CargoEmpleado_codigoCargoEmpleado)
            values (codigoEmpleado, nombreEmpleado, apellidoEmpleado, sueldo, direccionEmpleado, turno, CargoEmpleado_codigoCargoEmpleado);
		End $$
Delimiter ;

-- ----------Listar Empleados -----------------------

Delimiter $$
	create procedure sp_ListarEmpleados()
    Begin
		select
			E.codigoEmpleado,
			E.nombreEmpleado,
			E.apellidoEmpleado,
			E.sueldo,
			E.direccionEmpleado,
			E.turno,
			E.CargoEmpleado_codigoCargoEmpleado
        from Empleados E;
	End $$
Delimiter ;

-- ----------Buscar Empleados -----------------------

Delimiter $$
	create procedure sp_BuscarEmpleados(in codEmp int)
    Begin
		select
			E.codigoEmpleado,
			E.nombreEmpleado,
			E.apellidoEmpleado,
			E.sueldo,
			E.direccionEmpleado,
			E.turno,
			E.CargoEmpleado_codigoCargoEmpleado
		from Empleados E
        where codigoEmpleado = codEmp;
	End $$
Delimiter ;

-- ----------Eliminar Empleados -----------------------

Delimiter $$
	Create procedure sp_EliminarEmpleados(in codEmp int)
		Begin
			Delete from Empleados
            where codigoEmpleado = codEmp;
		End $$
Delimiter ;

-- ----------Editar Empelados -----------------------
Delimiter $$
	create procedure sp_EditarEmpelados (in codEmp int, nomEmp varchar(50), apeEmp varchar(50), suelEmp decimal(10,2),
    dirEmp varchar(150), turnEmp varchar(15), carEmp int)
		Begin
			Update Empelados E
				set
			E.codigoEmpleado = codEmp,
			E.nombreEmpleado = nomEmp,
			E.apellidoEmpleado = apeEmp,
			E.sueldo = suelEmp,
			E.direccionEmpleado = dirEmp,
			E.turno = turnEmp,
			E.CargoEmpleado_codigoCargoEmpleado = carEmp
            where codigoEmpleado = codEmp;
		End $$
Delimiter ;
    
    -- ----------Agregar CargoEmpleado -----------------------

Delimiter $$
	create procedure sp_AgregarCargoEmpleado(in codigoCargoEmpleado int, nombreCargo varchar(45), descripcionCargo varchar(60))
		Begin
			Insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
            values (codigoCargoEmpleado, nombreCargo, descripcionCargo);
		End $$
Delimiter ;

-- ----------Listar CargoEmpleado -----------------------

Delimiter $$
	create procedure sp_ListarCargoEmpleado()
    Begin
		select
			CE.codigoCargoEmpleado,
			CE.nombreCargo,
			CE.descripcionCargo
        from CargoEmpleado CE;
	End $$
Delimiter ;

-- ----------Buscar CargoEmpleado -----------------------

Delimiter $$
	create procedure sp_BuscarCargoEmpleado(in codCemp int)
    Begin
		select
			CE.codigoCargoEmpleado,
			CE.nombreCargo,
			CE.descripcionCargo
		from CargoEmpleado CE
        where codigoCargoEmpleado = codCemp;
	End $$
Delimiter ;

-- ----------Eliminar CargoEmpleado -----------------------

Delimiter $$
	Create procedure sp_EliminarCargoEmpleado(in codCemp int)
		Begin
			Delete from CargoEmpleado
            where codigoCargoEmpleado = codCemp;
		End $$
Delimiter ;

-- ----------Editar CargoEmpleado -----------------------
Delimiter $$
	create procedure sp_EditarCargoEmpleado(in codCemp int, nomCe varchar(45), descCe varchar(60))
		Begin
			Update CargoEmpleado CE
				set
			CE.codigoCargoEmpleado = codCemp,
			CE.nombreCargo = nomCe,
			CE.descripcionCargo = descCe
            where codigoCargoEmpleado = codCemp;
		End $$
Delimiter ;
    
    -- ----------Agregar Compras -----------------------

Delimiter $$
	create procedure sp_AgregarCompras(in numeroDocumento int, fechaDocumento date, descripcion varchar(60), totalDocumento decimal(10,2))
		Begin
			Insert into Compras(numeroDocumento, fechaDocumento, descripcion, totalDocumento)
            values (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
		End $$
Delimiter ;

-- ----------Listar Compras -----------------------

Delimiter $$
	create procedure sp_ListarCompras()
    Begin
		select
			CO.numeroDocumento,
			CO.fechaDocumento,
			CO.descripcion,
            CO.totalDocumento
        from Compras CO;
	End $$
Delimiter ;

-- ----------Buscar Compras -----------------------

Delimiter $$
	create procedure sp_BuscarCompras(in numComp int)
    Begin
		select
			CO.numeroDocumento,
			CO.fechaDocumento,
			CO.descripcion,
            CO.totalDocumento
		from Compras CO
        where numeroDocumento = numComp;
	End $$
Delimiter ;

-- ----------Eliminar Compras -----------------------

Delimiter $$
	Create procedure sp_EliminarCompras(in numComp int)
		Begin
			Delete from Compras
            where numeroDocumento = numComp;
		End $$
Delimiter ;

-- ----------Editar Compras -----------------------
Delimiter $$
	create procedure sp_EditarCompras(in numComp int, fechComp date, desComp varchar(60), totDoc decimal(10,2))
		Begin
			Update Compras CO
				set
			CO.numeroDocumento = numComp,
			CO.fechaDocumento = fechComp,
			CO.descripcion = desComp,
            CO.totalDocumento = totDoc
            where numeroDocumento = numComp;
		End $$
Delimiter ;

TipoProducto(
	codigoTipoProducto int not null auto_increment,
    descripcion varchar(45) not null,
    
    -- ----------Agregar TipoProducto -----------------------

Delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int, descripcion varchar(45))
		Begin
			Insert into TipoProducto(codigoTipoProducto, descripcion)
            values (codigoTipoProducto, descripcion);
		End $$
Delimiter ;

-- ----------Listar TipoProducto -----------------------

Delimiter $$
	create procedure sp_ListarTipoProducto()
    Begin
		select
			TP.codigoTipoProducto,
			TP.descripcion
        from TipoProducto TP;
	End $$
Delimiter ;

-- ----------Buscar TipoProducto -----------------------

Delimiter $$
	create procedure sp_BuscarTipoProducto(in codTP int)
    Begin
		select
			TP.codigoTipoProducto,
			TP.descripcion
		from TipoProducto CO
        where codigoTipoProducto = codTP;
	End $$
Delimiter ;

-- ----------Eliminar TipoProducto -----------------------

Delimiter $$
	Create procedure sp_EliminarTipoProducto(in codTP int)
		Begin
			Delete from TipoProducto
            where codigoTipoProducto = codTP;
		End $$
Delimiter ;

-- ----------Editar TipoProducto -----------------------
Delimiter $$
	create procedure sp_EditarTipoProducto(in codTP int, descTP varchar(45))
		Begin
			Update TipoProducto TP
				set
			TP.codigoTipoProducto = codTP,
			TP.descripcion = descTP
            where codigoTipoProducto = codTP;
		End $$
Delimiter ;