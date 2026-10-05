CREATE DATABASE IF NOT EXISTS online_exam;
USE online_exam;

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS exams (
    exam_id INT PRIMARY KEY,
    exam_name VARCHAR(150) NOT NULL,
    duration_minutes INT NOT NULL,
    total_marks INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    exam_id INT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_option CHAR(1) NOT NULL,
    marks INT NOT NULL DEFAULT 1,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE,
    CHECK (correct_option IN ('A','B','C','D'))
);

CREATE TABLE IF NOT EXISTS exam_attempts (
    attempt_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(30) NOT NULL,
    exam_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NULL,
    score INT NOT NULL DEFAULT 0,
    attempted INT NOT NULL DEFAULT 0,
    correct INT NOT NULL DEFAULT 0,
    wrong INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id)
);

CREATE TABLE IF NOT EXISTS student_answers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    attempt_id INT NOT NULL,
    question_id INT NOT NULL,
    selected_option CHAR(1),
    is_correct BOOLEAN NOT NULL,
    FOREIGN KEY (attempt_id) REFERENCES exam_attempts(attempt_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(question_id),
    UNIQUE KEY unique_attempt_question (attempt_id, question_id)
);

INSERT INTO admins(username, password)
SELECT 'admin', 'admin123'
WHERE NOT EXISTS (SELECT 1 FROM admins WHERE username = 'admin');

INSERT INTO students(student_id, name, password, email)
SELECT 'STU001', 'Demo Student', 'student123', 'stu001@example.com'
WHERE NOT EXISTS (SELECT 1 FROM students WHERE student_id = 'STU001');

INSERT INTO exams(exam_id, exam_name, duration_minutes, total_marks, active)
SELECT 101, 'Java Programming', 5, 5, TRUE
WHERE NOT EXISTS (SELECT 1 FROM exams WHERE exam_id = 101);

INSERT INTO questions(exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
SELECT 101, 'Which keyword is used for inheritance?', 'implements', 'extends', 'inherits', 'super', 'B', 1
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE exam_id = 101);

INSERT INTO questions(exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
SELECT 101, 'Which method is the entry point of a Java application?', 'start()', 'run()', 'main()', 'execute()', 'C', 1
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE exam_id = 101 AND question_text = 'Which method is the entry point of a Java application?');

INSERT INTO questions(exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
SELECT 101, 'Which collection does not allow duplicate elements?', 'List', 'Set', 'Vector', 'ArrayList', 'B', 1
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE exam_id = 101 AND question_text LIKE 'Which collection does not allow duplicate%');

INSERT INTO questions(exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
SELECT 101, 'Which keyword handles an exception?', 'try', 'throw', 'catch', 'throws', 'C', 1
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE exam_id = 101 AND question_text LIKE 'Which keyword handles an exception?');

INSERT INTO questions(exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
SELECT 101, 'Which API is commonly used for Java database connectivity?', 'JDBC', 'JVM', 'JRE', 'JDK', 'A', 1
WHERE NOT EXISTS (SELECT 1 FROM questions WHERE exam_id = 101 AND question_text LIKE 'Which API is commonly used%');
