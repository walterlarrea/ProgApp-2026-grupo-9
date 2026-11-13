USE edext;

START TRANSACTION;

INSERT INTO `institutos` (`nombreI`)
VALUES
	('INCO'), -- IN
	('IMERL'), -- IL
	('Física'), -- IF
	('IMPII'), -- IM
	('Eléctrica'), -- IE
	('DISI'); -- DI

INSERT INTO `usuarios` (`nickname`, `apellido`, `email`, `fechaNac`, `imagen`, `nombre`, `password`)
VALUES
	('eleven11', 'Twelve', 'eleven11@gmail.com', '1971-12-31', NULL, 'Eleven', '1234'), -- EL
	('costas', 'Costas', 'gcostas@gmail.com', '1983-11-15', NULL, 'Gerardo', '1234'), -- CO
	('roro', 'Cotelo', 'rcotelo@yahoo.com', '1975-08-02', 'imagenes/usuarios/roro.jpg', 'Rodrigo', '1234'), -- RO
	('chechi', 'Garrido', 'cgarrido@hotmail.com', '1987-09-12', 'imagenes/usuarios/chechi.jpg', 'Cecilia', '1234'), -- CH
	('jeffw', 'Williams', 'jwilliams@gmail.com', '1964-11-27', 'imagenes/usuarios/jeffw.jpg', 'Jeff', '1234'), -- JW
	('weiss', 'Weiss', 'aweiss@hotmail.com', '1978-12-23', NULL, 'Adrian', '1234'), -- WE
	('heisenberg', 'White', 'heisenberg@gmail.com', '1956-03-07', 'imagenes/usuarios/heisenberg.jpg', 'Walter', '1234'), -- WW
	('benkenobi', 'Kenobi', 'benKenobi@gmail.com', '1914-04-02', 'imagenes/usuarios/benkenobi.jpg', 'Obi-Wan', '1234'), -- OK
	('waston', 'Watson', 'e.watson@gmail.com', '1990-04-15', 'imagenes/usuarios/waston.jpg', 'Emma', '1234'), -- EW
	('house', 'House', 'greghouse@gmail.com', '1959-05-15', 'imagenes/usuarios/house.jpg', 'Gregory', '1234'), -- GH
	('timmy', 'Cook', 'tim.cook@apple.com', '1960-11-01', 'imagenes/usuarios/timmy.jpg', 'Tim', '1234'), -- TC
	('danny', 'Riccio', 'dan.riccio@gmail.com', '1963-07-05', NULL, 'Daniel', '1234'), -- DR
	('phils', 'Schiller', 'schiller@gmail.com', '1961-10-07', NULL, 'Philip', '1234'), -- PS
	('bruces', 'Sewell', 'sewell@gmail.com', '1959-12-03', NULL, 'Bruce', '1234'), -- BS
	('adri', 'García', 'agarcia@gmail.com', '1978-07-28', NULL, 'Adriana', '1234'); -- AG

INSERT INTO `estudiantes` (`nickname`)
VALUES
	('eleven11'), -- EL
	('costas'), -- CO
	('roro'), -- RO
	('chechi'), -- CH
	('jeffw'), -- JW
	('weiss'); -- WE

INSERT INTO `docentes` (`nickname`, `nombreInst`)
VALUES
	('heisenberg', 'INCO'), -- WW, IN
	('benkenobi', 'INCO'), -- OK, IN
	('waston', 'INCO'), -- EW, IN
	('house', 'Eléctrica'), -- GH, IE
	('timmy', 'IMERL'), -- TC, IL
	('danny', 'IMERL'), -- DR, IL
	('phils', 'IMPII'), -- PS, IM
	('bruces', 'DISI'), -- BS, DI
	('adri', 'DISI'); -- AG, DI

INSERT INTO `docente_instituto` (`docente_nickname`, `instituto_nombreI`)
VALUES
	('heisenberg', 'INCO'), -- WW, IN
	('benkenobi', 'INCO'), -- OK, IN
	('waston', 'INCO'), -- EW, IN
	('house', 'Eléctrica'), -- GH, IE
	('timmy', 'IMERL'), -- TC, IL
	('danny', 'IMERL'), -- DR, IL
	('phils', 'IMPII'), -- PS, IM
	('bruces', 'DISI'), -- BS, DI
	('adri', 'DISI'); -- AG, DI

INSERT INTO `cursos` (`nombreCurso`, `descCurso`, `duracion`, `cantHoras`, `cantCred`, `fechaReg`, `url`, `nombreI`, `imagen`)
VALUES
	('Talleres plenarios', 'Talleres plenarios: presentados por cuatro reconocidos matemáticos uruguayos, plantearán diversos tópicos de matemática en el marco de los cuales se realizarán actividades fomentando la integración entre estudiantes, docentes e investigadores.', 3, 15, 1, '2026-02-01', 'www.tmu.edu.uy', 'IMERL', 'curso-placeholder.jpg'), -- C1
	('Seminarios de Resolución de Problemas', 'Seminario, todos los jueves en Facultad de Ingeniería a partir del jueves 25 de Julio, en las áreas en que se desarrollan los problemas de las Olimpíadas de Matemática.', 5, 30, 2, '2026-07-12', 'www.tmu.edu.uy', 'IMERL', 'curso-placeholder.jpg'), -- C2
	('Dalavuelta', 'Dalavuelta es un proyecto de extensión que nace en el Instituto de Ingeniería Mecánica y Producción Industrial (IIMPI) de Fing, que, si bien inicia su trabajo en el desarrollo de bicicletas accesibles para personas en situación de discapacidad motriz a partir de bicicletas abandonadas, se propuso diseñar otras herramientas para fomentar la accesibilidad.', 10, 60, 4, '2024-06-25', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IMPII', 'curso-placeholder.jpg'), -- C3
	('Extensionismo Industrial', 'El proyecto tiene como objetivo desarrollar intervenciones curriculares en pequeños emprendimientos productivos de diferentes sectores de la industria nacional. La metodología de trabajo permite articular diversas intervenciones, combinando actividades de enseñanza, extensión e investigación por parte de docentes del IMPII.', 12, 75, 5, '2025-06-16', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IMPII', 'curso-placeholder.jpg'), -- C4
	('Inclusión Energética', 'En el proyecto se conjuga el trabajo de docentes y estudiantes de la carrera Ingeniería Industrial Mecánica a través del Módulo de Extensión, en donde se trabaja en el diseño, construcción y prueba de un prototipo de colector solar adquiriendo conocimientos relevantes para luego poder replicarlos junto a las familias en los talleres. Las premisas fundamentales a la hora de pensar los diseños son el bajo costo de los materiales y la fácil construcción.', 6, 45, 3, '2026-02-01', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IMPII', 'curso-placeholder.jpg'), -- C5
	('Flor del Ceibo', 'Flor de Ceibo es un proyecto central de la Universidad de la República, que tiene como misión movilizar la participación de estudiantes universitarios en diversas tareas vinculadas con la puesta en funcionamiento del Plan Ceibal en el territorio nacional.', 15, 150, 10, '2008-07-27', 'http://www.flordeceibo.edu.uy/', 'DISI', 'curso-placeholder.jpg'), -- C6
	('Taller de robótica educativa.', 'La asignatura se organiza en dos etapas. La primera etapa se dicta a través de clases teórico-prácticas, donde se espera además que cada estudiante le dedique horas de estudio. La segunda etapa consiste en que los estudiantes trabajen en grupo sobre el diseño e implementación de una experiencia didáctica de inclusión del robot Butiá en el aula, utilizando los conocimientos aprendidos en clase.', 8, 90, 6, '2024-02-02', 'https://eva.fing.edu.uy/course/view.php?id=1187', 'INCO', 'curso-placeholder.jpg'), -- C7
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'Se propone desarrollar una aplicación interactiva para tablet Android basada en el juego de tablero Komikan (versión web del juego), que incluya los distintos aspectos concernientes al juego, así como a situaciones específicas particulares.', 9, 45, 3, '2026-06-15', 'https://eva.fing.edu.uy/mod/folder/view.php?id=89398', 'INCO', 'curso-placeholder.jpg'), -- C8
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación', 'Se realizarán visitas a escuelas rurales participantes en un proyecto conjunto del grupo PLN y el Programa de Políticas Lingüísticas de ANEP, en el marco del cual se desarrollaron diferentes herramientas para uso de maestros que enseñan inglés con apoyo remoto de profesores especializados desde Montevideo.', 12, 60, 4, '2026-05-24', 'https://eva.fing.edu.uy/mod/folder/view.php?id=89398', 'INCO', 'curso-placeholder.jpg'), -- C9
	('MicroBit', 'El Centro Ceibal se encuentra distribuyendo placas micro:bit para que estudiantes de primaria y secundaria aprendan nociones básicas de robótica, electrónica y programación de forma autónoma y lúdica. Estas placas se basan en un microcontrolador y cuentan con leds, botones, acelerómetro, brújula, bluetooth y otros sensores. Las placas se programan fácilmente con lenguaje tipo scratch y python, por lo que son muy útiles para un primer acercamiento a la temática.', 15, 105, 7, '2026-03-13', 'https://www.fing.edu.uy/noticias/extension/modulo-detallerextensionmicrobit', 'Eléctrica', 'curso-placeholder.jpg'); -- C10

INSERT INTO `visitas_cursos` (`curso_nombreCurso`, `cantidad_visitas`)
VALUES
	('Talleres plenarios', 150), -- C1
	('Seminarios de Resolución de Problemas', 120), -- C2
	('Dalavuelta', 132), -- C3
	('Extensionismo Industrial', 103), -- C4
	('Inclusión Energética', 98), -- C5
	('Flor del Ceibo', 430), -- C6
	('Taller de robótica educativa.', 318), -- C7
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 267), -- C8
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación', 169), -- C9
	('MicroBit', 212); -- C10

INSERT INTO `categorias` (`nombreCategoria`)
VALUES
	('Social'), -- T1
	('Industrial'), -- T2
	('Educativo'), -- T3
	('Interdisciplinario'); -- T4

INSERT INTO `categorias_cursos` (`categoria_nombreCategoria`, `curso_nombreCurso`)
VALUES
	('Social', 'Dalavuelta'), -- T1, C3
	('Social', 'Taller de robótica educativa.'), -- T1, C7
	('Social', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación'), -- T1, C9
	('Industrial', 'Extensionismo Industrial'), -- T2, C4
	('Industrial', 'Inclusión Energética'), -- T2, C5
	('Industrial', 'MicroBit'), -- T2, C10
	('Educativo', 'Flor del Ceibo'), -- T3, C6
	('Educativo', 'Taller de robótica educativa.'), -- T3, C7
	('Educativo', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela'), -- T3, C8
	('Educativo', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación'), -- T3, C9
	('Educativo', 'MicroBit'), -- T3, C10
	('Interdisciplinario', 'Flor del Ceibo'); -- T4, C6

INSERT INTO `usuario_seguidos` (`usuario_nick`, `seguido_nick`)
VALUES
	('eleven11', 'roro'), -- EL, RO
	('eleven11', 'heisenberg'), -- EL, WW
	('eleven11', 'waston'), -- EL, EW
	('costas', 'chechi'), -- CO, CH
	('costas', 'jeffw'), -- CO, JW
	('costas', 'benkenobi'), -- CO, OK
	('costas', 'danny'), -- CO, DR
	('costas', 'phils'), -- CO, PS
	('roro', 'costas'), -- RO, CO
	('roro', 'weiss'), -- RO, WE
	('chechi', 'eleven11'), -- CH, EL
	('chechi', 'waston'), -- CH, EW
	('chechi', 'house'), -- CH, GH
	('jeffw', 'eleven11'), -- JW, EL
	('weiss', 'costas'), -- WE, CO
	('weiss', 'heisenberg'), -- WE, WW
	('weiss', 'timmy'), -- WE, TC
	('heisenberg', 'roro'), -- WW, RO
	('benkenobi', 'timmy'), -- OK, TC
	('waston', 'roro'), -- EW, RO
	('waston', 'weiss'), -- EW, WE
	('waston', 'heisenberg'), -- EW, WW
	('house', 'jeffw'), -- GH, JW
	('house', 'weiss'), -- GH, WE
	('house', 'benkenobi'), -- GH, OK
	('timmy', 'eleven11'), -- TC, EL
	('timmy', 'chechi'), -- TC, CH
	('timmy', 'jeffw'), -- TC, JW
	('danny', 'costas'), -- DR, CO
	('danny', 'weiss'), -- DR, WE
	('danny', 'heisenberg'), -- DR, WW
	('phils', 'bruces'), -- PS, BS
	('bruces', 'phils'), -- BS, PS
	('adri', 'heisenberg'); -- AG, WW

INSERT INTO `cursos_previas` (`nombre_curso`, `nombre_curso_previa`)
VALUES
	('Seminarios de Resolución de Problemas', 'Talleres plenarios'), -- C2, C1
	('Dalavuelta', 'Talleres plenarios'), -- C3, C1
	('Extensionismo Industrial', 'Talleres plenarios'); -- C4, C1

INSERT INTO `ediciones_de_cursos` (`nombreEdi`, `fechaInicio`, `fechaFin`, `cupo`, `fechaPub`, `curso_nombreCurso`, `imagen`, `cerrada`)
VALUES
	('Flor del Ceibo - 2010', '2010-03-15', '2010-07-07', null, '2010-02-16', 'Flor del Ceibo', 'edicion-placeholder.jpg', 0), -- E1 (C6)
	('Flor del Ceibo - 2012', '2012-08-01', '2012-11-20', null, '2012-07-10', 'Flor del Ceibo', 'edicion-placeholder.jpg', 0), -- E2 (C6)
	('Flor del Ceibo - 2025', '2025-04-10', '2025-08-07', null, '2025-03-06', 'Flor del Ceibo', 'edicion-placeholder.jpg', 0), -- E3 (C6)
	('Dalavuelta - 2025', '2024-08-20', '2024-11-10', 15, '2024-07-20', 'Dalavuelta', 'edicion-placeholder.jpg', 0), -- E4 (C3)
	('Extensionismo Industrial - 2025', '2025-08-10', '2025-11-10', 15, '2025-07-08', 'Extensionismo Industrial', 'edicion-placeholder.jpg', 0), -- E5 (C4)
	('Inclusión Energética - 2026', '2026-03-15', '2026-04-30', 30, '2026-02-20', 'Inclusión Energética', 'edicion-placeholder.jpg', 0), -- E6 (C5)
	('Taller de robótica educativa - 2024', '2024-03-10', '2024-05-10', 10, '2024-02-15', 'Taller de robótica educativa.', 'edicion-placeholder.jpg', 0), -- E7 (C7)
	('Taller de robótica educativa - 2026', '2026-03-10', '2026-05-10', 10, '2026-02-15', 'Taller de robótica educativa.', 'edicion-placeholder.jpg', 0), -- E8 (C7)
	('Taller de robótica educativa - 2026-2', '2026-09-10', '2026-11-08', 20, '2026-08-15', 'Taller de robótica educativa.', 'edicion-placeholder.jpg', 0), -- E9 (C7)
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', '2026-07-29', '2026-10-07', 5, '2026-07-10', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'edicion-placeholder.jpg', 0), -- E10 (C8)
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', '2026-09-15', '2026-12-15', 5, '2026-06-02', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación', 'edicion-placeholder.jpg', 0), -- E11 (C9)
	('MicroBit - 2026', '2026-08-12', '2026-12-05', 30, '2026-07-02', 'MicroBit', 'edicion-placeholder.jpg', 0), -- E12 (C10)
	('Talleres plenarios - 2026', '2026-03-10', '2026-03-30', null, '2026-03-02', 'Talleres plenarios', 'edicion-placeholder.jpg', 0), -- E13 (C1)
	('Seminarios de Resolución de Problemas - 2026', '2026-09-10', '2026-10-20', null, '2026-07-12', 'Seminarios de Resolución de Problemas', 'edicion-placeholder.jpg', 0); -- E14 (C2)

INSERT INTO `edicion_docente` (`edicion_nombreEdi`, `docente_nickname`)
VALUES
	('Flor del Ceibo - 2010', 'bruces'), -- E1, BS
	('Flor del Ceibo - 2012', 'bruces'), -- E2, BS
	('Flor del Ceibo - 2012', 'adri'), -- E2, AG
	('Flor del Ceibo - 2025', 'bruces'), -- E3, BS
	('Flor del Ceibo - 2025', 'adri'), -- E3, AG
	('Dalavuelta - 2025', 'phils'), -- E4, PS
	('Extensionismo Industrial - 2025', 'phils'), -- E5, PS
	('Inclusión Energética - 2026', 'phils'), -- E6, PS
	('Taller de robótica educativa - 2024', 'heisenberg'), -- E7, WW
	('Taller de robótica educativa - 2026', 'heisenberg'), -- E8, WW
	('Taller de robótica educativa - 2026', 'benkenobi'), -- E8, OK
	('Taller de robótica educativa - 2026-2', 'benkenobi'), -- E9, OK
	('Taller de robótica educativa - 2026-2', 'waston'), -- E9, EW
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'waston'), -- E10, EW
	('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'heisenberg'), -- E11, WW
	('MicroBit - 2026', 'house'), -- E12, GH
	('Talleres plenarios - 2026', 'timmy'), -- E13, TC
	('Talleres plenarios - 2026', 'danny'), -- E13, DR
	('Seminarios de Resolución de Problemas - 2026', 'timmy'); -- E14, TC

INSERT INTO `inscripciones_a_ediciones` (`fechaInscE`, `estudiane_nickname`, `edicion_nombreEdi`, `estado`, `nota`)
VALUES
	('2017-02-20', 'eleven11', 'Flor del Ceibo - 2010', 'ACEPTADA', 6), -- E1, EL
	('2017-02-25', 'chechi', 'Flor del Ceibo - 2010', 'ACEPTADA', 8), -- E1, CH
	('2019-07-12', 'costas', 'Flor del Ceibo - 2012', 'ACEPTADA', 9), -- E2, CO
	('2019-07-15', 'roro', 'Flor del Ceibo - 2012', 'RECHAZADA', null), -- E2, RO
	('2019-07-30', 'weiss', 'Flor del Ceibo - 2012', 'ACEPTADA', 0), -- E2, WE
	('2025-03-10', 'roro', 'Flor del Ceibo - 2025', 'ACEPTADA', 9), -- E3, RO
	('2025-03-15', 'jeffw', 'Flor del Ceibo - 2025', 'ACEPTADA', 6), -- E3, JW
	('2024-07-25', 'chechi', 'Dalavuelta - 2025', 'ACEPTADA', 7), -- E4, CH
	('2024-07-28', 'eleven11', 'Dalavuelta - 2025', 'RECHAZADA', null), -- E4, EL
	('2024-08-02', 'roro', 'Dalavuelta - 2025', 'ACEPTADA', 7), -- E4, RO
	('2024-08-10', 'costas', 'Dalavuelta - 2025', 'ACEPTADA', 6), -- E4, CO
	('2024-08-15', 'jeffw', 'Dalavuelta - 2025', 'RECHAZADA', null), -- E4, JW
	('2025-07-18', 'costas', 'Extensionismo Industrial - 2025', 'ACEPTADA', 9), -- E5, CO
	('2025-07-20', 'chechi', 'Extensionismo Industrial - 2025', 'ACEPTADA', 9), -- E5, CH
	('2025-07-29', 'eleven11', 'Extensionismo Industrial - 2025', 'ACEPTADA', 1), -- E5, EL
	('2025-08-05', 'weiss', 'Extensionismo Industrial - 2025', 'ACEPTADA', 11), -- E5, WE
	('2026-02-23', 'roro', 'Inclusión Energética - 2026', 'ACEPTADA', 7), -- E6, RO
	('2026-02-25', 'weiss', 'Inclusión Energética - 2026', 'RECHAZADA', null), -- E6, WE
	('2026-02-28', 'chechi', 'Inclusión Energética - 2026', 'ACEPTADA', 11), -- E6, CH
	('2026-03-03', 'eleven11', 'Inclusión Energética - 2026', 'RECHAZADA', null), -- E6, EL
	('2024-02-18', 'weiss', 'Taller de robótica educativa - 2024', 'RECHAZADA', null), -- E7, WE
	('2024-02-20', 'roro', 'Taller de robótica educativa - 2024', 'RECHAZADA', null), -- E7, RO
	('2024-03-03', 'eleven11', 'Taller de robótica educativa - 2024', 'ACEPTADA', 0), -- E7, EL
	('2024-03-05', 'chechi', 'Taller de robótica educativa - 2024', 'RECHAZADA', null), -- E7, CH
	('2026-02-18', 'jeffw', 'Taller de robótica educativa - 2026', 'ACEPTADA', 8), -- E8, JW
	('2026-02-22', 'costas', 'Taller de robótica educativa - 2026', 'ACEPTADA', 10), -- E8, CO
	('2026-02-23', 'roro', 'Taller de robótica educativa - 2026', 'RECHAZADA', null), -- E8, RO
	('2026-08-18', 'weiss', 'Taller de robótica educativa - 2026-2', 'INSCRIPTO', null), -- E9, WE
	('2026-08-22', 'chechi', 'Taller de robótica educativa - 2026-2', 'INSCRIPTO', null), -- E9, CH
	('2026-09-03', 'roro', 'Taller de robótica educativa - 2026-2', 'INSCRIPTO', null), -- E9, RO
	('2026-07-13', 'chechi', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'ACEPTADA', 0), -- E10, CH
	('2026-07-20', 'weiss', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'ACEPTADA', 0), -- E10, WE
	('2026-07-22', 'roro', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026', 'ACEPTADA', 0), -- E10, RO
	('2026-06-04', 'weiss', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'INSCRIPTO', null), -- E11, WE
	('2026-07-18', 'eleven11', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'INSCRIPTO', null), -- E11, EL
	('2026-08-20', 'jeffw', 'Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26', 'INSCRIPTO', null), -- E11, JW
	('2026-07-12', 'chechi', 'MicroBit - 2026', 'INSCRIPTO', null), -- E12, CH
	('2026-07-14', 'roro', 'MicroBit - 2026', 'INSCRIPTO', null), -- E12, RO
	('2026-07-25', 'eleven11', 'MicroBit - 2026', 'INSCRIPTO', null), -- E12, EL
	('2026-08-05', 'jeffw', 'MicroBit - 2026', 'INSCRIPTO', null), -- E12, JW
	('2026-03-05', 'costas', 'Talleres plenarios - 2026', 'RECHAZADA', null), -- E13, CO
	('2026-03-04', 'weiss', 'Talleres plenarios - 2026', 'ACEPTADA', 6), -- E13, WE
	('2026-03-07', 'roro', 'Talleres plenarios - 2026', 'ACEPTADA', 7), -- E13, RO
	('2026-07-15', 'weiss', 'Seminarios de Resolución de Problemas - 2026', 'ACEPTADA', 10), -- E14, WE
	('2026-07-20', 'costas', 'Seminarios de Resolución de Problemas - 2026', 'RECHAZADA', null), -- E14, CO
	('2026-08-06', 'roro', 'Seminarios de Resolución de Problemas - 2026', 'ACEPTADA', 2), -- E14, RO
	('2026-08-30', 'chechi', 'Seminarios de Resolución de Problemas - 2026', 'RECHAZADA', null); -- E14, CH

INSERT INTO `valoraciones` (`curso_nombreCurso`, `estudiante_nickname`, `puntaje`)
VALUES
	('Flor del Ceibo', 'eleven11', 3), -- C6, EL
	('Flor del Ceibo', 'chechi', 4), -- C6, CH
	('Flor del Ceibo', 'costas', 5), -- C6, CO
	('Flor del Ceibo', 'weiss', 4), -- C6, WE
	('Flor del Ceibo', 'roro', 3), -- C6, RO
	('Flor del Ceibo', 'jeffw', 3), -- C6, JW
	('Dalavuelta', 'chechi', 2), -- C3, CH
	('Dalavuelta', 'roro', 5), -- C3, RO
	('Dalavuelta', 'costas', 4), -- C3, CO
	('Extensionismo Industrial', 'costas', 1), -- C4, CO
	('Extensionismo Industrial', 'chechi', 2), -- C4, CH
	('Extensionismo Industrial', 'eleven11', 3), -- C4, EL
	('Extensionismo Industrial', 'weiss', 1), -- C4, WE
	('Inclusión Energética', 'roro', 5), -- C5, RO
	('Inclusión Energética', 'chechi', 5), -- C5, CH
	('Taller de robótica educativa.', 'eleven11', 4), -- C7, EL
	('Taller de robótica educativa.', 'jeffw', 5), -- C7, JW
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'chechi', 4), -- C8, CH
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'weiss', 4), -- C8, WE
	('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 'roro', 4), -- C8, RO
	('Talleres plenarios', 'weiss', 1), -- C1, WE
	('Seminarios de Resolución de Problemas', 'roro', 2); -- C2, RO

INSERT INTO `programas_de_formacion` (`nombre`, `descripcion`, `fechaInicio`, `fechaFin`, `fechaDeCreacion`, `imagen`)
VALUES
	('EFI Ingeniería Mecánica', 'Programa mecánica', '2026-05-01', '2026-10-31', CURDATE(), 'imagenes/programas/p1.jpg'), -- P1
	('Formación integral', 'Programa varios institutos', '2026-07-15', '2027-01-01', CURDATE(), 'imagenes/programas/p2.jpg'), -- P2
	('EFI Robótica', 'Programa robótica', '2026-09-03', '2026-11-18', CURDATE(), 'imagenes/programas/p3.jpg'); -- P3

INSERT INTO `programas_cursos` (`nombre_programa`, `nombre_curso`)
VALUES
	('EFI Ingeniería Mecánica', 'Dalavuelta'), -- P1, C3
	('EFI Ingeniería Mecánica', 'Extensionismo Industrial'), -- P1, C4
	('EFI Ingeniería Mecánica', 'Inclusión Energética'), -- P1, C5
	('Formación integral', 'Seminarios de Resolución de Problemas'), -- P2, C2
	('Formación integral', 'Extensionismo Industrial'), -- P2, C4
	('Formación integral', 'Flor del Ceibo'), -- P2, C6
	('Formación integral', 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela'), -- P2, C8
	('EFI Robótica', 'Taller de robótica educativa.'), -- P3, C7
	('EFI Robótica', 'MicroBit'); -- P3, C10

INSERT INTO `inscripciones_a_programas` (`estudiante_nickname`, `programa_nombre`, `fechaInscripcion`)
VALUES
	('eleven11', 'EFI Ingeniería Mecánica', '2026-04-15'), -- EL, P1
	('chechi', 'EFI Ingeniería Mecánica', '2026-04-20'), -- CH, P1
	('costas', 'Formación integral', '2026-07-10'), -- CO, P2
	('chechi', 'Formación integral', '2026-07-10'), -- CH, P2
	('eleven11', 'Formación integral', '2026-07-12'), -- EL, P2
	('weiss', 'Formación integral', '2026-07-15'), -- WE, P2
	('roro', 'Formación integral', '2026-07-18'), -- RO, P2
	('costas', 'EFI Robótica', '2026-08-11'), -- CO, P3
	('chechi', 'EFI Robótica', '2026-08-15'), -- CH, P3
	('roro', 'EFI Robótica', '2026-08-22'), -- RO, P3
	('jeffw', 'EFI Robótica', '2026-08-30'); -- JW, P3

COMMIT;
