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
    cgpa DOUBLE
);

-- Organisation Table
CREATE TABLE Organisation (
    name VARCHAR(255) PRIMARY KEY,
    industry VARCHAR(255),
    description TEXT,
    location VARCHAR(255),
    contactEmail VARCHAR(255),
    isVerified BOOLEAN
);

-- Organisation Representative Table
CREATE TABLE OrganisationRepresentative (
    name VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255) PRIMARY KEY,
    position VARCHAR(255),
    phone VARCHAR(20),
    orgID VARCHAR(255),
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
    ownerType ENUM('Student', 'Organisation'),
    studentID VARCHAR(255),
    organisationID VARCHAR(255),
    FOREIGN KEY (studentID) REFERENCES Student(rollNo),
    FOREIGN KEY (organisationID) REFERENCES Organisation(name)
);

-- Chat Table
CREATE TABLE Chat (
    chatID INT PRIMARY KEY AUTO_INCREMENT,
    createdAt DATETIME,
    orgId VARCHAR(255),
    studentId VARCHAR(255),
    chatBoxId INT,
    FOREIGN KEY (orgId) REFERENCES Organisation(name),
    FOREIGN KEY (studentId) REFERENCES Student(rollNo),
    FOREIGN KEY (chatBoxId) REFERENCES ChatBox(chatBoxId)
);

-- Message Table
CREATE TABLE Message (
    senderId VARCHAR(255),
    receiverId VARCHAR(255),
    text TEXT,
    chatID INT,
    PRIMARY KEY (senderId, receiverId, chatID),
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
DROP TABLE FlexJobs.OrganisationRepresentative;
DROP TABLE FlexJobs.Organisation;
DROP TABLE FlexJobs.Registration;
DELETE FROM FlexJobs.Organisation WHERE name = "Devsinc";
DELETE FROM FlexJobs.OrganisationRepresentative WHERE repID = "REP-fdcd9b8c-cc2e-4182-a3ab-436489335c01" || repID = "REP-fe5ef4f6-6d79-436c-8101-d8b38a694500";

