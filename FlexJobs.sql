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
    type ENUM('Job', 'Educational')
);

-- Job Table (Subclass of Opportunity)
CREATE TABLE Job (
    opportunityID INT PRIMARY KEY,
    category VARCHAR(255),
    applicationID INT,
    FOREIGN KEY (opportunityID) REFERENCES Opportunity(opportunityID),
    FOREIGN KEY (applicationID) REFERENCES Application(applicationID)
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
    status VARCHAR(50),
    feedback TEXT,
    studentID VARCHAR(255),
    interviewID VARCHAR(255),
    FOREIGN KEY (studentID) REFERENCES Student(rollNo),
    FOREIGN KEY (interviewID) REFERENCES Interview(interviewID)
);

-- Interview Table
CREATE TABLE Interview (
    interviewID VARCHAR(255) PRIMARY KEY,
    candidateID VARCHAR(255),
    timeSlot DATETIME,
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
    createdAt DATETIME,
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

Update Organisation SET isVerified = 0 where name = 'FinServe' ;
Update OrganisationRepresentative SET isVerified = 0 where orgID = 'FinServe' ;

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
