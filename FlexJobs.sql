-- Step 1: Create the Database
CREATE DATABASE FlexJobs;

-- Step 2: Use the Database
USE FlexJobs;

-- Step 3: Create the Tables

-- Admin Table
CREATE TABLE Admin (
    name VARCHAR(255),
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255)
);

-- Student Table
CREATE TABLE Student (
    rollNo VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    department VARCHAR(255),
    semester INT,
    cgpa DOUBLE,
    resume Text,
    chatBoxId int,
	FOREIGN KEY (chatBoxId) REFERENCES ChatBox(chatBoxId)
);

-- Organisation Table
CREATE TABLE Organisation (
    name VARCHAR(255) PRIMARY KEY,
    industry VARCHAR(255),
    description TEXT,
    location VARCHAR(255),
    contactEmail VARCHAR(255),
    isVerified BOOLEAN,
    chatBoxId int,
	FOREIGN KEY (chatBoxId) REFERENCES ChatBox(chatBoxId)
);

-- Organisation Representative Table
CREATE TABLE OrganisationRepresentative (
    name VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255) PRIMARY KEY,
    position VARCHAR(255),
    phone VARCHAR(20),
    orgID VARCHAR(255),
	isVerified BOOLEAN,
    FOREIGN KEY (orgID) REFERENCES Organisation(name)
);
-- Registration Table
CREATE TABLE Registration (
    registrationId INT PRIMARY KEY AUTO_INCREMENT,
    organizationId VARCHAR(255),
    registrationDate DATE,
    status BOOLEAN,
    isApproved BOOLEAN,
    orgRepresentativeId VARCHAR(255),
    isNewOrg BOOLEAN,
    FOREIGN KEY (organizationId) REFERENCES Organisation(name),
    FOREIGN KEY (orgRepresentativeId) REFERENCES OrganisationRepresentative(email)
);

-- Opportunity Table
CREATE TABLE Opportunity (
    opportunityID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    postedBy VARCHAR(255),
    FOREIGN KEY (postedBy) REFERENCES Organisation(name)
);


-- Job Table (Subclass of Opportunity)
CREATE TABLE Job (
    opportunityID INT PRIMARY KEY,
    category VARCHAR(255),
    requirements TEXT, -- Added this column
    FOREIGN KEY (opportunityID) REFERENCES Opportunity(opportunityID)
);


-- Educational Table (Subclass of Opportunity)
CREATE TABLE Educational (
    opportunityID INT PRIMARY KEY,
    FOREIGN KEY (opportunityID) REFERENCES Opportunity(opportunityID)
);


-- Application Table
CREATE TABLE Application (
    applicationID INT PRIMARY KEY AUTO_INCREMENT,
    submitDate DATE,
    status VARCHAR(50) NOT NULL,
    feedback TEXT,
    studentID VARCHAR(255) NOT NULL,
    interviewID int,
    opportunityID INT NOT NULL,
    FOREIGN KEY (studentID) REFERENCES Student(rollNo),
    FOREIGN KEY (interviewID) REFERENCES Interview(interviewID),
    FOREIGN KEY (opportunityID) REFERENCES Opportunity(opportunityID)
);



-- Interview Table
CREATE TABLE Interview (
    interviewID int PRIMARY KEY AUTO_INCREMENT,
    candidateID VARCHAR(255),
    timeSlot DATETIME,
    type varchar(255),
    status varchar(255),
    location VARCHAR(255),
    FOREIGN KEY (candidateID) REFERENCES Student(rollNo)
);



-- ChatBox Table
CREATE TABLE ChatBox (
    chatBoxId INT PRIMARY KEY AUTO_INCREMENT,
    ownerType ENUM('Student', 'Organisation')
);

-- Chat Table
CREATE TABLE Chat (
    chatID INT PRIMARY KEY AUTO_INCREMENT,
    createdAt Timestamp,
    orgId VARCHAR(255),
    studentId VARCHAR(255),
    FOREIGN KEY (orgId) REFERENCES Organisation(name),
    FOREIGN KEY (studentId) REFERENCES Student(rollNo)
);

-- Message Table
CREATE TABLE Message (
    msgId INT PRIMARY KEY AUTO_INCREMENT,
    senderId VARCHAR(255),
    receiverId VARCHAR(255),
    text TEXT,
    transmittedAt timestamp,
    chatID INT,
    FOREIGN KEY (chatID) REFERENCES Chat(chatID)
);

-- Notification Table
CREATE TABLE Notification (
    notificationId INT PRIMARY KEY AUTO_INCREMENT,
    senderId VARCHAR(255),
    receiverId VARCHAR(255),
    isRead BOOLEAN,
    message TEXT,
    timestamp DATETIME
);

-- Report Table
CREATE TABLE Report (
    reportID VARCHAR(255) PRIMARY KEY,
    reportType VARCHAR(50),
    content TEXT,
    generatedDate DATE
);

show tables;
select * from FlexJobs.Registration;
select * from FlexJobs.OrganisationRepresentative;
select * from FlexJobs.Organisation;
select * from admin;
select * from student;
select * from notification;
select * from chatbox;
select * from chat;
select * from message;
DROP TABLE FlexJobs.OrganisationRepresentative;
DROP TABLE FlexJobs.Organisation;
DROP TABLE FlexJobs.Registration;
DROP Table flexjobs.chatbox;
DROP Table flexjobs.chat;
DROP Table flexjobs.message;
DROP Table Student;
DROP Table Interview;
DROP Table application;
DROP Table job;
drop table student;
Truncate table flexjobs.organisation;
DELETE FROM FlexJobs.Organisation WHERE name = "Devsinc";
DELETE FROM FlexJobs.OrganisationRepresentative WHERE repID = "REP-fdcd9b8c-cc2e-4182-a3ab-436489335c01" || repID = "REP-fe5ef4f6-6d79-436c-8101-d8b38a694500";


-- Insert Dummy Unverified Organizations
INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified)
VALUES 
    ('TechCorp', 'Technology', 'A leading tech company', 'New York', 'contact@techcorp.com', false),
    ('FinServe', 'Finance', 'Financial services and consultancy', 'London', 'contact@finserve.com', false),
    ('EduWorld', 'Education', 'A global education provider', 'Sydney', 'contact@eduworld.com', false);

-- Insert Dummy Unverified Organisation Representatives
INSERT INTO OrganisationRepresentative (name, password, email, position, phone, orgID, isVerified)
VALUES 
    ('Alice Johnson', 'password123', 'alice@techcorp.com', 'Manager', '1234567890', 'TechCorp', false),
    ('Bob Smith', 'password456', 'bob@finserve.com', 'Senior Consultant', '0987654321', 'FinServe', false),
    ('Charlie Davis', 'password789', 'charlie@eduworld.com', 'Administrator', '1122334455', 'EduWorld', false);

Update Organisation SET isVerified = 0 where name = 'google' ;
Update OrganisationRepresentative SET isVerified = 0 where orgID = 'google' ;

INSERT INTO Notification (senderId, receiverId, isRead, message, timestamp)
VALUES 
('user123', 'admin', FALSE, 'A new user has registered.', '2024-11-24 09:15:00'),
('system', 'admin', TRUE, 'Database maintenance completed successfully.', '2024-11-23 14:30:00'),
('user456', 'admin', FALSE, 'Please verify my account details.', '2024-11-24 10:45:00'),
('system', 'admin', FALSE, 'Server update scheduled for tonight at 12:00 AM.', '2024-11-24 08:00:00');


-- Step 1: Insert ChatBox for the Student
INSERT INTO ChatBox (ownerType)
VALUES ('Student');

-- Step 2: Insert Student (using the chatBoxId from the previous insert)
INSERT INTO Student (rollNo, email, name, password, department, semester, cgpa, chatBoxId)
VALUES ('S12345', 'student@example.com', 'John Doe', 'password123', 'Computer Science', 3, 3.8, LAST_INSERT_ID());

-- Step 3: Insert ChatBox for the Organization
INSERT INTO ChatBox (ownerType)
VALUES ('Organisation');

-- Step 4: Insert Organization (using the chatBoxId from the previous insert)
INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified, chatBoxId)
VALUES ('Org1', 'Software Development', 'A leading software company', 'New York, USA', 'contact@org1.com', true, LAST_INSERT_ID());

INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified, chatBoxId)
VALUES ('Org2', 'Software Development', 'A leading software company', 'New York, USA', 'contact@org1.com', true, LAST_INSERT_ID());


-- Step 5: Insert a chat between the student (S12345) and the organization (Org1)
-- We'll use the chatBoxId for the student and the organization to link them
-- Get the chatBoxId for the student and the organization

-- Fetch the chatBoxId for student
SET @studentChatBoxId = (SELECT chatBoxId FROM Student WHERE rollNo = 'S12345');

-- Fetch the chatBoxId for organization
SET @orgChatBoxId = (SELECT chatBoxId FROM Organisation WHERE name = 'Org1');

-- Insert Chat (Student chatting with the Organization)
INSERT INTO Chat (studentId, orgId,createdAt)
VALUES ('S12345', 'Org1',NOW())

INSERT INTO Message (senderId, receiverId, text, transmittedAt, chatID)
VALUES 
    ('S12345', 'Org1', 'Hello! I am interested in your job posting.', '2024-11-23 09:30:00', 2),
    ('Org1', 'S12345', 'Thank you for reaching out! Could you please share your resume?', '2024-11-23 09:32:00', 2),
    ('S12345', 'Org1', 'Sure, I will send it right away.', '2024-11-23 09:35:00', 2),
    ('Org1', 'S12345', 'Great! Looking forward to reviewing it.', '2024-11-23 09:40:00', 2),
    ('S12345', 'Org1', 'Let me know if you need any additional details.', '2024-11-23 09:45:00', 2),
    ('Org1', 'S12345', 'Thank you. We will get back to you soon.', '2024-11-23 09:50:00', 2);

INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified) VALUES
('Tech Innovators', 'Technology', 'A leading software development company focused on innovative solutions.', 'Silicon Valley, CA', 'info@techinnovators.com', TRUE),
('Green Future Inc.', 'Sustainability', 'Dedicated to creating eco-friendly and sustainable solutions.', 'Eco City, NY', 'contact@greenfuture.com', TRUE),
('Education for All', 'Education', 'A non-profit organization providing affordable education for everyone.', 'EduTown, MA', 'support@educationforall.org', FALSE);

-- Insert opportunities into Opportunity table
INSERT INTO Opportunity (title, description, postedBy) VALUES
('Software Engineer', 'Develop and maintain cutting-edge software solutions.', 'Tech Innovators'),
('Data Analyst', 'Analyze and interpret complex datasets for business insights.', 'Tech Innovators'),
('Environmental Scientist', 'Conduct research on sustainable solutions for environmental challenges.', 'Green Future Inc.'),
('Marketing Manager', 'Lead marketing campaigns to promote eco-friendly products.', 'Green Future Inc.'),
('IT Support Specialist', 'Provide technical support for organizational IT needs.', 'Tech Innovators'),
('Machine Learning Workshop', 'An intensive workshop on machine learning techniques.', 'Education for All'),
('Sustainability Seminar', 'A seminar on innovative approaches to sustainability.', 'Green Future Inc.'),
('Coding Bootcamp', 'Learn to code from scratch in 12 weeks.', 'Tech Innovators'),
('Renewable Energy Research Program', 'Explore cutting-edge renewable energy technologies.', 'Green Future Inc.'),
('Leadership Training', 'Develop leadership skills through practical sessions.', 'Education for All');

-- Link opportunities in Job table
INSERT INTO Job (opportunityID, category, requirements) VALUES
(1, 'Technology', 'Experience with modern programming languages such as Python, Java, or C++; strong problem-solving skills.'),
(2, 'Data Science', 'Proficiency in machine learning frameworks; expertise in Python and SQL; data visualization experience.'),
(3, 'Research', 'Strong analytical skills; experience in academic or industrial research; ability to write detailed reports.'),
(4, 'Marketing', 'Excellent communication skills; experience with marketing campaigns and SEO; ability to analyze market trends.'),
(5, 'Technology', 'Familiarity with cloud platforms such as AWS or Azure; knowledge of DevOps practices; experience in system design.');

delete from job;
INSERT INTO Job (opportunityID, category, requirements) 
VALUES 
(1, 'Software', 'Proficiency in programming languages like Java, Python, and experience with cloud technologies.'),
(2, 'Data Analysis', 'Strong background in data analytics, statistical methods, and proficiency with tools like Excel, R, or Python.'),
(3, 'Environmental Research', 'Knowledge in environmental science, sustainability practices, and research methodologies.'),
(4, 'Marketing', 'Experience in leading marketing teams, developing campaigns, and managing eco-friendly product promotions.'),
(5, 'IT Support', 'Experience in troubleshooting hardware/software issues, network maintenance, and providing technical assistance.'),
(6, 'Workshop Facilitator', 'Experience in teaching machine learning concepts, Python programming, and hands-on workshops.'),
(7, 'Seminar Speaker', 'Experience in sustainability practices and effective public speaking for seminars and conferences.'),
(8, 'Coding Instructor', 'Proficiency in multiple programming languages, experience in teaching coding, and strong communication skills.'),
(9, 'Renewable Energy Researcher', 'Experience in renewable energy technologies, research methodologies, and environmental impact analysis.'),
(10, 'Leadership Coach', 'Experience in coaching, team management, and leadership development through workshops and hands-on training.');

select * from student;
SELECT o.title, o.description, o.postedBy, j.category, j.requirements FROM Opportunity o INNER JOIN Job j ON j.opportunityID = 3;
-- Link opportunities in Educational table
INSERT INTO Educational (opportunityID) VALUES (6), (7), (8), (9), (10);


INSERT INTO Notification (senderId, receiverId, isRead, message, timestamp) 
VALUES 
('admin', '22i-0970', FALSE, 'Your job application has been successfully submitted.', NOW()),
('hr@company.com', '22i-0970', FALSE, 'New job opportunity: Software Engineer position available.', NOW()),
('admin', '22i-0970', TRUE, 'Reminder: Please complete your profile information.', NOW()),
('hr@company.com', '22i-0970', FALSE, 'Interview scheduled for the Software Engineer position on 15th December.', NOW()),
('admin', '22i-0970', FALSE, 'Your application status has been updated to "Under Review".', NOW());

 SELECT o.opportunityID, o.title FROM Opportunity o INNER JOIN Job j ON o.opportunityID = j.opportunityID WHERE LOWER(j.category) LIKE LOWER("%data%");


update notification set isRead=False;
select * from student;
delete from student where rollNo="22i-1024";

update flexjobs.organisation set isVerified=true where name = "TechCorp" 