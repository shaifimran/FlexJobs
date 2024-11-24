package application.handlers;

import application.Chat;
import application.Message;
import application.Organisation;
import application.Student;

public class ChatHandler {
	
	  public Message sendMsg(String messageText, Chat chat, Student student) {
	        Message message = new Message();
	        try {
	            // Populate the message object
	            message.setSenderId(student.getRollNo());
	            message.setReceiverId(chat.getOrgId()); // Assuming chat contains orgId as the receiver
	            message.setText(messageText);
	            message.setTransmittedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
	            message.setChatId(chat.getChatID());

	            // Add the message to the chat's list (to keep UI and data in sync)
	            chat.getMessages().add(message);
	        } catch (Exception e) {
	            e.printStackTrace(); // Handle any exceptions
	        }
	        return message;
	    }

	    /**
	     * Creates a message object for an organisation sender.
	     * @param messageText the text of the message.
	     * @param chat the chat object to which the message belongs.
	     * @param organisation the organisation sending the message.
	     * @return the created message object.
	     */
	    public Message sendMsg(String messageText, Chat chat, Organisation organisation) {
	        Message message = new Message();
	        try {
	            // Populate the message object
	        	
	            message.setSenderId(organisation.getName());
	            message.setReceiverId(chat.getStudentId()); // Assuming chat contains studentId as the receiver
	            message.setText(messageText);
	            message.setTransmittedAt(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
	            message.setChatId(chat.getChatID());

	            // Add the message to the chat's list (to keep UI and data in sync)
	            chat.getMessages().add(message);
	        } catch (Exception e) {
	            e.printStackTrace(); // Handle any exceptions
	        }
	        return message;
	    }
}
