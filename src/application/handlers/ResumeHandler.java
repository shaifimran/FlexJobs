package application.handlers;

import java.io.File;

public class ResumeHandler {

	private static ResumeHandler instance;

	private ResumeHandler() {
	}

	public static ResumeHandler getInstance() {
		if (instance == null) {
			instance = new ResumeHandler();
		}
		return instance;
	}

	public static Boolean checkResumeValidity(String resumePath) {
		try {

			File resumeFile = new File(resumePath);

			if (!resumeFile.exists() || !resumeFile.isFile()) {
				System.out.println("The specified path does not point to a valid file.");
				return false;
			}

			String fileName = resumeFile.getName().toLowerCase();
			if (!fileName.endsWith(".pdf")) {
				System.out.println("The file is not a PDF.");
				return false;
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An error occurred while checking resume validity: " + e.getMessage());
			return false;
		}
	}
}
