USE smart_study_planner;

INSERT INTO students (name, course, email)
SELECT 'Aarav', 'BTECH', 'AARAV.14811@STU.UPES.AC.IN'
WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE email = 'AARAV.14811@STU.UPES.AC.IN'
);

INSERT INTO students (name, course, email)
SELECT 'Diya', 'BCOM', 'DIYA.14829@STU.UPES.AC.IN'
WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE email = 'DIYA.14829@STU.UPES.AC.IN'
);

INSERT INTO students (name, course, email)
SELECT 'Kavya', 'BA', 'KAVYA.14863@STU.UPES.AC.IN'
WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE email = 'KAVYA.14863@STU.UPES.AC.IN'
);

INSERT INTO students (name, course, email)
SELECT 'Rohan', 'BTECH', 'ROHAN.14874@STU.UPES.AC.IN'
WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE email = 'ROHAN.14874@STU.UPES.AC.IN'
);

INSERT INTO students (name, course, email)
SELECT 'Saanvi', 'BCOM', 'SAANVI.14892@STU.UPES.AC.IN'
WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE email = 'SAANVI.14892@STU.UPES.AC.IN'
);

INSERT INTO subjects (name, description) VALUES
    ('Java OOP', 'Classes, inheritance, abstraction, and interfaces'),
    ('DBMS', 'Normalization, joins, and SQL queries'),
    ('Operating Systems', 'Processes, threads, scheduling, and memory management'),
    ('Data Structures', 'Stacks, queues, trees, and graphs'),
    ('Computer Networks', 'OSI model, routing, and protocols')
ON DUPLICATE KEY UPDATE description = VALUES(description);

INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status)
SELECT s.subject_id, 'Finish inheritance notes', 'Study', '2026-05-08', 'High', 'Completed'
FROM subjects s
WHERE s.name = 'Java OOP'
  AND NOT EXISTS (
      SELECT 1 FROM tasks t WHERE t.title = 'Finish inheritance notes'
  );

INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status)
SELECT s.subject_id, 'Practice JDBC program', 'Revision', '2026-05-10', 'Medium', 'Pending'
FROM subjects s
WHERE s.name = 'Java OOP'
  AND NOT EXISTS (
      SELECT 1 FROM tasks t WHERE t.title = 'Practice JDBC program'
  );

INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status)
SELECT s.subject_id, 'Prepare SQL joins questions', 'Study', '2026-05-09', 'High', 'Pending'
FROM subjects s
WHERE s.name = 'DBMS'
  AND NOT EXISTS (
      SELECT 1 FROM tasks t WHERE t.title = 'Prepare SQL joins questions'
  );

INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status)
SELECT s.subject_id, 'Revise process synchronization', 'Revision', '2026-05-11', 'Medium', 'Pending'
FROM subjects s
WHERE s.name = 'Operating Systems'
  AND NOT EXISTS (
      SELECT 1 FROM tasks t WHERE t.title = 'Revise process synchronization'
  );

INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status)
SELECT s.subject_id, 'Solve binary tree problems', 'Study', '2026-05-12', 'High', 'Completed'
FROM subjects s
WHERE s.name = 'Data Structures'
  AND NOT EXISTS (
      SELECT 1 FROM tasks t WHERE t.title = 'Solve binary tree problems'
  );

INSERT INTO tasks (subject_id, title, task_type, deadline, priority, status)
SELECT s.subject_id, 'Review transport layer concepts', 'Revision', '2026-05-13', 'Low', 'Pending'
FROM subjects s
WHERE s.name = 'Computer Networks'
  AND NOT EXISTS (
      SELECT 1 FROM tasks t WHERE t.title = 'Review transport layer concepts'
  );
