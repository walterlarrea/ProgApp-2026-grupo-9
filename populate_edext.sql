USE edext;

START TRANSACTION;

INSERT INTO `institutos` (`nombreI`)
VALUES
	('INCO'),
	('IMERL'),
	('Física'),
	('IMPII'),
	('Eléctrica'),
	('DISI');

INSERT INTO `usuarios` (`nickname`, `apellido`, `email`, `fechaNac`, `imagen`, `nombre`, `password`)
VALUES
	('eleven11', 'Twelve', 'eleven11@gmail.com', '1971-12-31', NULL, 'Eleven', '1234'),
	('costas', 'Costas', 'gcostas@gmail.com', '1983-11-15', NULL, 'Gerardo', '1234'),
	('roro', 'Cotelo', 'rcotelo@yahoo.com', '1975-08-02', 'imagenes/usuarios/roro.jpg', 'Rodrigo', '1234'),
	('chechi', 'Garrido', 'cgarrido@hotmail.com', '1987-09-12', 'imagenes/usuarios/chechi.jpg', 'Cecilia', '1234'),
	('jeffw', 'Williams', 'jwilliams@gmail.com', '1964-11-27', 'imagenes/usuarios/jeffw.jpg', 'Jeff', '1234'),
	('weiss', 'Weiss', 'aweiss@hotmail.com', '1978-12-23', NULL, 'Adrian', '1234'),
	('heisenberg', 'White', 'heisenberg@gmail.com', '1956-03-07', 'imagenes/usuarios/heisenberg.jpg', 'Walter', '1234'),
	('benkenobi', 'Kenobi', 'benKenobi@gmail.com', '1914-04-02', 'imagenes/usuarios/benkenobi.jpg', 'Obi-Wan', '1234'),
	('waston', 'Watson', 'e.watson@gmail.com', '1990-04-15', 'imagenes/usuarios/waston.jpg', 'Emma', '1234'),
	('house', 'House', 'greghouse@gmail.com', '1959-05-15', 'imagenes/usuarios/house.jpg', 'Gregory', '1234'),
	('timmy', 'Cook', 'tim.cook@apple.com', '1960-11-01', 'imagenes/usuarios/timmy.jpg', 'Tim', '1234'),
	('danny', 'Riccio', 'dan.riccio@gmail.com', '1963-07-05', NULL, 'Daniel', '1234'),
	('phils', 'Schiller', 'schiller@gmail.com', '1961-10-07', NULL, 'Philip', '1234'),
	('bruces', 'Sewell', 'sewell@gmail.com', '1959-12-03', NULL, 'Bruce', '1234'),
	('adri', 'García', 'agarcia@gmail.com', '1978-07-28', NULL, 'Adriana', '1234');

INSERT INTO `estudiantes` (`nickname`)
VALUES
	('eleven11'),
	('costas'),
	('roro'),
	('chechi'),
	('jeffw'),
	('weiss');

INSERT INTO `docentes` (`nickname`, `nombreInst`)
VALUES
	('heisenberg', 'INCO'), -- Walter White
	('benkenobi', 'INCO'), -- Obi-Wan Kenobi
	('waston', 'INCO'), -- Emma Watson
	('house', 'Eléctrica'), -- Gregory House
	('timmy', 'IMERL'), -- Tim Cook
	('danny', 'IMERL'), -- Daniel Riccio
	('phils', 'IMPII'), -- Philip Schiller
	('bruces', 'DISI'), -- Bruce Sewell
	('adri', 'DISI'); -- Adriana García

INSERT INTO `docente_instituto` (`docente_nickname`, `instituto_nombreI`)
VALUES
	('heisenberg', 'INCO'),
	('benkenobi', 'INCO'),
	('waston', 'INCO'),
	('house', 'Eléctrica'),
	('timmy', 'IMERL'),
	('danny', 'IMERL'),
	('phils', 'IMPII'),
	('bruces', 'DISI'),
	('adri', 'DISI');

INSERT INTO `cursos` (`nombreCurso`, `descCurso`, `duracion`, `cantHoras`, `cantCred`, `fechaReg`, `url`, `nombreI`, `imagen`)
VALUES
		('Talleres plenarios', 'Talleres plenarios: presentados por cuatro reconocidos matemáticos uruguayos, plantearán diversos tópicos de matemática en el marco de los cuales se realizarán actividades fomentando la integración entre estudiantes, docentes e investigadores.', 3, 15, 1, '2026-02-01', 'www.tmu.edu.uy', 'IMERL', 'curso-placeholder.jpg'),
		('Seminarios de Resolución de Problemas', 'Seminario, todos los jueves en Facultad de Ingeniería a partir del jueves 25 de Julio, en las áreas en que se desarrollan los problemas de las Olimpíadas de Matemática.', 5, 30, 2, '2026-07-12', 'www.tmu.edu.uy', 'IMERL', 'curso-placeholder.jpg'),
		('Dalavuelta', 'Dalavuelta es un proyecto de extensión que nace en el Instituto de Ingeniería Mecánica y Producción Industrial (IIMPI) de Fing, que, si bien inicia su trabajo en el desarrollo de bicicletas accesibles para personas en situación de discapacidad motriz a partir de bicicletas abandonadas, se propuso diseñar otras herramientas para fomentar la accesibilidad.', 10, 60, 4, '2024-06-25', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IMPII', 'curso-placeholder.jpg'),
		('Extensionismo Industrial', 'El proyecto tiene como objetivo desarrollar intervenciones curriculares en pequeños emprendimientos productivos de diferentes sectores de la industria nacional. La metodología de trabajo permite articular diversas intervenciones, combinando actividades de enseñanza, extensión e investigación por parte de docentes del IMPII.', 12, 75, 5, '2025-06-16', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IMPII', 'curso-placeholder.jpg'),
		('Inclusión Energética', 'En el proyecto se conjuga el trabajo de docentes y estudiantes de la carrera Ingeniería Industrial Mecánica a través del Módulo de Extensión, en donde se trabaja en el diseño, construcción y prueba de un prototipo de colector solar adquiriendo conocimientos relevantes para luego poder replicarlos junto a las familias en los talleres. Las premisas fundamentales a la hora de pensar los diseños son el bajo costo de los materiales y la fácil construcción.', 6, 45, 3, '2026-02-01', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IMPII', 'curso-placeholder.jpg'),
		('Flor del Ceibo', 'Flor de Ceibo es un proyecto central de la Universidad de la República, que tiene como misión movilizar la participación de estudiantes universitarios en diversas tareas vinculadas con la puesta en funcionamiento del Plan Ceibal en el territorio nacional.', 15, 150, 10, '2008-07-27', 'http://www.flordeceibo.edu.uy/', 'DISI', 'curso-placeholder.jpg'),
		('Taller de robótica educativa.', 'La asignatura se organiza en dos etapas. La primera etapa se dicta a través de clases teórico-prácticas, donde se espera además que cada estudiante le dedique horas de estudio. La segunda etapa consiste en que los estudiantes trabajen en grupo sobre el diseño e implementación de una experiencia didáctica de inclusión del robot Butiá en el aula, utilizando los conocimientos aprendidos en clase.', 8, 90, 6, '2024-02-02', 'https://eva.fing.edu.uy/course/view.php?id=1187', 'INCO', 'curso-placeholder.jpg'),
		('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'Se propone desarrollar una aplicación interactiva para tablet Android basada en el juego de tablero Komikan (versión web del juego), que incluya los distintos aspectos concernientes al juego, así como a situaciones específicas particulares.', 9, 45, 3, '2026-06-15', 'https://eva.fing.edu.uy/mod/folder/view.php?id=89398', 'INCO', 'curso-placeholder.jpg'),
		('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación', 'Se realizarán visitas a escuelas rurales participantes en un proyecto conjunto del grupo PLN y el Programa de Políticas Lingüísticas de ANEP, en el marco del cual se desarrollaron diferentes herramientas para uso de maestros que enseñan inglés con apoyo remoto de profesores especializados desde Montevideo.', 12, 60, 4, '2026-05-24', 'https://eva.fing.edu.uy/mod/folder/view.php?id=89398', 'INCO', 'curso-placeholder.jpg'),
		('MicroBit', 'El Centro Ceibal se encuentra distribuyendo placas micro:bit para que estudiantes de primaria y secundaria aprendan nociones básicas de robótica, electrónica y programación de forma autónoma y lúdica. Estas placas se basan en un microcontrolador y cuentan con leds, botones, acelerómetro, brújula, bluetooth y otros sensores. Las placas se programan fácilmente con lenguaje tipo scratch y python, por lo que son muy útiles para un primer acercamiento a la temática.', 15, 105, 7, '2026-03-13', 'https://www.fing.edu.uy/noticias/extension/modulo-detallerextensionmicrobit', 'Eléctrica', 'curso-placeholder.jpg');


INSERT INTO `categorias` (`nombreCategoria`)
VALUES
	('Social'),
	('Industrial'),
	('Educativo'),
	('Interdisciplinario');

INSERT INTO `categorias_cursos` (`categoria_nombreCategoria`, `curso_nombreCurso`)
VALUES
	('Social', 'Dalavuelta'),
	('Social', 'Taller de robótica educativa.'),
	('Social', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación'),
	('Industrial', 'Extensionismo Industrial'),
	('Industrial', 'Inclusión Energética'),
	('Industrial', 'MicroBit'),
	('Educativo', 'Flor del Ceibo'),
	('Educativo', 'Taller de robótica educativa.'),
	('Educativo', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela'),
	('Educativo', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación'),
	('Educativo', 'MicroBit'),
	('Interdisciplinario', 'Flor del Ceibo');

INSERT INTO `usuario_seguidos` (`usuario_nick`, `seguido_nick`)
VALUES
	('eleven11', 'roro'), ('eleven11', 'heisenberg'), ('eleven11', 'waston'),
	('costas', 'chechi'), ('costas', 'jeffw'), ('costas', 'benkenobi'), ('costas', 'danny'), ('costas', 'phils'),
	('roro', 'costas'), ('roro', 'weiss'),
	('chechi', 'eleven11'), ('chechi', 'waston'), ('chechi', 'house'),
	('jeffw', 'eleven11'),
	('weiss', 'costas'), ('weiss', 'heisenberg'), ('weiss', 'timmy'),
	('heisenberg', 'roro'),
	('benkenobi', 'timmy'),
	('waston', 'roro'), ('waston', 'weiss'), ('waston', 'heisenberg'),
	('house', 'jeffw'), ('house', 'weiss'), ('house', 'benkenobi'),
	('timmy', 'eleven11'), ('timmy', 'chechi'), ('timmy', 'jeffw'),
	('danny', 'costas'), ('danny', 'weiss'), ('danny', 'heisenberg'),
	('phils', 'bruces'),
	('bruces', 'phils'),
	('adri', 'heisenberg');

INSERT INTO `cursos_previas` (`nombre_curso`, `nombre_curso_previa`)
VALUES
	('Seminarios de Resolución de Problemas', 'Talleres plenarios'),
	('Dalavuelta', 'Talleres plenarios'),
	('Extensionismo Industrial', 'Talleres plenarios');

INSERT INTO `ediciones_de_cursos` (`nombreEdi`, `fechaInicio`, `fechaFin`, `cupo`, `fechaPub`, `curso_nombreCurso`, `imagen`)
VALUES
	('Flor del Ceibo - 2010', '2010-03-15', '2010-07-07', null, '2010-02-16', 'Flor del Ceibo', 'edicion-placeholder.jpg'),
	('Flor del Ceibo - 2012', '2012-08-01', '2012-11-20', null, '2012-07-10', 'Flor del Ceibo', 'edicion-placeholder.jpg'),
	('Flor del Ceibo - 2025', '2025-04-10', '2025-08-07', null, '2025-03-06', 'Flor del Ceibo', 'edicion-placeholder.jpg'),
	('Dalavuelta - 2025', '2024-08-20', '2024-11-10', 15, '2024-07-20', 'Dalavuelta', 'edicion-placeholder.jpg'),
	('Extensionismo Industrial - 2025', '2025-08-10', '2025-11-10', 15, '2025-07-08', 'Extensionismo Industrial', 'edicion-placeholder.jpg'),
	('Inclusión Energética - 2026', '2026-03-15', '2026-04-30', 30, '2026-02-20', 'Inclusión Energética', 'edicion-placeholder.jpg'),
	('Taller de robótica educativa - 2024', '2024-03-10', '2024-05-10', 10, '2024-02-15', 'Taller de robótica educativa.', 'edicion-placeholder.jpg'),
	('Taller de robótica educativa - 2026', '2026-03-10', '2026-05-10', 10, '2026-02-15', 'Taller de robótica educativa.', 'edicion-placeholder.jpg'),
	('Taller de robótica educativa - 2026-2', '2026-09-10', '2026-11-08', 20, '2026-08-15', 'Taller de robótica educativa.', 'edicion-placeholder.jpg'),
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', '2026-07-29', '2026-10-07', 5, '2026-07-10', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'edicion-placeholder.jpg'),
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', '2026-09-15', '2026-12-15', 5, '2026-06-02', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación', 'edicion-placeholder.jpg'),
	('MicroBit - 2026', '2026-08-12', '2026-12-05', 30, '2026-07-02', 'MicroBit', 'edicion-placeholder.jpg'),
	('Talleres plenarios - 2026', '2026-03-10', '2026-03-30', null, '2026-03-02', 'Talleres plenarios', 'edicion-placeholder.jpg'),
	('Seminarios de Resolución de Problemas - 2026', '2026-09-10', '2026-10-20', null, '2026-07-12', 'Seminarios de Resolución de Problemas', 'edicion-placeholder.jpg');


INSERT INTO `edicion_docente` (`edicion_nombreEdi`, `docente_nickname`)
VALUES
	('Flor del Ceibo - 2010', 'bruces'),
	('Flor del Ceibo - 2012', 'bruces'),
	('Flor del Ceibo - 2012', 'adri'),
	('Flor del Ceibo - 2025', 'bruces'),
	('Flor del Ceibo - 2025', 'adri'),
	('Dalavuelta - 2025', 'phils'),
	('Extensionismo Industrial - 2025', 'phils'),
	('Inclusión Energética - 2026', 'phils'),
	('Taller de robótica educativa - 2024', 'heisenberg'),
	('Taller de robótica educativa - 2026', 'heisenberg'),
	('Taller de robótica educativa - 2026', 'benkenobi'),
	('Taller de robótica educativa - 2026-2', 'benkenobi'),
	('Taller de robótica educativa - 2026-2', 'waston'),
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'waston'),
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'heisenberg'),
	('MicroBit - 2026', 'house'),
	('Talleres plenarios - 2026', 'timmy'),
	('Talleres plenarios - 2026', 'danny'),
	('Seminarios de Resolución de Problemas - 2026', 'timmy');

INSERT INTO `inscripciones_a_ediciones` (`fechaInscE`, `estudiane_nickname`, `edicion_nombreEdi`)
VALUES
	('2010-02-20', 'eleven11', 'Flor del Ceibo - 2010'),
	('2010-02-25', 'chechi', 'Flor del Ceibo - 2010'),
	('2012-07-12', 'costas', 'Flor del Ceibo - 2012'),
	('2012-07-15', 'roro', 'Flor del Ceibo - 2012'),
	('2012-07-30', 'weiss', 'Flor del Ceibo - 2012'),
	('2025-03-10', 'roro', 'Flor del Ceibo - 2025'),
	('2025-03-15', 'jeffw', 'Flor del Ceibo - 2025'),
	('2024-07-25', 'chechi', 'Dalavuelta - 2025'),
	('2024-07-28', 'eleven11', 'Dalavuelta - 2025'),
	('2024-08-02', 'roro', 'Dalavuelta - 2025'),
	('2024-08-10', 'costas', 'Dalavuelta - 2025'),
	('2024-08-15', 'jeffw', 'Dalavuelta - 2025'),
	('2025-07-18', 'costas', 'Extensionismo Industrial - 2025'),
	('2025-07-20', 'chechi', 'Extensionismo Industrial - 2025'),
	('2025-07-29', 'eleven11', 'Extensionismo Industrial - 2025'),
	('2025-08-05', 'weiss', 'Extensionismo Industrial - 2025'),
	('2026-02-23', 'roro', 'Inclusión Energética - 2026'),
	('2026-02-25', 'weiss', 'Inclusión Energética - 2026'),
	('2026-02-28', 'chechi', 'Inclusión Energética - 2026'),
	('2026-03-03', 'eleven11', 'Inclusión Energética - 2026'),
	('2017-02-18', 'weiss', 'Taller de robótica educativa - 2024'),
	('2024-02-20', 'roro', 'Taller de robótica educativa - 2024'),
	('2024-03-03', 'eleven11', 'Taller de robótica educativa - 2024'),
	('2024-03-05', 'chechi', 'Taller de robótica educativa - 2024'),
	('2026-02-18', 'jeffw', 'Taller de robótica educativa - 2026'),
	('2026-02-22', 'costas', 'Taller de robótica educativa - 2026'),
	('2026-08-18', 'weiss', 'Taller de robótica educativa - 2026-2'),
	('2026-08-22', 'chechi', 'Taller de robótica educativa - 2026-2'),
	('2026-09-03', 'roro', 'Taller de robótica educativa - 2026-2'),
	('2026-07-13', 'chechi', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026'),
	('2026-07-20', 'weiss', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026'),
	('2026-07-22', 'roro', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026'),
	('2026-06-04', 'weiss', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26'),
	('2026-07-18', 'eleven11', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26'),
	('2026-08-20', 'jeffw', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26'),
	('2026-07-12', 'chechi', 'MicroBit - 2026'),
	('2026-07-14', 'roro', 'MicroBit - 2026'),
	('2026-07-25', 'eleven11', 'MicroBit - 2026'),
	('2026-08-05', 'jeffw', 'MicroBit - 2026'),
	('2026-03-05', 'costas', 'Talleres plenarios - 2026'),
	('2026-03-04', 'weiss', 'Talleres plenarios - 2026'),
	('2026-03-07', 'roro', 'Talleres plenarios - 2026'),
	('2026-07-15', 'weiss', 'Seminarios de Resolución de Problemas - 2026'),
	('2026-07-20', 'costas', 'Seminarios de Resolución de Problemas - 2026'),
	('2026-08-06', 'roro', 'Seminarios de Resolución de Problemas - 2026'),
	('2026-08-30', 'chechi', 'Seminarios de Resolución de Problemas - 2026');

UPDATE `inscripciones_a_ediciones`
SET `fechaInscE` = CASE
	WHEN `edicion_nombreEdi` = 'Flor del Ceibo - 2010' AND `estudiane_nickname` = 'eleven11' THEN '2017-02-20'
	WHEN `edicion_nombreEdi` = 'Flor del Ceibo - 2010' AND `estudiane_nickname` = 'chechi' THEN '2017-02-25'
	WHEN `edicion_nombreEdi` = 'Flor del Ceibo - 2012' AND `estudiane_nickname` = 'costas' THEN '2019-07-12'
	WHEN `edicion_nombreEdi` = 'Flor del Ceibo - 2012' AND `estudiane_nickname` = 'roro' THEN '2019-07-15'
	WHEN `edicion_nombreEdi` = 'Flor del Ceibo - 2012' AND `estudiane_nickname` = 'weiss' THEN '2019-07-30'
	WHEN `edicion_nombreEdi` = 'Taller de robótica educativa - 2024' AND `estudiane_nickname` = 'weiss' THEN '2024-02-18'
	ELSE `fechaInscE`
END;

UPDATE `inscripciones_a_ediciones`
SET `estado` = 'ACEPTADA'
WHERE (`edicion_nombreEdi`, `estudiane_nickname`) IN (
	('Flor del Ceibo - 2010', 'eleven11'), ('Flor del Ceibo - 2010', 'chechi'),
	('Flor del Ceibo - 2012', 'costas'), ('Flor del Ceibo - 2012', 'weiss'),
	('Flor del Ceibo - 2025', 'roro'), ('Flor del Ceibo - 2025', 'jeffw'),
	('Dalavuelta - 2025', 'chechi'), ('Dalavuelta - 2025', 'roro'), ('Dalavuelta - 2025', 'costas'),
	('Extensionismo Industrial - 2025', 'costas'), ('Extensionismo Industrial - 2025', 'chechi'),
	('Extensionismo Industrial - 2025', 'eleven11'), ('Extensionismo Industrial - 2025', 'weiss'),
	('Inclusión Energética - 2026', 'roro'), ('Inclusión Energética - 2026', 'chechi'),
	('Taller de robótica educativa - 2024', 'eleven11'),
	('Taller de robótica educativa - 2026', 'jeffw'), ('Taller de robótica educativa - 2026', 'costas'),
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'chechi'),
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'weiss'),
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'roro'),
	('Talleres plenarios - 2026', 'weiss'), ('Talleres plenarios - 2026', 'roro'),
	('Seminarios de Resolución de Problemas - 2026', 'weiss'),
	('Seminarios de Resolución de Problemas - 2026', 'roro')
);

UPDATE `inscripciones_a_ediciones`
SET `estado` = 'RECHAZADA'
WHERE (`edicion_nombreEdi`, `estudiane_nickname`) IN (
	('Flor del Ceibo - 2012', 'roro'),
	('Dalavuelta - 2025', 'eleven11'), ('Dalavuelta - 2025', 'jeffw'),
	('Inclusión Energética - 2026', 'weiss'), ('Inclusión Energética - 2026', 'eleven11'),
	('Taller de robótica educativa - 2024', 'weiss'), ('Taller de robótica educativa - 2024', 'roro'),
	('Taller de robótica educativa - 2026', 'roro'),
	('Talleres plenarios - 2026', 'costas'),
	('Seminarios de Resolución de Problemas - 2026', 'costas'),
	('Seminarios de Resolución de Problemas - 2026', 'chechi')
);

UPDATE `inscripciones_a_ediciones`
SET `estado` = 'INSCRIPTO'
WHERE (`edicion_nombreEdi`, `estudiane_nickname`) IN (
	('Taller de robótica educativa - 2026-2', 'weiss'),
	('Taller de robótica educativa - 2026-2', 'chechi'),
	('Taller de robótica educativa - 2026-2', 'roro'),
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'weiss'),
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'eleven11'),
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'jeffw'),
	('MicroBit - 2026', 'chechi'), ('MicroBit - 2026', 'roro'),
	('MicroBit - 2026', 'eleven11'), ('MicroBit - 2026', 'jeffw')
);

INSERT INTO `programas_de_formacion` (`nombre`, `descripcion`, `fechaInicio`, `fechaFin`, `fechaDeCreacion`, `imagen`)
VALUES
	('EFI Ingeniería Mecánica', 'Programa mecánica', '2026-05-01', '2026-10-31', CURDATE(), 'imagenes/programas/p1.jpg'),
	('Formación integral', 'Programa varios institutos', '2026-07-15', '2027-01-01', CURDATE(), 'imagenes/programas/p2.jpg'),
	('EFI Robótica', 'Programa robótica', '2026-09-03', '2026-11-18', CURDATE(), 'imagenes/programas/p3.jpg');

INSERT INTO `programas_cursos` (`nombre_programa`, `nombre_curso`)
VALUES
	('EFI Ingeniería Mecánica', 'Dalavuelta'),
	('EFI Ingeniería Mecánica', 'Extensionismo Industrial'),
	('EFI Ingeniería Mecánica', 'Inclusión Energética'),
	('Formación integral', 'Seminarios de Resolución de Problemas'),
	('Formación integral', 'Extensionismo Industrial'),
	('Formación integral', 'Flor del Ceibo'),
	('Formación integral', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela'),
	('EFI Robótica', 'Taller de robótica educativa.'),
	('EFI Robótica', 'MicroBit');

INSERT INTO `inscripciones_a_programas` (`estudiante_nickname`, `programa_nombre`, `fechaInscripcion`)
VALUES
	('eleven11', 'EFI Ingeniería Mecánica', '2026-04-15'),
	('chechi', 'EFI Ingeniería Mecánica', '2026-04-20'),
	('costas', 'Formación integral', '2026-07-10'),
	('chechi', 'Formación integral', '2026-07-10'),
	('eleven11', 'Formación integral', '2026-07-12'),
	('weiss', 'Formación integral', '2026-07-15'),
	('roro', 'Formación integral', '2026-07-18'),
	('costas', 'EFI Robótica', '2026-08-11'),
	('chechi', 'EFI Robótica', '2026-08-15'),
	('roro', 'EFI Robótica', '2026-08-22'),
	('jeffw', 'EFI Robótica', '2026-08-30');

COMMIT;
