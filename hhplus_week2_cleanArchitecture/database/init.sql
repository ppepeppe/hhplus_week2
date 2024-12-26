CREATE TABLE Lecture (
    lecture_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    lecturer VARCHAR(255) NOT NULL,
    current_count INT NOT NULL,
    max_capacity INT NOT NULL,
    date DATE NOT NULL
);

CREATE TABLE LectureApplication (
    application_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    lecture_id BIGINT NOT NULL,
    applied_at DATETIME NOT NULL,
    UNIQUE(user_id, lecture_id)
);

CREATE TABLE User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

INSERT INTO Lecture (title, lecturer, current_count, max_capacity, date)
VALUES ("math", "kim", 0, 30, "2025-01-01");

INSERT INTO User (name, email)
VALUES ("seongdo", "dmdsns@naver.com");

