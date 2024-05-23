-- Eliminar la base de datos si existe y luego crearla
DROP DATABASE IF EXISTS DBKinalExpress;
CREATE DATABASE DBKinalExpress;

USE DBKinalExpress;

-- Definición de la tabla Clientes
CREATE TABLE Clientes (
    codigoCliente INT NOT NULL,
    NITCliente VARCHAR(10) NOT NULL,
    nombreCliente VARCHAR(50) NOT NULL,
    apellidoCliente VARCHAR(50) NOT NULL,
    direccionCliente VARCHAR(150),
    telefonoCliente VARCHAR(15),
    correoCliente VARCHAR(45),
    PRIMARY KEY (codigoCliente)
);



-- Definición de la tabla Proveedores
CREATE TABLE Proveedores (
    codigoProveedor INT NOT NULL,
    NITProveedor VARCHAR(10) NOT NULL,
    nombreProveedor VARCHAR(60),
    apellidoProveedor VARCHAR(60),
    direccionProveedor VARCHAR(150),
    razonSocial VARCHAR(60),
    contactoProveedor VARCHAR(100),
    PRIMARY KEY (codigoProveedor)
);

-- Definición de la tabla Cargos
CREATE TABLE Cargos (
    codigoCargoEmpleado INT NOT NULL AUTO_INCREMENT,
    nombreCargo VARCHAR(45) NOT NULL,
    descripcionCargo VARCHAR(60) NOT NULL,
    PRIMARY KEY (codigoCargoEmpleado)
);

-- Definición de la tabla Compras
CREATE TABLE Compras (
    numeroDocumento INT NOT NULL AUTO_INCREMENT,
    fechaDocumento DATE NOT NULL,
    descripcion VARCHAR(60) NOT NULL,
    totalDocumento DECIMAL(10,2),
    PRIMARY KEY (numeroDocumento)
);

-- Definición de la tabla TipoProducto
CREATE TABLE TipoProducto (
    codigoTipoProducto INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(45) NOT NULL,
    PRIMARY KEY (codigoTipoProducto)
);

-- Definición de la tabla Empleados
CREATE TABLE Empleados (
    empleadoId INT NOT NULL,
    nombreEmpleado VARCHAR(30) NOT NULL,
    apellidoEmpleado VARCHAR(30) NOT NULL,
    sueldo DECIMAL(10,2) NOT NULL,
    horaEntrada TIME,
    horaSalida TIME,
    cargoId INT,
    PRIMARY KEY (empleadoId),
    CONSTRAINT FK_Cargos_Empleados FOREIGN KEY (cargoId) REFERENCES Cargos(codigoCargoEmpleado)
);

-- Definición de la tabla Facturas
CREATE TABLE Facturas (
    facturaId INT NOT NULL,
    fecha DATE,
    hora TIME,
    total DECIMAL(10,2) NOT NULL,
    codigoCliente INT,
    empleadoId INT,
    PRIMARY KEY (facturaId),
    CONSTRAINT FK_Clientes_Facturas FOREIGN KEY (codigoCliente) REFERENCES Clientes(codigoCliente),
    CONSTRAINT FK_Empleados_Facturas FOREIGN KEY (empleadoId) REFERENCES Empleados(empleadoId)
);

-- Definición de la tabla telefonoProveedor
CREATE TABLE telefonoProveedor (
    codigoTelefonoProveedor INT NOT NULL,
    numeroPrincipal VARCHAR(10) NOT NULL,
    numeroSecundario VARCHAR(10),
    observaciones VARCHAR(45),
    codigoProveedor INT,
    PRIMARY KEY (codigoTelefonoProveedor),
    CONSTRAINT FK_Proveedores_telefonoProveedor FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)
);

-- Definición de la tabla EmailProveedor
CREATE TABLE EmailProveedor (
    codigoEmailProveedor INT NOT NULL,
    emailProveedor VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),
    codigoProveedor INT,
    PRIMARY KEY (codigoEmailProveedor),
    CONSTRAINT FK_Proveedores_EmailProveedor FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)
);

-- Definición de la tabla Productos
CREATE TABLE Productos (
    productoId INT NOT NULL,
    imagenProducto LONGBLOB not null,
    nombreProducto VARCHAR(50) NOT NULL,
    descripcionProducto VARCHAR(100) NOT NULL,
    cantidadStock INT NOT NULL,
    precioVentaUnitario DECIMAL(10,2) NOT NULL,
    precioVentaMayor DECIMAL(10,2) NOT NULL,
    precioCompra DECIMAL(10,2) NOT NULL,
    codigoProveedor INT,
    codigoTipoProducto INT,
    PRIMARY KEY (productoId),
    CONSTRAINT FK_Proveedores_Productos FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor),
    CONSTRAINT FK_TipoProducto_Productos FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto)
);

-- Definición de la tabla detalleFactura
CREATE TABLE detalleFactura (
    detalleFacturaId INT NOT NULL,
    facturaId INT NOT NULL,
    productoId INT NOT NULL,
    PRIMARY KEY (detalleFacturaId),
    CONSTRAINT FK_Facturas_detalleFactura FOREIGN KEY (facturaId) REFERENCES Facturas(facturaId),
    CONSTRAINT FK_Productos_detalleFactura FOREIGN KEY (productoId) REFERENCES Productos(productoId)
);

-- Definición de la tabla detalleCompra
CREATE TABLE detalleCompra (
    detalleCompraId INT NOT NULL,
    cantidadCompra INT NOT NULL,
    productoId INT,
    numeroDocumento INT,
    PRIMARY KEY (detalleCompraId),
    CONSTRAINT FK_Compras_detalleCompra FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento),
    CONSTRAINT FK_Productos_detalleCompra FOREIGN KEY (productoId) REFERENCES Productos(productoId)
);

-- Crear los procedimientos almacenados

-- Agregar Clientes
DELIMITER $$
CREATE PROCEDURE sp_AgregarClientes(
    IN codigoCliente INT,
    IN NITCliente VARCHAR(10),
    IN nombreCliente VARCHAR(50),
    IN apellidoCliente VARCHAR(50),
    IN direccionCliente VARCHAR(150),
    IN telefonoCliente VARCHAR(15),
    IN correoCliente VARCHAR(45)
)
BEGIN
    INSERT INTO Clientes (codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente)
    VALUES (codigoCliente, NITCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
END$$
DELIMITER ;

-- Listar Clientes
DELIMITER $$
CREATE PROCEDURE sp_ListarClientes()
BEGIN
    SELECT * FROM Clientes;
END$$
DELIMITER ;

-- Buscar Clientes
DELIMITER $$
CREATE PROCEDURE sp_BuscarClientes(IN codCli INT)
BEGIN
    SELECT * FROM Clientes WHERE codigoCliente = codCli;
END$$
DELIMITER ;

-- Eliminar Clientes
DELIMITER $$
CREATE PROCEDURE sp_EliminarClientes(IN codClie INT)
BEGIN
    DELETE FROM Clientes WHERE codigoCliente = codClie;
END$$
DELIMITER ;

-- Editar Clientes
DELIMITER $$
CREATE PROCEDURE sp_EditarClientes(
    IN codCli INT, 
    IN nCliente VARCHAR(10), 
    IN nomClientes VARCHAR(50), 
    IN apCliente VARCHAR(50), 
    IN direcCliente VARCHAR(150), 
    IN telCliente VARCHAR(15), 
    IN corrCliente VARCHAR(45)
)
BEGIN
    UPDATE Clientes
    SET 
        NITCliente = nCliente,
        nombreCliente = nomClientes,
        apellidoCliente = apCliente,
        direccionCliente = direcCliente,
        telefonoCliente = telCliente,
        correoCliente = corrCliente
    WHERE codigoCliente = codCli;
END$$
DELIMITER ;

-- Agregar Proveedores
DELIMITER $$
CREATE PROCEDURE sp_AgregarProveedores(
    IN codigoProveedor INT,
    IN NITProveedor VARCHAR(10),
    IN nombreProveedor VARCHAR(60),
    IN apellidoProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoProveedor VARCHAR(100)
)
BEGIN
    INSERT INTO Proveedores (codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoProveedor)
    VALUES (codigoProveedor, NITProveedor, nombreProveedor, apellidoProveedor, direccionProveedor, razonSocial, contactoProveedor);
END$$
DELIMITER ;

-- Listar Proveedores
DELIMITER $$
CREATE PROCEDURE sp_ListarProveedores()
BEGIN
    SELECT * FROM Proveedores;
END$$
DELIMITER ;

-- Buscar Proveedores
DELIMITER $$
CREATE PROCEDURE sp_BuscarProveedores(IN codPro INT)
BEGIN
    SELECT * FROM Proveedores WHERE codigoProveedor = codPro;
END$$
DELIMITER ;

-- Eliminar Proveedores
DELIMITER $$
CREATE PROCEDURE sp_EliminarProveedores(IN codPro INT)
BEGIN
    DELETE FROM Proveedores WHERE codigoProveedor = codPro;
END$$
DELIMITER ;

-- Editar Proveedores
DELIMITER $$
CREATE PROCEDURE sp_EditarProveedores(
    IN codPro INT, 
    IN nPro VARCHAR(10), 
    IN nomPro VARCHAR(50), 
    IN apPro VARCHAR(50), 
    IN direcPro VARCHAR(150), 
    IN razSocial VARCHAR(60), 
    IN contP VARCHAR(100)
)
BEGIN
    UPDATE Proveedores
    SET 
        NITProveedor = nPro,
        nombreProveedor = nomPro,
        apellidoProveedor = apPro,
        direccionProveedor = direcPro,
        razonSocial = razSocial,
        contactoProveedor = contP
    WHERE codigoProveedor = codPro;
END$$
DELIMITER ;

-- Agregar CargoEmpleado
DELIMITER $$
CREATE PROCEDURE sp_AgregarCargoEmpleado(
    IN codigoCargoEmpleado INT,
    IN nombreCargo VARCHAR(45),
    IN descripcionCargo VARCHAR(60)
)
BEGIN
    INSERT INTO Cargos (codigoCargoEmpleado, nombreCargo, descripcionCargo)
    VALUES (codigoCargoEmpleado, nombreCargo, descripcionCargo);
END$$
DELIMITER ;

-- Listar CargoEmpleado
DELIMITER $$
CREATE PROCEDURE sp_ListarCargoEmpleado()
BEGIN
    SELECT * FROM Cargos;
END$$
DELIMITER ;

-- Buscar CargoEmpleado
DELIMITER $$
CREATE PROCEDURE sp_BuscarCargoEmpleado(IN codCemp INT)
BEGIN
    SELECT * FROM Cargos WHERE codigoCargoEmpleado = codCemp;
END$$
DELIMITER ;

-- Eliminar CargoEmpleado
DELIMITER $$
CREATE PROCEDURE sp_EliminarCargoEmpleado(IN codCemp INT)
BEGIN
    DELETE FROM Cargos WHERE codigoCargoEmpleado = codCemp;
END$$
DELIMITER ;

-- Editar CargoEmpleado
DELIMITER $$
CREATE PROCEDURE sp_EditarCargoEmpleado(
    IN codCemp INT, 
    IN nomCe VARCHAR(45), 
    IN descCe VARCHAR(60)
)
BEGIN
    UPDATE Cargos
    SET 
        nombreCargo = nomCe,
        descripcionCargo = descCe
    WHERE codigoCargoEmpleado = codCemp;
END$$
DELIMITER ;

-- Agregar Compras
DELIMITER $$
CREATE PROCEDURE sp_AgregarCompras(
    IN numeroDocumento INT,
    IN fechaDocumento DATE,
    IN descripcion VARCHAR(60),
    IN totalDocumento DECIMAL(10,2)
)
BEGIN
    INSERT INTO Compras (numeroDocumento, fechaDocumento, descripcion, totalDocumento)
    VALUES (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
END$$
DELIMITER ;

-- Listar Compras
DELIMITER $$
CREATE PROCEDURE sp_ListarCompras()
BEGIN
    SELECT * FROM Compras;
END$$
DELIMITER ;

-- Buscar Compras
DELIMITER $$
CREATE PROCEDURE sp_BuscarCompras(IN numComp INT)
BEGIN
    SELECT * FROM Compras WHERE numeroDocumento = numComp;
END$$
DELIMITER ;

-- Eliminar Compras
DELIMITER $$
CREATE PROCEDURE sp_EliminarCompras(IN numComp INT)
BEGIN
    DELETE FROM Compras WHERE numeroDocumento = numComp;
END$$
DELIMITER ;

-- Editar Compras
DELIMITER $$
CREATE PROCEDURE sp_EditarCompras(
    IN numComp INT, 
    IN fechComp DATE, 
    IN desComp VARCHAR(60), 
    IN totDoc DECIMAL(10,2)
)
BEGIN
    UPDATE Compras
    SET 
        fechaDocumento = fechComp,
        descripcion = desComp,
        totalDocumento = totDoc
    WHERE numeroDocumento = numComp;
END$$
DELIMITER ;

-- Agregar TipoProducto
DELIMITER $$
CREATE PROCEDURE sp_AgregarTipoProducto(
    IN codigoTipoProducto INT,
    IN descripcion VARCHAR(45)
)
BEGIN
    INSERT INTO TipoProducto (codigoTipoProducto, descripcion)
    VALUES (codigoTipoProducto, descripcion);
END$$
DELIMITER ;

-- Listar TipoProducto
DELIMITER $$
CREATE PROCEDURE sp_ListarTipoProducto()
BEGIN
    SELECT * FROM TipoProducto;
END$$
DELIMITER ;

-- Buscar TipoProducto
DELIMITER $$
CREATE PROCEDURE sp_BuscarTipoProducto(IN codTipP INT)
BEGIN
    SELECT * FROM TipoProducto WHERE codigoTipoProducto = codTipP;
END$$
DELIMITER ;

-- Eliminar TipoProducto
DELIMITER $$
CREATE PROCEDURE sp_EliminarTipoProducto(IN codTipP INT)
BEGIN
    DELETE FROM TipoProducto WHERE codigoTipoProducto = codTipP;
END$$
DELIMITER ;

-- Editar TipoProducto
DELIMITER $$
CREATE PROCEDURE sp_EditarTipoProducto(
    IN codTipP INT, 
    IN desTipP VARCHAR(45)
)
BEGIN
    UPDATE TipoProducto
    SET 
        descripcion = desTipP
    WHERE codigoTipoProducto = codTipP;
END$$
DELIMITER ;

-- Agregar Empleados
DELIMITER $$
CREATE PROCEDURE sp_AgregarEmpleados(
    IN empleadoId INT,
    IN nombreEmpleado VARCHAR(30),
    IN apellidoEmpleado VARCHAR(30),
    IN sueldo DECIMAL(10,2),
    IN horaEntrada TIME,
    IN horaSalida TIME,
    IN cargoId INT
)
BEGIN
    INSERT INTO Empleados (empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId)
    VALUES (empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId);
END$$
DELIMITER ;

-- Listar Empleados
DELIMITER $$
CREATE PROCEDURE sp_ListarEmpleados()
BEGIN
    SELECT * FROM Empleados;
END$$
DELIMITER ;

-- Buscar Empleados
DELIMITER $$
CREATE PROCEDURE sp_BuscarEmpleados(IN empId INT)
BEGIN
    SELECT * FROM Empleados WHERE empleadoId = empId;
END$$
DELIMITER ;

-- Eliminar Empleados
DELIMITER $$
CREATE PROCEDURE sp_EliminarEmpleados(IN empId INT)
BEGIN
    DELETE FROM Empleados WHERE empleadoId = empId;
END$$
DELIMITER ;

-- Editar Empleados
DELIMITER $$
CREATE PROCEDURE sp_EditarEmpleados(
    IN empId INT, 
    IN nomEmp VARCHAR(30), 
    IN apEmp VARCHAR(30), 
    IN suel DECIMAL(10,2), 
    IN horaEnt TIME, 
    IN horaSal TIME, 
    IN cargId INT
)
BEGIN
    UPDATE Empleados
    SET 
        nombreEmpleado = nomEmp,
        apellidoEmpleado = apEmp,
        sueldo = suel,
        horaEntrada = horaEnt,
        horaSalida = horaSal,
        cargoId = cargId
    WHERE empleadoId = empId;
END$$
DELIMITER ;

-- Agregar Facturas
DELIMITER $$
CREATE PROCEDURE sp_AgregarFacturas(
    IN facturaId INT,
    IN fecha DATE,
    IN hora TIME,
    IN total DECIMAL(10,2),
    IN codigoCliente INT,
    IN empleadoId INT
)
BEGIN
    INSERT INTO Facturas (facturaId, fecha, hora, total, codigoCliente, empleadoId)
    VALUES (facturaId, fecha, hora, total, codigoCliente, empleadoId);
END$$
DELIMITER ;

-- Listar Facturas
DELIMITER $$
CREATE PROCEDURE sp_ListarFacturas()
BEGIN
    SELECT * FROM Facturas;
END$$
DELIMITER ;

-- Buscar Facturas
DELIMITER $$
CREATE PROCEDURE sp_BuscarFacturas(IN factId INT)
BEGIN
    SELECT * FROM Facturas WHERE facturaId = factId;
END$$
DELIMITER ;

-- Eliminar Facturas
DELIMITER $$
CREATE PROCEDURE sp_EliminarFacturas(IN factId INT)
BEGIN
    DELETE FROM Facturas WHERE facturaId = factId;
END$$
DELIMITER ;

-- Editar Facturas
DELIMITER $$
CREATE PROCEDURE sp_EditarFacturas(
    IN factId INT, 
    IN fec DATE, 
    IN hor TIME, 
    IN tot DECIMAL(10,2), 
    IN codCli INT, 
    IN empId INT
)
BEGIN
    UPDATE Facturas
    SET 
        fecha = fec,
        hora = hor,
        total = tot,
        codigoCliente = codCli,
        empleadoId = empId
    WHERE facturaId = factId;
END$$
DELIMITER ;

-- Agregar Teléfono Proveedor
DELIMITER $$
CREATE PROCEDURE sp_AgregarTelefonoProveedor(
    IN codigoTelefonoProveedor INT,
    IN numeroPrincipal VARCHAR(10),
    IN numeroSecundario VARCHAR(10),
    IN observaciones VARCHAR(45),
    IN codigoProveedor INT
)
BEGIN
    INSERT INTO telefonoProveedor (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    VALUES (codigoTelefonoProveedor, numeroPrincipal, numeroSecundario, observaciones, codigoProveedor);
END$$
DELIMITER ;

-- Listar Teléfono Proveedor
DELIMITER $$
CREATE PROCEDURE sp_ListarTelefonoProveedor()
BEGIN
    SELECT * FROM telefonoProveedor;
END$$
DELIMITER ;

-- Buscar Teléfono Proveedor
DELIMITER $$
CREATE PROCEDURE sp_BuscarTelefonoProveedor(IN codTelProv INT)
BEGIN
    SELECT * FROM telefonoProveedor WHERE codigoTelefonoProveedor = codTelProv;
END$$
DELIMITER ;

-- Eliminar Teléfono Proveedor
DELIMITER $$
CREATE PROCEDURE sp_EliminarTelefonoProveedor(IN codTelProv INT)
BEGIN
    DELETE FROM telefonoProveedor WHERE codigoTelefonoProveedor = codTelProv;
END$$
DELIMITER ;

-- Editar Teléfono Proveedor
DELIMITER $$
CREATE PROCEDURE sp_EditarTelefonoProveedor(
    IN codTelProv INT, 
    IN numPrinc VARCHAR(10), 
    IN numSec VARCHAR(10), 
    IN obs VARCHAR(45), 
    IN codProv INT
)
BEGIN
    UPDATE telefonoProveedor
    SET 
        numeroPrincipal = numPrinc,
        numeroSecundario = numSec,
        observaciones = obs,
        codigoProveedor = codProv
    WHERE codigoTelefonoProveedor = codTelProv;
END$$
DELIMITER ;

-- Agregar Email Proveedor
DELIMITER $$
CREATE PROCEDURE sp_AgregarEmailProveedor(
    IN codigoEmailProveedor INT,
    IN emailProveedor VARCHAR(50),
    IN descripcion VARCHAR(100),
    IN codigoProveedor INT
)
BEGIN
    INSERT INTO EmailProveedor (codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor)
    VALUES (codigoEmailProveedor, emailProveedor, descripcion, codigoProveedor);
END$$
DELIMITER ;

-- Listar Email Proveedor
DELIMITER $$
CREATE PROCEDURE sp_ListarEmailProveedor()
BEGIN
    SELECT * FROM EmailProveedor;
END$$
DELIMITER ;

-- Buscar Email Proveedor
DELIMITER $$
CREATE PROCEDURE sp_BuscarEmailProveedor(IN codEmailProv INT)
BEGIN
    SELECT * FROM EmailProveedor WHERE codigoEmailProveedor = codEmailProv;
END$$
DELIMITER ;

-- Eliminar Email Proveedor
DELIMITER $$
CREATE PROCEDURE sp_EliminarEmailProveedor(IN codEmailProv INT)
BEGIN
    DELETE FROM EmailProveedor WHERE codigoEmailProveedor = codEmailProv;
END$$
DELIMITER ;

-- Editar Email Proveedor
DELIMITER $$
CREATE PROCEDURE sp_EditarEmailProveedor(
    IN codEmailProv INT, 
    IN emailProv VARCHAR(50), 
    IN descProv VARCHAR(100), 
    IN codProv INT
)
BEGIN
    UPDATE EmailProveedor
    SET 
        emailProveedor = emailProv,
        descripcion = descProv,
        codigoProveedor = codProv
    WHERE codigoEmailProveedor = codEmailProv;
END$$
DELIMITER ;

-- Agregar Productos
DELIMITER $$
CREATE PROCEDURE sp_AgregarProductos(
    IN productoId INT,
    IN imagenProducto LONGBLOB,
    IN nombreProducto VARCHAR(50),
    IN descripcionProducto VARCHAR(100),
    IN cantidadStock INT,
    IN precioVentaUnitario DECIMAL(10,2),
    IN precioVentaMayor DECIMAL(10,2),
    IN precioCompra DECIMAL(10,2),
    IN codigoProveedor INT,
    IN codigoTipoProducto INT
)
BEGIN
    INSERT INTO Productos (productoId, imagenProducto, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, codigoProveedor, codigoTipoProducto)
    VALUES (productoId, imagenProducto, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, codigoProveedor, codigoTipoProducto);
END$$
DELIMITER ;

-- Listar Productos
DELIMITER $$
CREATE PROCEDURE sp_ListarProductos()
BEGIN
    SELECT * FROM Productos;
END$$
DELIMITER ;

-- Buscar Productos
DELIMITER $$
CREATE PROCEDURE sp_BuscarProductos(IN prodId INT)
BEGIN
    SELECT * FROM Productos WHERE productoId = prodId;
END$$
DELIMITER ;

-- Eliminar Productos
DELIMITER $$
CREATE PROCEDURE sp_EliminarProductos(IN prodId INT)
BEGIN
    DELETE FROM Productos WHERE productoId = prodId;
END$$
DELIMITER ;

-- Editar Productos
DELIMITER $$
CREATE PROCEDURE sp_EditarProductos(
    IN prodId INT, 
    IN nomProd VARCHAR(50), 
    IN imgProd LONGBLOB,
    IN descProd VARCHAR(100), 
    IN cantStock INT, 
    IN precioUnit DECIMAL(10,2), 
    IN precioMay DECIMAL(10,2), 
    IN precioComp DECIMAL(10,2), 
    IN codProv INT, 
    IN codTipoProd INT
)
BEGIN
    UPDATE Productos
    SET 
        nombreProducto = nomProd,
        descripcionProducto = descProd,
        imagenProducto = imgProd,
        cantidadStock = cantStock,
        precioVentaUnitario = precioUnit,
        precioVentaMayor = precioMay,
        precioCompra = precioComp,
        codigoProveedor = codProv,
        codigoTipoProducto = codTipoProd
    WHERE productoId = prodId;
END$$
DELIMITER ;

-- Agregar Detalle Factura
DELIMITER $$
CREATE PROCEDURE sp_AgregarDetalleFactura(
    IN detalleFacturaId INT,
    IN facturaId INT,
    IN productoId INT
)
BEGIN
    INSERT INTO detalleFactura (detalleFacturaId, facturaId, productoId)
    VALUES (detalleFacturaId, facturaId, productoId);
END$$
DELIMITER ;

-- Listar Detalle Factura
DELIMITER $$
CREATE PROCEDURE sp_ListarDetalleFactura()
BEGIN
    SELECT * FROM detalleFactura;
END$$
DELIMITER ;

-- Buscar Detalle Factura
DELIMITER $$
CREATE PROCEDURE sp_BuscarDetalleFactura(IN detFactId INT)
BEGIN
    SELECT * FROM detalleFactura WHERE detalleFacturaId = detFactId;
END$$
DELIMITER ;

-- Eliminar Detalle Factura
DELIMITER $$
CREATE PROCEDURE sp_EliminarDetalleFactura(IN detFactId INT)
BEGIN
    DELETE FROM detalleFactura WHERE detalleFacturaId = detFactId;
END$$
DELIMITER ;

-- Editar Detalle Factura
DELIMITER $$
CREATE PROCEDURE sp_EditarDetalleFactura(
    IN detFactId INT, 
    IN factId INT, 
    IN prodId INT
)
BEGIN
    UPDATE detalleFactura
    SET 
        facturaId = factId,
        productoId = prodId
    WHERE detalleFacturaId = detFactId;
END$$
DELIMITER ;

-- Agregar Detalle Compra
DELIMITER $$
CREATE PROCEDURE sp_AgregarDetalleCompra(
    IN detalleCompraId INT,
    IN cantidadCompra INT,
    IN productoId INT,
    IN numeroDocumento INT
)
BEGIN
    INSERT INTO detalleCompra (detalleCompraId, cantidadCompra, productoId, numeroDocumento)
    VALUES (detalleCompraId, cantidadCompra, productoId, numeroDocumento);
END$$
DELIMITER ;

-- Listar Detalle Compra
DELIMITER $$
CREATE PROCEDURE sp_ListarDetalleCompra()
BEGIN
    SELECT * FROM detalleCompra;
END$$
DELIMITER ;

-- Buscar Detalle Compra
DELIMITER $$
CREATE PROCEDURE sp_BuscarDetalleCompra(IN detCompId INT)
BEGIN
    SELECT * FROM detalleCompra WHERE detalleCompraId = detCompId;
END$$
DELIMITER ;

-- Eliminar Detalle Compra
DELIMITER $$
CREATE PROCEDURE sp_EliminarDetalleCompra(IN detCompId INT)
BEGIN
    DELETE FROM detalleCompra WHERE detalleCompraId = detCompId;
END$$
DELIMITER ;

-- Editar Detalle Compra
DELIMITER $$
CREATE PROCEDURE sp_EditarDetalleCompra(
    IN detCompId INT, 
    IN cantComp INT, 
    IN prodId INT, 
    IN numDoc INT
)
BEGIN
    UPDATE detalleCompra
    SET 
        cantidadCompra = cantComp,
        productoId = prodId,
        numeroDocumento = numDoc
    WHERE detalleCompraId = detCompId;
END$$
DELIMITER ;