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

call sp_AgregarClientes(1, 52723542, 'Adrian', 'Morataya', '3ra calle, 340', '37406554', 'ssoto-2023147@kinal.edu.gt');
call sp_AgregarClientes(2, 12345678, 'Maria', 'Gonzalez', 'Avenida Principal, 123', '98765432', 'maria@gmail.com');
call sp_AgregarClientes(3, 87654321, 'Juan', 'Martinez', 'Calle Secundaria, 456', '45678901', 'juan@gmail.com');
call sp_AgregarClientes(4, 23456789, 'Laura', 'Lopez', 'Calle Principal, 789', '34567890', 'laura@gmail.com');
call sp_AgregarClientes(5, 34567890, 'Carlos', 'Perez', 'Avenida Central, 567', '23456789', 'carlos@gmail.com');
call sp_AgregarClientes(6, 45678901, 'Ana', 'Rodriguez', 'Calle Central, 890', '12345678', 'ana@gmail.com');
call sp_AgregarClientes(7, 56789012, 'Pedro', 'Sanchez', 'Avenida Norte, 678', '01234567', 'pedro@gmail.com');
call sp_AgregarClientes(8, 67890123, 'Sofia', 'Gomez', 'Calle Norte, 901', '89012345', 'sofia@gmail.com');
call sp_AgregarClientes(9, 78901234, 'Luis', 'Hernandez', 'Avenida Sur, 234', '78901234', 'luis@outlook.com');
call sp_AgregarClientes(10, 89012345, 'Elena', 'Vasquez', 'Calle Sur, 567', '67890123', 'elena@gmail.com');

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

call sp_AgregarProveedores(1, 56793201, 'Dylan', 'Barrientos', '5a calle, 720, zona 3', 'Alimentos&Bebidas.tm', 'DylBarr@gmail.com');
call sp_AgregarProveedores(2, 12345678, 'Marcela', 'Gutierrez', '3ra Avenida, 456, Zona 1', 'SuministrosCorp', 'marce_guti@gmail.com');
call sp_AgregarProveedores(3, 87654321, 'Roberto', 'Perez', '2da Calle, 789, Zona 2', 'Tech Solutions', 'robperez@gmail.com');
call sp_AgregarProveedores(4, 23456789, 'Camila', 'Lopez', '1ra Avenida, 890, Zona 4', 'ElectroHogar SA', 'camilalopez@gmail.com');
call sp_AgregarProveedores(5, 34567890, 'Diego', 'Martinez', '4ta Calle, 123, Zona 5', 'IndustriaPlastica Ltda', 'diegomartinez@gmail.com');
call sp_AgregarProveedores(6, 45678901, 'Laura', 'Hernandez', '6ta Avenida, 456, Zona 6', 'MueblesComfort', 'laurahernandez@gmail.com');
call sp_AgregarProveedores(7, 56789012, 'Carlos', 'Gomez', '7ma Calle, 789, Zona 7', 'TextilArte', 'carlosgomez@gmail.com');
call sp_AgregarProveedores(8, 67890123, 'Ana', 'Rodriguez', '8va Avenida, 901, Zona 8', 'ConstruccionesRapidas', 'anarodriguez@gmail.com');
call sp_AgregarProveedores(9, 78901234, 'Pedro', 'Sanchez', '9na Calle, 234, Zona 9', 'PapeleriaModerna', 'pedrosanchez@gmail.com');
call sp_AgregarProveedores(10, 89012345, 'Sofia', 'Vasquez', '10ma Avenida, 567, Zona 10', 'ElectrodomesticosExpress', 'sofiavasquez@gmail.com');

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

call sp_AgregarCargoEmpleado(1, 'Jefe', 'El es el encargado de liderar a los demas cargos.');
call sp_AgregarCargoEmpleado(2, 'Asistente Administrativo', 'Encargado de apoyar en labores administrativas y logísticas.');
call sp_AgregarCargoEmpleado(3, 'Contador', 'Responsable de llevar los registros contables de la empresa.');
call sp_AgregarCargoEmpleado(4, 'Técnico de Soporte', 'Encargado de brindar soporte técnico a los usuarios.');
call sp_AgregarCargoEmpleado(5, 'Diseñador Gráfico', 'Responsable de la creación y diseño de material gráfico.');
call sp_AgregarCargoEmpleado(6, 'Analista de Marketing', 'Encargado de desarrollar estrategias de marketing.');
call sp_AgregarCargoEmpleado(7, 'Gerente de Recursos Humanos', 'Responsable de la gestión del talento humano.');
call sp_AgregarCargoEmpleado(8, 'Especialista en Ventas', 'Encargado de gestionar y cerrar acuerdos comerciales.');
call sp_AgregarCargoEmpleado(9, 'Ingeniero de Software', 'Responsable del desarrollo de software de la empresa.');
call sp_AgregarCargoEmpleado(10, 'Analista de Datos', 'Encargado de analizar grandes volúmenes de datos.');

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

call sp_AgregarCompras(1, '2000-02-05', 'Compras de consumo Basico', 250);
call sp_AgregarCompras(2, '2024-03-10', 'Compras de Papelería', 150);
call sp_AgregarCompras(3, '2023-04-15', 'Compras de Material de Oficina', 300);
call sp_AgregarCompras(4, '2006-05-20', 'Compras de Suministros de Limpieza', 200);
call sp_AgregarCompras(5, '2013-06-25', 'Compras de Equipos de Oficina', 1000);
call sp_AgregarCompras(6, '2002-07-30', 'Compras de Software', 800);
call sp_AgregarCompras(7, '2024-08-05', 'Compras de Hardware', 1500);
call sp_AgregarCompras(8, '2024-09-10', 'Compras de Mobiliario', 2000);
call sp_AgregarCompras(9, '2019-10-15', 'Compras de Material de Construcción', 2500);
call sp_AgregarCompras(10, '2020-11-20', 'Compras de Materias Primas', 3000);

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

call sp_AgregarTipoProducto(1, 'Alimento');
call sp_AgregarTipoProducto(2, 'Bebida');
call sp_AgregarTipoProducto(3, 'Electrodoméstico');
call sp_AgregarTipoProducto(4, 'Electrónico');
call sp_AgregarTipoProducto(5, 'Mueble');
call sp_AgregarTipoProducto(6, 'Ropa');
call sp_AgregarTipoProducto(7, 'Accesorio');
call sp_AgregarTipoProducto(8, 'Herramienta');
call sp_AgregarTipoProducto(9, 'Juguete');
call sp_AgregarTipoProducto(10, 'Libro');

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

call sp_AgregarEmpleados(1, 'David', 'Hernandez', 3500, '15:00', '20:00', 1);
call sp_AgregarEmpleados(2, 'Laura', 'Gomez', 3000, '08:00', '17:00', 2);
call sp_AgregarEmpleados(3, 'Carlos', 'Perez', 3200, '09:00', '18:00', 3);
call sp_AgregarEmpleados(4, 'Ana', 'Martinez', 2800, '10:00', '19:00', 4);
call sp_AgregarEmpleados(5, 'Sofia', 'Lopez', 3300, '11:00', '20:00', 5);
call sp_AgregarEmpleados(6, 'Juan', 'Gonzalez', 3100, '12:00', '21:00', 6);
call sp_AgregarEmpleados(7, 'Maria', 'Rodriguez', 3400, '13:00', '22:00', 7);
call sp_AgregarEmpleados(8, 'Pedro', 'Sanchez', 2900, '14:00', '23:00', 8);
call sp_AgregarEmpleados(9, 'Elena', 'Vasquez', 3600, '16:00', '01:00', 9);
call sp_AgregarEmpleados(10, 'Diego', 'Gutierrez', 3800, '17:00', '02:00', 10);

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

call sp_AgregarFacturas(1, '2005-06-06', '16:45', 250, 1, 1);
call sp_AgregarFacturas(2, '2005-07-07', '12:30', 150, 2, 2);
call sp_AgregarFacturas(3, '2005-08-08', '10:15', 300, 3, 3);
call sp_AgregarFacturas(4, '2005-09-09', '14:00', 200, 4, 4);
call sp_AgregarFacturas(5, '2005-10-10', '18:20', 1000, 5, 5);
call sp_AgregarFacturas(6, '2005-11-11', '20:05', 800, 6, 6);
call sp_AgregarFacturas(7, '2005-12-12', '09:45', 1500, 7, 7);
call sp_AgregarFacturas(8, '2006-01-01', '17:30', 2000, 8, 8);
call sp_AgregarFacturas(9, '2006-02-02', '15:10', 2500, 9, 9);
call sp_AgregarFacturas(10, '2006-03-03', '11:55', 3000, 10, 10);

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

call sp_AgregarTelefonoProveedor(1, '37406556', '56014889', 'El segundo numero no es usado con frecuencia', 1);
call sp_AgregarTelefonoProveedor(2, '98765432', '56123456', 'Se puede contactar en cualquier momento', 2);
call sp_AgregarTelefonoProveedor(3, '45678901', '56987654', 'Número principal de contacto', 3);
call sp_AgregarTelefonoProveedor(4, '34567890', '56234567', 'Llamar solo en caso de emergencia', 4);
call sp_AgregarTelefonoProveedor(5, '23456789', '56345678', 'Disponible en horario laboral', 5);
call sp_AgregarTelefonoProveedor(6, '01234567', '56456789', 'Prefiere el contacto por correo electrónico', 6);
call sp_AgregarTelefonoProveedor(7, '89012345', '56567890', 'Mantener comunicación vía WhatsApp', 7);
call sp_AgregarTelefonoProveedor(8, '67890123', '56678901', 'Número de respaldo en caso de fallo', 8);
call sp_AgregarTelefonoProveedor(9, '56789012', '56789012', 'Contacto directo del gerente de ventas', 9);
call sp_AgregarTelefonoProveedor(10, '43210987', '56890123', 'Solo en caso de consultas urgentes', 10);

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

call sp_AgregarEmailProveedor(1, 'dylkBarr@gmail.com', 'Proveedor fiable', 1);
call sp_AgregarEmailProveedor(2, 'marce_guti@gmail.com', 'Contacto principal de la empresa', 2);
call sp_AgregarEmailProveedor(3, 'robperez@gmail.com', 'Correo electrónico preferido para comunicaciones', 3);
call sp_AgregarEmailProveedor(4, 'camilalopez@gmail.com', 'Correo para notificaciones de pedidos', 4);
call sp_AgregarEmailProveedor(5, 'diegomartinez@gmail.com', 'Contacto de atención al cliente', 5);
call sp_AgregarEmailProveedor(6, 'laurahernandez@gmail.com', 'Correo para envío de facturas', 6);
call sp_AgregarEmailProveedor(7, 'carlosgomez@gmail.com', 'Correo para consultas técnicas', 7);
call sp_AgregarEmailProveedor(8, 'anarodriguez@gmail.com', 'Correo para solicitudes de información adicional', 8);
call sp_AgregarEmailProveedor(9, 'pedrosanchez@gmail.com', 'Correo para cambios en pedidos', 9);
call sp_AgregarEmailProveedor(10, 'sofiavasquez@gmail.com', 'Correo para seguimiento de proyectos', 10);

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

call sp_AgregarProductos(1, '0', 'Lechuga', 'Vegetal comestible', 150, 6, 5, 10, 1, 1);
call sp_AgregarProductos(2, '0', 'Manzana', 'Fruta fresca', 200, 7, 5, 12, 2, 1);
call sp_AgregarProductos(3, '0', 'Arroz', 'Grano básico', 100, 8, 1, 15, 3, 1);
call sp_AgregarProductos(4, '0', 'Aceite de oliva', 'Aceite vegetal', 300, 9, 2, 20, 4, 1);
call sp_AgregarProductos(5, '0', 'Pollo', 'Carne de ave', 250, 10, 1, 25, 5, 1);
call sp_AgregarProductos(6, '0', 'Cerveza', 'Bebida alcohólica', 180, 11, 2, 30, 6, 1);
call sp_AgregarProductos(7, '0', 'Televisor', 'Electrodoméstico', 1000, 12, 3, 35, 7, 1);
call sp_AgregarProductos(8, '0', 'Teléfono móvil', 'Dispositivo electrónico', 800, 13, 4, 40, 8, 1);
call sp_AgregarProductos(9, '0', 'Silla', 'Mobiliario', 50, 14, 5, 45, 9, 1);
call sp_AgregarProductos(10, '0', 'Camisa', 'Prenda de vestir', 30, 15, 6, 50, 10, 1);

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

call sp_AgregarDetalleFactura(1, 1, 1);
call sp_AgregarDetalleFactura(2, 2, 2);
call sp_AgregarDetalleFactura(3, 3, 3);
call sp_AgregarDetalleFactura(4, 4, 4);
call sp_AgregarDetalleFactura(5, 5, 5);
call sp_AgregarDetalleFactura(6, 6, 6);
call sp_AgregarDetalleFactura(7, 7, 7);
call sp_AgregarDetalleFactura(8, 8, 8);
call sp_AgregarDetalleFactura(9, 9, 9);
call sp_AgregarDetalleFactura(10, 10, 10);

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

call sp_AgregarDetalleCompra(1, 20, 1, 1);
call sp_AgregarDetalleCompra(2, 15, 2, 2);
call sp_AgregarDetalleCompra(3, 25, 3, 3);
call sp_AgregarDetalleCompra(4, 30, 4, 4);
call sp_AgregarDetalleCompra(5, 10, 5, 5);
call sp_AgregarDetalleCompra(6, 18, 6, 6);
call sp_AgregarDetalleCompra(7, 22, 7, 7);
call sp_AgregarDetalleCompra(8, 12, 8, 8);
call sp_AgregarDetalleCompra(9, 35, 9, 9);
call sp_AgregarDetalleCompra(10, 40, 10, 10);

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

-- ALTER USER '2023147_IN5BV' IDENTIFIED WITH mysql_native_password BY 'abc123!!';