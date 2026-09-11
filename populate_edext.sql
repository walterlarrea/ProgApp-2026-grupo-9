-- Test-data seed script for the edEXT database.
-- Add INSERT statements in the section below when the test data is available.
-- This script intentionally contains no CREATE, ALTER, DROP, or TRUNCATE statements.

USE edEXT;

START TRANSACTION;

INSERT INTO `instituto` (`nombreI`)
VALUES
	('IN'),
	('IL'),
	('IF'),
	('IM'),
	('IE'),
	('DI');

INSERT INTO `usuario` (`nickname`, `apellido`, `email`, `fechaNac`, `imagen`, `nombre`)
VALUES
	('eleven11', 'Twelve', 'eleven11@gmail.com', '1971-12-31', NULL, 'Eleven'),
	('costas', 'Costas', 'gcostas@gmail.com', '1983-11-15', NULL, 'Gerardo'),
	('roro', 'Cotelo', 'rcotelo@yahoo.com', '1975-08-02', NULL, 'Rodrigo'),
	('chechi', 'Garrido', 'cgarrido@hotmail.com', '1987-09-12', NULL, 'Cecilia'),
	('jeffw', 'Williams', 'jwilliams@gmail.com', '1964-11-27', NULL, 'Jeff'),
	('weiss', 'Weiss', 'aweiss@hotmail.com', '1978-12-23', NULL, 'Adrian'),
	('heisenberg', 'White', 'heisenberg@gmail.com', '1956-03-07', NULL, 'Walter'),
	('benkenobi', 'Kenobi', 'benKenobi@gmail.com', '1914-04-02', NULL, 'Obi-Wan'),
	('waston', 'Watson', 'e.watson@gmail.com', '1990-04-15', NULL, 'Emma'),
	('house', 'House', 'greghouse@gmail.com', '1959-05-15', NULL, 'Gregory'),
	('timmy', 'Cook', 'tim.cook@apple.com', '1960-11-01', NULL, 'Tim'),
	('danny', 'Riccio', 'dan.riccio@gmail.com', '1963-07-05', NULL, 'Daniel'),
	('phils', 'Schiller', 'schiller@gmail.com', '1961-10-07', NULL, 'Philip'),
	('bruces', 'Sewell', 'sewell@gmail.com', '1959-12-03', NULL, 'Bruce'),
	('adri', 'García', 'agarcia@gmail.com', '1978-07-28', NULL, 'Adriana');

INSERT INTO `estudiante` (`nickname`)
VALUES
	('eleven11'),
	('costas'),
	('roro'),
	('chechi'),
	('jeffw'),
	('weiss');

INSERT INTO `docente` (`nickname`, `nombreInst`)
VALUES
	('heisenberg', 'IN'), -- Walter White
	('benkenobi', 'IN'), -- Obi-Wan Kenobi
	('waston', 'IN'), -- Emma Watson
	('house', 'IE'), -- Gregory House
	('timmy', 'IL'), -- Tim Cook
	('danny', 'IL'), -- Daniel Riccio
	('phils', 'IM'), -- Philip Schiller
	('bruces', 'DI'), -- Bruce Sewell
	('adri', 'DI'); -- Adriana García

INSERT INTO `docente_instituto` (`docente_nickname`, `instituto_nombreI`)
VALUES
	('heisenberg', 'IN'),
	('benkenobi', 'IN'),
	('waston', 'IN'),
	('house', 'IE'),
	('timmy', 'IL'),
	('danny', 'IL'),
	('phils', 'IM'),
	('bruces', 'DI'),
	('adri', 'DI');

INSERT INTO `curso` (`nombreCurso`, `descCurso`, `duracion`, `cantHoras`, `cantCred`, `fechaReg`, `url`, `nombreI`)
VALUES
	('C1', 'Talleres plenarios: presentados por cuatro reconocidos matemáticos uruguayos, plantearán diversos tópicos de matemática en el marco de los cuales se realizarán actividades fomentando la integración entre estudiantes, docentes e investigadores.', 3, 15, 1, '2026-02-01', 'www.tmu.edu.uy', 'IL'),
	('C2', 'Seminario, todos los jueves en Facultad de Ingeniería a partir del jueves 25 de Julio, en las áreas en que se desarrollan los problemas de las Olimpíadas de Matemática.', 5, 30, 2, '2026-07-12', 'www.tmu.edu.uy', 'IL'),
	('C3', 'Dalavuelta es un proyecto de extensión que nace en el Instituto de Ingeniería Mecánica y Producción Industrial (IIMPI) de Fing, que, si bien inicia su trabajo en el desarrollo de bicicletas accesibles para personas en situación de discapacidad motriz a partir de bicicletas abandonadas, se propuso diseñar otras herramientas para fomentar la accesibilidad.', 10, 60, 4, '2024-06-25', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IM'),
	('C4', 'El proyecto tiene como objetivo desarrollar intervenciones curriculares en pequeños emprendimientos productivos de diferentes sectores de la industria nacional. La metodología de trabajo permite articular diversas intervenciones, combinando actividades de enseñanza, extensión e investigación por parte de docentes del IMPII.', 12, 75, 5, '2025-06-16', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IM'),
	('C5', 'En el proyecto se conjuga el trabajo de docentes y estudiantes de la carrera Ingeniería Industrial Mecánica a través del Módulo de Extensión, en donde se trabaja en el diseño, construcción y prueba de un prototipo de colector solar adquiriendo conocimientos relevantes para luego poder replicarlos junto a las familias en los talleres. Las premisas fundamentales a la hora de pensar los diseños son el bajo costo de los materiales y la fácil construcción.', 6, 45, 3, '2026-02-01', 'https://eva.fing.edu.uy/course/view.php?id=783#section-2', 'IM'),
	('C6', 'Flor de Ceibo es un proyecto central de la Universidad de la República, que tiene como misión movilizar la participación de estudiantes universitarios en diversas tareas vinculadas con la puesta en funcionamiento del Plan Ceibal en el territorio nacional.', 15, 150, 10, '2008-07-27', 'http://www.flordeceibo.edu.uy/', 'DI'),
	('C7', 'La asignatura se organiza en dos etapas. La primera etapa se dicta a través de clases teórico-prácticas, donde se espera además que cada estudiante le dedique horas de estudio. La segunda etapa consiste en que los estudiantes trabajen en grupo sobre el diseño e implementación de una experiencia didáctica de inclusión del robot Butiá en el aula, utilizando los conocimientos aprendidos en clase.', 8, 90, 6, '2024-02-02', 'https://eva.fing.edu.uy/course/view.php?id=1187', 'IN'),
	('C8', 'Se propone desarrollar una aplicación interactiva para tablet Android basada en el juego de tablero Komikan (versión web del juego), que incluya los distintos aspectos concernientes al juego, así como a situaciones específicas particulares.', 9, 45, 3, '2026-06-15', 'https://eva.fing.edu.uy/mod/folder/view.php?id=89398', 'IN'),
	('C9', 'Se realizarán visitas a escuelas rurales participantes en un proyecto conjunto del grupo PLN y el Programa de Políticas Lingüísticas de ANEP, en el marco del cual se desarrollaron diferentes herramientas para uso de maestros que enseñan inglés con apoyo remoto de profesores especializados desde Montevideo.', 12, 60, 4, '2026-05-24', 'https://eva.fing.edu.uy/mod/folder/view.php?id=89398', 'IN'),
	('C10', 'El Centro Ceibal se encuentra distribuyendo placas micro:bit para que estudiantes de primaria y secundaria aprendan nociones básicas de robótica, electrónica y programación de forma autónoma y lúdica. Estas placas se basan en un microcontrolador y cuentan con leds, botones, acelerómetro, brújula, bluetooth y otros sensores. Las placas se programan fácilmente con lenguaje tipo scratch y python, por lo que son muy útiles para un primer acercamiento a la temática.', 15, 105, 7, '2026-03-13', 'https://www.fing.edu.uy/noticias/extension/modulo-detallerextensionmicrobit', 'IE');

INSERT INTO `cursos_previas` (`nombre_curso`, `nombre_curso_previa`)
VALUES
	('C2', 'C1'),
	('C3', 'C1'),
	('C4', 'C1');

INSERT INTO `edicioncurso` (`nombreEdi`, `fechaInicio`, `fechaFin`, `cupo`, `fechaPub`, `docente_nickname`)
VALUES
	('E1', '2010-03-15', '2010-07-07', -1, '2010-02-16', 'bruces'),
	('E2', '2012-08-01', '2012-11-20', -1, '2012-07-10', 'bruces'),
	('E3', '2025-04-10', '2025-08-07', -1, '2025-03-06', 'bruces'),
	('E4', '2024-08-20', '2024-11-10', 15, '2024-07-20', 'phils'),
	('E5', '2025-08-10', '2025-11-10', 15, '2025-07-08', 'phils'),
	('E6', '2026-03-15', '2026-04-30', 30, '2026-02-20', 'phils'),
	('E7', '2024-03-10', '2024-05-10', 10, '2024-02-15', 'heisenberg'),
	('E8', '2026-03-10', '2026-05-10', 10, '2026-02-15', 'heisenberg'),
	('E9', '2026-09-10', '2026-11-08', 20, '2026-08-15', 'benkenobi'),
	('E10', '2026-07-29', '2026-10-07', 5, '2026-07-10', 'waston'),
	('E11', '2026-09-15', '2026-12-15', 5, '2026-06-02', 'heisenberg'),
	('E12', '2026-08-12', '2026-12-05', 30, '2026-07-02', 'house'),
	('E13', '2026-03-10', '2026-03-30', -1, '2026-03-02', 'timmy'),
	('E14', '2026-09-10', '2026-10-20', -1, '2026-07-12', 'timmy');

-- Actualmente EdicionCurso modela un docente_nickname por edición.
-- El primer docente listado en la especificación se almacena para ediciones con múltiples docentes.

-- El siguiente insert está comentado porque la tabla edicion_docente no existe en el modelo actual.
-- Se mantiene como referencia para futuras implementaciones que permitan múltiples docentes por edición.

-- INSERT INTO `edicion_docente` (`edicion_nombreEdi`, `docente_nickname`)
-- VALUES
--   ('E1', 'bruces'),
--   ('E2', 'bruces'),
--   ('E2', 'adri'),
--   ('E3', 'bruces'),
--   ('E3', 'adri'),
--   ('E4', 'phils'),
--   ('E5', 'phils'),
--   ('E6', 'phils'),
--   ('E7', 'heisenberg'),
--   ('E8', 'heisenberg'),
--   ('E8', 'benkenobi'),
--   ('E9', 'benkenobi'),
--   ('E9', 'watson'),
--   ('E10', 'waston'),
--   ('E11', 'heisenberg'),
--   ('E12', 'house'),
--   ('E13', 'timmy'),
--   ('E13', 'danny'),
--   ('E14', 'timmy');

INSERT INTO `edicion_curso` (`edicion_nombreEdi`, `curso_nombreCurso`)
VALUES
	('E1', 'C6'),
	('E2', 'C6'),
	('E3', 'C6'),
	('E4', 'C3'),
	('E5', 'C4'),
	('E6', 'C5'),
	('E7', 'C7'),
	('E8', 'C7'),
	('E9', 'C7'),
	('E10', 'C8'),
	('E11', 'C9'),
	('E12', 'C10'),
	('E13', 'C1'),
	('E14', 'C2');

INSERT INTO `inscedicion` (`fechaInscE`, `estudiane_nickname`, `edicion_nombreEdi`)
VALUES
	('2010-02-20', 'eleven11', 'E1'),
	('2010-02-25', 'chechi', 'E1'),
	('2012-07-12', 'costas', 'E2'),
	('2012-07-15', 'roro', 'E2'),
	('2012-07-30', 'weiss', 'E2'),
	('2025-03-10', 'roro', 'E3'),
	('2025-03-15', 'jeffw', 'E3'),
	('2024-07-25', 'chechi', 'E4'),
	('2024-07-28', 'eleven11', 'E4'),
	('2024-08-02', 'roro', 'E4'),
	('2024-08-10', 'costas', 'E4'),
	('2024-08-15', 'jeffw', 'E4'),
	('2025-07-18', 'costas', 'E5'),
	('2025-07-20', 'chechi', 'E5'),
	('2025-07-29', 'eleven11', 'E5'),
	('2025-08-05', 'weiss', 'E5'),
	('2026-02-23', 'roro', 'E6'),
	('2026-02-25', 'weiss', 'E6'),
	('2026-02-28', 'chechi', 'E6'),
	('2026-03-03', 'eleven11', 'E6'),
	('2017-02-18', 'weiss', 'E7'),
	('2024-02-20', 'roro', 'E7'),
	('2024-03-03', 'eleven11', 'E7'),
	('2024-03-05', 'chechi', 'E7'),
	('2026-02-18', 'jeffw', 'E8'),
	('2026-02-22', 'costas', 'E8'),
	('2026-08-18', 'weiss', 'E9'),
	('2026-08-22', 'chechi', 'E9'),
	('2026-09-03', 'roro', 'E9'),
	('2026-07-13', 'chechi', 'E10'),
	('2026-07-20', 'weiss', 'E10'),
	('2026-07-22', 'roro', 'E10'),
	('2026-06-04', 'weiss', 'E11'),
	('2026-07-18', 'eleven11', 'E11'),
	('2026-08-20', 'jeffw', 'E11'),
	('2026-07-12', 'chechi', 'E12'),
	('2026-07-14', 'roro', 'E12'),
	('2026-07-25', 'eleven11', 'E12'),
	('2026-08-05', 'jeffw', 'E12'),
	('2026-03-05', 'costas', 'E13'),
	('2026-03-04', 'weiss', 'E13'),
	('2026-03-07', 'roro', 'E13'),
	('2026-07-15', 'weiss', 'E14'),
	('2026-07-20', 'costas', 'E14'),
	('2026-08-06', 'roro', 'E14'),
	('2026-08-30', 'chechi', 'E14');

INSERT INTO `programadeformacion` (`nombre`, `descripcion`, `fechaInicio`, `fechaFin`, `fechaDeCreacion`)
VALUES
	('EFI Ingeniería Mecánica', 'Programa mecánica', '2026-05-01', '2026-10-31', CURDATE()),
	('Formación integral', 'Programa varios institutos', '2026-07-15', '2027-01-01', CURDATE()),
	('EFI Robótica', 'Programa robótica', '2026-09-03', '2026-11-18', CURDATE());

INSERT INTO `programas_cursos` (`nombre_programa`, `nombre_curso`)
VALUES
	('EFI Ingeniería Mecánica', 'C3'),
	('EFI Ingeniería Mecánica', 'C4'),
	('EFI Ingeniería Mecánica', 'C5'),
	('Formación integral', 'C2'),
	('Formación integral', 'C4'),
	('Formación integral', 'C6'),
	('Formación integral', 'C8'),
	('EFI Robótica', 'C7'),
	('EFI Robótica', 'C10');

COMMIT;
